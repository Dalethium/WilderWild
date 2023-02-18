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

package net.frozenblock.wilderwild.world.generation.treedecorators;

import com.mojang.serialization.Codec;
import net.frozenblock.wilderwild.misc.WilderSharedConstants;
import net.minecraft.core.Registry;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;

public final class WilderTreeDecorators {
    public static final TreeDecoratorType<ShelfFungusTreeDecorator> FUNGUS_TREE_DECORATOR = register("shelf_fungus_tree_decorator", ShelfFungusTreeDecorator.CODEC);
    public static final TreeDecoratorType<HeightBasedVineTrunkDecorator> HEIGHT_BASED_VINE_TRUNK_DECORATOR = register("height_based_vine_trunk_decorator", HeightBasedVineTrunkDecorator.CODEC);
	public static final TreeDecoratorType<LeavesAroundTopLogDecorator> LEAVES_AROUND_TOP_LOG_DECORATOR_TREE_DECORATOR = register("leaves_around_top_decorator", LeavesAroundTopLogDecorator.CODEC);
	public static final TreeDecoratorType<HeightBasedCobwebTrunkDecorator> HEIGHT_BASED_COBWEB_TRUNK_DECORATOR = register("height_based_cobweb_trunk_decorator", HeightBasedCobwebTrunkDecorator.CODEC);

	public static void generateTreeDecorators() {
        //Just to ensure the class is loaded.
    }

    private static <P extends TreeDecorator> TreeDecoratorType<P> register(String id, Codec<P> codec) {
        return Registry.register(Registry.TREE_DECORATOR_TYPES, WilderSharedConstants.id(id), new TreeDecoratorType<P>(codec));
    }
}