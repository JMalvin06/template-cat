package oreo.fabricmod;

import oreo.fabricmod.blocks.ModBlocks;
import oreo.fabricmod.entities.ModEntities;
import oreo.fabricmod.items.ModItems;
import oreo.fabricmod.util.ModEvents;
import oreo.fabricmod.util.ModSounds;
import net.fabricmc.api.ModInitializer;
import net.minecraft.registry.Registries;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class MalvinFabricMod implements ModInitializer {

    public static final Logger LOGGER = LoggerFactory.getLogger("malvinfabricmod");
	public static final String MOD_ID = "malvinfabricmod";


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