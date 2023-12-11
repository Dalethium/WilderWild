/*
 * Copyright 2023 FrozenBlock
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

package net.frozenblock.wilderwild.mixin.worldgen.tree;

import com.google.common.collect.Iterables;
import java.util.Set;
import net.frozenblock.wilderwild.misc.interfaces.TreeFeatureLeavesUpdate;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.TreeFeature;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.phys.shapes.DiscreteVoxelShape;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(value = TreeFeature.class, priority = 69420)
public abstract class TreeFeatureMixin implements TreeFeatureLeavesUpdate {

	@Shadow
	private static DiscreteVoxelShape updateLeaves(LevelAccessor level, BoundingBox box, Set<BlockPos> rootPositions, Set<BlockPos> trunkPositions, Set<BlockPos> foliagePositions) {
		return null;
	}

	@Inject(
		method = "place",
		at = @At(
			value = "INVOKE", target = "Lnet/minecraft/world/level/levelgen/structure/BoundingBox;encapsulatingPositions(Ljava/lang/Iterable;)Ljava/util/Optional;",
			shift = At.Shift.BEFORE
		),
		locals = LocalCapture.CAPTURE_FAILHARD,
		cancellable = true
	)
	private void wilderWild$place(FeaturePlaceContext<TreeConfiguration> context, CallbackInfoReturnable<Boolean> info, WorldGenLevel worldGenLevel, RandomSource randomSource, BlockPos blockPos, TreeConfiguration treeConfiguration, Set<BlockPos> set, Set<BlockPos> set2, Set<BlockPos> set3, Set<BlockPos> set4) {
		info.setReturnValue(this.wilderWild$encapsulatePositionsAndUpdateLeaves(worldGenLevel, set, set2, set3, set4));
	}

	@Unique
	public boolean wilderWild$encapsulatePositionsAndUpdateLeaves(LevelAccessor worldGenLevel, Set<BlockPos> set, Set<BlockPos> set2, Set<BlockPos> set3, Set<BlockPos> set4) {
		return BoundingBox.encapsulatingPositions(Iterables.concat(set, set2, set3, set4)).map((boundingBox) -> {
			DiscreteVoxelShape discreteVoxelShape = this.wilderWild$updateLeaves(worldGenLevel, boundingBox, set2, set4, set);
			StructureTemplate.updateShapeAtEdge(worldGenLevel, 3, discreteVoxelShape, boundingBox.minX(), boundingBox.minY(), boundingBox.minZ());
			return true;
		}).orElse(false);
	}

	@Unique
	@Override
	public DiscreteVoxelShape wilderWild$updateLeaves(LevelAccessor level, @NotNull BoundingBox box, Set<BlockPos> rootPositions, Set<BlockPos> trunkPositions, Set<BlockPos> foliagePositions) {
		return updateLeaves(level, box, rootPositions, trunkPositions, foliagePositions);
	}
}
