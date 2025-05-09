package oreo.fabricmod.entities;

import oreo.fabricmod.OreoMod;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModEntities {
    public static final EntityType<OreoEntity> OREO_ENTITY = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(OreoMod.MOD_ID, "oreo_entity"),
            FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, OreoEntity::new).dimensions(EntityDimensions.fixed(.6f, .7f)).build()
    );

    public static void registerEntities(){
        FabricDefaultAttributeRegistry.register(OREO_ENTITY, OreoEntity.setAttributes());
    }
}
