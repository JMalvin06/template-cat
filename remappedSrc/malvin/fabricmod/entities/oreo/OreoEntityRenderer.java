package oreo.fabricmod.entities.client;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;

/*
 * A renderer is used to provide an entity model, shadow size, and texture.
 */
public class OreoEntityRenderer extends GeoEntityRenderer<OreoEntity> {
    public OreoEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new OreoEntityModel());
    }

    @Override
    public Identifier getTextureLocation(OreoEntity animatable) {
        return new Identifier("malvinfabricmod", "textures/entity/oreo/oreo.png");
    }

    @Override
    public void render(OreoEntity entity, float entityYaw, float partialTick, MatrixStack poseStack, VertexConsumerProvider bufferSource, int packedLight) {
        poseStack.scale(.8f,.8f,.8f);
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }
}
