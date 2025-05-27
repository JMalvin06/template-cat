package oreo.fabricmod;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffects;
import oreo.fabricmod.blocks.ModBlocks;
import oreo.fabricmod.entities.ModEntities;
import oreo.fabricmod.items.ModItems;
import oreo.fabricmod.util.ModEvents;
import oreo.fabricmod.util.ModSounds;
import net.fabricmc.api.ModInitializer;
import org.apache.commons.logging.Log;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;


public class OreoMod implements ModInitializer {
	public static final String MOD_ID = "oreomod";

	public static final HashMap<String, StatusEffect> statusEffectMap = new HashMap<>(){
		{
			put("Speed", StatusEffects.SPEED);
			put("Slowness", StatusEffects.SLOWNESS);
			put("Haste", StatusEffects.HASTE);
			put("Fatigue", StatusEffects.MINING_FATIGUE);
			put("Strength", StatusEffects.STRENGTH);
			put("Jump Boost", StatusEffects.JUMP_BOOST);
			put("Nausea", StatusEffects.NAUSEA);
			put("Regeneration", StatusEffects.REGENERATION);
			put("Resistance", StatusEffects.RESISTANCE);
			put("Fire Resistance", StatusEffects.FIRE_RESISTANCE);
			put("Water Breathing", StatusEffects.WATER_BREATHING);
			put("Invisibility", StatusEffects.INVISIBILITY);
			put("Blindness", StatusEffects.BLINDNESS);
			put("Night Vision", StatusEffects.NIGHT_VISION);
			put("Hunger", StatusEffects.HUNGER);
			put("Weakness", StatusEffects.WEAKNESS);
			put("Poison", StatusEffects.POISON);
			put("Wither", StatusEffects.WITHER);
			put("Health", StatusEffects.HEALTH_BOOST);
			put("Absorption", StatusEffects.ABSORPTION);
			put("Saturation", StatusEffects.SATURATION);
			put("Glowing", StatusEffects.GLOWING);
			put("Levitation", StatusEffects.LEVITATION);
			put("Luck", StatusEffects.LUCK);
			put("Unluck", StatusEffects.UNLUCK);
			put("Slow Falling", StatusEffects.SLOW_FALLING);
			put("Dolphin's Grace", StatusEffects.DOLPHINS_GRACE);
			put("Darkness", StatusEffects.DARKNESS);
		}
	};


    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);


	static JSONObject json;
	public static String customName = "";
	public static HashMap<StatusEffect, Integer> customEffects = new HashMap<StatusEffect, Integer>();

	public void setupCustomEntity(){
		File file = new File("../src/main/java/oreo/fabricmod/custom_cat.json");
		StringBuilder data = new StringBuilder();
		try {
			Scanner fin = new Scanner(file);
			while (fin.hasNextLine()) {
				data.append(fin.nextLine());
			}
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}

		try {
			json = (JSONObject) new JSONParser().parse(data.toString());
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
		try {
			customName = json.get("name").toString();
		} catch (NullPointerException e) {
			throw new NullPointerException("No name variable in json file");
		}
		try {
			@SuppressWarnings("unchecked")
			HashMap<String, Long> jsonEffects = (HashMap<String, Long>) json.get("statusEffects");
			for(Map.Entry<String, Long> entry : jsonEffects.entrySet()){
				customEffects.put(statusEffectMap.get(entry.getKey()), entry.getValue().intValue());
			}
		} catch (NullPointerException e) {
            LOGGER.info("No status effects for for cat \"{}\"", customName);
		}
	}

	@Override
	public void onInitialize() {
		setupCustomEntity();
		LOGGER.info("Registered items");
		ModEntities.registerEntities();
        ModItems.registerItems();
		LOGGER.info("Registered entities");
		ModBlocks.registerBlocks();
		LOGGER.info("Registered blocks");
		ModEvents.registerEvents();
		LOGGER.info("Registered events");
		ModSounds.registerSounds();
		LOGGER.info("Registered sounds");
	}
}