package oreo.fabricmod.ai;

import oreo.fabricmod.OreoMod;
import oreo.fabricmod.blocks.CatBed;
import oreo.fabricmod.blocks.ModBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.entity.ai.goal.GoToBedAndSleepGoal;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldView;
import oreo.fabricmod.blocks.CatBed;
import oreo.fabricmod.blocks.ModBlocks;
import oreo.fabricmod.entities.EnhancedCat;

public class SleepInCatBedGoal extends GoToBedAndSleepGoal {

    private final EnhancedCat cat;

    public SleepInCatBedGoal(EnhancedCat cat, double speed, int range) {
        super(cat, speed, range);
        this.lowestY = 0; // Allow cat to find bed on same level
        this.cat = cat;
    }

    @Override
    protected boolean hasReached() {
        return this.targetPos.equals(this.cat.getBlockPos());
    }


    @Override
    protected boolean isTargetPos(WorldView world, BlockPos pos) {
        BlockState block = world.getBlockState(pos);
        if(block.isOf(ModBlocks.CAT_BED)) {
            EnhancedCat user = (EnhancedCat) ((CatBed) block.getBlock()).getUser();
            // Only set as target if the user of the bed is this entity
            if (user != null)
                return world.isAir(pos.up()) && ((CatBed) block.getBlock()).getUser().equals(this.cat);
        }
        return false;
    }
}
