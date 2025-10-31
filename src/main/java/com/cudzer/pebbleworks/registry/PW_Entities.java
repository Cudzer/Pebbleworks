package com.cudzer.pebbleworks.registry;

import com.cudzer.pebbleworks.PebbleworksMod;
import com.cudzer.pebbleworks.entity.PebbleEntity;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

import static com.cudzer.pebbleworks.PebbleworksMod.MODID;

public class PW_Entities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, MODID);

    public static final Supplier<EntityType<PebbleEntity>> PEBBLE = ENTITY_TYPES.register("pebble",
            () -> EntityType.Builder.of(PebbleEntity::new, MobCategory.CREATURE)
                    .sized(0.5f, 0.5f) // Small size
                    .build(PebbleworksMod.MODID + ":pebble")
    );

    public static void register(IEventBus modEventBus){
        ENTITY_TYPES.register(modEventBus);
    }
}
