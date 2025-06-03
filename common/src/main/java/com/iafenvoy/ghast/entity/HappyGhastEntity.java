package com.iafenvoy.ghast.entity;

import com.google.common.collect.ImmutableList;
import com.iafenvoy.ghast.entity.goal.*;
import com.iafenvoy.ghast.item.HarnessItem;
import com.iafenvoy.ghast.mixin.LivingEntityAccessor;
import com.iafenvoy.ghast.registry.HGEntities;
import com.iafenvoy.ghast.registry.HGSounds;
import com.iafenvoy.ghast.registry.HGTags;
import com.mojang.serialization.Dynamic;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.brain.Activity;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.control.LookControl;
import net.minecraft.entity.ai.control.MoveControl;
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

import java.util.Set;

public class HappyGhastEntity extends AnimalEntity {
    private static final double STANDARD_RIDER_OFFSET = 1.35;
    private long currentAge = 0;
    private int stillTimeout = 0;
    private HappyGhastTemptGoal foodGoal, harnessGoal;

    public HappyGhastEntity(EntityType<HappyGhastEntity> type, World world) {
        super(type, world);
        this.experiencePoints = 0;
        this.setAiDisabled(false);
        this.setPersistent();
        this.moveControl = new HappyGhastMoveControl(this);
        this.lookControl = new HappyGhastEntity.HappyGhastLookControl(this);
        this.setEquipmentDropChance(EquipmentSlot.CHEST, 1);
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putLong("currentAge", this.currentAge);
        nbt.putInt("still_timeout", this.stillTimeout);
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        this.currentAge = nbt.getLong("currentAge");
        this.stillTimeout = nbt.getInt("still_timeout");
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
        this.goalSelector.add(1, new HappyGhastSwimGoal(this));
        this.goalSelector.add(3, new HappyGhastFlyRandomlyGoal(this));
        this.goalSelector.add(4, new FollowEntityPredicateGoal(this, target -> target.getType().isIn(HGTags.HAPPY_GHAST_FOLLOW) && !target.isBaby(), 1.1, 5, 16));
        this.goalSelector.add(5, new HappyGhastLookAtEntityGoal(this, PlayerEntity.class, 64));
        this.goalSelector.add(5, new FollowEntityPredicateGoal(this, target -> target.getType() == EntityType.PLAYER, 1.1, 5, 16));
        this.goalSelector.add(7, this.foodGoal = new HappyGhastTemptGoal(this, Ingredient.ofItems(Items.SNOWBALL)));
        this.goalSelector.add(7, this.harnessGoal = new HappyGhastFollowHarnessGoal(this));
    }

    @Override
    public boolean canImmediatelyDespawn(double distanceToClosestPlayer) {
        return false;
    }

    public double getMountedHeightOffset() {
        return (double) this.getDimensions(EntityPose.STANDING).height() * 0.75 + 0.5D;
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
        if (!this.hasPassengers() && !this.getBodyArmor().isEmpty())
            this.getWorld().playSound(null, this.getX(), this.getY(), this.getZ(), HGSounds.ENTITY_HAPPY_GHAST_HARNESS_GOGGLES_DOWN.get(), this.getSoundCategory(), 1, 1);
        super.addPassenger(passenger);
    }

    @Override
    protected void removePassenger(Entity passenger) {
        super.removePassenger(passenger);
        if (!this.hasPassengers() && !this.getBodyArmor().isEmpty()) {
            this.getWorld().playSound(null, this.getX(), this.getY(), this.getZ(), HGSounds.ENTITY_HAPPY_GHAST_HARNESS_GOGGLES_UP.get(), this.getSoundCategory(), 1, 1);
            this.rememberHomePos();
        }
    }

    @Override
    public void detachLeash(boolean sendPacket, boolean dropItem) {
        super.detachLeash(sendPacket, dropItem);
        this.rememberHomePos();
    }

    @Nullable
    @Override
    public LivingEntity getControllingPassenger() {
        return !this.isAiDisabled() && !this.shouldStay() && !this.getBodyArmor().isEmpty() && this.getFirstPassenger() instanceof PlayerEntity playerEntity ? playerEntity : super.getControllingPassenger();
    }

    @Override
    public boolean canAddPassenger(Entity passenger) {
        return !this.isBaby() && this.getPassengerList().size() < 4;
    }

