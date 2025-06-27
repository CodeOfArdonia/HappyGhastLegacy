package com.iafenvoy.ghast.render;

import com.iafenvoy.ghast.HappyGhastLegacy;
import com.iafenvoy.ghast.entity.HappyGhastEntity;
import com.iafenvoy.ghast.registry.HGModelLayers;
import com.iafenvoy.ghast.render.model.HappyGhastHarnessEntityModel;
import com.iafenvoy.ghast.render.state.HappyGhastRenderState;
import dev.architectury.platform.Platform;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.model.GhastEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Box;

import java.util.Locale;

public class HappyGhastRenderer extends MobEntityRenderer<HappyGhastEntity, HappyGhastRenderState, GhastEntityModel> {
    private static final boolean SOW_LOADED = Platform.isModLoaded("sow");
    private static final Identifier TEXTURE = Identifier.of(HappyGhastLegacy.MOD_ID, "textures/entity/happy_ghast.png");
    private static final Identifier BABY_TEXTURE = Identifier.of(HappyGhastLegacy.MOD_ID, "textures/entity/ghastling.png");
    private static final Identifier KIKI_TEXTURE = Identifier.of(HappyGhastLegacy.MOD_ID, "textures/entity/kiki.png");

    public HappyGhastRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new GhastEntityModel(ctx.getPart(HGModelLayers.HAPPY_GHAST)), 0.9F);
        this.addFeature(new HarnessFeatureRenderer(this, new HappyGhastHarnessEntityModel(ctx.getPart(HGModelLayers.HAPPY_GHAST_HARNESS))));
        this.addFeature(new HappyGhastRopesFeatureRenderer(this));
    }

    @Override
    public HappyGhastRenderState createRenderState() {
        return new HappyGhastRenderState();
    }

    @Override
    protected void scale(HappyGhastRenderState state, MatrixStack matrices) {
        float scale = state.baby ? 0.375F : 1;
        matrices.scale(scale, scale, scale);
    }

    @Override
    protected Box getBoundingBox(HappyGhastEntity happyGhastEntity) {
        Box box = super.getBoundingBox(happyGhastEntity);
        float f = happyGhastEntity.getHeight();
        return box.withMinY(box.minY - (double) (f / 2.0F));
    }

    @Override
    public Identifier getTexture(HappyGhastRenderState state) {
        return SOW_LOADED || state.name.getString().toLowerCase(Locale.ROOT).equals("kiki") ? KIKI_TEXTURE : state.baby ? BABY_TEXTURE : TEXTURE;
    }

    @Override
    public void updateRenderState(HappyGhastEntity entity, HappyGhastRenderState state, float f) {
        super.updateRenderState(entity, state, f);
        state.passengers = entity.hasPassengers();
        state.baby = entity.isBaby();
        state.name = entity.getName();
        state.bodyArmor = entity.getBodyArmor();
    }
}