package com.cudzer.pebbleworks.data.loader;

import com.cudzer.pebbleworks.core.PW_Logger;
import com.cudzer.pebbleworks.data.registry.ExampleDefinition;
import com.cudzer.pebbleworks.data.registry.ExampleDefinitionManager;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.Map;

public class JsonDataLoader extends SimpleJsonResourceReloadListener {
    public JsonDataLoader() {
        super(new Gson(), "pebbleworks");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> map, ResourceManager manager, ProfilerFiller filler) {
        ExampleDefinitionManager.clear();
        map.forEach((id, json) -> {
            ExampleDefinition def = ExampleDefinition.CODEC.parse(JsonOps.INSTANCE, json)
                    .getOrThrow();
            ExampleDefinitionManager.register(id, def);
        });
        PW_Logger.info("Loaded " + map.size() + " example definitions");
    }
}
