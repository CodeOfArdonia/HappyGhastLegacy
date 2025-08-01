package com.iafenvoy.ghast.entity.goal;

import com.iafenvoy.ghast.entity.HappyGhastEntity;
import net.minecraft.entity.ai.control.LookControl;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.pathing.BirdNavigation;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.ai.pathing.MobNavigation;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.entity.mob.MobEntity;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.List;
import java.util.function.Predicate;

public class FollowEntityPredicateGoal extends Goal {
    private final HappyGhastEntity mob;
    private final Predicate<MobEntity> targetPredicate;
    @Nullable
    private MobEntity target;
    private final double speed;
    private final EntityNavigation navigation;
    private int updateCountdownTicks;
    private final float minDistance;
    private float oldWaterPathFindingPenalty;
    private final float maxDistance;

    public FollowEntityPredicateGoal(HappyGhastEntity mob, Predicate<MobEntity> predicate, double speed, float minDistance, float maxDistance) {
        this.mob = mob;
        this.targetPredicate = predicate;
        this.speed = speed;
        this.navigation = mob.getNavigation();
        this.minDistance = minDistance;
        this.maxDistance = maxDistance;
        this.setControls(EnumSet.of(Control.MOVE, Control.LOOK));
        if (!(mob.getNavigation() instanceof MobNavigation) && !(mob.getNavigation() instanceof BirdNavigation))
            throw new IllegalArgumentException("Unsupported mob type for FollowMobGoal");
    }

    @Override
    public boolean canStart() {
        List<MobEntity> list = this.mob.getWorld().getEntitiesByClass(MobEntity.class, this.mob.getBoundingBox().expand(this.maxDistance), this.targetPredicate);
        if (!list.isEmpty())
            for (MobEntity mobEntity : list)
                if (!mobEntity.isInvisible()) {
                    this.target = mobEntity;
                    return true;
                }
        return false;
    }

    @Override
    public boolean shouldContinue() {
        return this.target != null && !this.navigation.isIdle() && this.mob.squaredDistanceTo(this.target) > (double) (this.minDistance * this.minDistance);
    }

    @Override
    public void start() {
        this.updateCountdownTicks = 0;
        this.oldWaterPathFindingPenalty = this.mob.getPathfindingPenalty(PathNodeType.WATER);
        this.mob.setPathfindingPenalty(PathNodeType.WATER, 0.0F);
    }

    @Override
    public void stop() {
        this.target = null;
        this.navigation.stop();
        this.mob.setPathfindingPenalty(PathNodeType.WATER, this.oldWaterPathFindingPenalty);
        this.mob.rememberHomePos();
    }

    @Override
    public void tick() {
        if (this.target != null && !this.mob.isLeashed()) {
            this.mob.getLookControl().lookAt(this.target, 10.0F, this.mob.getMaxLookPitchChange());
            if (--this.updateCountdownTicks <= 0) {
                this.updateCountdownTicks = this.getTickCount(10);
                double d = this.mob.getX() - this.target.getX();
                double e = this.mob.getY() - this.target.getY();
                double f = this.mob.getZ() - this.target.getZ();
                double g = d * d + e * e + f * f;
                if (g > this.minDistance * this.minDistance)
                    this.navigation.startMovingTo(this.target, this.speed);
                else {
                    this.navigation.stop();
                    LookControl lookControl = this.target.getLookControl();
                    if (g <= this.minDistance || lookControl.getLookX() == this.mob.getX() && lookControl.getLookY() == this.mob.getY() && lookControl.getLookZ() == this.mob.getZ()) {
                        double h = this.target.getX() - this.mob.getX();
                        double i = this.target.getZ() - this.mob.getZ();
                        this.navigation.startMovingTo(this.mob.getX() - h, this.mob.getY(), this.mob.getZ() - i, this.speed);
                    }
                }
            }
        }
    }
}
