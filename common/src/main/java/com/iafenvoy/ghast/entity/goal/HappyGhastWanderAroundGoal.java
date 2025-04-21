package com.iafenvoy.ghast.entity.goal;

import com.iafenvoy.ghast.entity.HappyGhastEntity;
import net.minecraft.entity.ai.goal.WanderAroundGoal;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;

public class HappyGhastWanderAroundGoal extends WanderAroundGoal {
    private final HappyGhastEntity happyGhast;

    public HappyGhastWanderAroundGoal(HappyGhastEntity happyGhast) {
        super(happyGhast, 0.8D, 20);
        this.happyGhast = happyGhast;
    }

    @Override
    protected Vec3d getWanderTarget() {
        Random random = happyGhast.getRandom();
        double x = happyGhast.getX() + (random.nextFloat() * 2.0F - 1.0F) * 16.0F;
        double y = happyGhast.getY() + (random.nextFloat() * 2.0F - 1.0F) * 16.0F;
        double z = happyGhast.getZ() + (random.nextFloat() * 2.0F - 1.0F) * 16.0F;
        return new Vec3d(x, y, z);
    }

    @Override
    public boolean canStart() {
        if (!super.canStart()) return false;
        return !this.happyGhast.getBodyArmor().isEmpty();
    }
}
