package net.frozenblock.wilderwild.world.generation.features.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.List;
import java.util.Objects;
import net.frozenblock.wilderwild.block.SmallSpongeBlock;
import net.frozenblock.wilderwild.registry.RegisterBlocks;
import net.minecraft.Util;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public class SmallSpongeFeatureConfig implements FeatureConfiguration {
    public static final Codec<SmallSpongeFeatureConfig> CODEC = RecordCodecBuilder.create(
            (instance) -> instance.group(Registry.BLOCK.byNameCodec().fieldOf("block").flatXmap(SmallSpongeFeatureConfig::validateBlock, DataResult::success).orElse((SmallSpongeBlock) RegisterBlocks.SMALL_SPONGE).forGetter(
                    (config) -> config.sponge), Codec.intRange(1, 64).fieldOf("search_range").orElse(10).forGetter(
                    (config) -> config.searchRange), Codec.BOOL.fieldOf("can_place_on_floor").orElse(false).forGetter(
                    (config) -> config.placeOnFloor), Codec.BOOL.fieldOf("can_place_on_ceiling").orElse(false).forGetter(
                    (config) -> config.placeOnCeiling), Codec.BOOL.fieldOf("can_place_on_wall").orElse(false).forGetter(
                    (config) -> config.placeOnWalls), TagKey.codec(Registry.BLOCK_REGISTRY).fieldOf("can_be_placed_on").forGetter(
                    (config) -> config.canPlaceOn)).apply(instance, SmallSpongeFeatureConfig::new)
    );
    public final SmallSpongeBlock sponge;
    public final int searchRange;
    public final boolean placeOnFloor;
    public final boolean placeOnCeiling;
    public final boolean placeOnWalls;
    public final TagKey<Block> canPlaceOn;
    private final ObjectArrayList<Direction> directions;

    private static DataResult<SmallSpongeBlock> validateBlock(Block block) {
        DataResult<SmallSpongeBlock> var10000;
        if (block instanceof SmallSpongeBlock smallSpongeBlock) {
            var10000 = DataResult.success(smallSpongeBlock);
        } else {
            var10000 = DataResult.error("Growth block should be a small sponge block bruh bruh bruh bruh bruh");
        }

        return var10000;
    }

    public SmallSpongeFeatureConfig(SmallSpongeBlock sponge, int searchRange, boolean placeOnFloor, boolean placeOnCeiling, boolean placeOnWalls, TagKey<Block> canPlaceOn) {
        this.sponge = sponge;
        this.searchRange = searchRange;
        this.placeOnFloor = placeOnFloor;
        this.placeOnCeiling = placeOnCeiling;
        this.placeOnWalls = placeOnWalls;
        this.canPlaceOn = canPlaceOn;
        this.directions = new ObjectArrayList<>(6);
        if (placeOnCeiling) {
            this.directions.add(Direction.UP);
        }

        if (placeOnFloor) {
            this.directions.add(Direction.DOWN);
        }

        if (placeOnWalls) {
            Direction.Plane var10000 = Direction.Plane.HORIZONTAL;
            ObjectArrayList<Direction> var10001 = this.directions;
            Objects.requireNonNull(var10001);
            var10000.forEach(var10001::add);
        }

    }

    public List<Direction> shuffleDirections(RandomSource random, Direction excluded) {
        return Util.toShuffledList(this.directions.stream().filter((direction) -> direction != excluded), random);
    }

    public List<Direction> shuffleDirections(RandomSource random) {
        return Util.shuffledCopy(this.directions, random);
    }
}