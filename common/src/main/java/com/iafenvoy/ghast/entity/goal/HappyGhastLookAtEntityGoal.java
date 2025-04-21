package com.iafenvoy.ghast.entity.goal;

import com.iafenvoy.ghast.entity.HappyGhastEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;

public class HappyGhastLookAtEntityGoal extends LookAtEntityGoal {
    private final HappyGhastEntity happyGhast;

    public HappyGhastLookAtEntityGoal(HappyGhastEntity happyGhast, Class<? extends LivingEntity> targetType, float range) {
        super(happyGhast, targetType, range);
        this.happyGhast = happyGhast;
    }

    @Override
    public boolean canStart() {
        if (!super.canStart()) return false;
        return !happyGhast.getBodyArmor().isEmpty();
    }
}
