package oreo.fabricmod.entities;

import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.DyeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.EntityView;
import oreo.fabricmod.ai.SleepWhenOwnerGoal;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.recipe.Ingredient;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import oreo.fabricmod.OreoMod;
import oreo.fabricmod.ai.SleepInCatBedGoal;
import oreo.fabricmod.blocks.CatBed;
import oreo.fabricmod.blocks.ModBlocks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public abstract class EnhancedCat extends CatEntity {
    // private static final Ingredient TAMING_INGREDIENT = Ingredient.ofItems(Items.CHICKEN);
    private final Ingredient TAMING_INGREDIENT;
    public enum BehaviorState {FOLLOW, ROAM} // For behavior states
    protected BehaviorState currentState;
    private BlockPos homePos;
    private final Class<AnimalEntity>[] attackableEntities;
    public static final String name = "";


    private HashMap<StatusEffect, Integer> statusEffects;

    public EnhancedCat(EntityType<? extends CatEntity> entityType, World world, Ingredient tamingIngredient, Class<AnimalEntity>[] entities, HashMap<StatusEffect, Integer> statusEffects) {
        this(entityType,world,tamingIngredient,entities);
        this.statusEffects = statusEffects;
    }

    public EnhancedCat(EntityType<? extends CatEntity> entityType, World world, Ingredient tamingIngredient, Class<AnimalEntity>[] entities) {
        super(entityType, world);
        this.TAMING_INGREDIENT = tamingIngredient;
        this.attackableEntities = entities;

        for(Class<AnimalEntity> entity : attackableEntities){
            this.targetSelector.add(1, new ActiveTargetGoal<>(this, entity, false, null));
        }
        this.goalSelector.add(4, new TemptGoal(this, 0.6, TAMING_INGREDIENT , false));
    }

    /*protected void addStatusEffect(StatusEffect type, int seconds){
        int ticks = seconds * 20; // 20 ticks per second is the default tick speed
        statusEffects.add(new StatusEffectInstance(type, ticks, 0, true, false, true));
    }*/

    /*protected void addEffectsFromMap(HashMap<StatusEffect, Integer> statusMap){
        for (HashMap.Entry<StatusEffect, Integer> entry : statusMap.entrySet()){
            this.addStatusEffect(entry.getKey(), entry.getValue());
        }
    }*/




    @Override
    public boolean isBreedingItem(ItemStack stack) {
        return this.TAMING_INGREDIENT.test(stack);
    }

    @Override
    protected void initGoals() {
        super.initGoals();
        this.goalSelector.add(3, new SleepWhenOwnerGoal(this));
        this.goalSelector.add(8, new SleepInCatBedGoal(this, 0.8,5));

        // Remove unnecessary goals
        boolean removedGoals = this.removeGoals(new Class[]{GoToBedAndSleepGoal.class, SleepInCatBedGoal.class});
        if(!removedGoals) {
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

    @Override
    public void tick() {
        if(!this.getWorld().isClient()){
            // Set home position after assigned a BlockPos
            if (homePos == null && !this.getBlockPos().equals(new BlockPos(0, 0, 0)))
                homePos = this.getBlockPos();

            // Check blocks and set new home position
            this.checkBlocks(this.getWorld(), this.getBlockPos());


            if(!statusEffects.isEmpty()) {
                for (PlayerEntity player : this.getWorld().getPlayers()) {
                    if (player.getBlockPos().isWithinDistance(this.getBlockPos().toCenterPos(), 10)) {
                        // Give player all status effects attributed to this entity
                        for (Map.Entry<StatusEffect, Integer> statusEffect : statusEffects.entrySet()) {
                            StatusEffectInstance updatedEffect = new StatusEffectInstance(statusEffect.getKey(), statusEffect.getValue()*20, 0, true, false, true);
                            player.addStatusEffect(updatedEffect);
                        }
                    }

                }
            }
        }
        super.tick();
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
                    currentState  = BehaviorState.FOLLOW;
                    return ActionResult.SUCCESS;
                }

                switch (currentState) {
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
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);

        // Save home position to NBT
        int[] homePosArray = {homePos.getX(),homePos.getY(),homePos.getZ()};
        nbt.putIntArray("HomePos", homePosArray);
        boolean nbtCurrentMode = getCurrentBehavior() == BehaviorState.FOLLOW; // Follow = true - Roam = false
        nbt.putBoolean("CurrentMode", nbtCurrentMode);
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);

        // Get saved home position from NBT
        homePos = new BlockPos(nbt.getIntArray("HomePos")[0], nbt.getIntArray("HomePos")[1], nbt.getIntArray("HomePos")[2]);
        if(this.getWorld().getBlockState(homePos).isOf(ModBlocks.CAT_BED)) {
            CatBed bed = (CatBed) this.getWorld().getBlockState(homePos).getBlock();
            bed.setUser(this);
        }
        boolean nbtCurrentMode = nbt.getBoolean("CurrentMode");
        OreoMod.LOGGER.info("Current mode: {}", nbtCurrentMode);
        if(nbtCurrentMode)
            this.setFollowMode();
        else
            this.setRoamMode();

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
                        this.getOwner().sendMessage(Text.literal(this.getName().getString() + " home set to " + homePos.toShortString()));
                    homePos = pos;
                }
            }
        }
    }

    public void setRoamMode(){
        OreoMod.LOGGER.info("Roam mode!");
        if(getCurrentBehavior() != BehaviorState.ROAM) {
            if(removeGoals(new Class[]{FollowOwnerGoal.class})){
                this.goalSelector.add(6, new WanderAroundGoal(this, .8));
                if(this.hasOwner())
                    this.getOwner().sendMessage(Text.literal(this.getName().getString() + " will now roam."));
                currentState = BehaviorState.ROAM;
            }
        }
    }

    public void setFollowMode(){
        if(getCurrentBehavior() != BehaviorState.FOLLOW) {
            if (removeGoals(new Class[]{WanderAroundGoal.class})) {
                this.goalSelector.add(4, new FollowOwnerGoal(this, 1.0, 10.0F, 5.0F, false));
                if(this.hasOwner())
                    this.getOwner().sendMessage(Text.literal(this.getName().getString() + " will now follow " + this.getOwner().getEntityName() + "."));
                currentState = BehaviorState.FOLLOW;
            }
        }
    }

    public boolean hasOwner(){
        return this.getOwner() != null;
    }

    public BlockPos getHomePos(){
        return homePos;
    }

    public void setHomePos(BlockPos newHome){
        homePos = newHome;
    }

    public BehaviorState getCurrentBehavior(){
        return currentState;
    }

    @Override
    public EntityView method_48926() {
        return this.getWorld();
    }
}
