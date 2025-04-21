package com.iafenvoy.ghast.registry;

import com.google.common.base.Suppliers;
import com.iafenvoy.ghast.HappyGhastLegacy;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;

import java.util.function.Supplier;

public final class HGSounds {
    public static final DeferredRegister<SoundEvent> REGISTRY = DeferredRegister.create(HappyGhastLegacy.MOD_ID, RegistryKeys.SOUND_EVENT);
    public static final RegistrySupplier<SoundEvent> BLOCK_DRIED_GHAST_BREAK = register("block.dried_ghast.break");
    public static final RegistrySupplier<SoundEvent> BLOCK_DRIED_GHAST_STEP = register("block.dried_ghast.step");
    public static final RegistrySupplier<SoundEvent> BLOCK_DRIED_GHAST_FALL = register("block.dried_ghast.fall");
    public static final RegistrySupplier<SoundEvent> BLOCK_DRIED_GHAST_HIT = register("block.dried_ghast.hit");
    public static final RegistrySupplier<SoundEvent> BLOCK_DRIED_GHAST_AMBIENT = register("block.dried_ghast.ambient");
    public static final RegistrySupplier<SoundEvent> BLOCK_DRIED_GHAST_AMBIENT_WATER = register("block.dried_ghast.ambient_water");
    public static final RegistrySupplier<SoundEvent> BLOCK_DRIED_GHAST_PLACE = register("block.dried_ghast.place");
    public static final RegistrySupplier<SoundEvent> BLOCK_DRIED_GHAST_PLACE_IN_WATER = register("block.dried_ghast.place_in_water");
    public static final RegistrySupplier<SoundEvent> BLOCK_DRIED_GHAST_TRANSITION = register("block.dried_ghast.transition");
    public static final RegistrySupplier<SoundEvent> ENTITY_GHASTLING_AMBIENT = register("entity.ghastling.ambient");
    public static final RegistrySupplier<SoundEvent> ENTITY_GHASTLING_DEATH = register("entity.ghastling.death");
    public static final RegistrySupplier<SoundEvent> ENTITY_GHASTLING_HURT = register("entity.ghastling.hurt");
    public static final RegistrySupplier<SoundEvent> ENTITY_GHASTLING_SPAWN = register("entity.ghastling.spawn");
    public static final RegistrySupplier<SoundEvent> ENTITY_HAPPY_GHAST_AMBIENT = register("entity.happy_ghast.ambient");
    public static final RegistrySupplier<SoundEvent> ENTITY_HAPPY_GHAST_DEATH = register("entity.happy_ghast.death");
    public static final RegistrySupplier<SoundEvent> ENTITY_HAPPY_GHAST_HURT = register("entity.happy_ghast.hurt");
    public static final RegistrySupplier<SoundEvent> ENTITY_HAPPY_GHAST_RIDING = register("entity.happy_ghast.riding");
    public static final RegistrySupplier<SoundEvent> ENTITY_HAPPY_GHAST_EQUIP = register("entity.happy_ghast.equip");
    public static final RegistrySupplier<SoundEvent> ENTITY_HAPPY_GHAST_UNEQUIP = register("entity.happy_ghast.unequip");
    public static final RegistrySupplier<SoundEvent> ENTITY_HAPPY_GHAST_HARNESS_GOGGLES_UP = register("entity.happy_ghast.harness_goggles_up");
    public static final RegistrySupplier<SoundEvent> ENTITY_HAPPY_GHAST_HARNESS_GOGGLES_DOWN = register("entity.happy_ghast.harness_goggles_down");

    public static final Supplier<BlockSoundGroup> DRIED_GHAST = Suppliers.memoize(() -> new BlockSoundGroup(1.0F, 1.0F, BLOCK_DRIED_GHAST_BREAK.get(), BLOCK_DRIED_GHAST_STEP.get(), SoundEvents.INTENTIONALLY_EMPTY, BLOCK_DRIED_GHAST_HIT.get(), BLOCK_DRIED_GHAST_FALL.get()));

    public static RegistrySupplier<SoundEvent> register(String id) {
        return REGISTRY.register(id, () -> SoundEvent.of(Identifier.of(HappyGhastLegacy.MOD_ID, id)));
    }
}
