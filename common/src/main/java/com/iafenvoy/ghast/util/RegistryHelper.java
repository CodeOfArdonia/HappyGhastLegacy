package com.iafenvoy.ghast.util;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;

public class RegistryHelper {
    public static <T> T get(DynamicRegistryManager manager, RegistryKey<Registry<T>> registry, RegistryKey<T> key) {
        return manager.getOrThrow(registry).get(key);
    }

    public static <T> RegistryEntry<T> entry(DynamicRegistryManager manager, RegistryKey<Registry<T>> registry, T obj) {
        return manager.getOrThrow(registry).getEntry(obj);
    }

    public static <T> RegistryEntry<T> getEntry(DynamicRegistryManager manager, RegistryKey<Registry<T>> registry, RegistryKey<T> key) {
        return entry(manager, registry, get(manager, registry, key));
    }

    public static RegistryEntry<Enchantment> getEnchantment(DynamicRegistryManager manager, RegistryKey<Enchantment> key) {
        return getEntry(manager, RegistryKeys.ENCHANTMENT, key);
    }
}
