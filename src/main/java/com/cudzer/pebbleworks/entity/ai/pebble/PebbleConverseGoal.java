package com.cudzer.pebbleworks.entity.ai.pebble;

import com.cudzer.pebbleworks.entity.PebbleEntity;
import com.cudzer.pebbleworks.entity.PebbleJobType;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;
import java.util.List;

public class PebbleConverseGoal extends Goal {
    private final PebbleEntity pebble;
    private final float searchRange;
    private final float probability;
    private final double walkSpeed;
    private PebbleEntity targetPebble;
    private int conversationTime;
    private final double stopDistanceSqr;

    public PebbleConverseGoal(PebbleEntity pebble, float searchRange, float probability, double walkSpeed, double stopDistance) {
        this.pebble = pebble;
        this.searchRange = searchRange;
        this.probability = probability;
        this.walkSpeed = walkSpeed;
        this.stopDistanceSqr = stopDistance * stopDistance;
        // This goal controls movement and looking.
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        // 1. Check if we are idle (no job)
        if (this.pebble.getJob() != PebbleJobType.NONE) {
            return false;
        }

        // 2. Check the random probability
        if (this.pebble.getRandom().nextFloat() >= this.probability) {
            return false;
        }

        // 3. Find a target
        List<PebbleEntity> list = this.pebble.level().getEntitiesOfClass(
                PebbleEntity.class,
                this.pebble.getBoundingBox().inflate(this.searchRange, 3.0D, this.searchRange),
                (otherPebble) -> true // Basic filter
        );

        // 4. Find the first valid target from the list
        for (PebbleEntity other : list) {
            // Check if the other pebble is valid (not self, alive, and also idle)
            if (other != this.pebble && other.isAlive() && other.getJob() == PebbleJobType.NONE) {
                this.targetPebble = other;
                return true; // Found one!
            }
        }

        return false; // Found no valid targets
    }

    @Override
    public boolean canContinueToUse() {
        // 1. Is the target valid?
        if (this.targetPebble == null || !this.targetPebble.isAlive()) {
            return false;
        }

        // 2. Are we both still idle?
        if (this.pebble.getJob() != PebbleJobType.NONE || this.targetPebble.getJob() != PebbleJobType.NONE) {
            return false;
        }

        // 3. Is our timer still running?
        return this.conversationTime > 0;
    }

    @Override
    public void start() {
        // Set a random time to "talk" (2-4 seconds)
        // This timer won't tick down until we are close.
        this.conversationTime = 40 + this.pebble.getRandom().nextInt(40);
        this.pebble.setConversing(true);
    }

    @Override
    public void stop() {
        this.targetPebble = null;
        this.conversationTime = 0;
        // Make sure we stop moving
        this.pebble.getNavigation().stop();
        this.pebble.setConversing(false);
    }

    @Override
    public void tick() {
        // Always look at the target
        this.pebble.getLookControl().setLookAt(this.targetPebble, 10.0F, 10.0F);

        // Check if we are close enough to talk
        if (this.pebble.distanceToSqr(this.targetPebble) > this.stopDistanceSqr) {
            // Not close enough: move towards the target
            this.pebble.getNavigation().moveTo(this.targetPebble, this.walkSpeed);
        } else {
            // We are close enough: stop moving and "talk" (tick down the timer)
            this.pebble.getNavigation().stop();
            this.conversationTime--;
        }
    }
}
