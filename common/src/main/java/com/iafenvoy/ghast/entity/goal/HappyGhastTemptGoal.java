package com.iafenvoy.ghast.entity.goal;

import com.iafenvoy.ghast.entity.HappyGhastEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.recipe.Ingredient;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;

public class HappyGhastTemptGoal extends Goal {
    private static final TargetPredicate TEMPTING_ENTITY_PREDICATE = TargetPredicate.createNonAttackable().setBaseMaxDistance(100).ignoreVisibility();
    private final TargetPredicate predicate;
    protected final HappyGhastEntity happyGhast;
    private final double speed;
    @Nullable
    protected PlayerEntity closestPlayer;
    private int cooldown;
    private final Ingredient food;

    public HappyGhastTemptGoal(HappyGhastEntity entity, Ingredient food) {
        this.happyGhast = entity;
        this.speed = 1;
        this.food = food;
        this.setControls(EnumSet.of(Control.MOVE, Control.LOOK));
        this.predicate = TEMPTING_ENTITY_PREDICATE.copy().setPredicate(this::isTemptedBy);
    }

    @Override
    public boolean canStart() {
        if (this.cooldown > 0) {
            --this.cooldown;
            return false;
        } else {
            this.closestPlayer = this.happyGhast.getWorld().getClosestPlayer(this.predicate, this.happyGhast);
            return this.closestPlayer != null;
        }
    }

    private boolean isTemptedBy(LivingEntity entity) {
        return this.food.test(entity.getMainHandStack()) || this.food.test(entity.getOffHandStack());
    }

    @Override
    public boolean shouldContinue() {
        return this.canStart();
    }

    @Override
    public void stop() {
        this.closestPlayer = null;
        this.happyGhast.getNavigation().stop();
        this.cooldown = toGoalTicks(100);
        this.happyGhast.rememberHomePos();
    }

    @Override
    public void tick() {
        this.happyGhast.getLookControl().lookAt(this.closestPlayer, (float) (this.happyGhast.getMaxHeadRotation() + 20), (float) this.happyGhast.getMaxLookPitchChange());
        this.happyGhast.getNavigation().startMovingTo(this.closestPlayer, this.speed);
    }
}
