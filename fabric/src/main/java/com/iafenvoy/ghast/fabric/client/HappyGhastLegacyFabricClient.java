package com.iafenvoy.ghast.fabric.client;

import com.iafenvoy.ghast.HappyGhastLegacyClient;
import net.fabricmc.api.ClientModInitializer;

public final class HappyGhastLegacyFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        HappyGhastLegacyClient.process();
    }
}
