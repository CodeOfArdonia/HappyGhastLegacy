package com.iafenvoy.ghast.forge;

import com.iafenvoy.ghast.HappyGhastLegacy;
import com.iafenvoy.ghast.HappyGhastLegacyClient;
import dev.architectury.platform.Platform;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod(HappyGhastLegacy.MOD_ID)
@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
public final class HappyGhastLegacyForge {
    public HappyGhastLegacyForge() {
        HappyGhastLegacy.init();
        if (Platform.getEnv() == Dist.CLIENT)
            HappyGhastLegacyClient.init();
    }

    @SubscribeEvent
    public static void init(FMLCommonSetupEvent event) {
        event.enqueueWork(HappyGhastLegacy::process);
    }
}
