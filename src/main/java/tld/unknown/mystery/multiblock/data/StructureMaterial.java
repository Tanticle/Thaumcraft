package tld.unknown.mystery.multiblock.data;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Predicate;

public class StructureMaterial implements Predicate<BlockState> {

    private ResourceLocation id;


    @Override
    public boolean test(BlockState blockState) {
        return false;
    }
}
