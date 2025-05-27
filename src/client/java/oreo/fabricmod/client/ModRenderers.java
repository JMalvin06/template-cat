package oreo.fabricmod.client;

import oreo.fabricmod.entities.KikiEntity;
import oreo.fabricmod.entities.ModEntities;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;

public class ModRenderers {

    public static void registerRenderer(){
        EntityRendererRegistry.register(ModEntities.OREO_ENTITY, OreoEntityRenderer::new);
        EntityRendererRegistry.register(ModEntities.KIKI_ENTITY, KikiEntityRenderer::new);
        EntityRendererRegistry.register(ModEntities.TEMPLATE, TemplateEntityRenderer::new);
    }
}
