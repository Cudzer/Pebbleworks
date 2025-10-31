package com.cudzer.pebbleworks.api.jobs;

import net.minecraft.world.entity.PathfinderMob;

@FunctionalInterface
public interface IPebbleJobFactory {
    IPebbleJob create(PathfinderMob pebble, com.cudzer.pebbleworks.api.data.PebbleJobDefinition jobDefinition);
}
