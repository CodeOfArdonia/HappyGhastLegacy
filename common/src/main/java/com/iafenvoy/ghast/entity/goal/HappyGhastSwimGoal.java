package com.iafenvoy.ghast.entity.goal;

import com.iafenvoy.ghast.entity.HappyGhastEntity;
import net.minecraft.entity.ai.goal.SwimGoal;

public class HappyGhastSwimGoal extends SwimGoal {
    private final HappyGhastEntity happyGhast;

    public HappyGhastSwimGoal(HappyGhastEntity happyGhast) {
        super(happyGhast);
        this.happyGhast = happyGhast;
    }

    @Override
    public boolean canStart() {
        return !happyGhast.hasPlayerOnTop() && super.canStart();
    }
}
