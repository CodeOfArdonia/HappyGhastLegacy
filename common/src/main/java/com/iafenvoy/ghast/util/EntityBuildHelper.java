package com.iafenvoy.ghast.util;

import com.iafenvoy.ghast.HappyGhastLegacy;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

import java.util.function.Supplier;

public class EntityBuildHelper {
    public static <T extends Entity> Supplier<EntityType<T>> build(String name, EntityType.EntityFactory<T> constructor, SpawnGroup category, int trackingRange, int updateInterval, boolean fireImmune, Dimension dimension) {
        return () -> {
            EntityType.Builder<T> builder = EntityType.Builder.create(constructor, category).maxTrackingRange(trackingRange).trackingTickInterval(updateInterval).dimensions(dimension.sizeX, dimension.sizeY);
            if (fireImmune) builder.makeFireImmune();
            return builder.build(RegistryKey.of(RegistryKeys.ENTITY_TYPE, Identifier.of(HappyGhastLegacy.MOD_ID, name)));
        };
    }

    public record Dimension(float sizeX, float sizeY) {
    }
}
