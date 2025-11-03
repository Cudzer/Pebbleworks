package com.cudzer.pebbleworks.api;

import com.cudzer.pebbleworks.PebbleworksMod;
import com.cudzer.pebbleworks.api.jobs.IPebbleJobFactory;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.RegistryBuilder;

public class PebbleworksRegistries {
    public static final ResourceKey<Registry<IPebbleJobFactory>> JOB_FACTORIES_KEY =
            ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath("pebbleworks", "job_factories"));

    public static final Registry<IPebbleJobFactory> JOB_FACTORIES_REGISTRY = new RegistryBuilder<>(JOB_FACTORIES_KEY)
            // If you want to enable integer id syncing, for networking.
            // These should only be used in networking contexts, for example in packets or purely networking-related NBT data.
            .sync(true)
            // The default key. Similar to minecraft:air for blocks. This is optional.
            .defaultKey(ResourceLocation.fromNamespaceAndPath("pebbleworks", "empty"))
            // Effectively limits the max count. Generally discouraged, but may make sense in settings such as networking.
            .maxId(256)
            // Build the registry.
            .create();

    /**
     * Call this from your mod's setup event (e.g., FMLCommonSetupEvent)
     * to register your mod's built-in job logic types.
     */
    public static void registerBuiltInJobFactories() {
        // Example registration.
        // This links the "pebbles:simple_ai_sequence" ID from the JSON
        // to a specific Java class that provides the AI Goals.

        /*
        Registry.register(
            JOB_FACTORIES,
            ResourceLocation.fromNamespaceAndPath(PebblesMod.MOD_ID, "simple_ai_sequence"),
            SimpleAISequenceJob::new // This is a method reference to your factory class
        );
        */

        PebbleworksMod.LOGGER.info("Pebble Job Factories are ready for registration.");
    }
}
