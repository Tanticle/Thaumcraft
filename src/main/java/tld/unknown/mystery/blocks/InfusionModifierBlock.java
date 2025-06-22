package tld.unknown.mystery.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import tld.unknown.mystery.api.ThaumcraftData;
import tld.unknown.mystery.api.capabilities.InfusionModifierCapability;

public class InfusionModifierBlock extends Block implements InfusionModifierCapability {

    private final float costModifier;
    private final int cycleModifier;

    public InfusionModifierBlock(Properties properties, float costModifier, int cycleModifier) {
        super(properties);
        this.costModifier = costModifier;
        this.cycleModifier = cycleModifier;
    }

    @Override
    public int getCycleModifier(Level level, BlockPos pos) {
        return level.getBlockState(pos.above()).is(ThaumcraftData.Tags.INFUSION_PILLAR) ? this.cycleModifier : 0;
    }

    @Override
    public float getCostModifier(Level level, BlockPos pos) {
        return level.getBlockState(pos.above()).is(ThaumcraftData.Tags.INFUSION_PILLAR) ? this.costModifier : 0;
    }
}
