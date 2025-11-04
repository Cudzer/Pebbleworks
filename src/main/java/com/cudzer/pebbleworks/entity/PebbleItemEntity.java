package com.cudzer.pebbleworks.entity;

import com.cudzer.pebbleworks.items.PebbleItem;
import com.cudzer.pebbleworks.registry.PW_Entities;
import com.cudzer.pebbleworks.registry.PW_Items;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

public class PebbleItemEntity extends ThrowableItemProjectile {
    public PebbleItemEntity(EntityType<? extends PebbleItemEntity> type, Level level) {
        super(type, level);
    }

    public PebbleItemEntity(Level level, LivingEntity thrower) {
        super(PW_Entities.PEBBLE_ITEM_ENTITY.get(), thrower, level);
    }

    @Override
    protected Item getDefaultItem() {
        return PW_Items.PEBBLE_ITEM.get();
    }

    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);

        if (!this.level().isClientSide) {
            // Get the item (and its NBT data)
            ItemStack itemStack = this.getItem();

            // Get the landing position
            double x = this.getX();
            double y = this.getY();
            double z = this.getZ();

            // If we hit a block, adjust the spawn position to be on top
            if (result.getType() == HitResult.Type.BLOCK) {
                BlockPos pos = ((BlockHitResult) result).getBlockPos();
                y = pos.getY() + 1.0; // Spawn on top of the block
            }

            // Use the helper method from PebbleItem to spawn the entity
            PebbleItem.spawnPebble(this.level(), itemStack, x, y, z);

            // Remove the projectile
            this.discard();
        }
    }
}
