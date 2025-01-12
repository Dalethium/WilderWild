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

package net.frozenblock.wilderwild.mixin.entity.allay;

import net.frozenblock.wilderwild.entity.render.animation.WilderAllay;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.animal.allay.Allay;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Allay.class)
public class AllayMixin implements WilderAllay {

	@Unique
	private final AnimationState wilderWild$dancingAnimationState = new AnimationState();
	@Shadow
	private float dancingAnimationTicks;

	@Unique
	@Override
	public AnimationState wilderWild$getDancingAnimationState() {
		return this.wilderWild$dancingAnimationState;
	}

	@Inject(
		method = "tick",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/entity/animal/allay/Allay;isSpinning()Z",
			shift = At.Shift.BEFORE
		)
	)
	private void wilderWild$animateDance(CallbackInfo info) {
		this.wilderWild$getDancingAnimationState().startIfStopped((int) this.dancingAnimationTicks);
		this.wilderWild$getDancingAnimationState().stop();
	}

	@Inject(
		method = "tick",
		at = @At(
			value = "CONSTANT",
			args = "floatValue=0",
			ordinal = 0
		),
		slice = @Slice(
			from = @At(
				value = "FIELD",
				target = "Lnet/minecraft/world/entity/animal/allay/Allay;dancingAnimationTicks:F",
				ordinal = 2
			)
		)
	)
	private void wilderWild$stopDance(CallbackInfo info) {
		this.wilderWild$getDancingAnimationState().stop();
	}

}
