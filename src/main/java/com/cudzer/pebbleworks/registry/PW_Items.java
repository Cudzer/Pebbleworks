package com.cudzer.pebbleworks.registry;

import com.cudzer.pebbleworks.core.PW_Registries;
import com.cudzer.pebbleworks.items.PebbleItem;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.CustomData;
import net.neoforged.neoforge.common.DeferredSpawnEggItem;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;

import static com.cudzer.pebbleworks.PebbleworksMod.MODID;

public class PW_Items {
    public static final DeferredItem<Item> PEBBLE_SPAWN_EGG = PW_Registries.ITEMS.register("pebble_spawn_egg",
            () -> new DeferredSpawnEggItem(PW_Entities.PEBBLE, 0x31afaf, 0xffac00,
                    new Item.Properties()));

    public static final DeferredItem<Item> PEBBLE_ITEM = PW_Registries.ITEMS.register("pebble_item",
            () -> new PebbleItem(new Item.Properties().stacksTo(1)
                    .component(DataComponents.CUSTOM_DATA, CustomData.of(new CompoundTag()))));

    public static final DeferredItem<BlockItem> PEBBLE_STATION_ITEM = PW_Registries.ITEMS.registerSimpleBlockItem(
            "pebble_station", PW_Blocks.PEBBLE_STATION
    );

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> PEBBLEWORKS_TAB =
            PW_Registries.CREATIVE_MODE_TABS.register(MODID + "_tab", () ->
                    CreativeModeTab.builder().title(Component.translatable("itemGroup.pebbleworks")).
                            withTabsBefore(CreativeModeTabs.COMBAT).icon(Items.COBBLESTONE::getDefaultInstance).
                            displayItems((parameters, output) -> {
        output.accept(PEBBLE_SPAWN_EGG.get());
        output.accept(PW_Blocks.PEBBLE_STATION.get());
        output.accept(PW_Items.PEBBLE_ITEM.get());
    }).build());
}
