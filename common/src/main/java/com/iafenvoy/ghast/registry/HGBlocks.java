package com.iafenvoy.ghast.registry;

import com.iafenvoy.ghast.HappyGhastLegacy;
import com.iafenvoy.ghast.item.block.DriedGhastBlock;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;

import java.util.function.Supplier;

public final class HGBlocks {
    public static final DeferredRegister<Block> REGISTRY = DeferredRegister.create(HappyGhastLegacy.MOD_ID, RegistryKeys.BLOCK);

    public static final RegistrySupplier<Block> DRIED_GHAST = register("dried_ghast", DriedGhastBlock::new);

    public static <T extends Block> RegistrySupplier<T> register(String id, Supplier<T> block) {
        RegistrySupplier<T> r = REGISTRY.register(id, block);
        HGItems.register(id, () -> new BlockItem(r.get(), new Item.Settings()));
        return r;
    }
}