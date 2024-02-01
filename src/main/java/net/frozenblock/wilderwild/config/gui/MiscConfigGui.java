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

package net.frozenblock.wilderwild.config.gui;

import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.frozenblock.lib.config.api.instance.Config;
import net.frozenblock.lib.config.clothconfig.FrozenClothConfig;
import net.frozenblock.wilderwild.config.MiscConfig;
import net.frozenblock.wilderwild.misc.WilderSharedConstants;
import static net.frozenblock.wilderwild.misc.WilderSharedConstants.text;
import static net.frozenblock.wilderwild.misc.WilderSharedConstants.tooltip;
import org.jetbrains.annotations.NotNull;

@Environment(EnvType.CLIENT)
public final class MiscConfigGui {
	private MiscConfigGui() {
		throw new UnsupportedOperationException("MiscConfigGui contains only static declarations.");
	}

	public static void setupEntries(@NotNull ConfigCategory category, @NotNull ConfigEntryBuilder entryBuilder) {
		var config = MiscConfig.get(true);
		var modifiedConfig = MiscConfig.getWithSync();
		Class<? extends MiscConfig> clazz = config.getClass();
		Config<? extends MiscConfig> configInstance = MiscConfig.INSTANCE;
		var defaultConfig = MiscConfig.INSTANCE.defaultInstance();
		var biomeAmbience = config.biomeAmbience;
		var biomeMusic = config.biomeMusic;
		category.setBackground(WilderSharedConstants.id("textures/config/misc.png"));

		var modifyAdvancements = category.addEntry(
			FrozenClothConfig.syncedEntry(
				entryBuilder.startBooleanToggle(text("modify_advancements"), modifiedConfig.modifyAdvancements)
					.setDefaultValue(defaultConfig.modifyAdvancements)
					.setSaveConsumer(newValue -> config.modifyAdvancements = newValue)
					.setTooltip(tooltip("modify_advancements"))
					.build(),
				clazz,
				"modifyAdvancements",
				configInstance
			)
		);

		var cloudMovement = category.addEntry(entryBuilder.startBooleanToggle(text("cloud_movement"), config.cloudMovement)
			.setDefaultValue(defaultConfig.cloudMovement)
			.setSaveConsumer(newValue -> config.cloudMovement = newValue)
			.setTooltip(tooltip("cloud_movement"))
			.build()
		);

		var particleWindMovement = category.addEntry(entryBuilder.startIntSlider(text("particle_wind_movement"), config.particleWindMovement, 0, 500)
			.setDefaultValue(defaultConfig.particleWindMovement)
			.setSaveConsumer(newValue -> config.particleWindMovement = newValue)
			.setTooltip(tooltip("particle_wind_movement"))
			.build()
		);

		var fireworkWindMovement = category.addEntry(
			FrozenClothConfig.syncedEntry(
				entryBuilder.startIntSlider(text("firework_wind_movement"), modifiedConfig.fireworkWindMovement, 0, 500)
					.setDefaultValue(defaultConfig.fireworkWindMovement)
					.setSaveConsumer(newValue -> config.fireworkWindMovement = newValue)
					.setTooltip(tooltip("firework_wind_movement"))
					.build(),
				clazz,
				"fireworkWindMovement",
				configInstance
			)
		);

		var deepDarkAmbience = entryBuilder.startBooleanToggle(text("deep_dark_ambience"), biomeAmbience.deepDarkAmbience)
			.setDefaultValue(defaultConfig.biomeAmbience.deepDarkAmbience)
			.setSaveConsumer(newValue -> biomeAmbience.deepDarkAmbience = newValue)
			.setTooltip(tooltip("deep_dark_ambience"))
			.requireRestart()
			.build();

		var dripstoneCavesAmbience = entryBuilder.startBooleanToggle(text("dripstone_caves_ambience"), biomeAmbience.dripstoneCavesAmbience)
			.setDefaultValue(defaultConfig.biomeAmbience.dripstoneCavesAmbience)
			.setSaveConsumer(newValue -> biomeAmbience.dripstoneCavesAmbience = newValue)
			.setTooltip(tooltip("dripstone_caves_ambience"))
			.requireRestart()
			.build();

		var lushCavesAmbience = entryBuilder.startBooleanToggle(text("lush_caves_ambience"), biomeAmbience.lushCavesAmbience)
			.setDefaultValue(defaultConfig.biomeAmbience.lushCavesAmbience)
			.setSaveConsumer(newValue -> biomeAmbience.lushCavesAmbience = newValue)
			.setTooltip(tooltip("lush_caves_ambience"))
			.requireRestart()
			.build();

		var biomeAmbienceCategory = FrozenClothConfig.createSubCategory(entryBuilder, category, text("biome_ambience"),
			false,
			tooltip("biome_ambience"),
			deepDarkAmbience, dripstoneCavesAmbience, lushCavesAmbience
		);

		var wilderForestMusic = entryBuilder.startBooleanToggle(text("wilder_forest_music"), biomeMusic.wilderForestMusic)
			.setDefaultValue(defaultConfig.biomeMusic.wilderForestMusic)
			.setSaveConsumer(newValue -> biomeMusic.wilderForestMusic = newValue)
			.setTooltip(tooltip("wilder_forest_music"))
			.requireRestart()
			.build();

		var biomeMusicCategory = FrozenClothConfig.createSubCategory(entryBuilder, category, text("biome_music"),
			false,
			tooltip("biome_music"),
			wilderForestMusic
		);
	}
}
