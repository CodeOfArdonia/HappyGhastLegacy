package com.iafenvoy.ghast.registry;

import com.iafenvoy.ghast.render.HappyGhastRenderer;
import dev.architectury.registry.client.level.entity.EntityRendererRegistry;
import dev.architectury.registry.client.rendering.RenderTypeRegistry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.RenderLayer;


@Environment(EnvType.CLIENT)
public final class HGRenderers {
    public static void registerRenderLayers() {
        RenderTypeRegistry.register(RenderLayer.getCutout(), HGBlocks.DRIED_GHAST.get());
    }

    public static void registerEntityRenderers() {
        EntityRendererRegistry.register(HGEntities.HAPPY_GHAST, HappyGhastRenderer::new);
    }
}