package oreo.fabricmod.ai;

import oreo.fabricmod.entities.OreoEntity;
import net.minecraft.entity.ai.goal.MoveToTargetPosGoal;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldView;

public class OreoBackHomeGoal extends MoveToTargetPosGoal {

    private final OreoEntity oreo;

    public OreoBackHomeGoal(OreoEntity oreo, double speed){
        super(oreo, speed, 0); // Range never used
        this.oreo = oreo;
    }

    @Override
    public boolean canStart() {
        return this.mob.getWorld().isNight() &&
                !this.mob.isLeashed() &&
                !((CatEntity) this.mob).isSitting() &&
                !this.mob.getBlockPos().isWithinDistance(this.oreo.getHomePos().toCenterPos(), 10) &&
                ((OreoEntity) this.mob).getCurrentMode() == OreoEntity.OreoMode.ROAM;
    }

    @Override
    public void start() {
        this.targetPos = this.oreo.getHomePos();
        super.start();
    }

    @Override
    protected BlockPos getTargetPos() {
        return this.oreo.getHomePos().up();
    }

    // This method is not used.
    @Override
    protected boolean isTargetPos(WorldView world, BlockPos pos) {
        return false;
    }

}
