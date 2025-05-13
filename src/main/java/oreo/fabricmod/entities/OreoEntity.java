package oreo.fabricmod.entities;


import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import oreo.fabricmod.OreoMod;
import oreo.fabricmod.ai.BackHomeGoal;
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
import oreo.fabricmod.items.ModItems;

import java.util.*;


public class OreoEntity extends EnhancedCat {
    private static final Ingredient OREO_TAMING_INGREDIENT = Ingredient.ofItems(Items.CHICKEN);

    public OreoEntity(EntityType<? extends EnhancedCat> entityType, World world) {
        super(entityType, world, OREO_TAMING_INGREDIENT, new Class[]{ChickenEntity.class, RabbitEntity.class});
        super.addStatusEffect(StatusEffects.STRENGTH, 1000);
    }



    @Override
    protected void initGoals() {
        super.initGoals();
        this.goalSelector.add(2, new BackHomeGoal(this, 1.1f));
    }

    @Override
    public void revive() {
        if(this.dead) {
            try {
                OreoEntity newOreo = new OreoEntity(ModEntities.OREO_ENTITY, this.getWorld());
                if (this.getWorld().getBlockState(this.getHomePos()).isOf(ModBlocks.CAT_BED)) {
                    CatBed currentBed = (CatBed) this.getWorld().getBlockState(this.getHomePos()).getBlock();
                    currentBed.setUser(newOreo);
                }
                newOreo.setCustomName(Text.literal("Oreo"));
                if (this.getOwner() != null) {
                    newOreo.setOwner((PlayerEntity) this.getOwner());
                }
                Vec3d spawn = this.getHomePos().toCenterPos();
                newOreo.setPos(spawn.x, spawn.y + 1.1, spawn.z);
                newOreo.setHomePos(this.getHomePos());
                if (this.getWorld().spawnEntity(newOreo)) {
                    OreoMod.LOGGER.info("Welcome back Oreo!");
                }
            } catch (Exception e) {
                for (PlayerEntity player : this.getWorld().getPlayers()) {
                    player.sendMessage(Text.literal("Oreo was unfortunately not respawned.."));
                }
                OreoMod.LOGGER.info(e.toString());
                PlayerEntity owner = ((PlayerEntity) this.getOwner());
                if (owner != null) {
                    owner.giveItemStack(ModItems.OREO.getDefaultStack());
                } else
                    OreoMod.LOGGER.info("No Oreo owner to give item stack to");
            }
        }
    }

    /*private void lootChicken(){
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
            OreoMod.LOGGER.info("No held item");
    }*/



    @Override
    public EntityView method_48926() {
        return super.getWorld();
    }
}