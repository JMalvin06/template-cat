package oreo.fabricmod.entities;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.entity.passive.RabbitEntity;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.world.World;
import oreo.fabricmod.ai.BackHomeGoal;

import java.util.HashMap;

public class KikiEntity extends EnhancedCat{
    private static final Ingredient FOOD = Ingredient.ofItems(Items.SALMON);
    private static final Class<AnimalEntity>[] entities = new Class[]{RabbitEntity.class};
    private static final HashMap<StatusEffect, Integer> effects = new HashMap<>(){
        {
            put(StatusEffects.SPEED, 15);
        }
    };

    public KikiEntity(EntityType<? extends CatEntity> entityType, World world) {
        super(entityType, world, FOOD, entities);
        addEffectsFromMap(effects);
    }


    @Override
    protected void initGoals() {
        super.initGoals();
        this.goalSelector.add(2, new BackHomeGoal(this, 1.1f));
    }
}
