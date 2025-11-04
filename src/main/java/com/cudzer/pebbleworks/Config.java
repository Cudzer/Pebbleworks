package com.cudzer.pebbleworks;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

//@EventBusSubscriber(modid = PebbleworksMod.MODID)
//public class Config {
//    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
//    static final ModConfigSpec SPEC = BUILDER.build();
//    private static boolean validateItemName(final Object obj) {
//        return obj instanceof String itemName && BuiltInRegistries.ITEM.containsKey(ResourceLocation.parse(itemName));
//    }
//
//    @SubscribeEvent
//    static void onLoad(final ModConfigEvent event) {
//    }
//}
