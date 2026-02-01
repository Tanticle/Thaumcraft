package art.arcane.thaumcraft.blocks.world;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import art.arcane.thaumcraft.world.tree.GreatwoodTreeGrower;

public class GreatwoodSaplingBlock extends SaplingBlock {

    public static final MapCodec<GreatwoodSaplingBlock> CODEC = simpleCodec(GreatwoodSaplingBlock::new);

    public GreatwoodSaplingBlock(BlockBehaviour.Properties properties) {
        super(GreatwoodTreeGrower.GREATWOOD, properties
                .mapColor(MapColor.PLANT)
                .noCollission()
                .randomTicks()
                .instabreak()
                .sound(SoundType.GRASS)
                .pushReaction(PushReaction.DESTROY));
    }

    @Override
    public MapCodec<? extends SaplingBlock> codec() {
        return CODEC;
    }
}
