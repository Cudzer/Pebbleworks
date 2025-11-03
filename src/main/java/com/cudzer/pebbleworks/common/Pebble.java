package com.cudzer.pebbleworks.common;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public class Pebble {
    public static final int MAX_POINTS_PER_AFFINITY = 252;
    public static final int MAX_TOTAL_AFFINITY_POINTS = 510;

    private Map<ResourceLocation, Integer> affinityPoints;
    private int totalAffinityPoints;
    private int level;
    private int experience;

    public Pebble(/* PebbleEntity owner */) {
        // this.owner = owner;
        this.affinityPoints = new HashMap<>();
        this.totalAffinityPoints = 0;
        this.level = 1;
        this.experience = 0;
    }

    public int addAffinityPoints(ResourceLocation affinityId, int amount) {
        int currentPoints = this.affinityPoints.getOrDefault(affinityId, 0);
        int totalPoints = this.totalAffinityPoints;
        int pointsAdded = 0;

        for (int i = 0; i < amount; i++) {
            // Check total cap
            if (totalPoints >= MAX_TOTAL_AFFINITY_POINTS) {
                break;
            }
            // Check individual cap
            if (currentPoints >= MAX_POINTS_PER_AFFINITY) {
                break;
            }

            // All checks passed. Add the point.
            currentPoints++;
            totalPoints++;
            pointsAdded++;
        }

        if (pointsAdded > 0) {
            this.affinityPoints.put(affinityId, currentPoints);
            this.totalAffinityPoints = totalPoints;
            // We'd also notify the owner entity to sync the data
            // owner.syncAffinityMap(this.affinityPoints);
        }
        return pointsAdded;
    }

    public Map<ResourceLocation, Integer> getAffinityPoints() {
        return this.affinityPoints;
    }

    public void save(CompoundTag tag) {
        tag.putInt("Level", this.level);
        tag.putInt("Experience", this.experience);
        tag.putInt("TotalAffinity", this.totalAffinityPoints);

        // Save the affinity map
        ListTag affinityList = new ListTag();
        for (Map.Entry<ResourceLocation, Integer> entry : this.affinityPoints.entrySet()) {
            CompoundTag affinityTag = new CompoundTag();
            affinityTag.putString("Id", entry.getKey().toString());
            affinityTag.putInt("Value", entry.getValue());
            affinityList.add(affinityTag);
        }
        tag.put("Affinities", affinityList);
    }

    public void load(CompoundTag tag) {
        this.level = tag.getInt("Level");
        this.experience = tag.getInt("Experience");
        this.totalAffinityPoints = tag.getInt("TotalAffinity");

        // Load the affinity map
        this.affinityPoints.clear();
        ListTag affinityList = tag.getList("Affinities", Tag.TAG_COMPOUND);
        for (int i = 0; i < affinityList.size(); i++) {
            CompoundTag affinityTag = affinityList.getCompound(i);
            ResourceLocation id = ResourceLocation.parse(affinityTag.getString("Id"));
            int value = affinityTag.getInt("Value");
            this.affinityPoints.put(id, value);
        }
    }
}
