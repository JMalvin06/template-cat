package oreo.fabricmod.client;

import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import oreo.fabricmod.OreoMod;
import oreo.fabricmod.entities.OreoEntity;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.client.render.entity.EntityRendererFactory;

import java.util.List;

/*
 * A renderer is used to provide an entity model, shadow size, and texture.
 */
public class OreoEntityRenderer extends MobEntityRenderer<OreoEntity, OreoEntityModel<OreoEntity>> {
    public OreoEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new OreoEntityModel(context.getPart(EntityModelLayers.CAT)), 0.4f);
    }

    @Override
    public Identifier getTexture(OreoEntity entity) {
        return new Identifier(OreoMod.MOD_ID, "textures/entity/oreo/oreo.png");
    }

    @Override
    protected void scale(OreoEntity oreo, MatrixStack matrixStack, float f) {
        super.scale(oreo, matrixStack, f);
        matrixStack.scale(0.85f, 0.85f, 0.85f);
    }

    @Override
    protected void setupTransforms(OreoEntity oreo, MatrixStack matrixStack, float f, float g, float h) {
        super.setupTransforms(oreo, matrixStack, f, g, h);
        float i = oreo.getSleepAnimation(h);
        if (i > 0.0f) {
            matrixStack.translate(0.4f * i, 0.15f * i, 0.1f * i);
            matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(MathHelper.lerpAngleDegrees((float)i, (float)0.0f, (float)90.0f)));
            BlockPos blockPos = oreo.getBlockPos();
            List<PlayerEntity> list = oreo.getWorld().getNonSpectatingEntities(PlayerEntity.class, new Box(blockPos).expand(2.0, 2.0, 2.0));
            for (PlayerEntity playerEntity : list) {
                if (!playerEntity.isSleeping()) continue;
                matrixStack.translate(0.15f * i, 0.0f, 0.0f);
                break;
            }
        }
    }
}
