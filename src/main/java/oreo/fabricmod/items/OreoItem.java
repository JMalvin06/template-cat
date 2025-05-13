package oreo.fabricmod.items;

import oreo.fabricmod.OreoMod;
import oreo.fabricmod.StateSaverAndLoader;
import oreo.fabricmod.entities.ModEntities;
import oreo.fabricmod.entities.OreoEntity;
import oreo.fabricmod.util.ModSounds;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.entity.passive.CatVariant;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.registry.Registries;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;

import java.util.ArrayList;
import java.util.HashMap;


public class OreoItem extends Item {

    public OreoItem(Settings settings){
        super(settings);
    }


    @Override
    public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
        if(!user.getWorld().isClient()){
            if(entity.getType() == EntityType.CAT) {
                CatEntity cat = (CatEntity) entity;
                if (!cat.getVariant().equals(Registries.CAT_VARIANT.get(CatVariant.BLACK)) || !cat.getName().getString().equals("Oreo")) {
                    cat.setCustomName(Text.literal("Oreo"));
                    cat.setVariant(Registries.CAT_VARIANT.get(CatVariant.BLACK));
                    stack.decrement(1);
                    cat.playSound(SoundEvents.ENTITY_ITEM_BREAK, 1f, 1f);
                    return ActionResult.SUCCESS;
                }
            }
//          else if (entity.getType() == ModEntities.OREO_ENTITY){
//                OreoEntity oreo = (OreoEntity) entity;
//                if(oreo.getCurrentMode() == OreoEntity.OreoMode.FOLLOW)
//                    oreo.setRoamMode();
//                else if(oreo.getCurrentMode() == OreoEntity.OreoMode.ROAM)
//                    oreo.setFollowMode();
//                user.sendMessage(Text.literal("Set Mode to: " +
//                        (oreo.getCurrentMode() == OreoEntity.OreoMode.FOLLOW ? "Follow" : "Roam")));
//            }
        }
        return ActionResult.PASS;
    }


    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        if(!context.getWorld().isClient){
            StateSaverAndLoader state = StateSaverAndLoader.getServerState(context.getWorld().getServer());
            ArrayList<String> catsInWorld = state.catList;
            if(!catsInWorld.contains("Oreo")) {
                OreoEntity oreo = new OreoEntity(ModEntities.OREO_ENTITY, context.getWorld());
                oreo.setCustomName(Text.literal("Oreo"));
                oreo.setOwner(context.getPlayer());
                if(oreo.getCurrentBehavior() == null)
                    oreo.setFollowMode();
                oreo.setPos(context.getBlockPos().getX(), context.getBlockPos().getY() + 1.1, context.getBlockPos().getZ());
                if(context.getWorld().spawnEntity(oreo)) {
                    oreo.playSound(ModSounds.OREO_BOOM, 1f, 1f);
                    context.getStack().decrement(1);
                    return ActionResult.SUCCESS;
                } else {
                    OreoMod.LOGGER.info("Failed to spawn Oreo");
                    return ActionResult.FAIL;
                }
            }
        }

        return ActionResult.PASS;
    }
}
