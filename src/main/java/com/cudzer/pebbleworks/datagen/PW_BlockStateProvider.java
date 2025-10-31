package com.cudzer.pebbleworks.datagen;

import com.cudzer.pebbleworks.PebbleworksMod;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredBlock;

public class PW_BlockStateProvider extends BlockStateProvider {
    public PW_BlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, PebbleworksMod.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {

    }

    private void blockWithItem(DeferredBlock<?> deferredBlock) {
        simpleBlockWithItem(deferredBlock.get(), cubeAll(deferredBlock.get()));
    }
}
