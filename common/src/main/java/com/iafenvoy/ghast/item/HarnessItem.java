package com.iafenvoy.ghast.item;

import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.DispenserBehavior;
import net.minecraft.block.dispenser.ItemDispenserBehavior;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.BlockPointer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class HarnessItem extends Item {
    private static final Map<DyeColor, HarnessItem> BY_COLOR = new HashMap<>();
    private final DyeColor color;
    public static final DispenserBehavior DISPENSER_BEHAVIOR = new ItemDispenserBehavior() {
        @Override
        protected ItemStack dispenseSilently(BlockPointer pointer, ItemStack stack) {
            return dispenseArmor(pointer, stack) ? stack : super.dispenseSilently(pointer, stack);
        }
    };

    public HarnessItem(DyeColor color) {
        super(new Settings().maxCount(1));
        this.color = color;
        BY_COLOR.put(this.color, this);
        DispenserBlock.registerBehavior(this, DISPENSER_BEHAVIOR);
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

    public static boolean dispenseArmor(BlockPointer pointer, ItemStack armor) {
        BlockPos blockPos = pointer.pos().offset(pointer.state().get(DispenserBlock.FACING));
        List<HappyGhastEntity> list = pointer.world().getEntitiesByClass(HappyGhastEntity.class, new Box(blockPos), happyGhast -> happyGhast.getBodyArmor().isEmpty());
        if (list.isEmpty()) return false;
        else {
            list.get(0).setBodyArmor(armor.split(1));
            return true;
        }
    }
}