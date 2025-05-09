package oreo.fabricmod.blocks;

import oreo.fabricmod.OreoMod;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModBlocks {

    public static final CatBed CAT_BED = new CatBed(FabricBlockSettings.copyOf(Blocks.BLACK_WOOL));

    public static void registerBlocks(){
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.BUILDING_BLOCKS).register(content -> {
            content.add(CAT_BED);
        });
        Registry.register(Registries.BLOCK , new Identifier(OreoMod.MOD_ID, "cat_bed"), CAT_BED);
        Registry.register(Registries.ITEM, new Identifier(OreoMod.MOD_ID, "cat_bed"), new BlockItem(CAT_BED, new Item.Settings()));
    }

}
