package net.frozenblock.wilderwild.misc.mod_compat.terrablender;

import com.mojang.datafixers.util.Pair;
import java.util.List;
import java.util.function.Consumer;
import net.frozenblock.lib.worldgen.biome.api.parameters.FrozenBiomeParameters;
import net.frozenblock.lib.worldgen.biome.api.parameters.OverworldBiomeBuilderParameters;
import net.frozenblock.lib_compat.terrablender.FrozenTerraBlenderCompat;
import net.frozenblock.wilderwild.misc.config.ClothConfigInteractionHandler;
import net.frozenblock.wilderwild.registry.RegisterWorldgen;
import net.frozenblock.wilderwild.world.generation.WilderSharedWorldgen;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.Climate;
import terrablender.api.Region;
import terrablender.api.RegionType;

public class WilderOverworldRegion extends Region {
	public WilderOverworldRegion(ResourceLocation name, int weight) {
		super(name, RegionType.OVERWORLD, weight);
	}

	@Override
	public void addBiomes(Registry<Biome> registry, Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> mapper) {
		this.addModifiedVanillaOverworldBiomes(mapper, builder -> {

			if (ClothConfigInteractionHandler.generateMixedForest()) {
				OverworldBiomeBuilderParameters.points(Biomes.TAIGA).forEach(point -> {
					builder.replaceParameter(point,
							Climate.parameters(
									WilderSharedWorldgen.CypressWetlands.TEMPERATURE,
									WilderSharedWorldgen.CypressWetlands.HUMIDITY,
									WilderSharedWorldgen.CypressWetlands.CONTINENTALNESS,
									WilderSharedWorldgen.CypressWetlands.EROSION,
									point.depth(),
									point.weirdness(),
									WilderSharedWorldgen.CypressWetlands.OFFSET
							)
					);
					builder.replaceBiome(point, RegisterWorldgen.MIXED_FOREST);
				});
			}

			if (ClothConfigInteractionHandler.generateBirchTaiga()) {
				OverworldBiomeBuilderParameters.points(Biomes.BIRCH_FOREST).forEach(point -> {
					builder.replaceParameter(point,
							Climate.parameters(
									WilderSharedWorldgen.BirchTaiga.TEMPERATURE,
									WilderSharedWorldgen.BirchTaiga.HUMIDITY,
									point.continentalness(),
									point.erosion(),
									point.depth(),
									point.weirdness(),
									point.offset()
							)
					);
					builder.replaceBiome(point, RegisterWorldgen.BIRCH_TAIGA);
				});
			}

			if (ClothConfigInteractionHandler.generateFlowerField()) {
				OverworldBiomeBuilderParameters.points(Biomes.FLOWER_FOREST).forEach(point -> {
					builder.replaceParameter(point,
							Climate.parameters(
									WilderSharedWorldgen.FlowerField.TEMPERATURE_A,
									WilderSharedWorldgen.FlowerField.HUMIDITY_A,
									point.continentalness(),
									point.erosion(),
									point.depth(),
									point.weirdness(),
									point.offset()
							)
					);
					builder.replaceBiome(point, RegisterWorldgen.FLOWER_FIELD);
				});
				OverworldBiomeBuilderParameters.points(Biomes.FLOWER_FOREST).forEach(point -> {
					builder.replaceParameter(point,
							Climate.parameters(
									WilderSharedWorldgen.FlowerField.TEMPERATURE_B,
									WilderSharedWorldgen.FlowerField.HUMIDITY_B,
									point.continentalness(),
									point.erosion(),
									point.depth(),
									point.weirdness(),
									point.offset()
							)
					);
					builder.replaceBiome(point, RegisterWorldgen.FLOWER_FIELD);
				});
			}

			if (ClothConfigInteractionHandler.generateAridSavanna()) {
				OverworldBiomeBuilderParameters.points(Biomes.SAVANNA).forEach(point -> {
					builder.replaceParameter(point,
							Climate.parameters(
									WilderSharedWorldgen.AridSavanna.TEMPERATURE,
									WilderSharedWorldgen.AridSavanna.HUMIDITY,
									point.continentalness(),
									point.erosion(),
									point.depth(),
									point.weirdness(),
									point.offset()
							)
					);
					builder.replaceBiome(point, RegisterWorldgen.ARID_SAVANNA);
				});
			}

			if (ClothConfigInteractionHandler.generateParchedForest()) {
				OverworldBiomeBuilderParameters.points(Biomes.FOREST).forEach(point -> {
					builder.replaceParameter(point,
							Climate.parameters(
									WilderSharedWorldgen.ParchedForest.TEMPERATURE,
									WilderSharedWorldgen.ParchedForest.HUMIDITY,
									point.continentalness(),
									point.erosion(),
									point.depth(),
									point.weirdness(),
									point.offset()
							)
					);
					builder.replaceBiome(point, RegisterWorldgen.PARCHED_FOREST);
				});
			}

			if (ClothConfigInteractionHandler.generateAridForest()) {
				OverworldBiomeBuilderParameters.points(Biomes.FOREST).forEach(point -> {
					builder.replaceParameter(point,
							Climate.parameters(
									WilderSharedWorldgen.AridForest.TEMPERATURE,
									WilderSharedWorldgen.AridForest.HUMIDITY,
									point.continentalness(),
									point.erosion(),
									point.depth(),
									point.weirdness(),
									point.offset()
							)
					);
					builder.replaceBiome(point, RegisterWorldgen.ARID_FOREST);
				});
			}

			if (ClothConfigInteractionHandler.generateAridForest()) {
				OverworldBiomeBuilderParameters.points(Biomes.SNOWY_TAIGA).forEach(point -> {
					builder.replaceParameter(point,
							Climate.parameters(
									WilderSharedWorldgen.OldGrowthSnowySpruceTaiga.TEMPERATURE,
									WilderSharedWorldgen.OldGrowthSnowySpruceTaiga.HUMIDITY,
									point.continentalness(),
									point.erosion(),
									point.depth(),
									point.weirdness(),
									point.offset()
							)
					);
					builder.replaceBiome(point, RegisterWorldgen.OLD_GROWTH_SNOWY_SPRUCE_TAIGA);
				});
			}

			if (ClothConfigInteractionHandler.generateCypressWetlands()) {
				OverworldBiomeBuilderParameters.points(Biomes.SWAMP).forEach(point -> {
					builder.replaceParameter(point,
							Climate.parameters(
									WilderSharedWorldgen.CypressWetlands.TEMPERATURE,
									WilderSharedWorldgen.CypressWetlands.HUMIDITY,
									WilderSharedWorldgen.CypressWetlands.CONTINENTALNESS,
									WilderSharedWorldgen.CypressWetlands.EROSION,
									point.depth(),
									point.weirdness(),
									WilderSharedWorldgen.CypressWetlands.OFFSET
							)
					);
					builder.replaceBiome(point, RegisterWorldgen.CYPRESS_WETLANDS);
				});
			}

			if (ClothConfigInteractionHandler.generateCypressWetlands()) {
				OverworldBiomeBuilderParameters.points(Biomes.MANGROVE_SWAMP).forEach(point -> {
					builder.replaceParameter(point,
							Climate.parameters(
									WilderSharedWorldgen.CypressWetlands.TEMPERATURE,
									WilderSharedWorldgen.CypressWetlands.HUMIDITY,
									WilderSharedWorldgen.CypressWetlands.CONTINENTALNESS,
									WilderSharedWorldgen.CypressWetlands.EROSION,
									point.depth(),
									point.weirdness(),
									WilderSharedWorldgen.CypressWetlands.OFFSET
							)
					);
					builder.replaceBiome(point, RegisterWorldgen.CYPRESS_WETLANDS);
				});
			}

			if (ClothConfigInteractionHandler.generateJellyfishCaves()) {
				OverworldBiomeBuilderParameters.points(Biomes.DRIPSTONE_CAVES).forEach(point -> {
					builder.replaceParameter(point,
							WilderSharedWorldgen.semiDeepParameters(
									WilderSharedWorldgen.JellyfishCaves.TEMPERATURE,
									WilderSharedWorldgen.JellyfishCaves.HUMIDITY,
									WilderSharedWorldgen.JellyfishCaves.CONTINENTALNESS,
									WilderSharedWorldgen.JellyfishCaves.EROSION,
									WilderSharedWorldgen.JellyfishCaves.WEIRDNESS,
									WilderSharedWorldgen.JellyfishCaves.OFFSET
							)
					);
					builder.replaceBiome(point, RegisterWorldgen.JELLYFISH_CAVES);
				});
			}

			if (ClothConfigInteractionHandler.generateOasis()) {
				OverworldBiomeBuilderParameters.points(Biomes.DESERT).forEach(point -> {
					builder.replaceParameter(point,
							Climate.parameters(
									WilderSharedWorldgen.Oasis.TEMPERATURE,
									WilderSharedWorldgen.Oasis.HUMIDITY,
									WilderSharedWorldgen.Oasis.CONTINENTALNESS,
									WilderSharedWorldgen.Oasis.EROSION,
									point.depth(),
									point.weirdness(),
									WilderSharedWorldgen.Oasis.OFFSET
							)
					);
					builder.replaceBiome(point, RegisterWorldgen.OASIS);
				});
			}

			if (ClothConfigInteractionHandler.generateWarmRiver()) {
				OverworldBiomeBuilderParameters.points(Biomes.RIVER).forEach(point -> {
					builder.replaceParameter(point,
							Climate.parameters(
									WilderSharedWorldgen.WarmRiver.WARM_RANGE,
									WilderSharedWorldgen.WarmRiver.HUMIDITY,
									point.continentalness(),
									point.erosion(),
									point.depth(),
									point.weirdness(),
									point.offset()
							)
					);
					builder.replaceBiome(point, RegisterWorldgen.WARM_RIVER);
				});
			}

			if (ClothConfigInteractionHandler.modifyMangroveSwampPlacement()) {
				OverworldBiomeBuilderParameters.points(Biomes.MANGROVE_SWAMP).forEach(point ->
						builder.replaceParameter(point,
								new Climate.ParameterPoint(
										WilderSharedWorldgen.MangroveSwamp.TEMPERATURE,
										WilderSharedWorldgen.MangroveSwamp.HUMIDITY,
										point.continentalness(),
										point.erosion(),
										point.depth(),
										point.weirdness(),
										point.offset()
								)
						)
				);
			}

			if (ClothConfigInteractionHandler.modifySwampPlacement()) {
				OverworldBiomeBuilderParameters.points(Biomes.SWAMP).forEach(point ->
						builder.replaceParameter(point,
								new Climate.ParameterPoint(
										WilderSharedWorldgen.Swamp.TEMPERATURE,
										WilderSharedWorldgen.Swamp.HUMIDITY,
										point.continentalness(),
										point.erosion(),
										point.depth(),
										point.weirdness(),
										point.offset()
								)
						)
				);
			}
		});
	}
}
