package com.cudzer.pebbleworks.entity.client;

import com.cudzer.pebbleworks.entity.PebbleEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

import static com.cudzer.pebbleworks.PebbleworksMod.MODID;

public class PebbleGeoModel extends GeoModel<PebbleEntity> {
    @Override
    public ResourceLocation getModelResource(PebbleEntity entity) {
        return ResourceLocation.fromNamespaceAndPath(MODID, "geo/pebble_entity.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(PebbleEntity entity) {
        return ResourceLocation.fromNamespaceAndPath(MODID, "textures/entity/pebble.png");
    }

    @Override
    public ResourceLocation getAnimationResource(PebbleEntity entity) {
        return ResourceLocation.fromNamespaceAndPath(MODID, "animations/pebble.animation.json");
    }
}
