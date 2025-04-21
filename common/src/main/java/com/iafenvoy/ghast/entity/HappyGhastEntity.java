package com.iafenvoy.ghast.entity;

import com.google.common.collect.ImmutableList;
import com.iafenvoy.ghast.entity.goal.*;
import com.iafenvoy.ghast.item.HarnessItem;
import com.iafenvoy.ghast.mixin.LivingEntityAccessor;
import com.iafenvoy.ghast.registry.HGEntities;
import com.iafenvoy.ghast.registry.HGSounds;
import com.iafenvoy.ghast.registry.HGTags;
import com.mojang.serialization.Dynamic;
import it.unimi.dsi.fastutil.floats.FloatFloatImmutablePair;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.brain.Activity;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.control.FlightMoveControl;
import net.minecraft.entity.ai.goal.LookAroundGoal;
import net.minecraft.entity.ai.pathing.BirdNavigation;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.thrown.PotionEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.recipe.Ingredient;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.*;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedList;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;

public class HappyGhastEntity extends AnimalEntity {
    private static final double STANDARD_RIDER_OFFSET = 1.35;
    private static final int ROTATION_DELAY_TICKS = 20;
    private final Queue<FloatFloatImmutablePair> rotationCache = new LinkedList<>();

    public HappyGhastEntity(EntityType<HappyGhastEntity> type, World world) {
        super(type, world);
        this.setStepHeight(0.6F);
        this.experiencePoints = 0;
        this.setAiDisabled(false);
        this.setPersistent();
        this.moveControl = new FlightMoveControl(this, 10, true);
        this.setEquipmentDropChance(EquipmentSlot.CHEST, 1);
    }

    @Override
    protected Brain.Profile<HappyGhastEntity> createBrainProfile() {
        return Brain.createProfile(ImmutableList.of(MemoryModuleType.HOME), ImmutableList.of());
    }

    @Override
    protected Brain<?> deserializeBrain(Dynamic<?> dynamic) {
        return create(this.createBrainProfile().deserialize(dynamic));
    }

    protected static Brain<?> create(Brain<HappyGhastEntity> brain) {
        brain.setCoreActivities(Set.of(Activity.CORE));
        brain.setDefaultActivity(Activity.IDLE);
        brain.resetPossibleActivities();
        return brain;
    }

    @Override
    protected void onGrowUp() {
        if (this.isBaby()) this.navigation = new GhastlingNavigation(this, this.getWorld());
        else this.navigation = this.createNavigation(this.getWorld());
        super.onGrowUp();
    }

    @Override
    public Vec3d updatePassengerForDismount(LivingEntity p_20123_) {
        return new Vec3d(this.getX(), this.getBoundingBox().maxY + 0.45D, this.getZ());
    }

