package com.iafenvoy.ghast.registry;

import com.iafenvoy.ghast.HappyGhastLegacy;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;

public final class HGModelLayers {
    public static final EntityModelLayer HAPPY_GHAST = new EntityModelLayer(Identifier.of(HappyGhastLegacy.MOD_ID, "happy_ghast"), "main");
    public static final EntityModelLayer HAPPY_GHAST_HARNESS = new EntityModelLayer(Identifier.of(HappyGhastLegacy.MOD_ID, "happy_ghast_harness"), "main");

}