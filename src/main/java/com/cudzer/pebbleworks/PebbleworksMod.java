package com.cudzer.pebbleworks;

import com.cudzer.pebbleworks.api.PebbleworksRegistries;
import com.cudzer.pebbleworks.core.PW_DataSerializers;
import com.cudzer.pebbleworks.data.PebbleworksJobManager;
import com.cudzer.pebbleworks.data.PebbleworksTypeManager;
import com.cudzer.pebbleworks.entity.PebbleEntity;
import com.cudzer.pebbleworks.entity.client.PebbleGeoRenderer;
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
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import net.neoforged.neoforge.registries.RegisterEvent;
import org.slf4j.Logger;

@Mod(PebbleworksMod.MODID)
public class PebbleworksMod {
    public static final String MODID = "pebbleworks";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static final PebbleworksTypeManager PEBBLEWORKS_TYPE_MANAGER = new PebbleworksTypeManager();
    public static final PebbleworksJobManager PEBBLEWORKS_JOB_MANAGER = new PebbleworksJobManager();

    public PebbleworksMod(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::registerEntityAttributes);
        modEventBus.addListener(this::onRegister);

        PW_Blocks.register(modEventBus);
        PW_Items.register(modEventBus);
        PW_Entities.register(modEventBus);
        PW_Items.CREATIVE_MODE_TABS.register(modEventBus);

        //NeoForge.EVENT_BUS.register(this);
        modEventBus.addListener(this::addCreative);
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);

        NeoForge.EVENT_BUS.addListener(this::onAddReloadListeners);
    }

    public void onRegister(final RegisterEvent event) {

    }

    public void registerEntityAttributes(EntityAttributeCreationEvent event) {
        event.put(PW_Entities.PEBBLE.get(), PebbleEntity.createAttributes().build());
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

        @SubscribeEvent
        public static void onRegisterRenderers(final EntityRenderersEvent.RegisterRenderers event) {
            event.registerEntityRenderer(PW_Entities.PEBBLE.get(), PebbleGeoRenderer::new);
        }
    }
}
