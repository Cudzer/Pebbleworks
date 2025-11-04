package com.cudzer.pebbleworks.data.registry;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record ExampleDefinition(String id, float power, String element) {
    public static final Codec<ExampleDefinition> CODEC = RecordCodecBuilder.create(i -> i.group(
            Codec.STRING.fieldOf("id").forGetter(ExampleDefinition::id),
            Codec.FLOAT.fieldOf("power").forGetter(ExampleDefinition::power),
            Codec.STRING.fieldOf("element").forGetter(ExampleDefinition::element)
    ).apply(i, ExampleDefinition::new));
}
