package art.arcane.thaumcraft.data.aura;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.LevelChunk;
import art.arcane.thaumcraft.data.attachments.AuraAttachment;
import art.arcane.thaumcraft.registries.ConfigDataAttachments;

import java.util.ArrayList;
import java.util.List;

public final class VisFlowProcessor {

    private static final int[][] NEIGHBOR_OFFSETS = {
            {0, -1}, {0, 1}, {-1, 0}, {1, 0}
    };

    private static final float[] PHASE_VIS_TABLE = {0.25F, 0.15F, 0.1F, 0.05F, 0F, 0.05F, 0.1F, 0.15F};

    public static void processLevel(ServerLevel level) {
        int moonPhase = (int) (level.getDayTime() / 24000L % 8L);
        float phaseVis = PHASE_VIS_TABLE[moonPhase];
        float phaseFlux = 0.25F - phaseVis;

        List<LevelChunk> loadedChunks = new ArrayList<>();
        for (var entity : level.getAllEntities()) {
            ChunkPos chunkPos = new ChunkPos(entity.blockPosition());
            if (level.hasChunk(chunkPos.x, chunkPos.z)) {
                LevelChunk chunk = level.getChunk(chunkPos.x, chunkPos.z);
                if (chunk.hasData(ConfigDataAttachments.CHUNK_AURA.get()) && !loadedChunks.contains(chunk)) {
                    loadedChunks.add(chunk);
                }
            }
        }

        level.players().forEach(player -> {
            int radius = level.getServer().getPlayerList().getViewDistance();
            ChunkPos center = new ChunkPos(player.blockPosition());
            for (int x = -radius; x <= radius; x++) {
                for (int z = -radius; z <= radius; z++) {
                    if (level.hasChunk(center.x + x, center.z + z)) {
                        LevelChunk chunk = level.getChunk(center.x + x, center.z + z);
                        if (chunk.hasData(ConfigDataAttachments.CHUNK_AURA.get()) && !loadedChunks.contains(chunk)) {
                            loadedChunks.add(chunk);
                        }
                    }
                }
            }
        });

        for (LevelChunk chunk : loadedChunks) {
            processChunk(level, chunk, phaseVis, phaseFlux);
        }
    }

    private static void processChunk(ServerLevel level, LevelChunk chunk, float phaseVis, float phaseFlux) {
        AuraAttachment aura = chunk.getData(ConfigDataAttachments.CHUNK_AURA.get());
        ChunkPos pos = chunk.getPos();

        List<AuraAttachment> neighbors = new ArrayList<>();
        for (int[] offset : NEIGHBOR_OFFSETS) {
            int nx = pos.x + offset[0];
            int nz = pos.z + offset[1];
            if (level.hasChunk(nx, nz)) {
                LevelChunk neighborChunk = level.getChunk(nx, nz);
                if (neighborChunk.hasData(ConfigDataAttachments.CHUNK_AURA.get())) {
                    neighbors.add(neighborChunk.getData(ConfigDataAttachments.CHUNK_AURA.get()));
                }
            }
        }

        if (!neighbors.isEmpty()) {
            redistributeVis(aura, neighbors);
            redistributeFlux(aura, neighbors);
        }

        regenerateVis(aura, phaseVis);

        chunk.markUnsaved();
    }

    private static void redistributeVis(AuraAttachment source, List<AuraAttachment> neighbors) {
        float currentVis = source.getVis();
        float threshold = currentVis * AuraHelper.VIS_FLOW_THRESHOLD;

        for (AuraAttachment neighbor : neighbors) {
            if (neighbor.getVis() < threshold) {
                float diff = currentVis - neighbor.getVis();
                float transfer = Math.min(diff * 0.1F, AuraHelper.MAX_TRANSFER_PER_TICK);
                if (transfer > 0) {
                    source.setVis(source.getVis() - transfer);
                    neighbor.setVis(neighbor.getVis() + transfer);
                }
            }
        }
    }

    private static void redistributeFlux(AuraAttachment source, List<AuraAttachment> neighbors) {
        float currentFlux = source.getFlux();
        float baseThreshold = source.getBaseVis() / 10F;

        if (currentFlux <= baseThreshold) return;

        AuraAttachment lowestNeighbor = null;
        float lowestFlux = Float.MAX_VALUE;

        for (AuraAttachment neighbor : neighbors) {
            if (neighbor.getFlux() < lowestFlux) {
                lowestFlux = neighbor.getFlux();
                lowestNeighbor = neighbor;
            }
        }

        if (lowestNeighbor != null && lowestFlux < currentFlux / AuraHelper.FLUX_FLOW_THRESHOLD) {
            float transfer = Math.min((currentFlux - lowestFlux) * 0.1F, AuraHelper.MAX_TRANSFER_PER_TICK);
            if (transfer > 0) {
                source.setFlux(source.getFlux() - transfer);
                lowestNeighbor.setFlux(lowestNeighbor.getFlux() + transfer);
            }
        }
    }

    private static void regenerateVis(AuraAttachment aura, float phaseVis) {
        float currentVis = aura.getVis();
        float baseVis = aura.getBaseVis();

        if (currentVis < baseVis) {
            float regen = AuraHelper.VIS_REGEN_RATE * (1 + phaseVis);
            aura.setVis(Math.min(currentVis + regen, baseVis));
        }
    }
}
