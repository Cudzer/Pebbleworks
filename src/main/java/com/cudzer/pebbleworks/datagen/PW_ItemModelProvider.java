package com.cudzer.pebbleworks.datagen;

import com.cudzer.pebbleworks.PebbleworksMod;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class PW_ItemModelProvider extends ItemModelProvider {
    public PW_ItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, PebbleworksMod.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {

    }
}
