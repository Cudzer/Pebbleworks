package com.cudzer.pebbleworks.entity;

import com.cudzer.pebbleworks.PebbleworksMod;
import com.cudzer.pebbleworks.api.PebbleworksRegistries;
import com.cudzer.pebbleworks.api.data.PebbleJobDefinition;
import com.cudzer.pebbleworks.api.data.PebbleType;
import com.cudzer.pebbleworks.api.jobs.IPebbleJob;
import com.cudzer.pebbleworks.api.jobs.IPebbleJobFactory;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PebbleEntity extends PathfinderMob {
    private static final EntityDataAccessor<String> PEBBLE_TYPE_ID_STRING =
            SynchedEntityData.defineId(PebbleEntity.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<String> CURRENT_JOB_ID_STRING =
            SynchedEntityData.defineId(PebbleEntity.class, EntityDataSerializers.STRING);

    @Nullable
    private PebbleType pebbleType;
    @Nullable
    private IPebbleJob currentJobLogic;

    public PebbleEntity(EntityType<? extends PathfinderMob> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
        super.defineSynchedData(builder);
        builder.define(PEBBLE_TYPE_ID_STRING, PebbleworksMod.MODID + ":missing");
        builder.define(CURRENT_JOB_ID_STRING, PebbleworksMod.MODID + ":idle");
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(8, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(9, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(10, new RandomLookAroundGoal(this));
    }

    @Nullable
    public PebbleType getPebbleType() {
        if (this.pebbleType == null) {
            this.pebbleType = PebbleworksMod.PEBBLEWORKS_TYPE_MANAGER.getType(getPebbleTypeId());
        }

        if (this.pebbleType == null) {
            PebbleworksMod.LOGGER.warn("Pebble entity {} has missing type {}!", getUUID(), getPebbleTypeId());
            return null;
        }
        return this.pebbleType;
    }

    public ResourceLocation getPebbleTypeId() {
        return ResourceLocation.parse(this.entityData.get(PEBBLE_TYPE_ID_STRING));
    }

    public void setPebbleType(ResourceLocation id) {
        this.entityData.set(PEBBLE_TYPE_ID_STRING, id.toString());
        this.pebbleType = null;

        if (!level().isClientSide) {
            // ... (apply stats logic) ...
            return;
        }
    }

    public void setJob(ResourceLocation jobId) {
        if (!level().isClientSide) {
            this.entityData.set(CURRENT_JOB_ID_STRING, jobId.toString());
            updateJobLogic();
        }
    }

    public ResourceLocation getCurrentJobId() {
        return ResourceLocation.parse(this.entityData.get(CURRENT_JOB_ID_STRING));
    }

    private void updateJobLogic() {
        if (level().isClientSide) return; // SERVER ONLY

        // 1. Clear old job AI
        if (this.currentJobLogic != null) {
            this.currentJobLogic.onStop();
            this.currentJobLogic.getAIGoals().forEach(this.goalSelector::removeGoal);
            this.currentJobLogic = null;
        }

        // 2. Get definition for new job from our 'main' manager
        PebbleJobDefinition jobDef = PebbleworksMod.PEBBLEWORKS_JOB_MANAGER.getJobDefinition(getCurrentJobId());
        if (jobDef == null || jobDef.jobLogicId == null) {
            return; // Job is "idle" or invalid
        }

        // 3. Get the Job Factory from the PUBLIC API REGISTRY
        IPebbleJobFactory factory = PebbleworksRegistries.JOB_FACTORIES.get(
                ResourceLocation.tryParse(jobDef.jobLogicId)
        );

        // 4. If factory exists, create new job logic and add its AI goals
        if (factory != null) {
            // We pass 'this' (the PebbleEntity) to the factory
            this.currentJobLogic = factory.create(this, jobDef);
            this.currentJobLogic.onStart();
            this.currentJobLogic.getAIGoals().forEach(g -> this.goalSelector.addGoal(1, g)); // Add with high priority
        } else {
            PebbleworksMod.LOGGER.error("Missing job factory for ID: {}", jobDef.jobLogicId);
        }
    }

    @Override
    public void aiStep() {
        super.aiStep();
        // Tick the current job if it exists
        if (!level().isClientSide && this.currentJobLogic != null) {
            this.currentJobLogic.tick();
            if (this.currentJobLogic.isFinished()) {
                // Job is done, go back to idle
                setJob(ResourceLocation.fromNamespaceAndPath(PebbleworksMod.MODID, "idle"));
            }
        }
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putString("PebbleType", getPebbleTypeId().toString());
        compound.putString("CurrentJob", getCurrentJobId().toString());
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        if (compound.contains("PebbleType")) {
            setPebbleType(ResourceLocation.parse(compound.getString("PebbleType")));
        }
        if (compound.contains("CurrentJob")) {
            // Set the job. This will trigger updateJobLogic() on the server.
            setJob(ResourceLocation.parse(compound.getString("CurrentJob")));
        }
    }
}
