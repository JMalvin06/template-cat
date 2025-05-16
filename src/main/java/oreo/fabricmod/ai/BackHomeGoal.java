package oreo.fabricmod.ai;

import oreo.fabricmod.entities.EnhancedCat;
import net.minecraft.entity.ai.goal.MoveToTargetPosGoal;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldView;

public class BackHomeGoal extends MoveToTargetPosGoal {

    private final EnhancedCat cat;

    public BackHomeGoal(EnhancedCat cat, double speed){
        super(cat, speed, 0); // Range never used
        this.cat = cat;
    }

    @Override
    public boolean canStart() {
        return this.mob.getWorld().isNight() &&
                !this.mob.isLeashed() &&
                !((CatEntity) this.mob).isSitting() &&
                !this.mob.getBlockPos().isWithinDistance(this.cat.getHomePos().toCenterPos(), 10) &&
                ((EnhancedCat) this.mob).getCurrentBehavior() == EnhancedCat.BehaviorState.ROAM;
    }

    @Override
    public void start() {
        this.targetPos = this.cat.getHomePos();
        super.start();
    }

    @Override
    protected BlockPos getTargetPos() {
        return this.cat.getHomePos().up();
    }

    // This method is not used.
    @Override
    protected boolean isTargetPos(WorldView world, BlockPos pos) {
        return false;
    }

}
