package com.iafenvoy.ghast.entity.goal;

import com.iafenvoy.ghast.entity.HappyGhastEntity;
import net.minecraft.entity.ai.goal.TemptGoal;
import net.minecraft.recipe.Ingredient;

public class HappyGhastTemptGoal extends TemptGoal {
    private final HappyGhastEntity happyGhast;

    public HappyGhastTemptGoal(HappyGhastEntity happyGhast, double speed, Ingredient food, boolean canBeScared) {
        super(happyGhast, speed, food, canBeScared);
        this.happyGhast = happyGhast;
    }

    @Override
    public void stop() {
        super.stop();
        this.happyGhast.rememberHomePos();
    }
}
