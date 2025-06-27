package com.iafenvoy.ghast.mixin;

import com.iafenvoy.ghast.entity.HappyGhastEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(Camera.class)
public abstract class CameraMixin {
    @Shadow
    protected abstract void moveBy(float f, float g, float h);

    @Shadow
    protected abstract float clipToSpace(float g);

    @Inject(method = "update", at = @At("RETURN"))
    private void adjustWhenOnGhast(BlockView area, Entity focusedEntity, boolean thirdPerson, boolean inverseView, float tickDelta, CallbackInfo ci) {
        PlayerEntity player = MinecraftClient.getInstance().player;
        if (thirdPerson && player != null && player.getVehicle() instanceof HappyGhastEntity)
            this.moveBy(-this.clipToSpace(4), 0, 0);
    }
}
