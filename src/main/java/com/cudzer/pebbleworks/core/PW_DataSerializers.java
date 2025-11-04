package com.cudzer.pebbleworks.core;

import com.cudzer.pebbleworks.PebbleworksMod;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.neoforged.neoforge.registries.RegisterEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.cudzer.pebbleworks.PebbleworksMod.MODID;
import static net.neoforged.neoforge.registries.NeoForgeRegistries.Keys.ENTITY_DATA_SERIALIZERS;

public class PW_DataSerializers {
    public static final StreamCodec<ByteBuf, Map<ResourceLocation, Integer>> AFFINITY_MAP_SERIALIZER =
            ByteBufCodecs.map(
                    HashMap::new,
                    ResourceLocation.STREAM_CODEC,
                    ByteBufCodecs.INT
            );

    public static final DeferredRegister<EntityDataSerializer<?>> DATA_SERIALIZERS =
            DeferredRegister.create(NeoForgeRegistries.ENTITY_DATA_SERIALIZERS, PebbleworksMod.MODID);

    public static final EntityDataSerializer<Map<ResourceLocation, Integer>> AFFINITY_MAP =
            EntityDataSerializer.forValueType(AFFINITY_MAP_SERIALIZER);

    private static final ResourceKey<Registry<EntityDataSerializer<?>>> SERIALIZER_REGISTRY_KEY =
            ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath("minecraft", "entity_data_serializer"));

    public static final DeferredHolder<EntityDataSerializer<?>, EntityDataSerializer<Optional<BlockPos>>> OPTIONAL_BLOCK_POS =
            DATA_SERIALIZERS.register("optional_block_pos", () ->
                    EntityDataSerializer.forValueType(ByteBufCodecs.optional(BlockPos.STREAM_CODEC))
            );

    public static void register(RegisterEvent event) {
        // Check if this RegisterEvent is for DataSerializers
        if (event.getRegistryKey().equals(SERIALIZER_REGISTRY_KEY)) {
            event.register(
                    SERIALIZER_REGISTRY_KEY,
                    ResourceLocation.fromNamespaceAndPath(MODID, "affinity_map"),
                    () -> AFFINITY_MAP // Register our new DataSerializer
            );
        }
    }
}
