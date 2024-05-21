package tld.unknown.mystery.util.simple;

import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;

public final class SimpleBlockMaterials {

    public static final BlockBehaviour.Properties GLASS = BlockBehaviour.Properties.of()
            .mapColor(MapColor.NONE)
            .noOcclusion();

    public static final BlockBehaviour.Properties WOOD = BlockBehaviour.Properties.of()
            .mapColor(MapColor.WOOD)
            .ignitedByLava();

    public static final BlockBehaviour.Properties METAL = BlockBehaviour.Properties.of()
            .mapColor(MapColor.METAL)
            .requiresCorrectToolForDrops();

    public static final BlockBehaviour.Properties STONE = BlockBehaviour.Properties.of()
            .mapColor(MapColor.STONE)
            .requiresCorrectToolForDrops();
}
