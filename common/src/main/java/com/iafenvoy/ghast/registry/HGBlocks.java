package com.iafenvoy.ghast.registry;

import com.iafenvoy.ghast.HappyGhastLegacy;
import com.iafenvoy.ghast.item.block.DriedGhastBlock;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

import java.util.function.Function;

public final class HGBlocks {
    public static final DeferredRegister<Block> REGISTRY = DeferredRegister.create(HappyGhastLegacy.MOD_ID, RegistryKeys.BLOCK);

    public static final RegistrySupplier<Block> DRIED_GHAST = register("dried_ghast", DriedGhastBlock::new);

    public static <T extends Block> RegistrySupplier<T> register(String id, Function<AbstractBlock.Settings, T> block) {
        RegistrySupplier<T> r = REGISTRY.register(id, () -> block.apply(AbstractBlock.Settings.create().registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of(HappyGhastLegacy.MOD_ID, id)))));
        HGItems.register(id, settings -> new BlockItem(r.get(), settings));
        return r;
    }
}