package oreo.fabricmod;

import oreo.fabricmod.blocks.ModBlocks;
import oreo.fabricmod.entities.ModEntities;
import oreo.fabricmod.items.ModItems;
import oreo.fabricmod.util.ModEvents;
import oreo.fabricmod.util.ModSounds;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class OreoMod implements ModInitializer {
	public static final String MOD_ID = "oreomod";


    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);


	@Override
	public void onInitialize() {
		ModItems.registerItems();
		ModEntities.registerEntities();
		ModBlocks.registerBlocks();
		ModEvents.registerEvents();
		ModSounds.registerSounds();

		LOGGER.info("Hello Fabric world!");
	}
}