package art.arcane.thaumcraft.util.simple;

import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;

public final class SimpleBlockMaterials {

    public static BlockBehaviour.Properties glass(BlockBehaviour.Properties properties) {
        return properties.mapColor(MapColor.NONE).noOcclusion();
    }

    public static BlockBehaviour.Properties wood(BlockBehaviour.Properties properties) {
        return properties.mapColor(MapColor.WOOD).ignitedByLava();
    }

    public static BlockBehaviour.Properties metal(BlockBehaviour.Properties properties) {
        return properties.mapColor(MapColor.METAL).requiresCorrectToolForDrops();
    }

    public static BlockBehaviour.Properties stone(BlockBehaviour.Properties properties) {
        return properties.mapColor(MapColor.STONE).requiresCorrectToolForDrops();
    }
}
