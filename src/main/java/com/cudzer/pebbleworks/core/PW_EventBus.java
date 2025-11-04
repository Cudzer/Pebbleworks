package com.cudzer.pebbleworks.core;

import com.cudzer.pebbleworks.data.loader.JsonDataLoader;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.event.AddReloadListenerEvent;

public class PW_EventBus {
    public static void onCommonSetup(FMLCommonSetupEvent event) {
        PW_Logger.info("Running common setup...");
    }

    @SubscribeEvent
    public static void onDatapackReload(AddReloadListenerEvent event) {
        event.addListener(new JsonDataLoader());
    }
}
