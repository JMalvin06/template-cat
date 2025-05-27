package oreo.fabricmod.items;

import net.minecraft.entity.EntityType;
import net.minecraft.text.Text;
import oreo.fabricmod.OreoMod;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import oreo.fabricmod.entities.EnhancedCat;
import oreo.fabricmod.entities.ModEntities;

import static oreo.fabricmod.entities.ModEntities.*;

public class ModItems {
    public static final Item TEMP = createCatItem(TEMPLATE);

    public static CatItem createCatItem(EntityType<? extends EnhancedCat> type){
        Identifier id = Registries.ENTITY_TYPE.getId(type);
        String name = id.getPath().substring(0,id.getPath().indexOf("_entity"));
        name = name.substring(0,1).toUpperCase() + name.substring(1);
        OreoMod.LOGGER.info("Created {}'s item", name);
        return new CatItem(new FabricItemSettings().maxCount(1), type, name);
    }

    public static void registerItems(){
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.SPAWN_EGGS).register(content -> {
            content.add(TEMP);
        });
        Registry.register(Registries.ITEM, new Identifier(OreoMod.MOD_ID, OreoMod.customName.toLowerCase()+"_item"), TEMP);
    }


}
