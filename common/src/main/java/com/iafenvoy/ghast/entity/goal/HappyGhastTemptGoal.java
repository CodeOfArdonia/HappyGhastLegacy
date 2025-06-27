package com.iafenvoy.ghast.entity.goal;

import com.iafenvoy.ghast.entity.HappyGhastEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.recipe.Ingredient;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.function.Predicate;

public class HappyGhastTemptGoal extends Goal {
    private final Predicate<Entity> predicate;
    protected final HappyGhastEntity happyGhast;
    private final double speed;
    @Nullable
    protected PlayerEntity closestPlayer;
    private final Ingredient food;
    private int cooldown;
    private boolean active;

    public HappyGhastTemptGoal(HappyGhastEntity entity, Ingredient food) {
        this.happyGhast = entity;
        this.speed = 1.2;
        this.food = food;
        this.setControls(EnumSet.of(Control.MOVE, Control.LOOK));
        this.predicate = this::isTemptedBy;
    }

    @Override
    public boolean canStart() {
        if (this.cooldown > 0) {
            --this.cooldown;
            return false;
        } else {
            this.closestPlayer = this.happyGhast.getWorld().getClosestPlayer(this.happyGhast.getX(), this.happyGhast.getY(), this.happyGhast.getZ(), 32, this.predicate);
            return this.closestPlayer != null;
        }
    }

    private boolean isTemptedBy(Entity entity) {
        return entity instanceof LivingEntity living && (this.food.test(living.getMainHandStack()) || this.food.test(living.getOffHandStack()));
    }

    @Override
    public boolean shouldContinue() {
        return this.canStart();
    }

    @Override
    public void start() {
        super.start();
        this.active = true;
    }

    @Override
    public void stop() {
        this.closestPlayer = null;
        this.happyGhast.getNavigation().stop();
        this.cooldown = toGoalTicks(100);
        this.happyGhast.rememberHomePos();
        this.active = false;
    }

    @Override
    public void tick() {
        if (this.happyGhast.getRandom().nextInt(8) == 0) {
            this.happyGhast.getLookControl().lookAt(this.closestPlayer, (float) (this.happyGhast.getMaxHeadRotation() + 20), (float) this.happyGhast.getMaxLookPitchChange());
            this.happyGhast.getNavigation().startMovingTo(this.closestPlayer, this.speed);
        }
    }

    public boolean isActive() {
        return this.active;
    }
}
