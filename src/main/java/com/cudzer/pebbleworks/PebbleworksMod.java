package com.cudzer.pebbleworks;

import com.cudzer.pebbleworks.data.PebbleworksJobManager;
import com.cudzer.pebbleworks.data.PebbleworksTypeManager;
import com.cudzer.pebbleworks.registry.PW_Blocks;
import com.cudzer.pebbleworks.registry.PW_Entities;
import com.cudzer.pebbleworks.registry.PW_Items;
import com.mojang.logging.LogUtils;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import org.slf4j.Logger;

@Mod(PebbleworksMod.MODID)
public class PebbleworksMod {
    public static final String MODID = "pebbleworks";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static final PebbleworksTypeManager PEBBLEWORKS_TYPE_MANAGER = new PebbleworksTypeManager();
    public static final PebbleworksJobManager PEBBLEWORKS_JOB_MANAGER = new PebbleworksJobManager();

    public PebbleworksMod(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::commonSetup);

        PW_Blocks.register(modEventBus);
        PW_Items.register(modEventBus);
        PW_Entities.register(modEventBus);
        PW_Items.CREATIVE_MODE_TABS.register(modEventBus);

        NeoForge.EVENT_BUS.register(this);
        modEventBus.addListener(this::addCreative);
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);

        NeoForge.EVENT_BUS.addListener(this::onAddReloadListeners);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        //if (event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS) event.accept(EXAMPLE_BLOCK_ITEM);
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
    }

    private void onAddReloadListeners(AddReloadListenerEvent event) {
        LOGGER.info("Registering Pebble data managers...");
        event.addListener(PEBBLEWORKS_TYPE_MANAGER);
        event.addListener(PEBBLEWORKS_JOB_MANAGER);
    }

    @EventBusSubscriber(modid = MODID, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
        }
    }
}
