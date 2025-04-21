package com.iafenvoy.ghast.mixin;

import com.iafenvoy.ghast.entity.HappyGhastEntity;
import com.iafenvoy.ghast.sound.HappyGhastRidingSoundInstance;
import com.mojang.authlib.GameProfile;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(ClientPlayerEntity.class)
public abstract class ClientPlayerEntityMixin extends PlayerEntity {
    @Shadow
    @Final
    protected MinecraftClient client;

    public ClientPlayerEntityMixin(World world, BlockPos pos, float yaw, GameProfile gameProfile) {
        super(world, pos, yaw, gameProfile);
    }

    @Inject(method = "startRiding", at = @At("RETURN"))
    private void onClientStartRiding(Entity entity, boolean force, CallbackInfoReturnable<Boolean> cir) {
        if (super.startRiding(entity, force) && entity instanceof HappyGhastEntity happyGhast)
            this.client.getSoundManager().play(new HappyGhastRidingSoundInstance(this, happyGhast));
    }
}
