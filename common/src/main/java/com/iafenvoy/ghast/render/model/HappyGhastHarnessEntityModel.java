package com.iafenvoy.ghast.render.model;


import com.iafenvoy.ghast.entity.HappyGhastEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;

@Environment(EnvType.CLIENT)
public class HappyGhastHarnessEntityModel extends EntityModel<HappyGhastEntity> {
    private final ModelPart modelPart;
    private final ModelPart goggles;

    public HappyGhastHarnessEntityModel(ModelPart modelPart) {
        this.goggles = modelPart.getChild("goggles");
        this.modelPart = modelPart;
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        modelPartData.addChild("harness", ModelPartBuilder.create().uv(0, 0).cuboid(-8.0F, -16.0F, -8.0F, 16.0F, 16.0F, 16.0F), ModelTransform.pivot(0.0F, 24.0F, 0.0F));
        modelPartData.addChild("goggles", ModelPartBuilder.create().uv(0, 32).cuboid(-8.0F, -2.5F, -2.5F, 16.0F, 5.0F, 5.0F, new Dilation(0.15F)), ModelTransform.pivot(0.0F, 14.0F, -5.5F));
        return TexturedModelData.of(modelData, 64, 64);
    }

    @Override
    public void setAngles(HappyGhastEntity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
        if (entity.hasPassengers()) {
            this.goggles.pitch = 0.0F;
            this.goggles.pivotY = 14.0F;
        } else {
            this.goggles.pitch = -0.7854F;
            this.goggles.pivotY = 9.0F;
        }
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, int color) {
        this.modelPart.render(matrices, vertices, light, overlay, color);
    }
}

