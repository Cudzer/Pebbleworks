package com.cudzer.pebbleworks.registry;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import static com.cudzer.pebbleworks.PebbleworksMod.MODID;

public class PW_Entities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, MODID);

    public static void register(IEventBus modEventBus){
        ENTITY_TYPES.register(modEventBus);
    }
}
