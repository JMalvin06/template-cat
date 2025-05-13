package oreo.fabricmod.util;

import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.tag.TagKey;
import oreo.fabricmod.OreoMod;
import oreo.fabricmod.StateSaverAndLoader;
import oreo.fabricmod.blocks.CatBed;
import oreo.fabricmod.blocks.ModBlocks;
import oreo.fabricmod.entities.EnhancedCat;
import oreo.fabricmod.entities.ModEntities;
import oreo.fabricmod.entities.OreoEntity;
import oreo.fabricmod.items.ModItems;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;

public class ModEvents {

    public static void registerEvents(){
        ServerEntityEvents.ENTITY_LOAD.register((entity, world) -> {
            if(entity instanceof EnhancedCat) {
                StateSaverAndLoader serverState = StateSaverAndLoader.getServerState(world.getServer());
                String name = entity.getName().getString();
                if (!serverState.catList.contains(name)){
                    serverState.catList.add(name);
                } else {
                    entity.remove(Entity.RemovalReason.DISCARDED);
                }
            }
        });

        ServerEntityEvents.ENTITY_UNLOAD.register((entity, world) -> {
            if(entity instanceof EnhancedCat enhancedCat){
                StateSaverAndLoader serverState = StateSaverAndLoader.getServerState(world.getServer());
                OreoMod.LOGGER.info("Oreo is now not world");
                serverState.catList.remove("Oreo");

                if(enhancedCat.isDead()){
                    enhancedCat.revive();
                }
            }
        });
    }
}
