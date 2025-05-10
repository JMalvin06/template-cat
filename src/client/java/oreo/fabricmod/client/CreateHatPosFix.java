package oreo.fabricmod.client;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import oreo.fabricmod.entities.OreoEntity;

public class CreateHatPosFix<T extends OreoEntity, M extends OreoEntityModel<T>> extends FeatureRenderer<T, M> {

    public CreateHatPosFix(FeatureRendererContext<T, M> context) {
        super(context);
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, T entity,
                       float limbAngle, float limbDistance, float tickDelta, float animationProgress,
                       float headYaw, float headPitch) {
        matrices.translate(0.0, 0.06, 0.0);
    }
}
