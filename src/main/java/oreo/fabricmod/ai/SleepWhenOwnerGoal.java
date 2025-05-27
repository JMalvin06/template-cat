package oreo.fabricmod.ai;

import oreo.fabricmod.entities.EnhancedCat;
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
import oreo.fabricmod.entities.EnhancedCat;
import org.jetbrains.annotations.Nullable;
import java.util.List;

public class SleepWhenOwnerGoal extends Goal {

    private final EnhancedCat cat;
    @Nullable
    private PlayerEntity owner;
    private int ticksOnBed;

    public SleepWhenOwnerGoal(EnhancedCat cat) {
        this.cat = cat;
    }

    public boolean canStart() {
        if (this.cat.isTamed() && !this.cat.isSitting()) {
            LivingEntity livingEntity = this.cat.getOwner();
            if (livingEntity instanceof PlayerEntity) {
                this.owner = (PlayerEntity) livingEntity;
                return this.owner.isSleeping() && this.cat.getHomePos() != null;
            }
        }
        return false;
    }


    public boolean shouldContinue() {
        return this.cat.isTamed() && !this.cat.isSitting() &&
                this.owner != null && this.owner.isSleeping() &&
                this.cat.getHomePos() != null; // Has home
    }

    public void start() {
        this.cat.setInSittingPose(false); // Make sure cat is not sitting
        this.cat.getNavigation().startMovingTo(
                this.cat.getHomePos().getX(), this.cat.getHomePos().getY(), this.cat.getHomePos().getZ(),
                0.8);
    }

    public void stop() {
        this.cat.setInSleepingPose(false);
        double f = (double)this.cat.getWorld().getSkyAngle(1.0F);
        if (this.owner.getSleepTimer() >= 100 && f > 0.77 && f < 0.8 && (double)this.cat.getWorld().getRandom().nextFloat() < 0.7) {
            this.dropMorningGifts();
        }

        this.ticksOnBed = 0;
        this.cat.setHeadDown(false);
        this.cat.getNavigation().stop();
    }

    // Drop random gifts for owner in the morning
    private void dropMorningGifts() {
        Random random = this.cat.getRandom();

        BlockPos.Mutable mutable = new BlockPos.Mutable();
        mutable.set(this.cat.isLeashed() ? this.cat.getHoldingEntity().getBlockPos() : this.cat.getBlockPos());
        this.cat.teleport((mutable.getX() + random.nextInt(11) - 5), (mutable.getY() + random.nextInt(5) - 2), (mutable.getZ() + random.nextInt(11) - 5), false);
        mutable.set(this.cat.getBlockPos());

        LootTable lootTable = this.cat.getWorld().getServer().getLootManager().getLootTable(LootTables.CAT_MORNING_GIFT_GAMEPLAY);
        LootContextParameterSet lootContextParameterSet = (new LootContextParameterSet.Builder((ServerWorld)this.cat.getWorld())).add(LootContextParameters.ORIGIN, this.cat.getPos()).add(LootContextParameters.THIS_ENTITY, this.cat).build(LootContextTypes.GIFT);
        List<ItemStack> list = lootTable.generateLoot(lootContextParameterSet);

        for (ItemStack itemStack : list) {
            this.cat.getWorld().spawnEntity(new ItemEntity(this.cat.getWorld(),  mutable.getX() - (double) MathHelper.sin(this.cat.bodyYaw * 0.017453292F),  mutable.getY(), (double) mutable.getZ() + (double) MathHelper.cos(this.cat.bodyYaw * 0.017453292F), itemStack));
        }
    }

    public void tick() {
        if (this.owner != null && this.cat.getHomePos() != null) {
            this.cat.setInSittingPose(false); // Make sure cat is not sitting
            this.cat.getNavigation().startMovingTo(
                    this.cat.getHomePos().getX(), this.cat.getHomePos().getY(), this.cat.getHomePos().getZ(),
                    1.100000023841858);

            if (this.cat.squaredDistanceTo(this.cat.getHomePos().toCenterPos()) < 2.5) {
                ++this.ticksOnBed;
                if (this.ticksOnBed > this.getTickCount(16)) {
                    this.cat.setInSleepingPose(true);
                    this.cat.setHeadDown(false);
                } else {
                    this.cat.lookAtEntity(this.owner, 45.0F, 45.0F);
                    this.cat.setHeadDown(true);
                }
            } else {
                this.cat.setInSleepingPose(false);
            }
        }

    }
}
