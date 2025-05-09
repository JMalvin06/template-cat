package oreo.fabricmod;

import oreo.fabricmod.entities.ModEntities;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;
import software.bernie.example.GeckoLibMod;
import software.bernie.geckolib.GeckoLib;

@Environment(EnvType.CLIENT)
public class MalvinFabricModClient implements ClientModInitializer {

    public static final EntityModelLayer MODEL_OREO_LAYER = new EntityModelLayer(new Identifier("malvinfabricmod", "oreo_entity"), "main");

    @Override
    public void onInitializeClient() {
        /*
         * Registers our Cube Entity's renderer, which provides a model and texture for the entity.
         *
         * Entity Renderers can also manipulate the model before it renders based on entity context (EndermanEntityRenderer#render).
         */

        GeckoLib.initialize();

        //ModEntities.registerRenderer();
    }
}