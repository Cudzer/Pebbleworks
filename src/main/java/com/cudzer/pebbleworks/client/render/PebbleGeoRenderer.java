package com.cudzer.pebbleworks.client.render;

import com.cudzer.pebbleworks.entity.PebbleEntity;
import com.cudzer.pebbleworks.entity.client.PebbleGeoModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class PebbleGeoRenderer extends GeoEntityRenderer<PebbleEntity> {
    public PebbleGeoRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new PebbleGeoModel());
        this.shadowRadius = 0.2f;
    }
}
