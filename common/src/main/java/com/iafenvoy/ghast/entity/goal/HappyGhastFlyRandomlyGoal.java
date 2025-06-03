package com.iafenvoy.ghast.entity.goal;

import com.iafenvoy.ghast.entity.HappyGhastEntity;
import net.minecraft.entity.ai.control.MoveControl;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.random.Random;

import java.util.EnumSet;

public class HappyGhastFlyRandomlyGoal extends Goal {
    private final HappyGhastEntity happyGhast;

    public HappyGhastFlyRandomlyGoal(HappyGhastEntity happyGhast) {
        this.happyGhast = happyGhast;
        this.setControls(EnumSet.of(Control.MOVE));
    }

    @Override
    public boolean canStart() {
        if (this.happyGhast.isFollowingPlayer()) return false;
        MoveControl moveControl = this.happyGhast.getMoveControl();
        if (!moveControl.isMoving())
            return true;
        else {
            double d = moveControl.getTargetX() - this.happyGhast.getX();
            double e = moveControl.getTargetY() - this.happyGhast.getY();
            double f = moveControl.getTargetZ() - this.happyGhast.getZ();
            double g = d * d + e * e + f * f;
            return g < 1.0 || g > 3600.0;
        }
    }

    @Override
    public boolean shouldContinue() {
        return false;
    }

    @Override
    public void start() {
        Random random = this.happyGhast.getRandom();
        double d = this.happyGhast.getX() + (double) ((random.nextFloat() * 2.0F - 1.0F) * 16.0F);
        double e = this.happyGhast.getY() + (double) ((random.nextFloat() * 2.0F - 1.0F) * 16.0F);
        double f = this.happyGhast.getZ() + (double) ((random.nextFloat() * 2.0F - 1.0F) * 16.0F);
        this.happyGhast.getMoveControl().moveTo(d, e - 8, f, 0.1);
    }
}
