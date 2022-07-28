package net.frozenblock.wilderwild.mixin.server;

import net.frozenblock.wilderwild.WilderWildClient;
import net.frozenblock.wilderwild.block.entity.NewSculkSensorBlockEntity;
import net.frozenblock.wilderwild.registry.RegisterBlockEntities;
import net.frozenblock.wilderwild.registry.RegisterProperties;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.SculkSensorBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.SculkSensorBlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.event.listener.GameEventListener;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SculkSensorBlock.class)
public class SculkSensorBlockMixin {

    @Inject(at = @At("TAIL"), method = "appendProperties", cancellable = true)
    public void appendProperties(StateManager.Builder<Block, BlockState> builder, CallbackInfo info) {
        builder.add(RegisterProperties.NOT_HICCUPPING);
    }

    @Nullable
    @Inject(at = @At("HEAD"), method = "createBlockEntity", cancellable = true)
    public void createBlockEntity(BlockPos pos, BlockState state, CallbackInfoReturnable<BlockEntity> info) {
        info.setReturnValue(new NewSculkSensorBlockEntity(pos, state));
        info.cancel();
    }

    @Inject(at = @At("HEAD"), method = "setActive", cancellable = true)
    private static void setActive(@Nullable Entity entity, World world, BlockPos pos, BlockState state, int power, CallbackInfo info) {
        world.addSyncedBlockEvent(pos, state.getBlock(), 1, 1);
    }


    @Nullable
    @Inject(at = @At("HEAD"), method = "getGameEventListener", cancellable = true)
    public <T extends BlockEntity> void getGameEventListener(ServerWorld world, T blockEntity, CallbackInfoReturnable<GameEventListener> info) {
        info.setReturnValue(blockEntity instanceof NewSculkSensorBlockEntity ? ((NewSculkSensorBlockEntity) blockEntity).getEventListener() : blockEntity instanceof SculkSensorBlockEntity ? ((SculkSensorBlockEntity) blockEntity).getEventListener() : null);
        info.cancel();
    }

    @Nullable
    @Inject(at = @At("HEAD"), method = "getTicker", cancellable = true)
    public <T extends BlockEntity> void getTicker(World world, BlockState state, BlockEntityType<T> type, CallbackInfoReturnable<BlockEntityTicker<T>> info) {
        BlockEntityTicker<T> ticker = !world.isClient ? checkType(type, RegisterBlockEntities.NEW_SCULK_SENSOR, (worldx, pos, statex, blockEntity) -> {
            blockEntity.tickServer(worldx, pos, statex);
        }) : checkType(type, RegisterBlockEntities.NEW_SCULK_SENSOR, (worldx, pos, statex, blockEntity) -> {
            blockEntity.tickClient();
        });
        if (ticker == null) {
            ticker = !world.isClient ? checkType(type, BlockEntityType.SCULK_SENSOR, (worldx, pos, statex, blockEntity) -> {
                blockEntity.getEventListener().tick(worldx);
            }) : null;
        }
        info.setReturnValue(ticker);
        info.cancel();
    }

    @Inject(at = @At("HEAD"), method = "getRenderType", cancellable = true)
    public void getRenderType(BlockState state, CallbackInfoReturnable<BlockRenderType> info) {
        info.setReturnValue(WilderWildClient.RENDER_TENDRILS ? BlockRenderType.INVISIBLE : BlockRenderType.MODEL);
    }


    @Nullable
    private static <E extends BlockEntity, A extends BlockEntity> BlockEntityTicker<A> checkType(BlockEntityType<A> givenType, BlockEntityType<E> expectedType, BlockEntityTicker<? super E> ticker) {
        return expectedType == givenType ? (BlockEntityTicker<A>) ticker : null;
    }

}
