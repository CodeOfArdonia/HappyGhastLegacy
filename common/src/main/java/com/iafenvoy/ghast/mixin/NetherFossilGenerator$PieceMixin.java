package com.iafenvoy.ghast.mixin;

import com.iafenvoy.ghast.registry.HGBlocks;
import net.minecraft.block.Block;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.structure.*;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Function;

@Mixin(NetherFossilGenerator.Piece.class)
public abstract class NetherFossilGenerator$PieceMixin extends SimpleStructurePiece {
    public NetherFossilGenerator$PieceMixin(StructurePieceType type, NbtCompound nbt, StructureTemplateManager structureTemplateManager, Function<Identifier, StructurePlacementData> placementDataGetter) {
        super(type, nbt, structureTemplateManager, placementDataGetter);
    }

    @Inject(method = "generate", at = @At("RETURN"))
    private void appendDriedGhast(StructureWorldAccess world, StructureAccessor structureAccessor, ChunkGenerator chunkGenerator, Random random, BlockBox chunkBox, ChunkPos chunkPos, BlockPos pivot, CallbackInfo ci) {
        BlockBox blockBox = this.template.calculateBoundingBox(this.placementData, this.pos);
        Random random2 = Random.create(world.getSeed()).nextSplitter().split(blockBox.getCenter());
        if (random2.nextFloat() < 0.5F) {
            int i = blockBox.getMinX() + random2.nextInt(blockBox.getBlockCountX());
            int j = blockBox.getMinY();
            int k = blockBox.getMinZ() + random2.nextInt(blockBox.getBlockCountZ());
            BlockPos blockPos = new BlockPos(i, j, k);
            if (world.getBlockState(blockPos).isAir() && chunkBox.contains(blockPos))
                world.setBlockState(blockPos, HGBlocks.DRIED_GHAST.get().getDefaultState().rotate(BlockRotation.random(random2)), Block.NOTIFY_LISTENERS);
        }
    }
}
