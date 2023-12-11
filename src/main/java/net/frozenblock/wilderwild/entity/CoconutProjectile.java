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

package net.frozenblock.wilderwild.entity;

import net.frozenblock.wilderwild.registry.RegisterEntities;
import net.frozenblock.wilderwild.registry.RegisterItems;
import net.frozenblock.wilderwild.registry.RegisterParticles;
import net.frozenblock.wilderwild.registry.RegisterSounds;
import net.frozenblock.wilderwild.tag.WilderBlockTags;
import net.frozenblock.wilderwild.tag.WilderEntityTags;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;

public class CoconutProjectile extends ThrowableItemProjectile {

	public CoconutProjectile(@NotNull EntityType<? extends CoconutProjectile> entityType, @NotNull Level level) {
		super(entityType, level);
	}

	public CoconutProjectile(@NotNull Level level, @NotNull LivingEntity shooter) {
		super(RegisterEntities.COCONUT, shooter, level);
	}

	public CoconutProjectile(@NotNull Level level, double x, double y, double z) {
		super(RegisterEntities.COCONUT, x, y, z, level);
	}

	@Override
	@NotNull
	protected Item getDefaultItem() {
		return RegisterItems.COCONUT;
	}

	@Override
	public void handleEntityEvent(byte id) {
		if (id == 3) {
			for (int i = 0; i < 8; ++i) {
				this.level().addParticle(new ItemParticleOption(ParticleTypes.ITEM, new ItemStack(this.getDefaultItem())), this.getX(), this.getY(), this.getZ(), 0.0, 0.0, 0.0);
			}
		}
	}

	@Override
	protected void onHit(@NotNull HitResult result) {
		super.onHit(result);
		this.level().playSound(null, this.getX(), this.getY(), this.getZ(), RegisterSounds.ITEM_COCONUT_LAND, SoundSource.BLOCKS, 0.4F, 0.9F + (this.level().random.nextFloat() * 0.2F));
	}

	@Override
	protected void onHitEntity(@NotNull EntityHitResult result) {
		super.onHitEntity(result);
		Entity entity = result.getEntity();
		entity.hurt(entity.damageSources().thrown(this, this.getOwner()), 2F);
		if (this.position().y() > entity.getEyeY() && !entity.getType().is(WilderEntityTags.COCONUT_CANT_BONK)) {
			this.level().playSound(null, this.getX(), this.getY(), this.getZ(), RegisterSounds.ITEM_COCONUT_HIT_HEAD, SoundSource.BLOCKS, 1.0F, 0.9F + (this.level().random.nextFloat() * 0.2F));
		}
		if (!entity.getType().is(WilderEntityTags.COCONUT_CANT_SPLIT) && entity.getBoundingBox().getSize() > this.getBoundingBox().getSize() && this.random.nextDouble() < 0.7) {
			this.split();
		}
	}

	@Override
	protected void onHitBlock(@NotNull BlockHitResult result) {
		super.onHitBlock(result);
		if (this.level().getBlockState(result.getBlockPos()).is(WilderBlockTags.SPLITS_COCONUT)) {
			this.split();
			return;
		}
		this.level().addFreshEntity(new ItemEntity(this.level(), this.getX(), this.getY(), this.getZ(), this.getItem()));
		this.discard();
	}

	public void split() {
		if (this.level() instanceof ServerLevel server) {
			server.playSound(null, this.getX(), this.getY(), this.getZ(), RegisterSounds.ITEM_COCONUT_LAND_BREAK, SoundSource.BLOCKS, 1.0F, 0.9F + 0.2F * this.level().random.nextFloat());
			for (int i = 0; i < 2; ++i) {
				server.addFreshEntity(new ItemEntity(this.level(), this.getX(), this.getY(), this.getZ(), new ItemStack(RegisterItems.SPLIT_COCONUT)));
			}
			EntityDimensions dimensions = this.getDimensions(Pose.STANDING);
			server.sendParticles(RegisterParticles.COCONUT_SPLASH, this.position().x + (dimensions.width * 0.5), this.position().y + (dimensions.height * 0.5), this.position().z + (dimensions.width * 0.5), level().random.nextInt(1, 5), dimensions.width / 4F, dimensions.height / 4F, dimensions.width / 4F, 0.1D);
			this.discard();
		}
	}

}
