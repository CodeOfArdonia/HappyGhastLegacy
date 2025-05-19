package com.iafenvoy.ghast.registry;

import com.iafenvoy.ghast.HappyGhastLegacy;
import net.minecraft.block.jukebox.JukeboxSong;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

public class HGSongs {
    public static final RegistryKey<JukeboxSong> TEARS = RegistryKey.of(RegistryKeys.JUKEBOX_SONG, Identifier.of(HappyGhastLegacy.MOD_ID, "tears"));
}
