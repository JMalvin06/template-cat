package oreo.fabricmod.entities;

import net.minecraft.entity.*;
import net.minecraft.registry.RegistryOps;
import oreo.fabricmod.OreoMod;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModEntities {
    public static final EntityType<OreoEntity> OREO_ENTITY = registerEnhancedCat("oreo_entity", OreoEntity::new);

    public static final EntityType<KikiEntity> KIKI_ENTITY = registerEnhancedCat("kiki_entity", KikiEntity::new);

    public static <T extends EnhancedCat> EntityType<T> registerEnhancedCat(String path, EntityType.EntityFactory<T> factory){
        return Registry.register(
                Registries.ENTITY_TYPE,
                new Identifier(OreoMod.MOD_ID, path),
                FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, factory).dimensions(EntityDimensions.fixed(0.6f, 0.7f)).build()
        );
    }

    public static void registerEntities(){
        FabricDefaultAttributeRegistry.register(OREO_ENTITY, OreoEntity.setAttributes());
        FabricDefaultAttributeRegistry.register(KIKI_ENTITY, OreoEntity.setAttributes());
    }
}
