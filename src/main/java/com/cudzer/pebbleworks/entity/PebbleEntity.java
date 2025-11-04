package com.cudzer.pebbleworks.entity;

import com.cudzer.pebbleworks.PebbleworksMod;
import com.cudzer.pebbleworks.core.PW_DataSerializers;
import com.cudzer.pebbleworks.core.PW_Logger;
import com.cudzer.pebbleworks.entity.ai.pebble.PebbleConverseGoal;
import com.cudzer.pebbleworks.registry.PW_Items;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.Objects;
import java.util.Optional;

public class PebbleEntity extends Animal implements GeoEntity {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    private static final EntityDataAccessor<Integer> VARIANT =
            SynchedEntityData.defineId(PebbleEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> IS_CONVERSING =
            SynchedEntityData.defineId(PebbleEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Optional<BlockPos>> STORAGE_POS =
            SynchedEntityData.defineId(PebbleEntity.class, PW_DataSerializers.OPTIONAL_BLOCK_POS.get());

    public final SimpleContainer inventory = new SimpleContainer(1);

    public PebbleEntity(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
        super.defineSynchedData(builder);
        builder.define(VARIANT, 0);
        builder.define(IS_CONVERSING, false);
        builder.define(STORAGE_POS, Optional.empty());
    }

    @Override
    public SpawnGroupData finalizeSpawn(@NotNull ServerLevelAccessor level, @NotNull DifficultyInstance difficulty,
                                        @NotNull MobSpawnType spawnType, @Nullable SpawnGroupData spawnGroupData) {
        PebbleVariant variant = Util.getRandom(PebbleVariant.values(), this.random);
        this.setVariant(variant);
        return super.finalizeSpawn(level, difficulty, spawnType, spawnGroupData);
    }

    @Override
    public @Nullable AgeableMob getBreedOffspring(@NotNull ServerLevel serverLevel, @NotNull AgeableMob ageableMob) {
        return null;
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(8, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(10, new PebbleConverseGoal(this, 8.0F, 0.02F, 1.0D, 2.0D));
        this.goalSelector.addGoal(11, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(12, new RandomLookAroundGoal(this));
    }

    @Override
    public void aiStep() {
        super.aiStep();
    }

    private int getTypeVariant() {
        return this.entityData.get(VARIANT);
    }

    public PebbleJobType getJob(){
        return PebbleJobType.NONE;
    }

    public PebbleVariant getVariant() {
        return PebbleVariant.byId(this.getTypeVariant() & 255);
    }

    private void setVariant(PebbleVariant variant) {
        this.entityData.set(VARIANT, variant.getId() & 255);
    }

    public boolean isConversing(){
        return this.entityData.get(IS_CONVERSING);
    }

    public void setConversing(boolean value){
        this.entityData.set(IS_CONVERSING, value);
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("Variant", this.getTypeVariant());
        this.getStoragePos().ifPresent(pos -> {
            compound.put("StoragePos", NbtUtils.writeBlockPos(pos));
        });
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.entityData.set(VARIANT, compound.getInt("Variant"));
        if (compound.contains("StoragePos", Tag.TAG_COMPOUND)) {
            this.setStoragePos(NbtUtils.readBlockPos(compound.getCompound("StoragePos"), ""));
        } else {
            this.setStoragePos(Optional.empty());
        }
    }

    @Override
    public boolean isFood(@NotNull ItemStack itemStack) {
        return false;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "pebble_controller", 0, this::predicate));
    }

    private <E extends GeoEntity> PlayState predicate(AnimationState<E> event) {
        if (event.isMoving()) {
            event.getController().setAnimation(RawAnimation.begin().thenLoop("animation.pebble_entity.walk"));
        }else if (isConversing()) {
            event.getController().setAnimation(RawAnimation.begin().thenLoop("animation.pebble_entity.converse"));
        }else  {
            event.getController().setAnimation(RawAnimation.begin().thenLoop("animation.pebble_entity.idle"));
        }
        return PlayState.CONTINUE;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 20.0)
                .add(Attributes.MOVEMENT_SPEED, 0.3)
                .add(Attributes.ATTACK_DAMAGE, 4.0);
    }

    @Override
    public @NotNull InteractionResult mobInteract(Player player, @NotNull InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);

        // GOAL 1: Check if player is shifting
        if (player.isShiftKeyDown() && !this.level().isClientSide) {
            // 1. Create the item
            ItemStack pebbleItem = new ItemStack(PW_Items.PEBBLE_ITEM.get());

            // 2. Save NBT data from this entity *to* the item
            this.saveToItem(pebbleItem);

            // 3. Give the item to the player
            // We use setItemInHand to replace whatever is there (or nothing)
            // To be safer, we can try to add it to inventory first.
            if (!player.getInventory().add(pebbleItem)) {
                // If inventory is full, drop it at the player's feet
                player.drop(pebbleItem, false);
            }
            // Clear the hand if they were holding nothing (to prevent dupes)
//            if (itemStack.isEmpty()) {
//                player.setItemInHand(hand, ItemStack.EMPTY);
//            }


            // 4. Play a sound and discard the entity
            this.playSound(SoundEvents.PLAYER_SPLASH_HIGH_SPEED, 1.0F, 1.0F);
            this.discard();
            return InteractionResult.SUCCESS;
        }

        return super.mobInteract(player, hand);
    }

