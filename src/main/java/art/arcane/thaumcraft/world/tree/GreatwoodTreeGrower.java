package art.arcane.thaumcraft.world.tree;

import net.minecraft.world.level.block.grower.TreeGrower;

import java.util.Optional;

public final class GreatwoodTreeGrower {

    public static final TreeGrower GREATWOOD = new TreeGrower(
            "greatwood",
            Optional.of(ConfigTreeFeatures.GREATWOOD_TREE),
            Optional.empty(),
            Optional.empty());

    private GreatwoodTreeGrower() {
    }
}
