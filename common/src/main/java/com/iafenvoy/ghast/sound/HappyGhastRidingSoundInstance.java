package com.iafenvoy.ghast.sound;

import com.iafenvoy.ghast.entity.HappyGhastEntity;
import com.iafenvoy.ghast.registry.HGSounds;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.sound.MovingSoundInstance;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;

@Environment(EnvType.CLIENT)
public class HappyGhastRidingSoundInstance extends MovingSoundInstance {
    private final PlayerEntity player;
    private final HappyGhastEntity happyGhast;

    public HappyGhastRidingSoundInstance(PlayerEntity player, HappyGhastEntity happyGhast) {
        super(HGSounds.ENTITY_HAPPY_GHAST_RIDING.get(), happyGhast.getSoundCategory(), SoundInstance.createRandom());
        this.player = player;
        this.happyGhast = happyGhast;
        this.attenuationType = SoundInstance.AttenuationType.NONE;
        this.repeat = true;
        this.repeatDelay = 0;
        this.volume = 0.0F;
    }

    @Override
    public boolean shouldAlwaysPlay() {
        return true;
    }

    @Override
    public void tick() {
        if (!this.happyGhast.isRemoved() && this.player.hasVehicle() && this.player.getVehicle() == this.happyGhast) {
            float f = (float) this.happyGhast.getVelocity().length();
            if (f >= 0.01F) this.volume = 5.0F * MathHelper.clampedLerp(0.0F, 1.0F, f);
            else this.volume = 0.0F;
        } else this.setDone();
    }
}

