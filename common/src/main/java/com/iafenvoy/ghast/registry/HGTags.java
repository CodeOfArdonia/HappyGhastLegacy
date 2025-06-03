package com.iafenvoy.ghast.registry;

import com.iafenvoy.ghast.HappyGhastLegacy;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.EntityTypeTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public final class HGTags {
    public static final TagKey<Item> HAPPY_GHAST_FOOD = TagKey.of(RegistryKeys.ITEM, Identifier.of(HappyGhastLegacy.MOD_ID, "happy_ghast_food"));
    public static final TagKey<Item> HARNESS = TagKey.of(RegistryKeys.ITEM, Identifier.of(HappyGhastLegacy.MOD_ID, "harness"));
    public static final TagKey<EntityType<?>> HAPPY_GHAST_FOLLOW = TagKey.of(RegistryKeys.ENTITY_TYPE, Identifier.of(HappyGhastLegacy.MOD_ID, "happy_ghast_follow"));
}
