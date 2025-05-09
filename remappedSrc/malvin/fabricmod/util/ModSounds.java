package oreo.fabricmod.util;

import net.minecraft.registry.Registries;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModSounds {

    public static final Identifier OREO_SOUND_ID = new Identifier("malvinfabricmod:oreo_sound");
    public static SoundEvent OREO_BOOM = SoundEvent.of(OREO_SOUND_ID);

    public static void registerSounds(){
        Registry.register(Registries.SOUND_EVENT, OREO_SOUND_ID, OREO_BOOM);
    }
}
