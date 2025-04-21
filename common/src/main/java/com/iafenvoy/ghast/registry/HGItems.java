package com.iafenvoy.ghast.registry;

import com.iafenvoy.ghast.HappyGhastLegacy;
import com.iafenvoy.ghast.item.HarnessItem;
import dev.architectury.core.item.ArchitecturySpawnEggItem;
import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.DyeColor;

import java.util.function.Supplier;

@SuppressWarnings("unused")
public final class HGItems {
    public static final DeferredRegister<Item> REGISTRY = DeferredRegister.create(HappyGhastLegacy.MOD_ID, RegistryKeys.ITEM);

    public static final RegistrySupplier<Item> HAPPY_GHAST_SPAWN_EGG = register("happy_ghast_spawn_egg", () -> new ArchitecturySpawnEggItem(HGEntities.HAPPY_GHAST, -1, -26113, new Item.Settings()));
    public static final RegistrySupplier<Item> BLUE_HARNESS = register("blue_harness", () -> new HarnessItem(DyeColor.BLUE));
    public static final RegistrySupplier<Item> BLACK_HARNESS = register("black_harness", () -> new HarnessItem(DyeColor.BLACK));
    public static final RegistrySupplier<Item> BROWN_HARNESS = register("brown_harness", () -> new HarnessItem(DyeColor.BROWN));
    public static final RegistrySupplier<Item> CYAN_HARNESS = register("cyan_harness", () -> new HarnessItem(DyeColor.CYAN));
    public static final RegistrySupplier<Item> GRAY_HARNESS = register("gray_harness", () -> new HarnessItem(DyeColor.GRAY));
    public static final RegistrySupplier<Item> GREEN_HARNESS = register("green_harness", () -> new HarnessItem(DyeColor.GREEN));
    public static final RegistrySupplier<Item> LIGHT_BLUE_HARNESS = register("light_blue_harness", () -> new HarnessItem(DyeColor.LIGHT_BLUE));
    public static final RegistrySupplier<Item> LIGHT_GRAY_HARNESS = register("light_gray_harness", () -> new HarnessItem(DyeColor.LIGHT_GRAY));
    public static final RegistrySupplier<Item> LIME_HARNESS = register("lime_harness", () -> new HarnessItem(DyeColor.LIME));
    public static final RegistrySupplier<Item> MAGENTA_HARNESS = register("magenta_harness", () -> new HarnessItem(DyeColor.MAGENTA));
    public static final RegistrySupplier<Item> ORANGE_HARNESS = register("orange_harness", () -> new HarnessItem(DyeColor.ORANGE));
    public static final RegistrySupplier<Item> PINK_HARNESS = register("pink_harness", () -> new HarnessItem(DyeColor.PINK));
    public static final RegistrySupplier<Item> PURPLE_HARNESS = register("purple_harness", () -> new HarnessItem(DyeColor.PURPLE));
    public static final RegistrySupplier<Item> RED_HARNESS = register("red_harness", () -> new HarnessItem(DyeColor.RED));
    public static final RegistrySupplier<Item> WHITE_HARNESS = register("white_harness", () -> new HarnessItem(DyeColor.WHITE));
    public static final RegistrySupplier<Item> YELLOW_HARNESS = register("yellow_harness", () -> new HarnessItem(DyeColor.YELLOW));

    public static <T extends Item> RegistrySupplier<T> register(String id, Supplier<T> item) {
        return REGISTRY.register(id, item);
    }

    public static void init() {
        CreativeTabRegistry.append(ItemGroups.NATURAL, HGBlocks.DRIED_GHAST.get());
        CreativeTabRegistry.append(ItemGroups.TOOLS, HarnessItem.getAll().toArray(ItemConvertible[]::new));
        CreativeTabRegistry.append(ItemGroups.SPAWN_EGGS, HAPPY_GHAST_SPAWN_EGG.get());
    }
}