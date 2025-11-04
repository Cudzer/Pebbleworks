package com.cudzer.pebbleworks.data;

import com.cudzer.pebbleworks.PebbleworksMod;
import com.cudzer.pebbleworks.api.data.PebbleJobDefinition;
import com.cudzer.pebbleworks.core.PW_Logger;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class PebbleworksJobManager extends SimpleJsonResourceReloadListener {
    private static final Gson GSON = new GsonBuilder().create();
    private Map<ResourceLocation, PebbleJobDefinition> jobDefinitions = new HashMap<>();

    public PebbleworksJobManager() {
        super(GSON, "pebble_jobs");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> jsonMap, @NotNull ResourceManager resourceManager, @NotNull ProfilerFiller profiler) {
        Map<ResourceLocation, PebbleJobDefinition> loadedJobs = new HashMap<>();

        for (Map.Entry<ResourceLocation, JsonElement> entry : jsonMap.entrySet()) {
            ResourceLocation id = entry.getKey();
            try {
                PebbleJobDefinition job = GSON.fromJson(entry.getValue(), PebbleJobDefinition.class);
                loadedJobs.put(id, job);
            } catch (Exception e) {
                PW_Logger.error("Could not parse pebble job: " + id + " Exception: " + e);
            }
        }

        jobDefinitions = loadedJobs;
        PW_Logger.info("Loaded " + jobDefinitions.size() + " pebble jobs.");
    }

    public PebbleJobDefinition getJobDefinition(ResourceLocation id) {
        return jobDefinitions.get(id);
    }

    public Map<ResourceLocation, PebbleJobDefinition> getAllJobDefinitions() {
        return Collections.unmodifiableMap(jobDefinitions);
    }
}
