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

package net.frozenblock.wilderwild.entity.variant;

import com.mojang.serialization.Codec;
import net.minecraft.Util;
import net.frozenblock.wilderwild.WWConstants;
import net.frozenblock.wilderwild.registry.WilderWildRegistries;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import java.util.List;

public record FireflyColor(ResourceLocation key, ResourceLocation texture) {
	public static final Codec<FireflyColor> CODEC = ResourceLocation.CODEC
		.listOf()
		.comapFlatMap(
			list -> Util.fixedSize(list, 2).map(listx -> new FireflyColor(listx.get(0), listx.get(1))), color -> List.of(color.key(), color.texture())
		);

	public static final FireflyColor BLACK = register(WWConstants.id("black"), WWConstants.id("textures/entity/firefly/firefly_black.png"));
	public static final FireflyColor BLUE = register(WWConstants.id("blue"), WWConstants.id("textures/entity/firefly/firefly_blue.png"));
	public static final FireflyColor BROWN = register(WWConstants.id("brown"), WWConstants.id("textures/entity/firefly/firefly_brown.png"));
	public static final FireflyColor CYAN = register(WWConstants.id("cyan"), WWConstants.id("textures/entity/firefly/firefly_cyan.png"));
	public static final FireflyColor GRAY = register(WWConstants.id("gray"), WWConstants.id("textures/entity/firefly/firefly_gray.png"));
	public static final FireflyColor GREEN = register(WWConstants.id("green"), WWConstants.id("textures/entity/firefly/firefly_green.png"));
	public static final FireflyColor LIGHT_BLUE = register(WWConstants.id("light_blue"), WWConstants.id("textures/entity/firefly/firefly_light_blue.png"));
	public static final FireflyColor LIGHT_GRAY = register(WWConstants.id("light_gray"), WWConstants.id("textures/entity/firefly/firefly_light_gray.png"));
	public static final FireflyColor LIME = register(WWConstants.id("lime"), WWConstants.id("textures/entity/firefly/firefly_lime.png"));
	public static final FireflyColor MAGENTA = register(WWConstants.id("magenta"), WWConstants.id("textures/entity/firefly/firefly_magenta.png"));
	public static final FireflyColor ON = register(WWConstants.id("on"), WWConstants.id("textures/entity/firefly/firefly_on.png"));
	public static final FireflyColor ORANGE = register(WWConstants.id("orange"), WWConstants.id("textures/entity/firefly/firefly_orange.png"));
	public static final FireflyColor PINK = register(WWConstants.id("pink"), WWConstants.id("textures/entity/firefly/firefly_pink.png"));
	public static final FireflyColor PURPLE = register(WWConstants.id("purple"), WWConstants.id("textures/entity/firefly/firefly_purple.png"));
	public static final FireflyColor RED = register(WWConstants.id("red"), WWConstants.id("textures/entity/firefly/firefly_red.png"));
	public static final FireflyColor WHITE = register(WWConstants.id("white"), WWConstants.id("textures/entity/firefly/firefly_white.png"));
	public static final FireflyColor YELLOW = register(WWConstants.id("yellow"), WWConstants.id("textures/entity/firefly/firefly_yellow.png"));

	public FireflyColor(@NotNull ResourceLocation key, @NotNull ResourceLocation texture) {
		this.key = key;
		this.texture = texture;
	}

	@NotNull
	public static FireflyColor register(@NotNull ResourceLocation key, @NotNull ResourceLocation texture) {
		return Registry.register(WilderWildRegistries.FIREFLY_COLOR, key, new FireflyColor(key, texture));
	}

	public static void init() {
	}

	@NotNull
	public ResourceLocation key() {
		return this.key;
	}

	@NotNull
	public ResourceLocation texture() {
		return this.texture;
	}
}
