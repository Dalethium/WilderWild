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

package net.frozenblock.wilderwild.datagen;

import java.util.concurrent.CompletableFuture;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.frozenblock.wilderwild.registry.RegisterEntities;
import net.frozenblock.wilderwild.tag.WilderEntityTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.entity.EntityType;
import org.jetbrains.annotations.NotNull;

final class WWEntityTagProvider extends FabricTagProvider.EntityTypeTagProvider {

	public WWEntityTagProvider(@NotNull FabricDataOutput output, @NotNull CompletableFuture<HolderLookup.Provider> completableFuture) {
		super(output, completableFuture);
	}

	@Override
	protected void addTags(@NotNull HolderLookup.Provider arg) {
		this.getOrCreateTagBuilder(WilderEntityTags.STAYS_IN_MESOGLEA)
			.add(RegisterEntities.JELLYFISH);

		this.getOrCreateTagBuilder(EntityTypeTags.CAN_BREATHE_UNDER_WATER)
			.add(EntityType.WARDEN);

		this.getOrCreateTagBuilder(WilderEntityTags.CRAB_HUNT_TARGETS)
			.add(EntityType.SQUID)
			.add(EntityType.GLOW_SQUID)
			.add(EntityType.COD)
			.add(EntityType.SALMON)
			.add(EntityType.TROPICAL_FISH)
			.add(EntityType.TADPOLE);
	}
}
