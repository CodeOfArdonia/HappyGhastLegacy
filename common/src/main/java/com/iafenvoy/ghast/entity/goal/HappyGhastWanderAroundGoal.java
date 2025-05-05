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
        Random random = this.happyGhast.getRandom();
        double x = this.happyGhast.getX() + (random.nextFloat() * 2.0F - 1.0F) * 16.0F;
        double y = this.happyGhast.getY() + (random.nextFloat() * 2.0F - 1.0F) * 16.0F;
        double z = this.happyGhast.getZ() + (random.nextFloat() * 2.0F - 1.0F) * 16.0F;
        Vec3d target = new Vec3d(x, y, z);
        if (this.happyGhast.outOfWanderRange())
            target = Vec3d.ofCenter(this.happyGhast.getHomePos().pos());
        return target;
    }

    @Override
    public boolean canStart() {
        if (!super.canStart()) return false;
        return !this.happyGhast.getBodyArmor().isEmpty();
    }
}
