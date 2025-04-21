package com.iafenvoy.ghast.fabric;

import net.fabricmc.api.ModInitializer;

import com.iafenvoy.ghast.HappyGhastLegacy;

public final class HappyGhastLegacyFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        HappyGhastLegacy.init();
        HappyGhastLegacy.process();
    }
}
