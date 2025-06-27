package com.iafenvoy.ghast.render.state;

import net.minecraft.client.render.entity.state.GhastEntityRenderState;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

public class HappyGhastRenderState extends GhastEntityRenderState {
    public boolean passengers;
    public boolean baby;
    public Text name = Text.empty();
    public ItemStack bodyArmor = ItemStack.EMPTY;
}
