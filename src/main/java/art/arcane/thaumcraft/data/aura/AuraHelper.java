package art.arcane.thaumcraft.data.aura;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;
import art.arcane.thaumcraft.data.attachments.AuraAttachment;
import art.arcane.thaumcraft.registries.ConfigDataAttachments;

import java.util.Optional;

public final class AuraHelper {

    public static final float VIS_FLOW_THRESHOLD = 0.75F;
    public static final float FLUX_FLOW_THRESHOLD = 1.75F;
    public static final float MAX_TRANSFER_PER_TICK = 1.0F;
    public static final float VIS_REGEN_RATE = 0.1F;

    public static Optional<AuraAttachment> getAura(Level level, BlockPos pos) {
        return getAura(level, new ChunkPos(pos));
    }

    public static Optional<AuraAttachment> getAura(Level level, ChunkPos chunkPos) {
        if (level.hasChunk(chunkPos.x, chunkPos.z)) {
            LevelChunk chunk = level.getChunk(chunkPos.x, chunkPos.z);
            if (chunk.hasData(ConfigDataAttachments.CHUNK_AURA.get())) {
                return Optional.of(chunk.getData(ConfigDataAttachments.CHUNK_AURA.get()));
            }
        }
        return Optional.empty();
    }

    public static float getVis(Level level, BlockPos pos) {
        return getAura(level, pos).map(AuraAttachment::getVis).orElse(0F);
    }

    public static float getFlux(Level level, BlockPos pos) {
        return getAura(level, pos).map(AuraAttachment::getFlux).orElse(0F);
    }

    public static int getAuraBase(Level level, BlockPos pos) {
        return getAura(level, pos).map(AuraAttachment::getBaseVis).orElse((short) 0);
    }

    public static float getFluxSaturation(Level level, BlockPos pos) {
        return getAura(level, pos).map(aura -> {
            if (aura.getBaseVis() <= 0) return 0F;
            return aura.getFlux() / aura.getBaseVis();
        }).orElse(0F);
    }

    public static float drainVis(Level level, BlockPos pos, float amount, boolean simulate) {
        Optional<AuraAttachment> auraOpt = getAura(level, pos);
        if (auraOpt.isEmpty()) return 0F;
        AuraAttachment aura = auraOpt.get();
        float available = Math.min(aura.getVis(), amount);
        if (!simulate && available > 0) {
            aura.setVis(aura.getVis() - available);
            markChunkDirty(level, pos);
        }
        return available;
    }

    public static float drainFlux(Level level, BlockPos pos, float amount, boolean simulate) {
        Optional<AuraAttachment> auraOpt = getAura(level, pos);
        if (auraOpt.isEmpty()) return 0F;
        AuraAttachment aura = auraOpt.get();
        float available = Math.min(aura.getFlux(), amount);
        if (!simulate && available > 0) {
            aura.setFlux(aura.getFlux() - available);
            markChunkDirty(level, pos);
        }
        return available;
    }

    public static void addVis(Level level, BlockPos pos, float amount) {
        getAura(level, pos).ifPresent(aura -> {
            aura.setVis(Math.min(aura.getVis() + amount, AuraAttachment.MAX_AURA));
            markChunkDirty(level, pos);
        });
    }

    public static void addFlux(Level level, BlockPos pos, float amount) {
        getAura(level, pos).ifPresent(aura -> {
            aura.setFlux(Math.min(aura.getFlux() + amount, AuraAttachment.MAX_AURA));
            markChunkDirty(level, pos);
        });
    }

    public static void polluteAura(Level level, BlockPos pos, float amount) {
        addFlux(level, pos, amount);
    }

    private static void markChunkDirty(Level level, BlockPos pos) {
        if (level instanceof ServerLevel serverLevel) {
            LevelChunk chunk = serverLevel.getChunkAt(pos);
            chunk.markUnsaved();
        }
    }
}
