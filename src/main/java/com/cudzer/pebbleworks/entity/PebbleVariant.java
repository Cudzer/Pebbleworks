package com.cudzer.pebbleworks.entity;

import java.util.Arrays;
import java.util.Comparator;

public enum PebbleVariant {
        STONE(0),
        COBBLESTONE(1),
        BASALT(2),
        ANDESITE(3),
        BLACKSTONE(4);

        private static final PebbleVariant[] BY_ID = Arrays.stream(values()).sorted(
                Comparator.comparingInt(PebbleVariant::getId)).toArray(PebbleVariant[]::new);
        private final int id;

        PebbleVariant(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }

        public static PebbleVariant byId(int id) {
            return BY_ID[id % BY_ID.length];
        }
}