    @Override
    public @Nullable PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
        return generateChild(world, entity.getBlockPos(), SpawnReason.BREEDING, 0);
    }

    @Override
    protected EntityNavigation createNavigation(World world) {
        return new BirdNavigation(this, world);
    }

    @Override
    protected void initGoals() {
        super.initGoals();
        this.goalSelector.add(1, new HappyGhastTemptGoal(this, 1, Ingredient.ofItems(Items.SNOWBALL), false));
        this.goalSelector.add(1, new HappyGhastFollowHarnessGoal(this));
        this.goalSelector.add(2, new HappyGhastLookAtEntityGoal(this, PlayerEntity.class, 128.0F));
        this.goalSelector.add(4, new HappyGhastWanderAroundGoal(this));
        this.goalSelector.add(5, new LookAroundGoal(this));
        this.goalSelector.add(6, new HappyGhastSwimGoal(this));
    }

    @Override
    public boolean canImmediatelyDespawn(double distanceToClosestPlayer) {
        return false;
    }

    @Override
    public double getMountedHeightOffset() {
        return super.getMountedHeightOffset() + 0.5D;
    }

    @Override
    public SoundEvent getAmbientSound() {
        return this.isBaby() ? HGSounds.ENTITY_GHASTLING_AMBIENT.get() : HGSounds.ENTITY_HAPPY_GHAST_AMBIENT.get();
    }

    @Override
    public SoundEvent getHurtSound(DamageSource ds) {
        return this.isBaby() ? HGSounds.ENTITY_GHASTLING_HURT.get() : HGSounds.ENTITY_HAPPY_GHAST_HURT.get();
    }

    @Override
    public SoundEvent getDeathSound() {
        return this.isBaby() ? HGSounds.ENTITY_GHASTLING_DEATH.get() : HGSounds.ENTITY_HAPPY_GHAST_DEATH.get();
    }

    @Override
    public boolean handleFallDamage(float l, float d, DamageSource source) {
        return false;
    }

    @Override
    protected void addPassenger(Entity passenger) {
        if (!this.hasPassengers())
            this.getWorld().playSound(null, this.getX(), this.getY(), this.getZ(), HGSounds.ENTITY_HAPPY_GHAST_HARNESS_GOGGLES_DOWN.get(), this.getSoundCategory(), 1, 1);
        super.addPassenger(passenger);
    }

    @Override
    protected void removePassenger(Entity passenger) {
        super.removePassenger(passenger);
        if (!this.hasPassengers()) {
            this.getWorld().playSound(null, this.getX(), this.getY(), this.getZ(), HGSounds.ENTITY_HAPPY_GHAST_HARNESS_GOGGLES_UP.get(), this.getSoundCategory(), 1, 1);
            this.rememberHomePos();
        }
    }

    @Override
    public LivingEntity getControllingPassenger() {
        return this.getFirstPassenger() instanceof LivingEntity living ? living : null;
    }

    @Override
    public boolean canAddPassenger(Entity passenger) {
        return this.getPassengerList().size() < 4;
    }

    @Override
    protected Vec3d getControlledMovementInput(PlayerEntity controllingPlayer, Vec3d movementInput) {
        float f = controllingPlayer.sidewaysSpeed;
        float g = 0.0F;
        float h = 0.0F;
        if (controllingPlayer.forwardSpeed != 0.0F) {
            float i = MathHelper.cos(controllingPlayer.getPitch() * (float) (Math.PI / 180.0));
            float j = -MathHelper.sin(controllingPlayer.getPitch() * (float) (Math.PI / 180.0));
            if (controllingPlayer.forwardSpeed < 0.0F) {
                i *= -0.5F;
                j *= -0.5F;
            }
            h = j;
            g = i;
        }
        return new Vec3d(f, h, g).multiply(0.18F);
    }

    @Override
    public void updatePassengerPosition(Entity passenger, PositionUpdater positionUpdater) {
        if (this.hasPassenger(passenger)) {
            Vec3d offset = switch (this.getPassengerList().indexOf(passenger)) {
                case 0 ->
                        new Vec3d(STANDARD_RIDER_OFFSET, this.getMountedHeightOffset(), 0).rotateY(-this.getYaw() * 0.017453292F - 1.5707964F);
                case 1 ->
                        new Vec3d(0, this.getMountedHeightOffset(), -STANDARD_RIDER_OFFSET).rotateY(-this.getYaw() * 0.017453292F - 1.5707964F);
                case 2 ->
                        new Vec3d(-STANDARD_RIDER_OFFSET, this.getMountedHeightOffset(), 0).rotateY(-this.getYaw() * 0.017453292F - 1.5707964F);
                case 3 ->
                        new Vec3d(0, this.getMountedHeightOffset(), STANDARD_RIDER_OFFSET).rotateY(-this.getYaw() * 0.017453292F - 1.5707964F);
                default -> Vec3d.ZERO;
            };
            positionUpdater.accept(passenger, this.getX() + offset.x, this.getY() + offset.y, this.getZ() + offset.z);
        }
    }

    @Override
    public boolean isInvulnerableTo(DamageSource source) {
        return source.getSource() instanceof PotionEntity ||
                source.getSource() instanceof AreaEffectCloudEntity ||
                source.isOf(DamageTypes.FALL) ||
                source.isOf(DamageTypes.CACTUS) ||
                source.isOf(DamageTypes.DROWN) && this.isBaby() ||
                source.isOf(DamageTypes.LIGHTNING_BOLT) ||
                source.isOf(DamageTypes.EXPLOSION) ||
                source.isOf(DamageTypes.PLAYER_EXPLOSION) ||
                source.isOf(DamageTypes.TRIDENT) ||
                source.isOf(DamageTypes.FALLING_ANVIL) ||
                source.isOf(DamageTypes.DRAGON_BREATH) ||
                source.isOf(DamageTypes.WITHER) ||
                source.isOf(DamageTypes.WITHER_SKULL) ||
                super.isInvulnerableTo(source);
    }

    @Override
    public boolean isImmuneToExplosion() {
        return true;
    }

    @Override
    public boolean isFireImmune() {
        return true;
    }

    @Override
    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        ItemStack stack = player.getStackInHand(hand);
        if (!this.isBaby() && !this.hasPassengers())
            if (this.getBodyArmor().isEmpty()) {
                if (stack.getItem() instanceof HarnessItem) {
                    if (!this.getWorld().isClient) {
                        this.setBodyArmor(stack.copyWithCount(1));
                        if (!player.getAbilities().creativeMode)
                            stack.decrement(1);
                    }
                    this.getWorld().playSoundFromEntity(null, this, HGSounds.ENTITY_HAPPY_GHAST_EQUIP.get(), SoundCategory.NEUTRAL, 1, 1);
                    return ActionResult.SUCCESS;
                }
            } else if (stack.isOf(Items.SHEARS) && (EnchantmentHelper.getLevel(Enchantments.BINDING_CURSE, this.getBodyArmor()) == 0 || player.isCreative())) {
                if (!this.getWorld().isClient) {
                    if (!player.getAbilities().creativeMode)
                        player.getInventory().offerOrDrop(this.getBodyArmor());
                    stack.damage(1, player, e -> e.sendEquipmentBreakStatus(hand == Hand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND));
                    this.setBodyArmor(ItemStack.EMPTY);
                }
                this.getWorld().playSoundFromEntity(null, this, HGSounds.ENTITY_HAPPY_GHAST_UNEQUIP.get(), SoundCategory.NEUTRAL, 1, 1);
                return ActionResult.SUCCESS;
            }
        if (this.isBreedingItem(stack)) {
            if (this.isBaby()) {
                if (!player.getAbilities().creativeMode)
                    stack.decrement(1);
                this.growUp(toGrowUpAge(-this.getBreedingAge()), true);
                this.getWorld().sendEntityStatus(this, (byte) 18);
                return ActionResult.SUCCESS;
            } else if (this.getHealth() <= this.getMaxHealth()) {
                if (!player.getAbilities().creativeMode)
                    stack.decrement(1);
                this.heal(4);
                return ActionResult.SUCCESS;
            }
        } else if (!this.getBodyArmor().isEmpty()) {
            player.startRiding(this);
            return ActionResult.SUCCESS;
        }
        return ActionResult.PASS;
    }

    @Override
    public boolean collidesWith(Entity entity) {
        return true;
    }

    @Override
    public boolean isCollidable() {
        return !this.isBaby() && this.hasPlayerOnTop();
    }

    public boolean hasPlayerOnTop() {
        Box box = this.getBoundingBox();
        Box box2 = new Box(box.minX - 1.0, box.maxY, box.minZ - 1.0, box.maxX + 1.0, box.maxY + box.getYLength() / 2.0, box.maxZ + 1.0);
        for (PlayerEntity playerEntity : this.getWorld().getPlayers())
            if (!playerEntity.isSpectator() && box2.contains(playerEntity.getX(), playerEntity.getY(), playerEntity.getZ()))
                return true;
        return false;
    }

    @Override
    public void travel(Vec3d movementInput) {
        Entity entity = this.getPassengerList().isEmpty() ? null : this.getPassengerList().get(0);
        if (entity != null) {
            this.rotationCache.add(new FloatFloatImmutablePair(entity.getPitch(), entity.getYaw()));
            if (!this.rotationCache.isEmpty() && this.rotationCache.size() > ROTATION_DELAY_TICKS) {
                FloatFloatImmutablePair pair = this.rotationCache.poll();
                this.setYaw(pair.rightFloat());
                this.prevYaw = this.getYaw();
                this.setPitch(pair.leftFloat() * 0.5F);
                this.setRotation(this.getYaw(), this.getPitch());
                this.headYaw = this.bodyYaw = pair.rightFloat();
            }
            if (entity instanceof LivingEntity passenger) {
                this.setMovementSpeed((float) this.getAttributeValue(EntityAttributes.GENERIC_FLYING_SPEED));
                float forward = passenger.forwardSpeed;
                double y = entity.getRotationVector().y * forward * 0.75D;
                if (((LivingEntityAccessor) passenger).isJumping()) y += 0.5F;
                super.travel(new Vec3d(0.0D, y, forward * 1.65D * (1.0D - Math.abs(entity.getRotationVector().y))));
            }
            double d1 = this.getX() - this.prevX;
            double d0 = this.getZ() - this.prevZ;
            float f1 = (float) Math.min(Math.sqrt(d1 * d1 + d0 * d0) * 4.0F, 1);
            this.limbAnimator.setSpeed(this.limbAnimator.getSpeed() + (f1 - this.limbAnimator.getSpeed()) * 0.4F);
            this.limbAnimator.getPos(this.limbAnimator.getPos() + this.limbAnimator.getSpeed());
            this.updateLimbs(true);
        } else super.travel(movementInput);
    }

    @Override
    protected void fall(double y, boolean onGroundIn, BlockState state, BlockPos pos) {
    }

    @Override
    public void setNoGravity(boolean ignored) {
        super.setNoGravity(true);
    }

    @Override
    public int getLimitPerChunk() {
        return 1;
    }

    @Override
    public boolean canBreatheInWater() {
        return this.isBaby() || super.canBreatheInWater();
    }

    @Override
    protected boolean shouldFollowLeash() {
        return false;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
    }

    @Override
    public float getSoundPitch() {
        return 1.0F;
    }

    @Override
    public SoundCategory getSoundCategory() {
        return SoundCategory.NEUTRAL;
    }

    @Override
    public int getMinAmbientSoundDelay() {
        int i = super.getMinAmbientSoundDelay();
        return this.hasPassengers() ? i * 6 : i;
    }

    @Override
    public boolean isBreedingItem(ItemStack stack) {
        return stack.isIn(HGTags.HAPPY_GHAST_FOOD);
    }

    @Override
    public void tickMovement() {
        super.tickMovement();
        this.setNoGravity(true);
        if (this.isTouchingWater()) this.removeAllPassengers();
        if (this.age % 600 == 0 || this.age % 20 == 0 && 192 <= this.getY() && this.getY() <= 196)
            this.heal(1);
        EntityAttributeInstance kbResist = this.getAttributeInstance(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE);
        if (kbResist != null)
            kbResist.setBaseValue(this.hasPlayerOnTop() ? 1 : this.getPassengerList().size() * 0.05);
        if (this.outOfWanderRange()) this.rememberHomePos();
    }

    @Override
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData, @Nullable NbtCompound entityNbt) {
        EntityData data = super.initialize(world, difficulty, spawnReason, entityData, entityNbt);
        this.rememberHomePos();
        return data;
    }

    public void rememberHomePos() {
        this.brain.remember(MemoryModuleType.HOME, GlobalPos.create(this.getWorld().getRegistryKey(), this.getBlockPos()));
    }

    public boolean outOfWanderRange() {
        if (this.getWorld().isClient) return false;
        GlobalPos pos = this.getHomePos();
        int range = this.getBodyArmor().isEmpty() && !this.isBaby() ? 64 : 32;
        return !this.getWorld().getRegistryKey().equals(pos.getDimension()) || this.getPos().distanceTo(Vec3d.ofCenter(pos.getPos())) > range * range;
    }

    public GlobalPos getHomePos() {
        if (!this.brain.hasMemoryModule(MemoryModuleType.HOME)) this.rememberHomePos();
        return Optional.ofNullable(this.getBrain().getOptionalMemory(MemoryModuleType.HOME)).map(o -> o.orElse(GlobalPos.create(World.OVERWORLD, BlockPos.ORIGIN))).orElse(GlobalPos.create(World.OVERWORLD, BlockPos.ORIGIN));
    }

    public void setBodyArmor(ItemStack stack) {
        this.equipStack(EquipmentSlot.CHEST, stack);
        this.rememberHomePos();
    }

    public ItemStack getBodyArmor() {
        return this.getEquippedStack(EquipmentSlot.CHEST);
    }

    public static HappyGhastEntity generateChild(ServerWorld world, BlockPos pos, SpawnReason reason, float yaw) {
        HappyGhastEntity ghast = new HappyGhastEntity(HGEntities.HAPPY_GHAST.get(), world);
        ghast.refreshPositionAndAngles(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, yaw, 0);
        ghast.setBaby(true);
        ghast.setHeadYaw(yaw);
        ghast.setVelocity(0, 0, 0);
        ghast.initialize(world, world.getLocalDifficulty(pos), reason, null, null);
        return ghast;
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return MobEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.15D)
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 40.0D)
                .add(EntityAttributes.GENERIC_ARMOR, 0.0D)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 3.0D)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 64.0D)
                .add(EntityAttributes.GENERIC_FLYING_SPEED, 0.32D);
    }

    static class GhastlingNavigation extends BirdNavigation {
        public GhastlingNavigation(HappyGhastEntity entity, World world) {
            super(entity, world);
            this.setCanEnterOpenDoors(false);
            this.setCanSwim(true);
            this.setRangeMultiplier(48.0F);
        }

        @Override
        protected boolean canPathDirectlyThrough(Vec3d origin, Vec3d target) {
            return doesNotCollide(this.entity, origin, target, false);
        }
    }
}