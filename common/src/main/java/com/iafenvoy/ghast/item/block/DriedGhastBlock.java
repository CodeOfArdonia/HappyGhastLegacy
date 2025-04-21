package com.iafenvoy.ghast.item.block;

import com.iafenvoy.ghast.entity.HappyGhastEntity;
import com.iafenvoy.ghast.registry.HGSounds;
import net.minecraft.block.*;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("deprecation")
public class DriedGhastBlock extends HorizontalFacingBlock implements Waterloggable {
    public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;
    public static final IntProperty HYDRATION = IntProperty.of("hydration", 0, 3);
    private static final VoxelShape SHAPE = createCuboidShape(3, 0, 3, 13, 10, 13);

    public DriedGhastBlock() {
        super(Settings.create().mapColor(MapColor.GRAY).breakInstantly().sounds(HGSounds.DRIED_GHAST.get()).nonOpaque().ticksRandomly());
        this.setDefaultState(this.stateManager.getDefaultState().with(FACING, Direction.NORTH).with(HYDRATION, 0).with(WATERLOGGED, false));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING, HYDRATION, WATERLOGGED);
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction facing, BlockState facingState, WorldAccess world, BlockPos currentPos, BlockPos facingPos) {
        if (state.get(WATERLOGGED))
            world.scheduleFluidTick(currentPos, Fluids.WATER, Fluids.WATER.getTickRate(world));
        return super.getStateForNeighborUpdate(state, facing, facingState, world, currentPos, facingPos);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    public int getHydration(BlockState state) {
        return state.get(HYDRATION);
    }

    private boolean isFullyHydrated(BlockState state) {
        return this.getHydration(state) == 3;
    }

    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (state.get(WATERLOGGED)) this.tickHydration(state, world, pos);
        else {
            int i = this.getHydration(state);
            if (i > 0)
                world.setBlockState(pos, state.with(HYDRATION, i - 1), Block.NOTIFY_LISTENERS);
        }
    }

    private void tickHydration(BlockState state, ServerWorld world, BlockPos pos) {
        if (!this.isFullyHydrated(state)) {
            world.playSound(null, pos, HGSounds.BLOCK_DRIED_GHAST_TRANSITION.get(), SoundCategory.BLOCKS, 1.0F, 1.0F);
            world.setBlockState(pos, state.with(HYDRATION, this.getHydration(state) + 1), Block.NOTIFY_LISTENERS);
        } else {
            this.spawnGhastling(world, pos, state);
        }
    }

    private void spawnGhastling(ServerWorld world, BlockPos pos, BlockState state) {
        world.removeBlock(pos, false);
        HappyGhastEntity ghast = HappyGhastEntity.generateChild(world, pos, SpawnReason.BREEDING, state.get(FACING).asRotation());
        world.spawnEntity(ghast);
        world.playSoundFromEntity(null, ghast, HGSounds.ENTITY_GHASTLING_SPAWN.get(), SoundCategory.BLOCKS, 1.0F, 1.0F);
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        double d = pos.getX() + 0.5;
        double e = pos.getY() + 0.5;
        double f = pos.getZ() + 0.5;
        if (!(Boolean) state.get(WATERLOGGED)) {
            if (random.nextInt(40) == 0)
                world.playSound(d, e, f, HGSounds.BLOCK_DRIED_GHAST_AMBIENT.get(), SoundCategory.AMBIENT, 1.0F, 1.0F, false);
            if (random.nextInt(6) == 0)
                world.addParticle(ParticleTypes.CLOUD, d, e, f, 0.0, 0.02, 0.0);
        } else {
            if (random.nextInt(40) == 0)
                world.playSound(d, e, f, HGSounds.BLOCK_DRIED_GHAST_AMBIENT_WATER.get(), SoundCategory.AMBIENT, 1.0F, 1.0F, false);
            if (random.nextInt(6) == 0)
                world.addParticle(ParticleTypes.HAPPY_VILLAGER, d + (random.nextFloat() * 2.0F - 1.0F) / 3.0F, e + 0.4, f + (random.nextFloat() * 2.0F - 1.0F) / 3.0F, 0.0, random.nextFloat(), 0.0);
        }
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if ((state.get(WATERLOGGED) || state.get(HYDRATION) > 0) && !world.getBlockTickScheduler().isQueued(pos, this))
            world.scheduleBlockTick(pos, this, 5000);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        FluidState fluidState = ctx.getWorld().getFluidState(ctx.getBlockPos());
        boolean bl = fluidState.isIn(FluidTags.WATER) && fluidState.getLevel() == 8;
        return this.getDefaultState().with(WATERLOGGED, bl).with(FACING, ctx.getHorizontalPlayerFacing().getOpposite());
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
    }

    @Override
    public boolean canFillWithFluid(BlockView world, BlockPos pos, BlockState state, Fluid fluid) {
        return fluid == Fluids.WATER || fluid == Fluids.FLOWING_WATER;
    }

    @Override
    public boolean tryFillWithFluid(WorldAccess world, BlockPos pos, BlockState state, FluidState fluidState) {
        if (!state.get(Properties.WATERLOGGED) && fluidState.isIn(FluidTags.WATER)) {
            if (!world.isClient()) {
                if (fluidState.getFluid() == Fluids.FLOWING_WATER) {
                    Block.dropStacks(state, world, pos, null);
                    world.setBlockState(pos, fluidState.getBlockState(), Block.NOTIFY_ALL);
                } else {
                    world.setBlockState(pos, state.with(Properties.WATERLOGGED, true), Block.NOTIFY_ALL);
                    world.scheduleFluidTick(pos, fluidState.getFluid(), fluidState.getFluid().getTickRate(world));
                    world.playSound(null, pos, HGSounds.BLOCK_DRIED_GHAST_PLACE_IN_WATER.get(), SoundCategory.BLOCKS, 1.0F, 1.0F);
                }
            }
            return true;
        } else return false;
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        super.onPlaced(world, pos, state, placer, itemStack);
        world.playSound(null, pos, (state.get(WATERLOGGED) ? HGSounds.BLOCK_DRIED_GHAST_PLACE_IN_WATER : HGSounds.BLOCK_DRIED_GHAST_PLACE).get(), SoundCategory.BLOCKS, 1.0F, 1.0F);
    }

    @Override
    public boolean canPathfindThrough(BlockState state, BlockView world, BlockPos pos, NavigationType type) {
        return false;
    }
}