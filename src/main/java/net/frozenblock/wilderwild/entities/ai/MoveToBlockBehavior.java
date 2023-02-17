package net.frozenblock.wilderwild.entities.ai;

import com.google.common.collect.ImmutableMap;
import net.frozenblock.wilderwild.util.interfaces.FrozenBehavior;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.level.LevelReader;

public abstract class MoveToBlockBehavior<E extends PathfinderMob> extends Behavior<E> {
    public static final int DURATION = 1200;
    protected final E mob;
    public final double speedModifier;
    protected int tryTicks;
    protected BlockPos blockPos;
    private boolean reachedTarget;
    private final int searchRange;
    private final int verticalSearchRange;
    protected int verticalSearchStart;

    public MoveToBlockBehavior(E mob, double speedModifier, int searchRange) {
        this(mob, speedModifier, searchRange, 1);
    }

    public MoveToBlockBehavior(E mob, double speedModifier, int searchRange, int verticalSearchRange) {
        super(ImmutableMap.of(), 1200);
        this.blockPos = BlockPos.ZERO;
        this.mob = mob;
        this.speedModifier = speedModifier;
        this.searchRange = searchRange;
        this.verticalSearchStart = 0;
        this.verticalSearchRange = verticalSearchRange;
    }

    public boolean checkExtraStartConditions(ServerLevel level, E owner) {
        return this.findNearestBlock();
    }

    public boolean canStillUse(ServerLevel level, E entity, long gameTime) {
        return this.tryTicks >= -((FrozenBehavior)this).getDuration() && this.tryTicks <= 1200 && this.isValidTarget(level, this.blockPos);
    }

    public void start(ServerLevel level, E entity, long gameTime) {
        this.moveMobToBlock();
        this.tryTicks = 0;
    }

    protected void moveMobToBlock() {
        this.mob.getNavigation().moveTo((double)this.blockPos.getX() + 0.5, (double)(this.blockPos.getY() + 1), (double)this.blockPos.getZ() + 0.5, this.speedModifier);
    }

    public double acceptedDistance() {
        return 1.0;
    }

    protected BlockPos getMoveToTarget() {
        return this.blockPos.above();
    }

    protected void tick(ServerLevel level, E owner, long gameTime) {
        BlockPos blockPos = this.getMoveToTarget();
        if (!blockPos.closerToCenterThan(owner.position(), this.acceptedDistance())) {
            this.reachedTarget = false;
            ++this.tryTicks;
            if (this.shouldRecalculatePath()) {
                this.mob.getNavigation().moveTo((double)blockPos.getX() + 0.5, (double)blockPos.getY(), (double)blockPos.getZ() + 0.5, this.speedModifier);
            }
        } else {
            this.reachedTarget = true;
            --this.tryTicks;
        }

    }

    public boolean shouldRecalculatePath() {
        return this.tryTicks % 40 == 0;
    }

    protected boolean isReachedTarget() {
        return this.reachedTarget;
    }

    protected boolean findNearestBlock() {
        BlockPos blockPos = this.mob.blockPosition();
        BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();

        for(int k = this.verticalSearchStart; k <= this.verticalSearchRange; k = k > 0 ? -k : 1 - k) {
            for(int l = 0; l < this.searchRange; ++l) {
                for(int m = 0; m <= l; m = m > 0 ? -m : 1 - m) {
                    for(int n = m < l && m > -l ? l : 0; n <= l; n = n > 0 ? -n : 1 - n) {
                        mutableBlockPos.setWithOffset(blockPos, m, k - 1, n);
                        if (this.mob.isWithinRestriction(mutableBlockPos) && this.isValidTarget(this.mob.level, mutableBlockPos)) {
                            this.blockPos = mutableBlockPos;
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    public abstract boolean isValidTarget(LevelReader var1, BlockPos var2);
}