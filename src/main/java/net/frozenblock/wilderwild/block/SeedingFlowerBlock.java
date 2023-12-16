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

package net.frozenblock.wilderwild.block;

import net.frozenblock.wilderwild.networking.packet.WilderSeedParticlePacket;
import net.frozenblock.wilderwild.particle.options.SeedParticleOptions;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class SeedingFlowerBlock extends FlowerBlock {

	public SeedingFlowerBlock(@NotNull MobEffect suspiciousStewEffect, int effectDuration, @NotNull Properties settings) {
		super(suspiciousStewEffect, effectDuration, settings);
	}

	@Override
	public void animateTick(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull RandomSource random) {
		if (random.nextFloat() > 0.95) {
			level.addParticle(new SeedParticleOptions(false, false), pos.getX() + 0.5, pos.getY() + 0.8, pos.getZ() + 0.5, 0.0D, 0.0D, 0.0D);
		}
	}

	@Override
	public void entityInside(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Entity entity) {
		if (level instanceof ServerLevel server) {
			if (server.random.nextFloat() > 0.95) {
				WilderSeedParticlePacket.sendToAll(level, Vec3.atCenterOf(pos).add(0, 0.3, 0), server.random.nextIntBetweenInclusive(1, 3), false);
			}
		}
	}

	@Override
	public void playerWillDestroy(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull Player player) {
		super.playerWillDestroy(level, pos, state, player);
		if (level instanceof ServerLevel server) {
			WilderSeedParticlePacket.sendToAll(level, Vec3.atCenterOf(pos).add(0, 0.3, 0), server.random.nextIntBetweenInclusive(3, 7), false);
		}
	}
}
