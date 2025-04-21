package com.iafenvoy.ghast.entity.goal;

import com.iafenvoy.ghast.item.HarnessItem;
import net.minecraft.entity.ai.goal.TemptGoal;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.item.ItemConvertible;
import net.minecraft.recipe.Ingredient;

public class FollowHarnessGoal extends TemptGoal {
    public FollowHarnessGoal(PathAwareEntity entity) {
        super(entity, 1, Ingredient.ofItems(HarnessItem.getAll().toArray(ItemConvertible[]::new)), false);
    }

    @Override
    public boolean canStart() {
        return !this.mob.isBaby() && super.canStart();
    }
}
