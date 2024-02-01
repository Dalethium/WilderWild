/*
 * Copyright 2023-2024 FrozenBlock
 * This file is part of Wilder Wild.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, see <https://www.gnu.org/licenses/>.
 */

package net.frozenblock.wilderwild.mixin.block.dispenser;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.frozenblock.wilderwild.block.GloryOfTheSnowBlock;
import net.frozenblock.wilderwild.block.MilkweedBlock;
import net.frozenblock.wilderwild.registry.RegisterBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.dispenser.ShearsDispenseItemBehavior;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ShearsDispenseItemBehavior.class)
public class ShearsDispenseItemBehaviorMixin {

	@WrapOperation(method = "execute", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/dispenser/ShearsDispenseItemBehavior;tryShearBeehive(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/core/BlockPos;)Z"))
	private static boolean wilderWild$execute(ServerLevel usedLevel, BlockPos pos, @NotNull Operation<Boolean> operation) {
		return operation.call(usedLevel, pos) || wilderWild$tryShearMilkweed(usedLevel, pos) || wilderWild$tryShearGloryOfTheSnow(usedLevel, pos);
	}

	@Unique
	private static boolean wilderWild$tryShearMilkweed(@NotNull ServerLevel level, BlockPos pos) {
		BlockState blockState = level.getBlockState(pos);
		if (blockState.getBlock() == RegisterBlocks.MILKWEED && MilkweedBlock.isFullyGrown(blockState)) {
			MilkweedBlock.shear(level, pos, blockState, null);
			return true;
		}
		return false;
	}

	@Unique
	private static boolean wilderWild$tryShearGloryOfTheSnow(@NotNull ServerLevel level, BlockPos pos) {
		BlockState blockState = level.getBlockState(pos);
		if (blockState.getBlock() == RegisterBlocks.GLORY_OF_THE_SNOW && GloryOfTheSnowBlock.hasColor(blockState)) {
			GloryOfTheSnowBlock.shear(level, pos, blockState, null);
			return true;
		}
		return false;
	}

}
