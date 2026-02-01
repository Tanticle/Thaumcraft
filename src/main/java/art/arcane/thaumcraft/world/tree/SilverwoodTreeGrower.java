package art.arcane.thaumcraft.world.tree;

import net.minecraft.world.level.block.grower.TreeGrower;

import java.util.Optional;

public final class SilverwoodTreeGrower {

    public static final TreeGrower SILVERWOOD = new TreeGrower(
            "silverwood",
            Optional.empty(),
            Optional.of(ConfigTreeFeatures.SILVERWOOD_TREE),
            Optional.empty());

    private SilverwoodTreeGrower() {
    }
}
