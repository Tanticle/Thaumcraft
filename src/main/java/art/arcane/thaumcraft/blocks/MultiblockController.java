package art.arcane.thaumcraft.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

import java.util.Map;

public interface MultiblockController {

    void onMultiblockFormed(Level level, Map<BlockPos, Block> componentOriginals);

    void onMultiblockBroken(Level level);
}
