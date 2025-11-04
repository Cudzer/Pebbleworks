package com.cudzer.pebbleworks.data;

import com.cudzer.pebbleworks.PebbleworksMod;
import com.cudzer.pebbleworks.api.data.PebbleType;
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

public class PebbleworksTypeManager extends SimpleJsonResourceReloadListener {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    private Map<ResourceLocation, PebbleType> pebbleTypes = new HashMap<>();

    public PebbleworksTypeManager() {
        super(GSON, "pebble_types");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> jsonMap, @NotNull ResourceManager resourceManager, @NotNull ProfilerFiller profiler) {
        Map<ResourceLocation, PebbleType> loadedTypes = new HashMap<>();

        for (Map.Entry<ResourceLocation, JsonElement> entry : jsonMap.entrySet()) {
            ResourceLocation id = entry.getKey();
            try {
                // Deserialize the JSON into our PebbleType data object
                // We use GSON here, but for complex data, Minecraft's Codec system is even better
                PebbleType type = GSON.fromJson(entry.getValue(), PebbleType.class);
                loadedTypes.put(id, type);
            } catch (Exception e) {
                PW_Logger.error("Could not parse pebble type: " + id + " Exception: " + e);
            }
        }

        pebbleTypes = loadedTypes;
        PW_Logger.info("Loaded " + pebbleTypes.size() + " pebble types.");
    }

    public PebbleType getType(ResourceLocation id) {
        return pebbleTypes.get(id);
    }

    public Map<ResourceLocation, PebbleType> getAllTypes() {
        return Collections.unmodifiableMap(pebbleTypes);
    }
}
