package com.cudzer.pebbleworks.entity;

import java.util.Arrays;
import java.util.Comparator;

public enum PebbleVariant {
        STONE(0, "Stone"),
        COBBLESTONE(1, "Cobblestone"),
        BASALT(2, "Basalt"),
        ANDESITE(3, "Andesite"),
        BLACKSTONE(4, "Blackstone");

        private static final PebbleVariant[] BY_ID = Arrays.stream(values()).sorted(
                Comparator.comparingInt(PebbleVariant::getId)).toArray(PebbleVariant[]::new);

        private final int id;
        private final String name;

        PebbleVariant(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public String getName(){
            return name;
        }

        public int getId() {
            return id;
        }

        public static PebbleVariant byId(int id) {
            return BY_ID[id % BY_ID.length];
        }
}
