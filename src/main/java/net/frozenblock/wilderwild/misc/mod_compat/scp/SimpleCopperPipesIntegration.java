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

package net.frozenblock.wilderwild.misc.mod_compat.scp;

import net.frozenblock.lib.FrozenBools;
import net.frozenblock.lib.FrozenSharedConstants;
import net.frozenblock.lib.math.api.AdvancedMath;
import net.frozenblock.lib.sound.api.FrozenSoundPackets;
import net.frozenblock.wilderwild.entity.AncientHornProjectile;
import net.frozenblock.wilderwild.entity.CoconutProjectile;
import net.frozenblock.wilderwild.entity.Tumbleweed;
import net.frozenblock.wilderwild.misc.WilderSharedConstants;
import net.frozenblock.wilderwild.networking.packet.WilderControlledSeedParticlePacket;
import net.frozenblock.wilderwild.networking.packet.WilderFloatingSculkBubbleParticlePacket;
import net.frozenblock.wilderwild.registry.RegisterBlocks;
import net.frozenblock.wilderwild.registry.RegisterEntities;
import net.frozenblock.wilderwild.registry.RegisterItems;
import net.frozenblock.wilderwild.registry.RegisterSounds;
import net.lunade.copper.CopperPipeMain;
import net.lunade.copper.FittingPipeDispenses;
import net.lunade.copper.PipeMovementRestrictions;
import net.lunade.copper.PoweredPipeDispenses;
import net.lunade.copper.RegisterPipeNbtMethods;
import net.lunade.copper.block_entity.CopperPipeEntity;
import net.lunade.copper.blocks.CopperPipe;
import net.lunade.copper.pipe_nbt.MoveablePipeDataHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustColorTransitionOptions;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;

public class SimpleCopperPipesIntegration extends AbstractSimpleCopperPipesIntegration {
	public static final ResourceLocation HORN = WilderSharedConstants.id("ancient_horn");

	public SimpleCopperPipesIntegration() {
		super();
	}

