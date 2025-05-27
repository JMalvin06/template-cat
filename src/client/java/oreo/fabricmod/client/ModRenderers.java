package oreo.fabricmod.client;

import oreo.fabricmod.entities.ModEntities;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;

public class ModRenderers {

    public static void registerRenderer(){
        EntityRendererRegistry.register(ModEntities.TEMPLATE, TemplateEntityRenderer::new);
    }
}
