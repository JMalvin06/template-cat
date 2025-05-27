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
import oreo.fabricmod.OreoMod;

import java.util.HashMap;

public class TemplateCat extends EnhancedCat{
    private static final HashMap<StatusEffect, Integer> effects = new HashMap<>(){
        {
            put(OreoMod.statusEffectMap.get("slowness"), 15);
        }
    };

    private static final Ingredient TEMPLATE_INGREDIENT = Ingredient.ofItems(Items.CHICKEN);
    private static final Class<AnimalEntity>[] entities = new Class[]{ChickenEntity.class, RabbitEntity.class};


    public TemplateCat(EntityType<? extends CatEntity> entityType, World world) {
        super(entityType, world, TEMPLATE_INGREDIENT, entities, OreoMod.customEffects);
    }
}
