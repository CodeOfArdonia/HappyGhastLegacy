package com.iafenvoy.ghast.render.model;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.*;
import net.minecraft.util.math.random.Random;

@Environment(EnvType.CLIENT)
public class HappyGhastEntityModel {
    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        modelPartData.addChild("body", ModelPartBuilder.create().uv(0, 0).cuboid(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F), ModelTransform.pivot(0.0F, 17.6F, 0.0F));
        Random random = Random.create(1660L);

        for (int i = 0; i < 9; ++i) {
            float f = (((float) (i % 3) - (float) (i / 3 % 2) * 0.5F + 0.25F) / 2.0F * 2.0F - 1.0F) * 5.0F;
            float g = ((float) (i / 3) / 2.0F * 2.0F - 1.0F) * 5.0F;
            int j = random.nextInt(5) + 4;
            modelPartData.addChild(getTentacleName(i), ModelPartBuilder.create().uv(0, 0).cuboid(-1.0F, 0.0F, -1.0F, 2.0F, (float) j, 2.0F), ModelTransform.pivot(f, 24.6F, g));
        }

        return TexturedModelData.of(modelData, 64, 32);
    }

    private static String getTentacleName(int index) {
        return "tentacle" + index;
    }
}
