package com.iafenvoy.ghast.render;

import com.iafenvoy.ghast.HappyGhastLegacy;
import com.iafenvoy.ghast.entity.HappyGhastEntity;
import com.iafenvoy.ghast.registry.HGModelLayers;
import com.iafenvoy.ghast.render.model.HappyGhastHarnessEntityModel;
import dev.architectury.platform.Platform;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.GhastEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

import java.util.Locale;

public class HappyGhastRenderer extends MobEntityRenderer<HappyGhastEntity, GhastEntityModel<HappyGhastEntity>> {
    private static final boolean SOW_LOADED = Platform.isModLoaded("sow");
    private static final Identifier TEXTURE = Identifier.of(HappyGhastLegacy.MOD_ID, "textures/entity/happy_ghast.png");
    private static final Identifier BABY_TEXTURE = Identifier.of(HappyGhastLegacy.MOD_ID, "textures/entity/ghastling.png");
    private static final Identifier KIKI_TEXTURE = Identifier.of(HappyGhastLegacy.MOD_ID, "textures/entity/kiki.png");

    public HappyGhastRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new GhastEntityModel<>(ctx.getPart(HGModelLayers.HAPPY_GHAST)), 0.9F);
        this.addFeature(new HarnessFeatureRenderer(this, new HappyGhastHarnessEntityModel(ctx.getPart(HGModelLayers.HAPPY_GHAST_HARNESS))));
        this.addFeature(new HappyGhastRopesFeatureRenderer(this));
    }

    @Override
    protected void scale(HappyGhastEntity entity, MatrixStack matrices, float f) {
        float scale = entity.isBaby() ? 1.6875F : 4.5F;
        matrices.scale(scale, scale, scale);
    }

    @Override
    public Identifier getTexture(HappyGhastEntity entity) {
        return SOW_LOADED || entity.getName().getString().toLowerCase(Locale.ROOT).equals("kiki") ? KIKI_TEXTURE : entity.isBaby() ? BABY_TEXTURE : TEXTURE;
    }
}