	@Override
	public void initPreFreeze() {
		WilderSharedConstants.log("WILDERWILD AND COPPER PIPES SECRET LOG MESSAGE UNLOCKED!!!", true);
		RegisterPipeNbtMethods.register(HORN, (nbt, level, pos, blockState, copperPipeEntity) -> {
			if (!nbt.getCanOnlyBeUsedOnce() || nbt.getUseCount() < 1) {
				BlockState state = level.getBlockState(pos);
				if (state.getBlock() instanceof CopperPipe pipe) {
					Direction direction = state.getValue(BlockStateProperties.FACING);
					if (nbt.getEntity(level) != null) {
						nbt.withUseCount(nbt.getUseCount() + 1);
						AncientHornProjectile projectileEntity = new AncientHornProjectile(level, pos.getX() + pipe.getDripX(direction), pos.getY() + pipe.getDripY(direction), pos.getZ() + pipe.getDripZ(direction));
						projectileEntity.shoot(direction.getStepX(), direction.getStepY(), direction.getStepZ(), 1.0F, 0.0F);
						projectileEntity.setOwner(nbt.foundEntity);
						projectileEntity.setShotByPlayer(true);
						projectileEntity.canInteractWithPipe = false;
						level.addFreshEntity(projectileEntity);
						FrozenSoundPackets.createMovingRestrictionLoopingSound(level, projectileEntity, RegisterSounds.ENTITY_ANCIENT_HORN_PROJECTILE_LOOP, SoundSource.NEUTRAL, 1.0F, 1.0F, FrozenSharedConstants.id("default"), true);
					}
				}
			}
		}, (nbt, level, pos, blockState, blockEntity) -> {

		}, (nbt, level, pos, blockState, blockEntity) -> {
			if (nbt.foundEntity != null) {
				nbt.vec3d2 = nbt.foundEntity.position();
			}
			if (blockState.getBlock() instanceof CopperPipe pipe) {
				Direction direction = blockState.getValue(BlockStateProperties.FACING);
				final var random = level.getRandom().nextIntBetweenInclusive(10, 20);
				for (int i = 0; i < random; i++) {
					level.sendParticles(new DustColorTransitionOptions(DustColorTransitionOptions.SCULK_PARTICLE_COLOR, DustColorTransitionOptions.SCULK_PARTICLE_COLOR, 1.0F), pos.getX() + pipe.getDripX(direction, level.getRandom()), pos.getY() + pipe.getDripY(direction, level.getRandom()), pos.getZ() + pipe.getDripZ(direction, level.getRandom()), 1, 0.0, 0.0, 0.0, 0.7);
				}
			}
		}, (nbt, level, pos, blockState, blockEntity) -> true);

		FittingPipeDispenses.register(RegisterItems.MILKWEED_POD, (level, stack, i, direction, position, state, corroded, pos, pipe) -> {
			double d = position.x();
			double e = position.y();
			double f = position.z();
			RandomSource random = level.random;
			double random1 = random.nextDouble() * 7.0D - 3.5D;
			double random2 = random.nextDouble() * 7.0D - 3.5D;
			Direction.Axis axis = direction.getAxis();
			if (axis == Direction.Axis.Y) {
				e -= 0.125D;
			} else {
				e -= 0.15625D;
			}

			int offX = direction.getStepX();
			int offY = direction.getStepY();
			int offZ = direction.getStepZ();
			double velX = axis == Direction.Axis.X ? (double) (i * offX * 2) : (axis == Direction.Axis.Z ? (corroded ? random2 : random2 * 0.1D) : (corroded ? random1 : random1 * 0.1D));
			double velY = axis == Direction.Axis.Y ? (double) (i * offY * 2) : (corroded ? random1 : random1 * 0.1D);
			double velZ = axis == Direction.Axis.Z ? (double) (i * offZ * 2) : (corroded ? random2 : random2 * 0.1D);
			UniformInt ran1 = UniformInt.of(-3, 3);
			UniformInt ran2 = UniformInt.of(-1, 1);
			UniformInt ran3 = UniformInt.of(-3, 3);
			for (int o = 0; o < random.nextIntBetweenInclusive(10, 30); ++o) {
				WilderControlledSeedParticlePacket.sendToAll(level, new Vec3(d + (double) ran1.sample(level.random) * 0.1D, e + (double) ran2.sample(level.random) * 0.1D, f + (double) ran3.sample(level.random) * 0.1D), velX, velY, velZ, 1, true, 0.3);
			}
		});

		FittingPipeDispenses.register(BuiltInRegistries.ITEM.get(WilderSharedConstants.id("seeding_dandelion")), (level, stack, i, direction, position, state, corroded, pos, pipe) -> {
			double d = position.x();
			double e = position.y();
			double f = position.z();
			RandomSource random = level.random;
			double random1 = random.nextDouble() * 7.0D - 3.5D;
			double random2 = random.nextDouble() * 7.0D - 3.5D;
			Direction.Axis axis = direction.getAxis();
			if (axis == Direction.Axis.Y) {
				e -= 0.125D;
			} else {
				e -= 0.15625D;
			}

			int offX = direction.getStepX();
			int offY = direction.getStepY();
			int offZ = direction.getStepZ();
			double velX = axis == Direction.Axis.X ? (double) (i * offX * 2) : (axis == Direction.Axis.Z ? (corroded ? random2 : random2 * 0.1D) : (corroded ? random1 : random1 * 0.1D));
			double velY = axis == Direction.Axis.Y ? (double) (i * offY * 2) : (corroded ? random1 : random1 * 0.1D);
			double velZ = axis == Direction.Axis.Z ? (double) (i * offZ * 2) : (corroded ? random2 : random2 * 0.1D);
			UniformInt ran1 = UniformInt.of(-3, 3);
			UniformInt ran2 = UniformInt.of(-1, 1);
			UniformInt ran3 = UniformInt.of(-3, 3);
			for (int o = 0; o < random.nextIntBetweenInclusive(1, 10); ++o) {
				WilderControlledSeedParticlePacket.sendToAll(level, new Vec3(d + (double) ran1.sample(level.random) * 0.1D, e + (double) ran2.sample(level.random) * 0.1D, f + (double) ran3.sample(level.random) * 0.1D), velX, velY, velZ, 1, false, 0.3);
			}
		});

		FittingPipeDispenses.register(RegisterItems.ANCIENT_HORN, (level, stack, i, direction, position, state, corroded, pos, pipe) -> {
			if (!level.isClientSide) {
				double d = position.x();
				double e = position.y();
				double f = position.z();
				RandomSource random = level.random;
				double random1 = random.nextDouble() * 7.0D - 3.5D;
				Direction.Axis axis = direction.getAxis();
				if (axis == Direction.Axis.Y) {
					e -= 0.125D;
				} else {
					e -= 0.15625D;
				}

				int offY = direction.getStepY();
				double velY = axis == Direction.Axis.Y ? (double) (i * offY * 2) : (corroded ? random1 : random1 * 0.1D);
				UniformInt ran1 = UniformInt.of(-3, 3);
				UniformInt ran2 = UniformInt.of(-1, 1);
				UniformInt ran3 = UniformInt.of(-3, 3);
				for (int o = 0; o < random.nextIntBetweenInclusive(1, 4); ++o) {
					WilderFloatingSculkBubbleParticlePacket.sendToAll(level, new Vec3(d + (double) ran1.sample(level.random) * 0.1D, e + (double) ran2.sample(level.random) * 0.1D, f + (double) ran3.sample(level.random) * 0.1D), AdvancedMath.random().nextDouble() > 0.7 ? 1 : 0, random.nextIntBetweenInclusive(60, 80), velY * 0.05, 1);
				}
			}
		});

		PoweredPipeDispenses.register(RegisterItems.COCONUT, (level, stack, i, direction, position, state, corroded, pos, pipe) -> {
			double d = position.x();
			double e = position.y();
			double f = position.z();
			RandomSource random = level.random;
			double random1 = random.nextDouble() * 10.0D - 5D;
			double random2 = random.nextDouble() * 10.0D - 5D;
			Direction.Axis axis = direction.getAxis();
			if (axis == Direction.Axis.Y) {
				e -= 0.125D;
			} else {
				e -= 0.15625D;
			}

			int offX = direction.getStepX();
			int offY = direction.getStepY();
			int offZ = direction.getStepZ();
			double velX = axis == Direction.Axis.X ? (double) (i * offX * 2) : (axis == Direction.Axis.Z ? (corroded ? random2 : random2 * 0.1D) : (corroded ? random1 : random1 * 0.1D));
			double velY = axis == Direction.Axis.Y ? (double) (i * offY * 2) : (corroded ? random1 : random1 * 0.1D);
			double velZ = axis == Direction.Axis.Z ? (double) (i * offZ * 2) : (corroded ? random2 : random2 * 0.1D);
			CoconutProjectile coconut = new CoconutProjectile(level, d, e, f);
			coconut.shoot(velX, velY, velZ, 0.8F, 0.8F);
			level.addFreshEntity(coconut);
		});

		PoweredPipeDispenses.register(Item.byBlock(RegisterBlocks.TUMBLEWEED), (level, stack, i, direction, position, state, corroded, pos, pipe) -> {
			double d = position.x();
			double e = position.y();
			double f = position.z();
			RandomSource random = level.random;
			double random1 = random.nextDouble() * 10.0D - 5D;
			double random2 = random.nextDouble() * 10.0D - 5D;
			Direction.Axis axis = direction.getAxis();
			if (axis == Direction.Axis.Y) {
				e -= 0.125D;
			} else {
				e -= 0.15625D;
			}

			int offX = direction.getStepX();
			int offY = direction.getStepY();
			int offZ = direction.getStepZ();
			double velX = axis == Direction.Axis.X ? (double) (i * offX * 2) : (axis == Direction.Axis.Z ? (corroded ? random2 : random2 * 0.1D) : (corroded ? random1 : random1 * 0.1D));
			double velY = axis == Direction.Axis.Y ? (double) (i * offY * 2) : (corroded ? random1 : random1 * 0.1D);
			double velZ = axis == Direction.Axis.Z ? (double) (i * offZ * 2) : (corroded ? random2 : random2 * 0.1D);
			Tumbleweed tumbleweed = new Tumbleweed(RegisterEntities.TUMBLEWEED, level);
			tumbleweed.setDeltaMovement(velX * 0.2, velY * 0.2, velZ * 0.2);
			tumbleweed.setPos(d, e, f);
			level.addFreshEntity(tumbleweed);
		});

		PipeMovementRestrictions.register(WilderSharedConstants.id("stone_chest"),
			((serverLevel, blockPos, blockState, copperPipeEntity, blockEntity) -> false),
			((serverLevel, blockPos, blockState, copperPipeEntity, blockEntity) -> false));

	}

	@Override
	public boolean addHornNbtToBlock(ServerLevel level, BlockPos pos, Entity owner) {
		BlockEntity entity = level.getBlockEntity(pos);
		if (entity != null) {
			if (entity instanceof CopperPipeEntity pipe) {
				level.playSound(null, pos, CopperPipeMain.ITEM_IN, SoundSource.BLOCKS, 0.2F, (level.random.nextFloat() * 0.25F) + 0.8F);
				pipe.moveablePipeDataHandler.addSaveableMoveablePipeNbt(new MoveablePipeDataHandler.SaveableMovablePipeNbt().withVec3d(owner.position()).withVec3d2(owner.position()).withString(owner.getStringUUID()).withOnlyThroughOnePipe(true).withOnlyUseableOnce(true).withNBTID(HORN));
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean isCopperPipe(BlockState state) {
		if (FrozenBools.HAS_SIMPLE_COPPER_PIPES) {
			return state.getBlock() instanceof CopperPipe;
		}
		return false;
	}

	@Override
	public void init() {

	}

}
