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
                    try {
                        EnhancedCat newCat = (EnhancedCat) entity.getType().create(world);
                        if (enhancedCat.getWorld().getBlockState(enhancedCat.getHomePos()).isOf(ModBlocks.CAT_BED)) {
                            CatBed currentBed = (CatBed) enhancedCat.getWorld().getBlockState(enhancedCat.getHomePos()).getBlock();
                            currentBed.setUser(newCat);
                        }
                        newCat.setCustomName(Text.literal(enhancedCat.getName().getString()));
                        if (enhancedCat.getOwner() != null) {
                            newCat.setOwner((PlayerEntity) enhancedCat.getOwner());
                        }
                        Vec3d spawn = enhancedCat.getHomePos().toCenterPos();
                        newCat.setPos(spawn.x, spawn.y + 1.1, spawn.z);
                        newCat.setHomePos(enhancedCat.getHomePos());
                        if (enhancedCat.getWorld().spawnEntity(newCat)) {
                            OreoMod.LOGGER.info("Welcome back " + newCat.getName().getString() + "!");
                        }
                    } catch (Exception e) {
                        for (PlayerEntity player : enhancedCat.getWorld().getPlayers()) {
                            player.sendMessage(Text.literal("Oreo was unfortunately not respawned.."));
                        }
                        OreoMod.LOGGER.info(e.toString());
                        PlayerEntity owner = ((PlayerEntity) enhancedCat.getOwner());
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
