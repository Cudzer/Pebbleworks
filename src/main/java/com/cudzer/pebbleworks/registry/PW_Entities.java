package com.cudzer.pebbleworks.registry;

import com.cudzer.pebbleworks.PebbleworksMod;
import com.cudzer.pebbleworks.core.PW_Registries;
import com.cudzer.pebbleworks.entity.PebbleEntity;
import com.cudzer.pebbleworks.entity.PebbleItemEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.registries.DeferredHolder;

public class PW_Entities {
    public static final DeferredHolder<EntityType<?>, EntityType<PebbleEntity>> PEBBLE =
            PW_Registries.ENTITIES.register("pebble",
            () -> EntityType.Builder.of(PebbleEntity::new, MobCategory.CREATURE)
                    .sized(0.5f, 0.5f)
                    .build(PebbleworksMod.MODID + ":pebble"));

    public static final DeferredHolder<EntityType<?>, EntityType<PebbleItemEntity>> PEBBLE_ITEM_ENTITY =
            PW_Registries.ENTITIES.register("pebble_item_entity",
                    () -> EntityType.Builder.<PebbleItemEntity>of(PebbleItemEntity::new, MobCategory.MISC)
                            .sized(0.25F, 0.25F)
                            .clientTrackingRange(4)
                            .updateInterval(10)
                            .build("pebble_item_entity"));

    public static void registerEntityAttributes(EntityAttributeCreationEvent event) {
        event.put(PEBBLE.get(), PebbleEntity.createAttributes().build());
    }
}
