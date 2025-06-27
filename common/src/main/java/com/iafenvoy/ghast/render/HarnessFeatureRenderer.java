package com.iafenvoy.ghast.render;

import com.iafenvoy.ghast.HappyGhastLegacy;
import com.iafenvoy.ghast.entity.HappyGhastEntity;
import com.iafenvoy.ghast.item.HarnessItem;
import com.iafenvoy.ghast.render.model.HappyGhastHarnessEntityModel;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.GhastEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

class HarnessFeatureRenderer extends FeatureRenderer<HappyGhastEntity, GhastEntityModel<HappyGhastEntity>> {

    private final HappyGhastHarnessEntityModel model;

    public HarnessFeatureRenderer(FeatureRendererContext<HappyGhastEntity, GhastEntityModel<HappyGhastEntity>> ctx, HappyGhastHarnessEntityModel model) {
        super(ctx);
        this.model = model;
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, HappyGhastEntity entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
        ItemStack stack = entity.getBodyArmor();
        if (!stack.isEmpty() && !entity.isBaby() && stack.getItem() instanceof HarnessItem harness) {
            VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getEntityCutoutNoCull(Identifier.of(HappyGhastLegacy.MOD_ID, "textures/entity/equipment/%s_harness.png".formatted(harness.getColor().asString()))));
            matrices.push();
            matrices.scale(1.05F, 1.05F, 1.05F);
            matrices.translate(0.0F, 0.05F, 0.0F);
            this.model.setAngles(entity, limbAngle, limbDistance, animationProgress, headYaw, headPitch);
            this.model.render(matrices, vertexConsumer, light, LivingEntityRenderer.getOverlay(entity, 0), -1);
            matrices.pop();
        }
    }
}
