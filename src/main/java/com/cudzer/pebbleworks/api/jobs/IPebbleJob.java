package com.cudzer.pebbleworks.api.jobs;

import net.minecraft.world.entity.ai.goal.Goal;

import java.util.List;

public interface IPebbleJob {
    /**
     * @return A list of AI Goals to be added to the Pebble when this job starts.
     */
    List<Goal> getAIGoals();

    /**
     * Called when the job is starting. Use this to set up initial state.
     */
    void onStart();

    void tick();

    /**
     * Called when the job is stopping (completed or cancelled).
     * Use this to clean up.
     */
    void onStop();

    /**
     * @return True if the job is considered complete and can be stopped.
     */
    boolean isFinished();
}
