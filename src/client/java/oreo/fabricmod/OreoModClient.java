package oreo.fabricmod;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;
import oreo.fabricmod.client.ModRenderers;
import oreo.fabricmod.entities.ModEntities;

@Environment(EnvType.CLIENT)
public class OreoModClient implements ClientModInitializer {
	public static final EntityModelLayer MODEL_OREO_LAYER = new EntityModelLayer(new Identifier("malvinfabricmod", "oreo_entity"), "main");



	@Override
	public void onInitializeClient() {
		/*
		 * Registers our Cube Entity's renderer, which provides a model and texture for the entity.
		 *
		 * Entity Renderers can also manipulate the model before it renders based on entity context (EndermanEntityRenderer#render).
		 */


		ModRenderers.registerRenderer();
	}
}