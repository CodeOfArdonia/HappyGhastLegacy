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
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;

import java.util.function.Function;

@SuppressWarnings("unused")
public final class HGItems {
    public static final DeferredRegister<Item> REGISTRY = DeferredRegister.create(HappyGhastLegacy.MOD_ID, RegistryKeys.ITEM);

    public static final RegistrySupplier<Item> HAPPY_GHAST_SPAWN_EGG = register("happy_ghast_spawn_egg", settings -> new ArchitecturySpawnEggItem(HGEntities.HAPPY_GHAST, settings));
    public static final RegistrySupplier<Item> BLUE_HARNESS = register("blue_harness", settings -> new HarnessItem(settings, DyeColor.BLUE));
    public static final RegistrySupplier<Item> BLACK_HARNESS = register("black_harness", settings -> new HarnessItem(settings, DyeColor.BLACK));
    public static final RegistrySupplier<Item> BROWN_HARNESS = register("brown_harness", settings -> new HarnessItem(settings, DyeColor.BROWN));
    public static final RegistrySupplier<Item> CYAN_HARNESS = register("cyan_harness", settings -> new HarnessItem(settings, DyeColor.CYAN));
    public static final RegistrySupplier<Item> GRAY_HARNESS = register("gray_harness", settings -> new HarnessItem(settings, DyeColor.GRAY));
    public static final RegistrySupplier<Item> GREEN_HARNESS = register("green_harness", settings -> new HarnessItem(settings, DyeColor.GREEN));
    public static final RegistrySupplier<Item> LIGHT_BLUE_HARNESS = register("light_blue_harness", settings -> new HarnessItem(settings, DyeColor.LIGHT_BLUE));
    public static final RegistrySupplier<Item> LIGHT_GRAY_HARNESS = register("light_gray_harness", settings -> new HarnessItem(settings, DyeColor.LIGHT_GRAY));
    public static final RegistrySupplier<Item> LIME_HARNESS = register("lime_harness", settings -> new HarnessItem(settings, DyeColor.LIME));
    public static final RegistrySupplier<Item> MAGENTA_HARNESS = register("magenta_harness", settings -> new HarnessItem(settings, DyeColor.MAGENTA));
    public static final RegistrySupplier<Item> ORANGE_HARNESS = register("orange_harness", settings -> new HarnessItem(settings, DyeColor.ORANGE));
    public static final RegistrySupplier<Item> PINK_HARNESS = register("pink_harness", settings -> new HarnessItem(settings, DyeColor.PINK));
    public static final RegistrySupplier<Item> PURPLE_HARNESS = register("purple_harness", settings -> new HarnessItem(settings, DyeColor.PURPLE));
    public static final RegistrySupplier<Item> RED_HARNESS = register("red_harness", settings -> new HarnessItem(settings, DyeColor.RED));
    public static final RegistrySupplier<Item> WHITE_HARNESS = register("white_harness", settings -> new HarnessItem(settings, DyeColor.WHITE));
    public static final RegistrySupplier<Item> YELLOW_HARNESS = register("yellow_harness", settings -> new HarnessItem(settings, DyeColor.YELLOW));
    public static final RegistrySupplier<Item> MUSIC_DISC_TEARS = register("music_disc_tears", settings -> new Item(settings.maxCount(1).rarity(Rarity.UNCOMMON).jukeboxPlayable(HGSongs.TEARS)));

    public static <T extends Item> RegistrySupplier<T> register(String id, Function<Item.Settings, T> item) {
        return REGISTRY.register(id, () -> item.apply(new Item.Settings().registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(HappyGhastLegacy.MOD_ID, id)))));
    }

    public static void init() {
        CreativeTabRegistry.append(ItemGroups.NATURAL, HGBlocks.DRIED_GHAST.get());
        CreativeTabRegistry.append(ItemGroups.TOOLS, MUSIC_DISC_TEARS.get());
        CreativeTabRegistry.append(ItemGroups.TOOLS, HarnessItem.getAll().toArray(ItemConvertible[]::new));
        CreativeTabRegistry.append(ItemGroups.SPAWN_EGGS, HAPPY_GHAST_SPAWN_EGG.get());
    }
}