package com.cudzer.pebbleworks.data.registry;

import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public class ExampleDefinitionManager {
    private static final Map<ResourceLocation, ExampleDefinition> DEFINITIONS = new HashMap<>();

    public static void register(ResourceLocation id, ExampleDefinition def) {
        DEFINITIONS.put(id, def);
    }

    public static ExampleDefinition get(ResourceLocation id) {
        return DEFINITIONS.get(id);
    }

    public static void clear() {
        DEFINITIONS.clear();
    }

    public static int size(){
        return DEFINITIONS.size();
    }
}
