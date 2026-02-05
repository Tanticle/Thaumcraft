package art.arcane.thaumcraft.blocks.entities;

import art.arcane.thaumcraft.blocks.NitorBlock;
import art.arcane.thaumcraft.client.fx.ThaumcraftFX;
import art.arcane.thaumcraft.registries.ConfigBlockEntities;
import art.arcane.thaumcraft.util.simple.SimpleBlockEntity;
import art.arcane.thaumcraft.util.simple.TickableBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class NitorBlockEntity extends SimpleBlockEntity implements TickableBlockEntity {

    private int count;

    public NitorBlockEntity(BlockPos pos, BlockState state) {
        super(ConfigBlockEntities.NITOR.entityType(), pos, state);
        this.count = 0;
    }

    @Override
    public TickSetting getTickSetting() {
        return TickSetting.CLIENT;
    }

    @Override
    public void onClientTick() {
        Level level = getLevel();
        if (level == null) return;

        BlockState state = level.getBlockState(getBlockPos());
        if (!(state.getBlock() instanceof NitorBlock nitorBlock)) return;

        int color = nitorBlock.getColor().getMapColor().col;
        BlockPos pos = getBlockPos();

        double x = pos.getX() + 0.5 + level.random.nextGaussian() * 0.025;
        double y = pos.getY() + 0.45 + level.random.nextGaussian() * 0.025;
        double z = pos.getZ() + 0.5 + level.random.nextGaussian() * 0.025;

        ThaumcraftFX.drawNitorFlames(
                x, y, z,
                level.random.nextGaussian() * 0.0025,
                level.random.nextFloat() * 0.06,
                level.random.nextGaussian() * 0.0025,
                color, 0
        );

        if (count++ % 10 == 0) {
            ThaumcraftFX.drawNitorCore(
                    pos.getX() + 0.5, pos.getY() + 0.49, pos.getZ() + 0.5,
                    0.0, 0.0, 0.0
            );
        }
    }

    @Override
    protected void readNbt(CompoundTag nbt, HolderLookup.Provider registries) {
    }

    @Override
    protected void writeNbt(CompoundTag nbt, HolderLookup.Provider registries) {
    }
}
