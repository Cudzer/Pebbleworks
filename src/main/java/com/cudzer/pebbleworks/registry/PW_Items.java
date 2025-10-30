package com.cudzer.pebbleworks.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import static com.cudzer.pebbleworks.PebbleworksMod.MODID;

public class PW_Items {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> PEBBLEWORKS_TAB = CREATIVE_MODE_TABS.register(MODID + "_tab", () -> CreativeModeTab.builder().title(Component.translatable("itemGroup.pebbleworks")).withTabsBefore(CreativeModeTabs.COMBAT).icon(Items.COBBLESTONE::getDefaultInstance).displayItems((parameters, output) -> {
        //output.accept(EXAMPLE_ITEM.get());
    }).build());

    //public static final DeferredItem<BlockItem> EXAMPLE_BLOCK_ITEM = ITEMS.registerSimpleBlockItem("example_block", EXAMPLE_BLOCK);
    //public static final DeferredItem<Item> EXAMPLE_ITEM = ITEMS.registerSimpleItem("example_item", new Item.Properties().food(new FoodProperties.Builder().alwaysEdible().nutrition(1).saturationModifier(2f).build()));


    public static void register(IEventBus modEventBus){
        ITEMS.register(modEventBus);
    }
}
