package com.iafenvoy.ghast.render;

import com.iafenvoy.ghast.HappyGhastLegacy;
import com.iafenvoy.ghast.entity.HappyGhastEntity;
import com.iafenvoy.ghast.registry.HGTags;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.GhastEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class HappyGhastRopesFeatureRenderer extends FeatureRenderer<HappyGhastEntity, GhastEntityModel<HappyGhastEntity>> {
    private static final Identifier ROPES_TEXTURE = Identifier.of(HappyGhastLegacy.MOD_ID, "textures/entity/happy_ghast_ropes.png");

    public HappyGhastRopesFeatureRenderer(FeatureRendererContext<HappyGhastEntity, GhastEntityModel<HappyGhastEntity>> ctx) {
        super(ctx);
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, HappyGhastEntity entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
        if (entity.getBodyArmor().isIn(HGTags.HARNESS) && entity.hasRopes())
            this.getContextModel().render(matrices, vertexConsumers.getBuffer(RenderLayer.getEntityCutoutNoCull(ROPES_TEXTURE)), light, OverlayTexture.DEFAULT_UV, -1);
    }
}
