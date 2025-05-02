package com.iafenvoy.ghast.mixin;

import com.iafenvoy.ghast.registry.HGBlocks;
import net.minecraft.entity.mob.PiglinBrain;
import net.minecraft.entity.mob.PiglinEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(PiglinBrain.class)
public class PiglinBrainMixin {
    @Inject(method = "getBarteredItem", at = @At("HEAD"), cancellable = true)
    private static void appendDriedGhastLoot(PiglinEntity piglin, CallbackInfoReturnable<List<ItemStack>> cir) {
        if (piglin.getRandom().nextDouble() < 10.0 / 469)
            cir.setReturnValue(List.of(new ItemStack(HGBlocks.DRIED_GHAST.get())));
    }
}
