package com.iafenvoy.ghast.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DyeColor;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class HarnessItem extends Item {
    private static final Map<DyeColor, HarnessItem> BY_COLOR = new HashMap<>();
    private final DyeColor color;

    public HarnessItem(DyeColor color) {
        super(new Settings().maxCount(1));
        this.color = color;
        BY_COLOR.put(this.color, this);
    }

    @Override
    public int getMaxUseTime(ItemStack stack, LivingEntity user) {
        return 0;
    }

    public DyeColor getColor() {
        return this.color;
    }

    public static Stream<HarnessItem> getAll() {
        return BY_COLOR.values().stream();
    }
}