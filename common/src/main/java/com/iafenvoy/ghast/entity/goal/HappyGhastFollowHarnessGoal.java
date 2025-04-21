package com.iafenvoy.ghast.entity.goal;

import com.iafenvoy.ghast.entity.HappyGhastEntity;
import com.iafenvoy.ghast.item.HarnessItem;
import net.minecraft.item.ItemConvertible;
import net.minecraft.recipe.Ingredient;

public class HappyGhastFollowHarnessGoal extends HappyGhastTemptGoal {
    public HappyGhastFollowHarnessGoal(HappyGhastEntity happyGhast) {
        super(happyGhast, 1, Ingredient.ofItems(HarnessItem.getAll().toArray(ItemConvertible[]::new)), false);
    }

    @Override
    public boolean canStart() {
        return !this.mob.isBaby() && super.canStart();
    }
}