    @Override
    public void updatePassengerPosition(Entity passenger, PositionUpdater positionUpdater) {
        if (this.hasPassenger(passenger)) {
            Vec3d offset = (switch (this.getPassengerList().indexOf(passenger)) {
                case 0 -> new Vec3d(STANDARD_RIDER_OFFSET, this.getMountedHeightOffset(), 0);
                case 1 -> new Vec3d(0, this.getMountedHeightOffset(), -STANDARD_RIDER_OFFSET);
                case 2 -> new Vec3d(-STANDARD_RIDER_OFFSET, this.getMountedHeightOffset(), 0);
                case 3 -> new Vec3d(0, this.getMountedHeightOffset(), STANDARD_RIDER_OFFSET);
                default -> Vec3d.ZERO;
            }).rotateY((float) (-Math.toRadians(this.getYaw()) - Math.PI / 2));
            positionUpdater.accept(passenger, this.getX() + offset.x, this.getY() + offset.y, this.getZ() + offset.z);
        }
    }

    @Override
    public boolean isInvulnerableTo(DamageSource source) {
        return source.isOf(DamageTypes.FALL) ||
                source.isOf(DamageTypes.DROWN) && this.isBaby() ||
                source.isOf(DamageTypes.EXPLOSION) ||
                source.isOf(DamageTypes.PLAYER_EXPLOSION) ||
                super.isInvulnerableTo(source);
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
                    stack.damage(1, player, hand == Hand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND);
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
        } else {
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
        return !this.isBaby() && this.shouldStay();
    }

    public boolean hasPlayerOnTop() {
        Box box = this.getBoundingBox();
        Box box2 = new Box(box.minX - 1.0, box.maxY, box.minZ - 1.0, box.maxX + 1.0, box.maxY + box.getLengthY() / 2.0, box.maxZ + 1.0);
        for (PlayerEntity playerEntity : this.getWorld().getPlayers())
            if (!playerEntity.isSpectator() && box2.contains(playerEntity.getX(), playerEntity.getY(), playerEntity.getZ()))
                return true;
        return false;
    }

    public boolean shouldStay() {
        return this.stillTimeout > 0;
    }

    @Override
    public void travel(Vec3d movementInput) {
        this.updateVelocity(0.09F, movementInput);
        this.move(MovementType.SELF, this.getVelocity());
        float multiplier = 0.91F;
        if (this.isTouchingWater()) multiplier = 0.8F;
        else if (this.isInLava()) multiplier = 0.5F;
        this.setVelocity(this.getVelocity().multiply(multiplier));
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
        if (((LivingEntityAccessor) controllingPlayer).isJumping())
            h += 0.5F;
        return new Vec3d(f, h, g).multiply(0.18F);
    }

    protected Vec2f getGhastRotation(LivingEntity controllingEntity) {
        return new Vec2f(controllingEntity.getPitch() * 0.5F, controllingEntity.getYaw());
    }

    @Override
    protected void tickControlled(PlayerEntity controllingPlayer, Vec3d movementInput) {
        super.tickControlled(controllingPlayer, movementInput);
        Vec2f vec2f = this.getGhastRotation(controllingPlayer);
        float f = this.getYaw();
        float g = MathHelper.wrapDegrees(vec2f.y - f);
        f += g * 0.08F;
        this.setRotation(f, vec2f.x);
        this.prevYaw = this.bodyYaw = this.headYaw = f;
    }

    @Override
    protected void fall(double y, boolean onGroundIn, BlockState state, BlockPos pos) {
    }

    @Override
    public boolean isClimbing() {
        return false;
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
    public void tick() {
        super.tick();
        this.currentAge++;
        if (this.currentAge % 600 == 0 || this.currentAge % 20 == 0 && (192 <= this.getY() && this.getY() <= 196 || this.getWorld().hasRain(this.getBlockPos())))
            this.heal(1);
        if (this.stillTimeout > 0) this.stillTimeout--;
        if (this.hasPlayerOnTop()) this.stillTimeout = 10;
        EntityAttributeInstance kbResist = this.getAttributeInstance(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE);
        if (kbResist != null) {
            double newValue = this.shouldStay() ? 1 : this.getPassengerList().size() * 0.05;
            if (newValue != kbResist.getValue())
                kbResist.setBaseValue(newValue);
        }
    }

    @Override
    public void tickMovement() {
        super.tickMovement();
        this.setNoGravity(true);
        if (this.isTouchingWater()) this.removeAllPassengers();
    }

    @Override
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData) {
        EntityData data = super.initialize(world, difficulty, spawnReason, entityData);
        this.rememberHomePos();
        return data;
    }

    public boolean isFollowingPlayer() {
        return this.foodGoal.isActive() || this.harnessGoal.isActive();
    }

    public void rememberHomePos() {
        this.brain.remember(MemoryModuleType.HOME, GlobalPos.create(this.getWorld().getRegistryKey(), this.getBlockPos()));
    }

    public void setBodyArmor(ItemStack stack) {
        this.equipStack(EquipmentSlot.CHEST, stack);
        this.rememberHomePos();
    }

    @Override
    public ItemStack getBodyArmor() {
        return this.getEquippedStack(EquipmentSlot.CHEST);
    }

