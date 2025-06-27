package com.iafenvoy.ghast.mixin;

import com.iafenvoy.ghast.registry.HGItems;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.FlyingEntity;
import net.minecraft.entity.mob.GhastEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GhastEntity.class)
public abstract class GhastEntityMixin extends FlyingEntity {
    @Shadow
    private static boolean isFireballFromPlayer(DamageSource damageSource) {
        return false;
    }

    protected GhastEntityMixin(EntityType<? extends FlyingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "damage", at = @At("HEAD"))
    private void addDiscDrop(ServerWorld world, DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (isFireballFromPlayer(source)) this.dropItem(world, HGItems.MUSIC_DISC_TEARS.get());
    }
}
