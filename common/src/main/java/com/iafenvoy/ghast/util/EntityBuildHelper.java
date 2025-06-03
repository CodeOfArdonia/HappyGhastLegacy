package com.iafenvoy.ghast.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;

import java.util.function.Supplier;

public class EntityBuildHelper {
    public static <T extends Entity> Supplier<EntityType<T>> build(String name, EntityType.EntityFactory<T> constructor, SpawnGroup category, int trackingRange, int updateInterval, boolean fireImmune, Dimension dimension) {
        return () -> {
            EntityType.Builder<T> builder = EntityType.Builder.create(constructor, category).maxTrackingRange(trackingRange).trackingTickInterval(updateInterval).setDimensions(dimension.sizeX, dimension.sizeY);
            if (fireImmune) builder.makeFireImmune();
            return builder.build(name);
        };
    }

    public record Dimension(float sizeX, float sizeY) {
    }
}
