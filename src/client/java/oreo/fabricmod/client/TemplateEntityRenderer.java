package oreo.fabricmod.client;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import oreo.fabricmod.OreoMod;
import oreo.fabricmod.entities.OreoEntity;
import oreo.fabricmod.entities.TemplateCat;

import java.util.List;

public class TemplateEntityRenderer extends MobEntityRenderer<TemplateCat, TemplateEntityModel<TemplateCat>> {
    public TemplateEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new TemplateEntityModel<>(context.getPart(EntityModelLayers.CAT)), 0.4f);
        this.addFeature(new CreateHatPosFix<>(this));
    }

    @Override
    public Identifier getTexture(TemplateCat entity) {
        return new Identifier(OreoMod.MOD_ID, "textures/entity/template/"+OreoMod.customName.toLowerCase()+".png");
    }

    @Override
    protected void scale(TemplateCat cat, MatrixStack matrixStack, float f) {
        super.scale(cat, matrixStack, f);
        matrixStack.scale(0.8f, 0.8f, 0.8f);
    }

    @Override
    protected void setupTransforms(TemplateCat cat, MatrixStack matrixStack, float f, float g, float h) {
        super.setupTransforms(cat, matrixStack, f, g, h);
        float i = cat.getSleepAnimation(h);
        if (i > 0.0f) {
            matrixStack.translate(0.4f * i, 0.15f * i, 0.1f * i);
            matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(MathHelper.lerpAngleDegrees((float)i, (float)0.0f, (float)90.0f)));
            BlockPos blockPos = cat.getBlockPos();
            List<PlayerEntity> list = cat.getWorld().getNonSpectatingEntities(PlayerEntity.class, new Box(blockPos).expand(2.0, 2.0, 2.0));
            for (PlayerEntity playerEntity : list) {
                if (!playerEntity.isSleeping()) continue;
                matrixStack.translate(0.15f * i, 0.0f, 0.0f);
                break;
            }
        }
    }
}
