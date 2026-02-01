package art.arcane.thaumcraft.blocks.entities;

import lombok.Getter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.state.BlockState;
import art.arcane.thaumcraft.data.attachments.AuraAttachment;
import art.arcane.thaumcraft.data.aura.AuraHelper;
import art.arcane.thaumcraft.registries.ConfigBlockEntities;
import art.arcane.thaumcraft.util.simple.SimpleBlockEntity;
import art.arcane.thaumcraft.util.simple.TickableBlockEntity;

public class DioptraBlockEntity extends SimpleBlockEntity implements TickableBlockEntity {

    public static final int GRID_SIZE = 13;
    public static final int GRID_RADIUS = 6;

    @Getter
    private final byte[] gridVis = new byte[GRID_SIZE * GRID_SIZE];
    @Getter
    private final byte[] gridFlux = new byte[GRID_SIZE * GRID_SIZE];

    @Getter
    private float centerVis;
    @Getter
    private float centerFlux;

    private int tickCounter;

    public DioptraBlockEntity(BlockPos pos, BlockState state) {
        super(ConfigBlockEntities.DIOPTRA.entityType(), pos, state);
    }

    @Override
    public TickSetting getTickSetting() {
        return TickSetting.SERVER;
    }

    @Override
    public void onServerTick() {
        tickCounter++;
        if (tickCounter >= 20) {
            tickCounter = 0;
            scanAura();
            sync();
        }
    }

    private void scanAura() {
        if (level == null) return;

        ChunkPos centerChunk = new ChunkPos(getBlockPos());

        for (int x = 0; x < GRID_SIZE; x++) {
            for (int z = 0; z < GRID_SIZE; z++) {
                int chunkX = centerChunk.x + (x - GRID_RADIUS);
                int chunkZ = centerChunk.z + (z - GRID_RADIUS);
                ChunkPos targetChunk = new ChunkPos(chunkX, chunkZ);

                int index = x + z * GRID_SIZE;
                boolean isCenter = x == GRID_RADIUS && z == GRID_RADIUS;

                var auraOpt = AuraHelper.getAura(level, targetChunk);
                if (auraOpt.isPresent()) {
                    AuraAttachment aura = auraOpt.get();
                    gridVis[index] = (byte) Math.min(64, (int) (aura.getVis() / AuraAttachment.MAX_AURA * 64));
                    gridFlux[index] = (byte) Math.min(64, (int) (aura.getFlux() / AuraAttachment.MAX_AURA * 64));

                    if (isCenter) {
                        centerVis = aura.getVis();
                        centerFlux = aura.getFlux();
                    }
                } else {
                    gridVis[index] = 0;
                    gridFlux[index] = 0;

                    if (isCenter) {
                        centerVis = 0;
                        centerFlux = 0;
                    }
                }
            }
        }
    }

    @Override
    protected void readNbt(CompoundTag nbt, HolderLookup.Provider registries) {
        if (nbt.contains("gridVis")) {
            byte[] vis = nbt.getByteArray("gridVis");
            System.arraycopy(vis, 0, gridVis, 0, Math.min(vis.length, gridVis.length));
        }
        if (nbt.contains("gridFlux")) {
            byte[] flux = nbt.getByteArray("gridFlux");
            System.arraycopy(flux, 0, gridFlux, 0, Math.min(flux.length, gridFlux.length));
        }
        centerVis = nbt.getFloat("centerVis");
        centerFlux = nbt.getFloat("centerFlux");
    }

    @Override
    protected void writeNbt(CompoundTag nbt, HolderLookup.Provider registries) {
        nbt.putByteArray("gridVis", gridVis);
        nbt.putByteArray("gridFlux", gridFlux);
        nbt.putFloat("centerVis", centerVis);
        nbt.putFloat("centerFlux", centerFlux);
    }
}
