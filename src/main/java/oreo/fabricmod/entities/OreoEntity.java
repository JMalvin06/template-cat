package oreo.fabricmod.entities;


import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.util.math.Vec3i;
import oreo.fabricmod.OreoMod;
import oreo.fabricmod.ai.OreoBackHomeGoal;
import oreo.fabricmod.ai.SleepInCatBedGoal;
import oreo.fabricmod.ai.SleepWhenOwnerGoal;
import oreo.fabricmod.blocks.CatBed;
import oreo.fabricmod.blocks.ModBlocks;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.recipe.Ingredient;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EntityView;
import net.minecraft.world.World;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.*;


public class OreoEntity extends CatEntity implements GeoEntity {

    protected static final RawAnimation WALK_ANIM =  RawAnimation.begin().thenLoop("animation.model.walk");
    protected static final RawAnimation SIT_ANIM =  RawAnimation.begin().thenLoop("animation.model.sit");
    protected static final RawAnimation LAY_ANIM =  RawAnimation.begin().thenLoop("animation.model.lay");

    private static final Ingredient OREO_TAMING_INGREDIENT = Ingredient.ofItems(Items.CHICKEN);
    private boolean isCarryingChicken;
    private Item heldItem;
    private Text heldItemName;


    public enum OreoMode {FOLLOW, ROAM} // For behavior states
    private OreoMode currentMode;

    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);
    private BlockPos homePos;

    ArrayList<StatusEffectInstance> statusEffects = new ArrayList<>(){
        {
            add(new StatusEffectInstance(StatusEffects.STRENGTH, 10, 0, false, false, true));
        }
    };


    public OreoEntity(EntityType<? extends CatEntity> entityType, World world) {
        super(entityType, world);
    }





    @Override
    protected void initGoals() {
        isCarryingChicken = false;
        // Target chickens and rabbits when tamed
        this.targetSelector.add(1, new ActiveTargetGoal<>(this, ChickenEntity.class, false, null));
        this.targetSelector.add(1, new ActiveTargetGoal<>(this, RabbitEntity.class, false, null));

        super.initGoals();
        this.goalSelector.add(3, new SleepWhenOwnerGoal(this));
        this.goalSelector.add(8, new SleepInCatBedGoal(this, 0.8,5));
        this.goalSelector.add(2, new OreoBackHomeGoal(this, 1.1f));
            this.goalSelector.add(4, new TemptGoal(this, 0.6, OREO_TAMING_INGREDIENT, false));

        // Remove unnecessary goals
        if(!this.removeGoals(new Class[]{GoToBedAndSleepGoal.class, SleepInCatBedGoal.class})) {
            // If goals were not removed, warn player
            if (this.getWorld().getServer() != null)
                this.getWorld().getServer().sendMessage(Text.literal("Could not remove goals.. (Restart Minecraft)"));
            else
                OreoMod.LOGGER.info("Server not found..");
        }
    }

    public static DefaultAttributeContainer.Builder setAttributes(){
        return TameableEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 25)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.35)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 3.0);
    }

    @Override
    public void registerControllers(final AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "Walking", 5, this::oreoAnimationController));
    }

    @Override
    public void tick() {
        if(!this.getWorld().isClient()){
            // Set home position after assigned a BlockPos
            if (homePos == null && !this.getBlockPos().equals(new BlockPos(0, 0, 0)))
                homePos = this.getBlockPos();

            // Check blocks and set new home position
            this.checkBlocks(this.getWorld(), this.getBlockPos());

            // Give all players within range strength 1 status effect
            for (PlayerEntity player : this.getWorld().getPlayers()) {
                if (player.getBlockPos().isWithinDistance(this.getBlockPos().toCenterPos(), 10)) {
                    // Give player all status effects attributed to this entity
                    for (StatusEffectInstance statusEffect : statusEffects)
                        player.addStatusEffect(statusEffect);
                }

            }
            //lootChicken();
        }
        super.tick();
    }



    private void lootChicken(){
        Vec3i vec3i = new Vec3i(1,0,1);
        List<ItemEntity> items = this.getWorld().getNonSpectatingEntities(ItemEntity.class, this.getBoundingBox().expand(vec3i.getX(), vec3i.getY(), vec3i.getZ()));
        for(ItemEntity item : items){
            ItemStack stack = item.getStack();
            if(this.heldItem == null && stack.isOf(Items.CHICKEN) && !stack.isEmpty()){
                this.heldItem = stack.getItem();
                this.heldItemName = stack.getName();
                OreoMod.LOGGER.info("Item: {}", this.heldItem.getName());
                OreoMod.LOGGER.info("Name: {}", this.heldItemName);
                stack.decrement(1);
                if(stack.isEmpty()){
                    item.discard();
                }
            }
            //OreoMod.LOGGER.info(stack.toString());
        }
        //OreoMod.LOGGER.info(String.valueOf(isCarryingChicken));
        /*if(heldItem != null)
            OreoMod.LOGGER.info("Item: {}", this.heldItem.getName().getString());
        else
            OreoMod.LOGGER.info("No held item");*/
    }

    protected <E extends OreoEntity> PlayState oreoAnimationController(final AnimationState<E> event) {
        if(this.isInSleepingPose())
            return event.setAndContinue(LAY_ANIM);
        else if (this.isInSittingPose())
            return event.setAndContinue(SIT_ANIM);
        else if (event.isMoving())
            return event.setAndContinue(WALK_ANIM);

        return PlayState.STOP;
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);

        // Save home position to NBT
        int[] homePosArray = {homePos.getX(),homePos.getY(),homePos.getZ()};
        nbt.putIntArray("HomePos", homePosArray);
        boolean nbtCurrentMode = getCurrentMode() == OreoMode.FOLLOW; // Follow = true - Roam = false
        nbt.putBoolean("CurrentMode", nbtCurrentMode);
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);

        // Get saved home position from NBT
        homePos = new BlockPos(nbt.getIntArray("HomePos")[0], nbt.getIntArray("HomePos")[1], nbt.getIntArray("HomePos")[2]);
        if(this.getWorld().getBlockState(homePos).isOf(ModBlocks.CAT_BED)) {
            CatBed oreoBed = (CatBed) this.getWorld().getBlockState(homePos).getBlock();
            oreoBed.setUser(this);
        }
        boolean nbtCurrentMode = nbt.getBoolean("CurrentMode");
        OreoMod.LOGGER.info("Current mode: {}", nbtCurrentMode);
        if(nbtCurrentMode)
            this.setFollowMode();
        else
            this.setRoamMode();

    }

    public void setFollowMode(){
        if(currentMode != OreoMode.FOLLOW) {
            if (removeGoals(new Class[]{WanderAroundGoal.class})) {
                this.goalSelector.add(4, new FollowOwnerGoal(this, 1.0, 10.0F, 5.0F, false));
                if(this.hasOwner())
                    this.getOwner().sendMessage(Text.literal("Oreo will now follow " + this.getOwner().getEntityName() + "."));
                currentMode = OreoMode.FOLLOW;
            }
        }
    }

    public boolean hasOwner(){
        return this.getOwner() != null;
    }

    public void setRoamMode(){
        OreoMod.LOGGER.info("Roam mode!");
        if(currentMode != OreoMode.ROAM) {
            if(removeGoals(new Class[]{FollowOwnerGoal.class})){
                this.goalSelector.add(6, new WanderAroundGoal(this, .8));
                if(this.hasOwner())
                    this.getOwner().sendMessage(Text.literal("Oreo will now roam."));
                currentMode = OreoMode.ROAM;
            }
        }
    }

    @Override
    public boolean isBreedingItem(ItemStack stack) {
        return super.isBreedingItem(stack) || OREO_TAMING_INGREDIENT.test(stack);
    }



    @Override
    public EntityView method_48926() {
        return super.getWorld();
    }


    public void checkBlocks(World world, BlockPos playerPos){
        for(int x = -2; x < 3; x++){
            for(int z = -2; z < 3; z++){
                    BlockPos pos = new BlockPos(playerPos.getX()+x, playerPos.getY(), playerPos.getZ()+z);

                    if(world.getBlockState(pos).getBlock() == ModBlocks.CAT_BED && !homePos.equals(pos)){
                        OreoMod.LOGGER.info(world.getBlockState(pos).toString());
                        if(world.getBlockState(homePos).getBlock() == ModBlocks.CAT_BED){
                            CatBed oldHome = (CatBed)(world.getBlockState(homePos).getBlock());
                            oldHome.clearUser();
                        }
                        CatBed newHome = (CatBed)world.getBlockState(pos).getBlock();
                        newHome.setUser(this);
                        if(this.hasOwner())
                            this.getOwner().sendMessage(Text.literal("Oreo home set to " + homePos.toShortString()));
                        homePos = pos;
                    }
                }
            }
    }



    @Override
    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        ItemStack itemStack = player.getActiveItem();
        Item item = itemStack.getItem();
        if (!this.getWorld().isClient)  {
                if (!(item instanceof DyeItem) && player.isSneaking()) {
                    // Set owner as player interacted with
                    if(!this.isOwner(player)){
                        this.setOwner(player);
                        if(!this.isTamed())
                            this.setTamed(true);
                        currentMode  = OreoMode.FOLLOW;
                        return ActionResult.SUCCESS;
                    }

                    switch (currentMode) {
                        case ROAM:
                            setFollowMode();
                            break;
                        case FOLLOW:
                            setRoamMode();
                            break;
                    }
                    return ActionResult.SUCCESS;
            }
        }
        return super.interactMob(player, hand);
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.geoCache;
    }

    public BlockPos getHomePos(){
        return homePos;
    }

    public void setHomePos(BlockPos newHome){
        homePos = newHome;
    }

    public boolean removeGoals(Class[] goals){
        ArrayList<Goal> goalsToRemove = new ArrayList<>();
        for (PrioritizedGoal oreoGoal : this.goalSelector.getGoals()) {
            for (Class removeGoal : goals) {
                if (oreoGoal.getGoal().getClass().equals(removeGoal)) {
                    goalsToRemove.add(oreoGoal.getGoal());
                }
            }
        }
        if(!goalsToRemove.isEmpty()) {
            for (Goal removedGoal : goalsToRemove) {
                this.goalSelector.remove(removedGoal);
            }
        }
        return true;
    }

    public OreoMode getCurrentMode(){
        return currentMode;
    }




}