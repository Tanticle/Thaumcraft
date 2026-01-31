package art.arcane.thaumcraft.api.capabilities;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;


public interface IInfusionModifierCapability {

    float getCostModifier(Level level, BlockPos pos);

    int getCycleModifier(Level level, BlockPos pos);
}
