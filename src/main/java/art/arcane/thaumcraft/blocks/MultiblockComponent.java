package art.arcane.thaumcraft.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;

public interface MultiblockComponent {

    void init(BlockPos controllerPos, Block originalBlock);

    BlockPos getControllerPos();

    Block getOriginalBlock();
}
