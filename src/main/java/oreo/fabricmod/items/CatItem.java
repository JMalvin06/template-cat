package oreo.fabricmod.items;

import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.world.World;
import oreo.fabricmod.OreoMod;
import oreo.fabricmod.StateSaverAndLoader;
import oreo.fabricmod.entities.EnhancedCat;
import oreo.fabricmod.util.ModSounds;

import java.util.ArrayList;
public class CatItem extends Item {
    EntityType<?> type;
    String catName;
    public CatItem(Settings settings, EntityType<? extends EnhancedCat> type, String name) {
        super(settings);
        this.type = type;
        catName = name;
    }


    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        if(!world.isClient){
            if(world instanceof ServerWorld serverWorld) {
                StateSaverAndLoader state = StateSaverAndLoader.getServerState(context.getWorld().getServer());
                ArrayList<String> catsInWorld = state.catList;
                if (!catsInWorld.contains(catName)) {
                    EnhancedCat cat = (EnhancedCat) type.create(serverWorld);
                    cat.setCustomName(Text.literal(catName));
                    cat.setOwner(context.getPlayer());
                    if (cat.getCurrentBehavior() == null)
                        cat.setFollowMode();
                    cat.setPos(context.getBlockPos().getX(), context.getBlockPos().getY() + 1.1, context.getBlockPos().getZ());
                    if (context.getWorld().spawnEntity(cat)) {
                        cat.playSound(ModSounds.OREO_BOOM, 1f, 1f);
                        context.getStack().decrement(1);
                        return ActionResult.SUCCESS;
                    } else {
                        OreoMod.LOGGER.info("Failed to spawn Oreo");
                        return ActionResult.FAIL;
                    }
                }
            }
        }

        return ActionResult.PASS;
    }

}
