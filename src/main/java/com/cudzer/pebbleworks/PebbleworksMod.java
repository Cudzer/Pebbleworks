package com.cudzer.pebbleworks;

import com.cudzer.pebbleworks.core.PW_Config;
import com.cudzer.pebbleworks.core.PW_EventBus;
import com.cudzer.pebbleworks.core.PW_Logger;
import com.cudzer.pebbleworks.core.PW_Registries;
import com.cudzer.pebbleworks.data.PebbleworksJobManager;
import com.cudzer.pebbleworks.data.PebbleworksTypeManager;
import com.cudzer.pebbleworks.registry.PW_Entities;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.registries.RegisterEvent;

@Mod(PebbleworksMod.MODID)
public class PebbleworksMod {
    public static final String MODID = "pebbleworks";

    public static final PebbleworksTypeManager PEBBLEWORKS_TYPE_MANAGER = new PebbleworksTypeManager();
    public static final PebbleworksJobManager PEBBLEWORKS_JOB_MANAGER = new PebbleworksJobManager();

    public PebbleworksMod(IEventBus modEventBus, ModContainer modContainer) {
        PW_Logger.info("Initializing Pebbleworks...");
        PW_Registries.register(modEventBus);
        modContainer.registerConfig(ModConfig.Type.COMMON, PW_Config.SPEC);
        modEventBus.addListener(PW_Entities::registerEntityAttributes);

        NeoForge.EVENT_BUS.register(this);
        modEventBus.addListener(PW_EventBus::onCommonSetup);

        //PW_Packets.register();

        //modEventBus.addListener(this::onRegister);


        NeoForge.EVENT_BUS.addListener(this::onAddReloadListeners);
    }

    public void onRegister(final RegisterEvent event) {

    }

    private void commonSetup(final FMLCommonSetupEvent event) {
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
    }

    private void onAddReloadListeners(AddReloadListenerEvent event) {
        PW_Logger.info("Registering Pebble data managers...");
        event.addListener(PEBBLEWORKS_TYPE_MANAGER);
        event.addListener(PEBBLEWORKS_JOB_MANAGER);
    }
}
