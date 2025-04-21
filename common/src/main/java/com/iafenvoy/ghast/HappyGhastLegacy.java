package com.iafenvoy.ghast;

import com.iafenvoy.ghast.registry.HGBlocks;
import com.iafenvoy.ghast.registry.HGEntities;
import com.iafenvoy.ghast.registry.HGItems;
import com.iafenvoy.ghast.registry.HGSounds;

public final class HappyGhastLegacy {
    public static final String MOD_ID = "happy_ghast_legacy";

    public static void init() {
        HGSounds.REGISTRY.register();
        HGBlocks.REGISTRY.register();
        HGEntities.REGISTRY.register();
        HGItems.REGISTRY.register();

        HGEntities.init();
    }

    public static void process() {
        HGItems.init();
    }
}
