package oreo.fabricmod.util;

import oreo.fabricmod.OreoMod;
import oreo.fabricmod.StateSaverAndLoader;
import oreo.fabricmod.blocks.CatBed;
import oreo.fabricmod.blocks.ModBlocks;
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
            if(entity.getType().equals(ModEntities.OREO_ENTITY)){
                StateSaverAndLoader serverState = StateSaverAndLoader.getServerState(world.getServer());
                OreoMod.LOGGER.info("Oreo is now in world");
                if(!serverState.isOreoInWorld)
                    serverState.isOreoInWorld = true;
                else
                    entity.remove(Entity.RemovalReason.DISCARDED);

            }
        });

        ServerEntityEvents.ENTITY_UNLOAD.register((entity, world) -> {
            if(entity.getType().equals(ModEntities.OREO_ENTITY)){
                OreoEntity oreo = (OreoEntity) entity;
                StateSaverAndLoader serverState = StateSaverAndLoader.getServerState(world.getServer());
                OreoMod.LOGGER.info("Oreo is now not world");
                serverState.isOreoInWorld = false;

                if(oreo.isDead()){
                    try {
                        OreoEntity newOreo = new OreoEntity(ModEntities.OREO_ENTITY, oreo.getWorld());
                        if(entity.getWorld().getBlockState(oreo.getHomePos()).isOf(ModBlocks.CAT_BED)){
                            CatBed currentBed = (CatBed)oreo.getWorld().getBlockState(oreo.getHomePos()).getBlock();
                            currentBed.setUser(newOreo);
                        }
                        newOreo.setCustomName(Text.literal("Oreo"));
                        if (oreo.getOwner() != null) {
                            newOreo.setOwner((PlayerEntity) oreo.getOwner());
                        }
                        Vec3d spawn = oreo.getHomePos().toCenterPos();
                        newOreo.setPos(spawn.x, spawn.y + 1.1, spawn.z);
                        newOreo.setHomePos(oreo.getHomePos());
                        if (oreo.getWorld().spawnEntity(newOreo)) {
                            OreoMod.LOGGER.info("Welcome back Oreo!");
                        }
                    } catch (Exception e) {
                        for(PlayerEntity player: oreo.getWorld().getPlayers()){
                            player.sendMessage(Text.literal("Oreo was unfortunately not respawned.."));
                        }
                        OreoMod.LOGGER.info(e.toString());
                        PlayerEntity owner = ((PlayerEntity) oreo.getOwner());
                        if (owner != null) {
                            owner.giveItemStack(ModItems.OREO.getDefaultStack());
                        } else
                            OreoMod.LOGGER.info("No Oreo owner to give item stack to");
                    }
                }
            }
        });
    }
}
