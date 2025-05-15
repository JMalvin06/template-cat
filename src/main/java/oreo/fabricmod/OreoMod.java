package oreo.fabricmod;

import oreo.fabricmod.blocks.ModBlocks;
import oreo.fabricmod.entities.ModEntities;
import oreo.fabricmod.items.ModItems;
import oreo.fabricmod.util.ModEvents;
import oreo.fabricmod.util.ModSounds;
import net.fabricmc.api.ModInitializer;
import org.apache.commons.logging.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class OreoMod implements ModInitializer {
	public static final String MOD_ID = "oreomod";


    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);


	@Override
	public void onInitialize() {
		ModItems.registerItems();
		LOGGER.info("Registered items");
		ModEntities.registerEntities();
		LOGGER.info("Registered entities");
		ModBlocks.registerBlocks();
		LOGGER.info("Registered blocks");
		ModEvents.registerEvents();
		LOGGER.info("Registered events");
		ModSounds.registerSounds();
		LOGGER.info("Registered sounds");
	}
}