package oreo.fabricmod.ai;

import oreo.fabricmod.entities.OreoEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.context.LootContextParameterSet;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import org.jetbrains.annotations.Nullable;
import java.util.List;

public class SleepWhenOwnerGoal extends Goal {

    private final OreoEntity oreo;
    @Nullable
    private PlayerEntity owner;
    private int ticksOnBed;

    public SleepWhenOwnerGoal(OreoEntity oreo) {
        this.oreo = oreo;
    }

    public boolean canStart() {
        if (this.oreo.isTamed() && !this.oreo.isSitting()) {
            LivingEntity livingEntity = this.oreo.getOwner();
            if (livingEntity instanceof PlayerEntity) {
                this.owner = (PlayerEntity) livingEntity;
                return this.owner.isSleeping() && this.oreo.getHomePos() != null;
            }
        }
        return false;
    }


    public boolean shouldContinue() {
        return this.oreo.isTamed() && !this.oreo.isSitting() &&
                this.owner != null && this.owner.isSleeping() &&
                this.oreo.getHomePos() != null; // Has home
    }

    public void start() {
        this.oreo.setInSittingPose(false); // Make sure cat is not sitting
        this.oreo.getNavigation().startMovingTo(
                this.oreo.getHomePos().getX(), this.oreo.getHomePos().getY(), this.oreo.getHomePos().getZ(),
                0.8);
    }

    public void stop() {
        this.oreo.setInSleepingPose(false);
        double f = (double)this.oreo.getWorld().getSkyAngle(1.0F);
        if (this.owner.getSleepTimer() >= 100 && f > 0.77 && f < 0.8 && (double)this.oreo.getWorld().getRandom().nextFloat() < 0.7) {
            this.dropMorningGifts();
        }

        this.ticksOnBed = 0;
        this.oreo.setHeadDown(false);
        this.oreo.getNavigation().stop();
    }

    // Drop random gifts for owner in the morning
    private void dropMorningGifts() {
        Random random = this.oreo.getRandom();

        BlockPos.Mutable mutable = new BlockPos.Mutable();
        mutable.set(this.oreo.isLeashed() ? this.oreo.getHoldingEntity().getBlockPos() : this.oreo.getBlockPos());
        this.oreo.teleport((mutable.getX() + random.nextInt(11) - 5), (mutable.getY() + random.nextInt(5) - 2), (mutable.getZ() + random.nextInt(11) - 5), false);
        mutable.set(this.oreo.getBlockPos());

        LootTable lootTable = this.oreo.getWorld().getServer().getLootManager().getLootTable(LootTables.CAT_MORNING_GIFT_GAMEPLAY);
        LootContextParameterSet lootContextParameterSet = (new LootContextParameterSet.Builder((ServerWorld)this.oreo.getWorld())).add(LootContextParameters.ORIGIN, this.oreo.getPos()).add(LootContextParameters.THIS_ENTITY, this.oreo).build(LootContextTypes.GIFT);
        List<ItemStack> list = lootTable.generateLoot(lootContextParameterSet);

        for (ItemStack itemStack : list) {
            this.oreo.getWorld().spawnEntity(new ItemEntity(this.oreo.getWorld(),  mutable.getX() - (double) MathHelper.sin(this.oreo.bodyYaw * 0.017453292F),  mutable.getY(), (double) mutable.getZ() + (double) MathHelper.cos(this.oreo.bodyYaw * 0.017453292F), itemStack));
        }
    }

    public void tick() {
        if (this.owner != null && this.oreo.getHomePos() != null) {
            this.oreo.setInSittingPose(false); // Make sure cat is not sitting
            this.oreo.getNavigation().startMovingTo(
                    this.oreo.getHomePos().getX(), this.oreo.getHomePos().getY(), this.oreo.getHomePos().getZ(),
                    1.100000023841858);

            if (this.oreo.squaredDistanceTo(this.oreo.getHomePos().toCenterPos()) < 2.5) {
                ++this.ticksOnBed;
                if (this.ticksOnBed > this.getTickCount(16)) {
                    this.oreo.setInSleepingPose(true);
                    this.oreo.setHeadDown(false);
                } else {
                    this.oreo.lookAtEntity(this.owner, 45.0F, 45.0F);
                    this.oreo.setHeadDown(true);
                }
            } else {
                this.oreo.setInSleepingPose(false);
            }
        }

    }
}
