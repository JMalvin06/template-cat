package oreo.fabricmod.client;

import oreo.fabricmod.OreoMod;
import oreo.fabricmod.entities.OreoEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class OreoEntityModel extends GeoModel<OreoEntity>{

    @Override
    public Identifier getModelResource(OreoEntity animatable) {
        return new Identifier(OreoMod.MOD_ID, "geo/oreo.geo.json");
    }

    @Override
    public Identifier getTextureResource(OreoEntity animatable) {
        return new Identifier(OreoMod.MOD_ID, "textures/entity/oreo/oreo.png");
    }

    @Override
    public Identifier getAnimationResource(OreoEntity animatable) {
        return new Identifier(OreoMod.MOD_ID, "animations/oreo.animation.json");
    }

    @Override
    public void setCustomAnimations(OreoEntity animatable, long instanceId, AnimationState<OreoEntity> animationState) {
        CoreGeoBone head = getAnimationProcessor().getBone("head");

        if(head != null){
            EntityModelData entityData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);
            head.setRotX(entityData.headPitch() * MathHelper.RADIANS_PER_DEGREE);
            if(!animatable.isInSleepingPose())
                head.setRotY(entityData.netHeadYaw() * MathHelper.RADIANS_PER_DEGREE);
        }
    }


}