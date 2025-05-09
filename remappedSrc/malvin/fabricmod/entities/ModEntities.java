package oreo.fabricmod.entities;

import oreo.fabricmod.entities.client.OreoEntity;
import oreo.fabricmod.entities.client.OreoEntityRenderer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModEntities {
    public static final EntityType<OreoEntity> OREO_ENTITY = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier("malvinfabricmod", "oreo_entity"),
            FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, OreoEntity::new).dimensions(EntityDimensions.fixed(.6f, .7f)).build()
    );

    public static void registerEntities(){
        FabricDefaultAttributeRegistry.register(OREO_ENTITY, OreoEntity.setAttributes());
    }

    public static void registerRenderer(){
        EntityRendererRegistry.register(OREO_ENTITY, OreoEntityRenderer::new);
    }
}
