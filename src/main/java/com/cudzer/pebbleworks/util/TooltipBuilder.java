package com.cudzer.pebbleworks.util;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;

public class TooltipBuilder {
    private final List<Component> lines = new ArrayList<>();

    public TooltipBuilder add(String text) {
        lines.add(Component.literal(text));
        return this;
    }

    public TooltipBuilder addGray(String text) {
        lines.add(Component.literal(text).withStyle(ChatFormatting.GRAY));
        return this;
    }

    public List<Component> build() {
        return lines;
    }
}
