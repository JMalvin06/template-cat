package oreo.fabricmod.entities;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.entity.passive.RabbitEntity;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.world.World;
import oreo.fabricmod.ai.BackHomeGoal;

public class KikiEntity extends EnhancedCat{
    private static final Ingredient FOOD = Ingredient.ofItems(Items.SALMON);
    private static final Class<AnimalEntity>[] entities = new Class[]{RabbitEntity.class};

    public KikiEntity(EntityType<? extends CatEntity> entityType, World world) {
        super(entityType, world, FOOD, entities);
        super.addStatusEffect(StatusEffects.SPEED, 1000);
    }

    @Override
    protected void initGoals() {
        super.initGoals();
        this.goalSelector.add(2, new BackHomeGoal(this, 1.1f));
    }
}
