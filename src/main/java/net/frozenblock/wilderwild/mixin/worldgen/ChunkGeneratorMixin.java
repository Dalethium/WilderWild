package net.frozenblock.wilderwild.mixin.worldgen;

import net.minecraft.CrashReport;
import net.minecraft.CrashReportCategory;
import net.minecraft.ReportedException;
import net.minecraft.core.Registry;
import net.minecraft.core.SectionPos;
import net.minecraft.network.protocol.game.DebugPackets;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(value = ChunkGenerator.class, priority = 69420)
public class ChunkGeneratorMixin {

    @Inject(method = "createReferences", at = @At("HEAD"), cancellable = true)
    public void createReferences(WorldGenLevel worldGenLevel, StructureManager structureManager, ChunkAccess chunkAccess, CallbackInfo ci) {
        int i = 16;
        ChunkPos chunkPos = chunkAccess.getPos();
        int j = chunkPos.x;
        int k = chunkPos.z;
        int l = chunkPos.getMinBlockX();
        int m = chunkPos.getMinBlockZ();
        SectionPos sectionPos = SectionPos.bottomOf(chunkAccess);
        for (int n = j - i; n <= j + i; ++n) {
            for (int o = k - i; o <= k + i; ++o) {
                long p = ChunkPos.asLong(n, o);
                for (StructureStart structureStart : worldGenLevel.getChunk(n, o).getAllStarts().values()) {
                    try {
                        if (!structureStart.isValid() || !structureStart.getBoundingBox().intersects(l, m, l + 15, m + 15))
                            continue;
                        structureManager.addReferenceForStructure(sectionPos, structureStart.getStructure(), p, chunkAccess);
                        DebugPackets.sendStructurePacket(worldGenLevel, structureStart);
                    } catch (Exception exception) {
                        CrashReport crashReport = CrashReport.forThrowable(exception, "Generating structure reference");
                        CrashReportCategory crashReportCategory = crashReport.addCategory("Structure");
                        Optional<Registry<Structure>> optional = (Optional<Registry<Structure>>) worldGenLevel.registryAccess().registry(Registry.STRUCTURE_REGISTRY);
                        crashReportCategory.setDetail("Id", () -> optional.map(registry -> registry.getKey(structureStart.getStructure()).toString()).orElse("UNKNOWN"));
                        crashReportCategory.setDetail("Name", () -> Registry.STRUCTURE_TYPES.getKey(structureStart.getStructure().type()).toString());
                        crashReportCategory.setDetail("Class", () -> structureStart.getStructure().getClass().getCanonicalName());
                        throw new ReportedException(crashReport);
                    }
                }
            }
        }
        ci.cancel();
    }

}