    public void saveToItem(ItemStack stack) {
        CompoundTag pebbleDataTag = new CompoundTag();

        pebbleDataTag.putInt("Variant", this.getVariant().getId());
        //pebbleDataTag.putString("Job", this.getJob().name());
        pebbleDataTag.putFloat("Health", this.getHealth());
        if (this.hasCustomName()) {
            pebbleDataTag.putString("CustomName", Component.Serializer.toJson(this.getCustomName(), this.level().registryAccess()));
        }

        pebbleDataTag.put("Inventory", this.inventory.createTag(this.level().registryAccess()));
        this.getStoragePos().ifPresent(pos -> {
            pebbleDataTag.put("StoragePos", NbtUtils.writeBlockPos(pos));
        });

        // 1. Get the existing custom data, or create a new empty tag if it doesn't exist
        CustomData customData = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.of(new CompoundTag()));

        // 2. Make a copy of its internal tag
        CompoundTag mainTag = customData.copyTag();

        // 3. Put our entity data into that tag
        mainTag.put("PebbleData", pebbleDataTag);
        PW_Logger.info("Saving the following to nbt:" + mainTag);

        // 4. Set the modified component back onto the item stack
        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(mainTag));
    }

    public void loadFromItem(ItemStack stack) {
        CustomData customData = stack.get(DataComponents.CUSTOM_DATA);
        if (customData == null) {
            return;
        }

        CompoundTag mainTag = customData.copyTag();
        PW_Logger.info("Loading the following from nbt:" + mainTag);
        if (!mainTag.contains("PebbleData", Tag.TAG_COMPOUND)) {
            return;
        }

        CompoundTag nbt = mainTag.getCompound("PebbleData");

        this.setVariant(PebbleVariant.byId(nbt.getInt("Variant")));
//        try {
//            PebbleJobType job = PebbleJobType.valueOf(nbt.getString("Job"));
//            this.setJob(job);
//        } catch (IllegalArgumentException e) {
//            this.setJob(PebbleJobType.NONE);
//        }

        if (nbt.contains("Health")) {
            this.setHealth(nbt.getFloat("Health"));
        }
        if (nbt.contains("CustomName")) {
            this.setCustomName(Component.Serializer.fromJson(nbt.getString("CustomName"), this.level().registryAccess()));
        }

        this.inventory.fromTag(nbt.getList("Inventory", Tag.TAG_COMPOUND), this.level().registryAccess());
        if (nbt.contains("StoragePos", Tag.TAG_COMPOUND)) {
            this.setStoragePos(NbtUtils.readBlockPos(nbt.getCompound("StoragePos"), ""));
        } else {
            this.setStoragePos(Optional.empty());
        }
    }

    public Optional<BlockPos> getStoragePos() {
        return this.entityData.get(STORAGE_POS);
    }
    public void setStoragePos(Optional<BlockPos> pos) {
        this.entityData.set(STORAGE_POS, pos);
    }
    public boolean isHoldingItem() {
        return !this.inventory.getItem(0).isEmpty();
    }
    public ItemStack getHeldItem() {
        return this.inventory.getItem(0);
    }
    public void setHeldItem(ItemStack stack) {
        this.inventory.setItem(0, stack);
    }
    public void clearHeldItem() {
        this.inventory.setItem(0, ItemStack.EMPTY);
    }
}
