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

package net.frozenblock.wilderwild.misc.wind;

import net.frozenblock.lib.wind.api.WindManager;
import net.frozenblock.lib.wind.api.WindManagerExtension;
import net.frozenblock.wilderwild.misc.WilderSharedConstants;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import org.jetbrains.annotations.NotNull;

public class WilderWindManager implements WindManagerExtension {

	private final WindManager manager;
	public double cloudX;
	public double cloudY;
	public double cloudZ;


	public WilderWindManager(WindManager manager) {
		this.manager = manager;
	}

	@Override
	public void tick() {
		this.cloudX += (manager.laggedWindX * 0.007);
		this.cloudY += (manager.laggedWindY * 0.01);
		this.cloudZ += (manager.laggedWindZ * 0.007);
	}

	@Override
	public void baseTick() {
	}

	@Override
	public boolean runResetsIfNeeded() {
		boolean needsReset = false;

		if (this.cloudX == Double.MAX_VALUE || this.cloudX == Double.MIN_VALUE) {
			needsReset = true;
			this.cloudX = 0;
		}
		if (this.cloudY == Double.MAX_VALUE || this.cloudY == Double.MIN_VALUE) {
			needsReset = true;
			this.cloudY = 0;
		}
		if (this.cloudZ == Double.MAX_VALUE || this.cloudZ == Double.MIN_VALUE) {
			needsReset = true;
			this.cloudZ = 0;
		}

		return needsReset;
	}

	@Override
	public void createSyncByteBuf(@NotNull FriendlyByteBuf original) {
		original.writeDouble(this.cloudX);
		original.writeDouble(this.cloudY);
		original.writeDouble(this.cloudZ);
	}

	@Override
	public void load(@NotNull CompoundTag compoundTag) {
		this.cloudX = compoundTag.getDouble(WilderSharedConstants.safeString("cloudX"));
		this.cloudY = compoundTag.getDouble(WilderSharedConstants.safeString("cloudY"));
		this.cloudZ = compoundTag.getDouble(WilderSharedConstants.safeString("cloudZ"));
	}

	@Override
	public void save(@NotNull CompoundTag compoundTag) {
		compoundTag.putDouble(WilderSharedConstants.safeString("cloudX"), this.cloudX);
		compoundTag.putDouble(WilderSharedConstants.safeString("cloudY"), this.cloudY);
		compoundTag.putDouble(WilderSharedConstants.safeString("cloudZ"), this.cloudZ);
	}
}
