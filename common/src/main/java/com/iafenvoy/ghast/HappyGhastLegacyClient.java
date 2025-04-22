package com.iafenvoy.ghast;

import com.iafenvoy.ghast.registry.HGRenderers;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public final class HappyGhastLegacyClient {
    public static void init() {
        HGRenderers.registerEntityRenderers();
    }

    public static void process() {
        HGRenderers.registerRenderLayers();
    }
}