package com.cudzer.pebbleworks.entity.client;

import com.cudzer.pebbleworks.entity.PebbleEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class PebbleGeoRenderer extends GeoEntityRenderer<PebbleEntity> {
    public PebbleGeoRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new PebbleGeoModel());
        this.shadowRadius = 0.2f;
    }
}
