package com.cudzer.pebbleworks.entity.client;

import com.cudzer.pebbleworks.entity.PebbleEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

import static com.cudzer.pebbleworks.PebbleworksMod.MODID;

public class PebbleGeoModel extends GeoModel<PebbleEntity> {

    private static final ResourceLocation TEXTURE_STONE =
            ResourceLocation.fromNamespaceAndPath(MODID, "textures/entity/pebble_entity_stone.png");
    private static final ResourceLocation TEXTURE_COBBLE =
            ResourceLocation.fromNamespaceAndPath(MODID, "textures/entity/pebble_entity_cobblestone.png");
    private static final ResourceLocation TEXTURE_ANDESITE =
            ResourceLocation.fromNamespaceAndPath(MODID, "textures/entity/pebble_entity_andesite.png");
    private static final ResourceLocation TEXTURE_BASALT =
            ResourceLocation.fromNamespaceAndPath(MODID, "textures/entity/pebble_entity_basalt.png");
    private static final ResourceLocation TEXTURE_BLACKSTONE =
            ResourceLocation.fromNamespaceAndPath(MODID, "textures/entity/pebble_entity_blackstone.png");


    @Override
    public ResourceLocation getModelResource(PebbleEntity entity) {
        return ResourceLocation.fromNamespaceAndPath(MODID, "geo/pebble_entity.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(PebbleEntity entity) {
        // Get the variant number *from the entity*
        int variant = entity.getVariant().getId();

        // Return the correct texture based on the variant
        switch (variant) {
            case 1:
                return TEXTURE_COBBLE;
             case 2:
                return TEXTURE_BASALT;
            case 3:
                return TEXTURE_ANDESITE;
            case 4:
                return TEXTURE_BLACKSTONE;
            default: // case 0
                return TEXTURE_STONE;
        }
    }

    @Override
    public ResourceLocation getAnimationResource(PebbleEntity entity) {
        return ResourceLocation.fromNamespaceAndPath(MODID, "animations/pebble_entity.animation.json");
    }
}
