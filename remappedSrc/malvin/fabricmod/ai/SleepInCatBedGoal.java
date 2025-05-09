package oreo.fabricmod.ai;

import oreo.fabricmod.MalvinFabricMod;
import oreo.fabricmod.blocks.CatBed;
import oreo.fabricmod.blocks.ModBlocks;
import oreo.fabricmod.entities.client.OreoEntity;
import net.minecraft.block.BlockState;
import net.minecraft.entity.ai.goal.GoToBedAndSleepGoal;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldView;

public class SleepInCatBedGoal extends GoToBedAndSleepGoal {

    private final OreoEntity oreo;

    public SleepInCatBedGoal(OreoEntity oreo, double speed, int range) {
        super(oreo, speed, range);
        this.lowestY = 0; // Allow cat to find bed on same level
        this.oreo = oreo;
    }

    @Override
    protected boolean hasReached() {
        return this.targetPos.equals(this.oreo.getBlockPos());
    }


    @Override
    protected boolean isTargetPos(WorldView world, BlockPos pos) {
        BlockState block = world.getBlockState(pos);
        if(block.isOf(ModBlocks.CAT_BED)) {
            OreoEntity user = (OreoEntity)((CatBed) block.getBlock()).getUser();
            MalvinFabricMod.LOGGER.info(((CatBed) block.getBlock()).getUser().toString());
            // Only set as target if the user of the bed is this entity
            if (user != null)
                return world.isAir(pos.up()) && ((CatBed) block.getBlock()).getUser().equals(this.oreo);
        }
        return false;
    }
}
