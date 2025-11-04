package com.cudzer.pebbleworks.entity.ai.pebble;

import com.cudzer.pebbleworks.entity.PebbleEntity;
import com.cudzer.pebbleworks.entity.PebbleJobType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemHandlerHelper;

import java.util.EnumSet;
import java.util.List;

public class HaulItemGoal extends Goal {
    private final PebbleEntity pebble;
    private final double speedModifier;
    private final int searchRange;

    private ItemEntity targetItem;
    private BlockPos storagePos;

    public HaulItemGoal(PebbleEntity pebble, double speedModifier, int searchRange) {
        this.pebble = pebble;
        this.speedModifier = speedModifier;
        this.searchRange = searchRange;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        // 1. Must be a HAULER, be empty-handed, and have a storage position
        if (this.pebble.getJob() != PebbleJobType.HAULER ||
                this.pebble.isHoldingItem() ||
                this.pebble.getStoragePos().isEmpty()) {
            return false;
        }

        // 2. Find the storage position
        this.storagePos = this.pebble.getStoragePos().get();

        // 3. Find the closest item to pick up
        this.targetItem = findClosestItemEntity();

        return this.targetItem != null;
    }

    @Override
    public boolean canContinueToUse() {
        // Continue if we are still empty-handed but our target item exists
        if (!this.pebble.isHoldingItem()) {
            return this.targetItem != null && this.targetItem.isAlive();
        }

        // Continue if we are holding an item and haven't reached storage yet
        if (this.pebble.isHoldingItem()) {
            return this.storagePos != null && !this.pebble.getNavigation().isDone();
        }

        return false;
    }

    @Override
    public void start() {
        // Pathfind to the item
        if (this.targetItem != null) {
            this.pebble.getNavigation().moveTo(this.targetItem, this.speedModifier);
        }
    }

    @Override
    public void stop() {
        this.targetItem = null;
        this.storagePos = null;
        this.pebble.getNavigation().stop();
    }

    @Override
    public void tick() {
        if (this.storagePos == null) {
            return; // Safety check
        }

        // --- Phase 1: Go to Item ---
        if (!this.pebble.isHoldingItem()) {
            if (this.targetItem == null || !this.targetItem.isAlive()) {
                return; // Target disappeared
            }

            this.pebble.getLookControl().setLookAt(this.targetItem, 10.0F, (float) this.pebble.getMaxHeadXRot());

            // If close enough, pick it up
            if (this.pebble.distanceToSqr(this.targetItem) < 2.0D) {
                pickUpItem();
            }
        }
        // --- Phase 2: Go to Storage ---
        else {
            this.pebble.getNavigation().moveTo(this.storagePos.getX(), this.storagePos.getY(), this.storagePos.getZ(), this.speedModifier);
            this.pebble.getLookControl().setLookAt(this.storagePos.getX(), this.storagePos.getY(), this.storagePos.getZ(), 10.0F, (float) this.pebble.getMaxHeadXRot());

            // If close enough, deposit
            if (this.pebble.blockPosition().closerToCenterThan(this.storagePos.getCenter(), 2.0D)) {
                depositItem();
            }
        }
    }

    private void pickUpItem() {
        if (this.targetItem == null || !this.targetItem.isAlive()) return;

        ItemStack stack = this.targetItem.getItem();
        this.pebble.setHeldItem(stack);
        this.targetItem.discard();
        this.targetItem = null;

        // Now, pathfind to storage
        this.pebble.getNavigation().moveTo(this.storagePos.getX(), this.storagePos.getY(), this.storagePos.getZ(), this.speedModifier);
    }

    private void depositItem() {
        BlockEntity be = this.pebble.level().getBlockEntity(this.storagePos);
        // We will just support basic chests for now
        if (!(be instanceof ChestBlockEntity)) {
            // Can't find storage, drop item and give up
            this.pebble.spawnAtLocation(this.pebble.getHeldItem());
            this.pebble.clearHeldItem();
            return; // Will stop the goal
        }

        // Use NeoForge capabilities to insert the item
        IItemHandler itemHandler = this.pebble.level().getCapability(
                Capabilities.ItemHandler.BLOCK,
                this.storagePos,
                null
        );

        ItemStack heldItem = this.pebble.getHeldItem();

        ItemStack remainder = ItemHandlerHelper.insertItem(itemHandler, heldItem, false);

        // Set held item to whatever couldn't fit
        this.pebble.setHeldItem(remainder);
        // If remainder is empty, we are done
    }

    private ItemEntity findClosestItemEntity() {
        Level level = this.pebble.level();
        List<ItemEntity> items = level.getEntitiesOfClass(
                ItemEntity.class,
                this.pebble.getBoundingBox().inflate(this.searchRange),
                (item) -> !item.getItem().isEmpty() && item.isAlive() && !item.hasPickUpDelay()
        );

        ItemEntity closestItem = null;
        double closestDist = -1.0D;

        for (ItemEntity item : items) {
            double dist = this.pebble.distanceToSqr(item);
            if (closestDist == -1.0D || dist < closestDist) {
                closestDist = dist;
                closestItem = item;
            }
        }
        return closestItem;
    }
}
