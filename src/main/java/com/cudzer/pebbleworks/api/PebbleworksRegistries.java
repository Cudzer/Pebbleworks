package com.cudzer.pebbleworks.api;

import com.cudzer.pebbleworks.PebbleworksMod;
import com.cudzer.pebbleworks.api.jobs.IPebbleJobFactory;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import net.neoforged.neoforge.registries.RegistryBuilder;
import net.neoforged.neoforge.registries.RegistryManager;

import java.util.function.Supplier;

public class PebbleworksRegistries {
    public static final ResourceKey<Registry<IPebbleJobFactory>> JOB_FACTORIES_KEY =
            ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(PebbleworksMod.MODID, "job_factories"));

    public static final Registry<IPebbleJobFactory> JOB_FACTORIES =
            new RegistryBuilder<>(JOB_FACTORIES_KEY).create();

    public static Supplier<Registry<IPebbleJobFactory>> getJobFactoryRegistry() {
        return () -> JOB_FACTORIES;
    }

    public static void register(NewRegistryEvent event) {

    }

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
