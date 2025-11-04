package com.cudzer.pebbleworks.client;

import com.cudzer.pebbleworks.client.render.PebbleGeoRenderer;
import com.cudzer.pebbleworks.registry.PW_Entities;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

import static com.cudzer.pebbleworks.PebbleworksMod.MODID;

public class ClientSetup {

    @EventBusSubscriber(modid = MODID, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
        }

        @SubscribeEvent
        public static void onRegisterRenderers(final EntityRenderersEvent.RegisterRenderers event) {
            event.registerEntityRenderer(PW_Entities.PEBBLE.get(), PebbleGeoRenderer::new);
            event.registerEntityRenderer(PW_Entities.PEBBLE_ITEM_ENTITY.get(), ThrownItemRenderer::new);
        }
    }
}
