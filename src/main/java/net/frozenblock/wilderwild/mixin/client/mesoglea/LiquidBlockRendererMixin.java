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

package net.frozenblock.wilderwild.mixin.client.mesoglea;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.frozenblock.lib.liquid.render.api.LiquidRenderUtils;
import net.frozenblock.wilderwild.block.MesogleaBlock;
import net.frozenblock.wilderwild.config.BlockConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.LiquidBlockRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Environment(EnvType.CLIENT)
@Mixin(value = LiquidBlockRenderer.class, priority = 69420)
public class LiquidBlockRendererMixin {

	@Shadow
	private TextureAtlasSprite waterOverlay;

	@Unique
	private float wilderWild$u0;
	@Unique
	private float wilderWild$u1;
	@Unique
	private float wilderWild$v0;
	@Unique
	private float wilderWild$v1;
	@Unique
	private boolean wilderWild$isWater;

	@Inject(method = "shouldRenderFace", at = @At(value = "HEAD"), cancellable = true, require = 0)
	private static void wilderWild$shouldRenderFace(BlockAndTintGetter blockAndTintGetter, BlockPos blockPos, FluidState fluidState, BlockState blockState, Direction side, FluidState fluidState2, CallbackInfoReturnable<Boolean> info) {
		if (blockState.getBlock() instanceof MesogleaBlock && side != Direction.UP) {
			info.setReturnValue(false);
		}
	}

	@Inject(method = "tesselate", at = @At("HEAD"), cancellable = true, require = 0)
	private void wilderWild$getIsWater(BlockAndTintGetter level, BlockPos pos, VertexConsumer vertexConsumer, BlockState blockState, FluidState fluidState, CallbackInfo info) {
		if (BlockConfig.get().mesoglea.mesogleaLiquid && blockState.getBlock() instanceof MesogleaBlock) {
			LiquidRenderUtils.tesselateWithSingleTexture(level, pos, vertexConsumer, blockState, fluidState, Minecraft.getInstance().getModelManager().getBlockModelShaper().getBlockModel(blockState).getParticleIcon());
			info.cancel();
			return;
		}
		this.wilderWild$isWater = fluidState.is(FluidTags.WATER);
	}

	@ModifyArgs(
		method = "tesselate",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/renderer/block/LiquidBlockRenderer;vertex(Lcom/mojang/blaze3d/vertex/VertexConsumer;DDDFFFFFI)V"
			, ordinal = 8
		),
		require = 0)
	private void sideTextureBottom1(Args args) {
		if (this.wilderWild$isWater) {
			this.wilderWild$u0 = this.waterOverlay.getU0();
			this.wilderWild$u1 = this.waterOverlay.getU1();
			this.wilderWild$v0 = this.waterOverlay.getV0();
			this.wilderWild$v1 = this.waterOverlay.getV1();
			args.set(7, this.wilderWild$u0);
			args.set(8, this.wilderWild$v1);
		}
	}

	@ModifyArgs(method = "tesselate", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/block/LiquidBlockRenderer;vertex(Lcom/mojang/blaze3d/vertex/VertexConsumer;DDDFFFFFI)V", ordinal = 9), require = 0)
	private void sideTextureBottom2(Args args) {
		if (this.wilderWild$isWater) {
			args.set(7, this.wilderWild$u0);
			args.set(8, this.wilderWild$v0);
		}
	}

	@ModifyArgs(method = "tesselate", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/block/LiquidBlockRenderer;vertex(Lcom/mojang/blaze3d/vertex/VertexConsumer;DDDFFFFFI)V", ordinal = 10), require = 0)
	private void wilderWild$sideTextureBottom3(Args args) {
		if (this.wilderWild$isWater) {
			args.set(7, this.wilderWild$u1);
			args.set(8, this.wilderWild$v0);
		}
	}

	@ModifyArgs(method = "tesselate", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/block/LiquidBlockRenderer;vertex(Lcom/mojang/blaze3d/vertex/VertexConsumer;DDDFFFFFI)V", ordinal = 11), require = 0)
	private void wilderWild$sideTextureBottom4(Args args) {
		if (this.wilderWild$isWater) {
			args.set(7, this.wilderWild$u1);
			args.set(8, this.wilderWild$v1);
		}
	}
}
