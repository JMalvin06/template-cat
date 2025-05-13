package oreo.fabricmod.items;

import net.minecraft.entity.EntityType;
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

public class ModItems {
    public static final Item OREO = new CatItem(new FabricItemSettings().maxCount(1), ModEntities.OREO_ENTITY, "Oreo");
    public static final Item KIKI = new CatItem(new FabricItemSettings().maxCount(1), ModEntities.KIKI_ENTITY, "Kiki");

    public static void registerItems(){
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.SPAWN_EGGS).register(content -> {
            content.add(OREO);
            content.add(KIKI);
        });
        Registry.register(Registries.ITEM, new Identifier(OreoMod.MOD_ID, "oreo_item"), OREO);
        Registry.register(Registries.ITEM, new Identifier(OreoMod.MOD_ID, "kiki_item"), KIKI);
    }


}
