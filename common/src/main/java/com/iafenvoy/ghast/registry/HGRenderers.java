package com.iafenvoy.ghast.registry;

import com.iafenvoy.ghast.render.HappyGhastRenderer;
import com.iafenvoy.ghast.render.model.HappyGhastHarnessEntityModel;
import dev.architectury.registry.client.level.entity.EntityModelLayerRegistry;
import dev.architectury.registry.client.level.entity.EntityRendererRegistry;
import dev.architectury.registry.client.rendering.RenderTypeRegistry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.model.GhastEntityModel;


@Environment(EnvType.CLIENT)
public final class HGRenderers {
    public static void registerModelLayers() {
        EntityModelLayerRegistry.register(HGModelLayers.HAPPY_GHAST, GhastEntityModel::getTexturedModelData);
        EntityModelLayerRegistry.register(HGModelLayers.HAPPY_GHAST_HARNESS, HappyGhastHarnessEntityModel::getTexturedModelData);
    }

    public static void registerEntityRenderers() {
        EntityRendererRegistry.register(HGEntities.HAPPY_GHAST, HappyGhastRenderer::new);
    }

    public static void registerRenderLayers() {
        RenderTypeRegistry.register(RenderLayer.getCutout(), HGBlocks.DRIED_GHAST.get());
    }
}