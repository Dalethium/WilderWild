/*
 * Copyright 2024 FrozenBlock
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

package net.frozenblock.wilderwild.datafix.wilderwild.schemas;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.templates.TypeTemplate;
import java.util.Map;
import java.util.function.Supplier;
import net.frozenblock.wilderwild.misc.WilderSharedConstants;
import net.minecraft.util.datafix.schemas.NamespacedSchema;
import net.minecraft.util.datafix.schemas.V100;

public class WWV17 extends NamespacedSchema {
	public WWV17(int versionKey, Schema parent) {
		super(versionKey, parent);
	}

	@Override
	public Map<String, Supplier<TypeTemplate>> registerBlockEntities(Schema schema) {
		Map<String, Supplier<TypeTemplate>> blockEntityMap = super.registerBlockEntities(schema);

		schema.register(
			blockEntityMap,
			WilderSharedConstants.string("display_lantern"),
			DSL::remainder
		);
		schema.register(
			blockEntityMap,
			WilderSharedConstants.string("hanging_tendril"),
			DSL::remainder
		);
		schema.register(
			blockEntityMap,
			WilderSharedConstants.string("scorched_block"),
			DSL::remainder
		);
		schema.register(
			blockEntityMap,
			WilderSharedConstants.string("stone_chest"),
			DSL::remainder
		);
		schema.register(
			blockEntityMap,
			WilderSharedConstants.string("termite_mound"),
			DSL::remainder
		);

		return blockEntityMap;
	}

	@Override
	public Map<String, Supplier<TypeTemplate>> registerEntities(Schema schema) {
		Map<String, Supplier<TypeTemplate>> entityMap = super.registerEntities(schema);
		schema.register(entityMap, WilderSharedConstants.string("jellyfish"), () -> V100.equipment(schema));
		schema.register(entityMap, WilderSharedConstants.string("ostrich"), () -> V100.equipment(schema));
		schema.register(entityMap, WilderSharedConstants.string("crab"), () -> V100.equipment(schema));
		schema.register(entityMap, WilderSharedConstants.string("firefly"), () -> V100.equipment(schema));
		schema.register(entityMap, WilderSharedConstants.string("ancient_horn_vibration"), DSL::remainder);
		schema.register(entityMap, WilderSharedConstants.string("coconut"), DSL::remainder);
		schema.register(entityMap, WilderSharedConstants.string("chest_bubbler"), DSL::remainder);
		schema.register(entityMap, WilderSharedConstants.string("sculk_spreader"), DSL::remainder);

		return entityMap;
	}
}
