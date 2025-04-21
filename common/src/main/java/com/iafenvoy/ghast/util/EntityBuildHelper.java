package com.iafenvoy.ghast.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;

import java.util.function.Supplier;

public class EntityBuildHelper {
    public static final Dimension PLAYER = new Dimension(0.6F, 1.8F);

    public static <T extends Entity> Supplier<EntityType<T>> build(String name, EntityType.EntityFactory<T> constructor, SpawnGroup category, int trackingRange, int updateInterval, boolean fireImmune, Dimension dimension) {
        return () -> {
            EntityType.Builder<T> builder = EntityType.Builder.create(constructor, category).maxTrackingRange(trackingRange).trackingTickInterval(updateInterval).setDimensions(dimension.sizeX, dimension.sizeY);
            if (fireImmune) builder.makeFireImmune();
            return builder.build(name);
        };
    }

    public record Dimension(float sizeX, float sizeY) {
        public Dimension scale(float scale) {
            return new Dimension(this.sizeX * scale, this.sizeY * scale);
        }
    }
}