    @Override
    public void stopMovement() {
        this.getNavigation().stop();
        this.setSidewaysSpeed(0.0F);
        this.setUpwardSpeed(0.0F);
        this.setMovementSpeed(0.0F);
        this.setVelocity(0.0, 0.0, 0.0);
    }

    public static HappyGhastEntity generateChild(ServerWorld world, BlockPos pos, SpawnReason reason, float yaw) {
        HappyGhastEntity ghast = new HappyGhastEntity(HGEntities.HAPPY_GHAST.get(), world);
        ghast.refreshPositionAndAngles(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, yaw, 0);
        ghast.setBaby(true);
        ghast.setHeadYaw(yaw);
        ghast.setVelocity(0, 0, 0);
        ghast.initialize(world, world.getLocalDifficulty(pos), reason, null);
        return ghast;
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return MobEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.18D)
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 40.0D)
                .add(EntityAttributes.GENERIC_ARMOR, 0.0D)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 3.0D)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 64.0D)
                .add(EntityAttributes.GENERIC_FLYING_SPEED, 0.32D)
                .add(EntityAttributes.GENERIC_STEP_HEIGHT, 0.6D);
    }

    public static void updateYaw(MobEntity ghast) {
        if (ghast.getTarget() == null) {
            Vec3d vec3d = ghast.getVelocity();
            ghast.setYaw(-((float) MathHelper.atan2(vec3d.x, vec3d.z)) * (180.0F / (float) Math.PI));
            ghast.bodyYaw = ghast.getYaw();
        } else {
            LivingEntity livingEntity = ghast.getTarget();
            if (livingEntity.squaredDistanceTo(ghast) < 4096) {
                double e = livingEntity.getX() - ghast.getX();
                double f = livingEntity.getZ() - ghast.getZ();
                ghast.setYaw(-((float) MathHelper.atan2(e, f)) * (180.0F / (float) Math.PI));
                ghast.bodyYaw = ghast.getYaw();
            }
        }
    }

    static class HappyGhastMoveControl extends MoveControl {
        private final HappyGhastEntity happyGhast;
        private int collisionCheckCooldown;

        public HappyGhastMoveControl(HappyGhastEntity happyGhast) {
            super(happyGhast);
            this.happyGhast = happyGhast;
        }

        @Override
        public void tick() {
            if (this.happyGhast.isCollidable()) {
                this.state = State.WAIT;
                this.happyGhast.stopMovement();
            } else if (this.state == State.MOVE_TO) {
                if (this.collisionCheckCooldown-- <= 0) {
                    this.collisionCheckCooldown += this.happyGhast.getRandom().nextInt(5) + 2;
                    Vec3d vec3d = new Vec3d(this.targetX - this.happyGhast.getX(), this.targetY - this.happyGhast.getY(), this.targetZ - this.happyGhast.getZ());
                    double d = vec3d.length();
                    vec3d = vec3d.normalize();
                    if (this.willCollide(vec3d, MathHelper.ceil(d)))
                        this.happyGhast.setVelocity(this.happyGhast.getVelocity().add(vec3d.multiply(0.05)));
                    else
                        this.state = State.WAIT;
                }
            }
        }

        private boolean willCollide(Vec3d direction, int steps) {
            Box box = this.happyGhast.getBoundingBox();
            for (int i = 1; i < steps; ++i) {
                box = box.offset(direction);
                if (!this.happyGhast.getWorld().isSpaceEmpty(this.happyGhast, box))
                    return false;
            }
            return true;
        }
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

    static class HappyGhastLookControl extends LookControl {
        private final HappyGhastEntity happyGhast;

        HappyGhastLookControl(HappyGhastEntity happyGhast) {
            super(happyGhast);
            this.happyGhast = happyGhast;
        }

        @Override
        public void tick() {
            if (this.happyGhast.shouldStay()) {
                float f = getYawToSubtract(this.happyGhast.getYaw());
                this.happyGhast.setYaw(this.happyGhast.getYaw() - f);
                this.happyGhast.setHeadYaw(this.happyGhast.getYaw());
            } else if (this.lookAtTimer > 0) {
                this.lookAtTimer--;
                double d = this.x - this.happyGhast.getX();
                double e = this.z - this.happyGhast.getZ();
                this.happyGhast.setYaw(-((float) MathHelper.atan2(d, e)) * (180.0F / (float) Math.PI));
                this.happyGhast.bodyYaw = this.happyGhast.getYaw();
                this.happyGhast.headYaw = this.happyGhast.bodyYaw;
            } else
                updateYaw(this.entity);
        }

        public static float getYawToSubtract(float yaw) {
            float f = yaw % 90.0F;
            if (f >= 45.0F) f -= 90.0F;
            if (f < -45.0F) f += 90.0F;
            return f;
        }
    }

}