package com.iafenvoy.ghast.render;

import com.iafenvoy.ghast.HappyGhastLegacy;
import com.iafenvoy.ghast.registry.HGTags;
import com.iafenvoy.ghast.render.state.HappyGhastRenderState;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.GhastEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class HappyGhastRopesFeatureRenderer extends FeatureRenderer<HappyGhastRenderState, GhastEntityModel> {
    private static final Identifier ROPES_TEXTURE = Identifier.of(HappyGhastLegacy.MOD_ID, "textures/entity/happy_ghast_ropes.png");

    public HappyGhastRopesFeatureRenderer(FeatureRendererContext<HappyGhastRenderState, GhastEntityModel> ctx) {
        super(ctx);
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, HappyGhastRenderState state, float limbAngle, float limbDistance) {
        if (state.bodyArmor.isIn(HGTags.HARNESS) && state.leashData != null)
            this.getContextModel().render(matrices, vertexConsumers.getBuffer(RenderLayer.getEntityCutoutNoCull(ROPES_TEXTURE)), light, OverlayTexture.DEFAULT_UV, -1);
    }
}
