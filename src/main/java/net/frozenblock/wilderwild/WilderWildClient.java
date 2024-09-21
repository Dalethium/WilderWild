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

package net.frozenblock.wilderwild;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.frozenblock.lib.menu.api.Panoramas;
import net.frozenblock.lib.menu.api.SplashTextAPI;
import net.frozenblock.wilderwild.client.WWBlockRenderLayers;
import net.frozenblock.wilderwild.client.WWFluidRendering;
import net.frozenblock.wilderwild.client.WWItemProperties;
import net.frozenblock.wilderwild.client.WWModelLayers;
import net.frozenblock.wilderwild.client.WWParticleEngine;
import net.frozenblock.wilderwild.client.WWTints;
import net.frozenblock.wilderwild.entity.render.easter.WilderEasterEggs;
import net.frozenblock.wilderwild.networking.WWClientNetworking;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.ResourceManager;
import org.jetbrains.annotations.NotNull;

@Environment(EnvType.CLIENT)
public final class WilderWildClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		SplashTextAPI.addSplashLocation(WWConstants.id("texts/splashes.txt"));
		addPanorama("birch_valley");
		WilderEasterEggs.hatchEasterEggs();

		WWBlockRenderLayers.init();
		WWItemProperties.init();
		WWModelLayers.init();
		WWParticleEngine.init();
		WWTints.initBlocks();
		WWTints.initItems();
		WWClientNetworking.registerPacketReceivers();

		ResourceManagerHelper.get(PackType.CLIENT_RESOURCES).registerReloadListener(new SimpleSynchronousResourceReloadListener() {
			private boolean hasRegistered = false;
			@Override
			public ResourceLocation getFabricId() {
				return WWConstants.id("mesoglea_liquid_rendering");
			}

			@Override
			public void onResourceManagerReload(@NotNull ResourceManager resourceManager) {
				if (!this.hasRegistered) {
					this.hasRegistered = true;
					WWFluidRendering.init();
				}
			}
		});

		ResourceManagerHelper.get(PackType.CLIENT_RESOURCES).registerReloadListener(new SimpleSynchronousResourceReloadListener() {
			@Override
			public ResourceLocation getFabricId() {
				return WWConstants.id("minecraft_live_sculk_sensor");
			}

			@Override
			public void onResourceManagerReload(@NotNull ResourceManager resourceManager) {
				WWConstants.MC_LIVE_TENDRILS = resourceManager.getResource(WWConstants.id("textures/entity/sculk_sensor/new_tendril_enabler.png")).isPresent();
			}
		});
	}

	private static void addPanorama(String panoramaName) {
		ResourceLocation panoramaLocation = WWConstants.id("textures/gui/title/" + panoramaName + "/panorama");
		Panoramas.addPanorama(panoramaLocation);
	}

}
