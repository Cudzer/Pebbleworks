package com.cudzer.pebbleworks.registry;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import static com.cudzer.pebbleworks.PebbleworksMod.MODID;

public class PW_Blocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MODID);

    //public static final DeferredBlock<Block> EXAMPLE_BLOCK = BLOCKS.registerSimpleBlock("example_block", BlockBehaviour.Properties.of().mapColor(MapColor.STONE));

    public static void register(IEventBus modEventBus){
        BLOCKS.register(modEventBus);
    }
}
