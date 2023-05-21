/*
 * Copyright 2022-2023 FrozenBlock
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

package net.frozenblock.wilderwild.entity.ai.warden;

import java.util.Optional;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.monster.warden.Warden;
import org.jetbrains.annotations.NotNull;

public class WardenMoveControl extends MoveControl {

	private final Warden mob;
	private final float pitchChange;
	private final float yawChange;
	private final float speedInWater;
	private final float speedInAir;
	private final boolean buoyant;

	public WardenMoveControl(@NotNull Warden mob, float pitchChange, float yawChange, float speedInWater, float speedInAir, boolean buoyant) {
		super(mob);
		this.mob = mob;
		this.pitchChange = pitchChange;
		this.yawChange = yawChange;
		this.speedInWater = speedInWater;
		this.speedInAir = speedInAir;
		this.buoyant = buoyant;
	}

	@Override
	public void tick() {
		if (this.touchingWaterOrLava(this.mob)) {
			if (this.buoyant) {
				if (this.mob.getBrain().hasMemoryValue(MemoryModuleType.ROAR_TARGET) || this.mob.getBrain().hasMemoryValue(MemoryModuleType.ATTACK_TARGET)) {
					Optional<LivingEntity> ATTACK_TARGET = this.mob.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET);
					Optional<LivingEntity> ROAR_TARGET = this.mob.getBrain().getMemory(MemoryModuleType.ROAR_TARGET);
					if (ATTACK_TARGET.isPresent()) {
						LivingEntity target = ATTACK_TARGET.get();
						if ((!this.touchingWaterOrLava(target) || !this.submergedInWaterOrLava(this.mob)) && target.getY() > this.mob.getY()) {
							this.mob.setDeltaMovement(this.mob.getDeltaMovement().add(0.0D, 0.01D, 0.0D));
						}
					} else if (ROAR_TARGET.isPresent()) {
						LivingEntity target = ROAR_TARGET.get();
						if ((!this.touchingWaterOrLava(target) || !this.submergedInWaterOrLava(this.mob)) && target.getY() > this.mob.getY()) {
							this.mob.setDeltaMovement(this.mob.getDeltaMovement().add(0.0D, 0.01D, 0.0D));
						}
					}
				} else {
					if (!this.submergedInWaterOrLava(this.mob)) {
						this.mob.setDeltaMovement(this.mob.getDeltaMovement().add(0.0D, 0.006D, 0.0D));
					} else {
						this.mob.setDeltaMovement(this.mob.getDeltaMovement().add(0.0D, 0.006D, 0.0D));
					}
				}
			}

			if (this.operation == MoveControl.Operation.MOVE_TO && !this.mob.getNavigation().isDone()) {
				double d = this.wantedX - this.mob.getX();
				double e = this.wantedY - this.mob.getY();
				double f = this.wantedZ - this.mob.getZ();
				double g = d * d + e * e + f * f;
				if (g < 2.5000003E-7F) {
					this.mob.setZza(0.0F);
				} else {
					float h = (float) (Mth.atan2(f, d) * 180.0F / (float) Math.PI) - 90.0F;
					this.mob.setYRot(this.rotlerp(this.mob.getYRot(), h, this.yawChange));
					this.mob.yBodyRot = this.mob.getYRot();
					this.mob.yHeadRot = this.mob.getYRot();
					float i = (float) (this.speedModifier * this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED));
					if (this.touchingWaterOrLava(mob)) {
						this.mob.setSpeed(i * this.speedInWater);
						double j = Math.sqrt(d * d + f * f);
						if (Math.abs(e) > 1.0E-5F || Math.abs(j) > 1.0E-5F) {
							float k = -((float) (Mth.atan2(e, j) * 180.0F / (float) Math.PI));
							k = Mth.clamp(Mth.wrapDegrees(k), -this.pitchChange, this.pitchChange);
							this.mob.setXRot(this.rotlerp(this.mob.getXRot(), k, 5.0F));
						}

						float k = Mth.cos(this.mob.getXRot() * (float) (Math.PI / 180.0));
						float l = Mth.sin(this.mob.getXRot() * (float) (Math.PI / 180.0));
						this.mob.zza = k * i;
						this.mob.yya = -l * i;
					} else {
						this.mob.setSpeed(i * this.speedInAir);
					}

				}
			} else {
				this.mob.setSpeed(0.0F);
				this.mob.setXxa(0.0F);
				this.mob.setYya(0.0F);
				this.mob.setZza(0.0F);
			}
		} else {
			super.tick();
		}
	}

	private boolean touchingWaterOrLava(@NotNull Entity entity) {
		return entity.isInWaterOrBubble() || entity.isInLava() || entity.isVisuallySwimming();
	}

	private boolean submergedInWaterOrLava(@NotNull Entity entity) {
		return entity.isEyeInFluid(FluidTags.WATER) || entity.isEyeInFluid(FluidTags.LAVA) || entity.isVisuallySwimming();
	}
}
