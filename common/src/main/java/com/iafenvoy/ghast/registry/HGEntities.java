package com.iafenvoy.ghast.registry;

import com.iafenvoy.ghast.HappyGhastLegacy;
import com.iafenvoy.ghast.entity.HappyGhastEntity;
import com.iafenvoy.ghast.util.EntityBuildHelper;
import dev.architectury.registry.level.entity.EntityAttributeRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.RegistryKeys;

public final class HGEntities {
    public static final DeferredRegister<EntityType<?>> REGISTRY = DeferredRegister.create(HappyGhastLegacy.MOD_ID, RegistryKeys.ENTITY_TYPE);

    public static final RegistrySupplier<EntityType<HappyGhastEntity>> HAPPY_GHAST = register("happy_ghast", HappyGhastEntity::new, SpawnGroup.MONSTER, 320, 3, true, new EntityBuildHelper.Dimension(4, 4));

    public static <T extends Entity> RegistrySupplier<EntityType<T>> register(String id, EntityType.EntityFactory<T> constructor, SpawnGroup category, int trackingRange, int updateInterval, boolean fireImmune, EntityBuildHelper.Dimension dimension) {
        return REGISTRY.register(id, EntityBuildHelper.build(id, constructor, category, trackingRange, updateInterval, fireImmune, dimension));
    }

    public static void init() {
        EntityAttributeRegistry.register(HAPPY_GHAST, HappyGhastEntity::createAttributes);
    }
}