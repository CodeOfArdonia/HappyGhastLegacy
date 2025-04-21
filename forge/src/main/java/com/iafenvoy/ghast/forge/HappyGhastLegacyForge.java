package com.iafenvoy.ghast.forge;

import com.iafenvoy.ghast.HappyGhastLegacy;
import dev.architectury.platform.forge.EventBuses;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(HappyGhastLegacy.MOD_ID)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
@SuppressWarnings("removal")
public final class HappyGhastLegacyForge {
    public HappyGhastLegacyForge() {
        EventBuses.registerModEventBus(HappyGhastLegacy.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
        HappyGhastLegacy.init();
    }

    @SubscribeEvent
    public static void init(FMLCommonSetupEvent event) {
        event.enqueueWork(HappyGhastLegacy::process);
    }
}
