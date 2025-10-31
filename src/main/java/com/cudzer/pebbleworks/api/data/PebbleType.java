package com.cudzer.pebbleworks.api.data;

import java.util.List;

public class PebbleType {
    // These fields will be populated by GSON from the JSON file.
    // Example: "baseHealth": 10
    public float baseHealth;

    // Example: "baseSpeed": 0.25
    public float baseSpeed;

    // Example: "modelTexture": "pebbles:textures/entity/stone_pebble.png"
    public String modelTexture;

    // Example: "allowedJobs": ["pebbles:haul_items", "pebbles:chop_wood"]
    public List<String> allowedJobs;

    // You can add many more properties here:
    // - Evolution data
    // - Damage attributes
    // - Particle effects
}
