package com.cudzer.pebbleworks.api.data;

import java.util.List;

public class PebbleJobDefinition {
    // This is the CRITICAL link for extensibility.
    // This ID maps to a registered IPebbleJobFactory.
    // Example: "jobLogicId": "pebbles:simple_ai_sequence"
    public String jobLogicId;

    // Example: "requiredTools": ["minecraft:wooden_axe", "minecraft:stone_axe"]
    // (You'd probably want to use item tags here in a real implementation)
    public List<String> requiredTools;

    // Example: "priority": 10
    public int priority;
}
