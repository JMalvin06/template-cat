package oreo.fabricmod.items;

import oreo.fabricmod.OreoMod;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModItems {
    public static final Item OREO = new OreoItem(new FabricItemSettings().maxCount(1));

    public static void registerItems(){
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.SPAWN_EGGS).register(content -> {
            content.add(OREO);
        });
        Registry.register(Registries.ITEM, new Identifier(OreoMod.MOD_ID, "oreo_item"), OREO);

    }


}
