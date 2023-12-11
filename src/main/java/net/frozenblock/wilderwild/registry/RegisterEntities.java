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

package net.frozenblock.wilderwild.registry;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.frozenblock.lib.mobcategory.api.FrozenMobCategories;
import net.frozenblock.wilderwild.entity.AncientHornProjectile;
import net.frozenblock.wilderwild.entity.ChestBubbleTicker;
import net.frozenblock.wilderwild.entity.CoconutProjectile;
import net.frozenblock.wilderwild.entity.Crab;
import net.frozenblock.wilderwild.entity.Firefly;
import net.frozenblock.wilderwild.entity.Jellyfish;
import net.frozenblock.wilderwild.entity.SculkSpreadTicker;
import net.frozenblock.wilderwild.entity.Tumbleweed;
import net.frozenblock.wilderwild.misc.WilderSharedConstants;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.levelgen.Heightmap;
import org.jetbrains.annotations.NotNull;

public final class RegisterEntities {
	public static final EntityType<AncientHornProjectile> ANCIENT_HORN_PROJECTILE_ENTITY = register(
		"ancient_horn_projectile",
		FabricEntityTypeBuilder.<AncientHornProjectile>create(MobCategory.MISC, AncientHornProjectile::new)
			.fireImmune()
			.dimensions(EntityDimensions.scalable(0.6F, 0.6F))
			.trackRangeBlocks(64)
			.trackedUpdateRate(2)
			.build()
	);
	public static final EntityType<Firefly> FIREFLY = register(
		"firefly",
		FabricEntityTypeBuilder.createMob()
			.spawnGroup(FrozenMobCategories.getCategory(WilderSharedConstants.MOD_ID, "fireflies"))
			.entityFactory(Firefly::new)
			.defaultAttributes(Firefly::addAttributes)
			.spawnRestriction(SpawnPlacements.Type.NO_RESTRICTIONS, Heightmap.Types.MOTION_BLOCKING, Firefly::canSpawn)
			.dimensions(EntityDimensions.scalable(0.3F, 0.3F))
			.build()
	);
	public static final EntityType<Jellyfish> JELLYFISH = register(
		"jellyfish",
		FabricEntityTypeBuilder.createMob()
			.spawnGroup(FrozenMobCategories.getCategory(WilderSharedConstants.MOD_ID, "jellyfish"))
			.entityFactory(Jellyfish::new)
			.defaultAttributes(Jellyfish::addAttributes)
			.spawnRestriction(SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Jellyfish::canSpawn)
			.dimensions(EntityDimensions.scalable(0.4F, 0.4F))
			.build()
	);
	public static final EntityType<Tumbleweed> TUMBLEWEED = register(
		"tumbleweed",
		FabricEntityTypeBuilder.createMob()
			.spawnGroup(FrozenMobCategories.getCategory(WilderSharedConstants.MOD_ID, "tumbleweed"))
			.entityFactory(Tumbleweed::new)
			.defaultAttributes(Tumbleweed::addAttributes)
			.spawnRestriction(SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Tumbleweed::canSpawn)
			.dimensions(EntityDimensions.scalable(0.98F, 0.98F))
			.build()
	);
	public static final EntityType<Crab> CRAB = register(
		"crab",
		FabricEntityTypeBuilder.createMob()
			.spawnGroup(FrozenMobCategories.getCategory(WilderSharedConstants.MOD_ID, "crab"))
			.entityFactory(Crab::new)
			.defaultAttributes(Crab::addAttributes)
			.spawnRestriction(SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Crab::canSpawn)
			.dimensions(EntityDimensions.scalable(0.5F, 0.5F))
			.build()
	);
	public static final EntityType<CoconutProjectile> COCONUT = register(
		"coconut",
		FabricEntityTypeBuilder.<CoconutProjectile>create(MobCategory.MISC, CoconutProjectile::new)
			.dimensions(EntityDimensions.scalable(0.25F, 0.25F))
			.trackRangeBlocks(64)
			.trackedUpdateRate(10)
			.build()
	);
	public static final EntityType<ChestBubbleTicker> CHEST_BUBBLER = register(
		"chest_bubbler",
		FabricEntityTypeBuilder.<ChestBubbleTicker>create(MobCategory.MISC, ChestBubbleTicker::new)
			.dimensions(EntityDimensions.scalable(1.0F, 1.0F))
			.trackRangeBlocks(0)
			.trackedUpdateRate(10)
			.build()
	);
	public static final EntityType<SculkSpreadTicker> SCULK_SPREADER = register(
		"sculk_spreader",
		FabricEntityTypeBuilder.<SculkSpreadTicker>create(MobCategory.MISC, SculkSpreadTicker::new)
			.dimensions(EntityDimensions.scalable(1.0F, 1.0F))
			.trackRangeBlocks(0)
			.trackedUpdateRate(10)
			.build()
	);

	private RegisterEntities() {
		throw new UnsupportedOperationException("RegisterBlockEntities contains only static declarations.");
	}

	public static void init() {
		WilderSharedConstants.logWild("Registering Entities for", WilderSharedConstants.UNSTABLE_LOGGING);
		RegisterDamageTypes.init();
	}

	@NotNull
	private static <E extends Entity, T extends EntityType<E>> T register(@NotNull String path, @NotNull T entityType) {
		return Registry.register(BuiltInRegistries.ENTITY_TYPE, WilderSharedConstants.id(path), entityType);
	}
}
