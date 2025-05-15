package oreo.fabricmod.util;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import oreo.fabricmod.OreoMod;

public class ModSounds {

    public static final Identifier OREO_SOUND_ID = new Identifier(OreoMod.MOD_ID, "oreo_sound");
    public static SoundEvent OREO_BOOM = SoundEvent.of(OREO_SOUND_ID);

    public static void registerSounds(){
        Registry.register(Registries.SOUND_EVENT, OREO_SOUND_ID, OREO_BOOM);
    }
}
