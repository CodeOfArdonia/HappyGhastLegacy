package com.iafenvoy.ghast.render;

import com.iafenvoy.ghast.HappyGhastLegacy;
import com.iafenvoy.ghast.item.HarnessItem;
import com.iafenvoy.ghast.render.model.HappyGhastHarnessEntityModel;
import com.iafenvoy.ghast.render.state.HappyGhastRenderState;
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

class HarnessFeatureRenderer extends FeatureRenderer<HappyGhastRenderState, GhastEntityModel> {

    private final HappyGhastHarnessEntityModel model;

    public HarnessFeatureRenderer(FeatureRendererContext<HappyGhastRenderState, GhastEntityModel> ctx, HappyGhastHarnessEntityModel model) {
        super(ctx);
        this.model = model;
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, HappyGhastRenderState state, float limbAngle, float limbDistance) {
        ItemStack stack = state.bodyArmor;
        if (!stack.isEmpty() && !state.baby && stack.getItem() instanceof HarnessItem harness) {
            VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getEntityCutoutNoCull(Identifier.of(HappyGhastLegacy.MOD_ID, "textures/entity/equipment/%s_harness.png".formatted(harness.getColor().asString()))));
            matrices.push();
            matrices.scale(4.725F, 4.725F, 4.725F);
            matrices.translate(0, -1.05, 0);
            this.model.setAngles(state);
            this.model.render(matrices, vertexConsumer, light, LivingEntityRenderer.getOverlay(state, 0), -1);
            matrices.pop();
        }
    }
}
