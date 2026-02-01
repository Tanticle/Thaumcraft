package art.arcane.thaumcraft.blocks.world;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import art.arcane.thaumcraft.world.tree.SilverwoodTreeGrower;

public class SilverwoodSaplingBlock extends SaplingBlock {

    public static final MapCodec<SilverwoodSaplingBlock> CODEC = simpleCodec(SilverwoodSaplingBlock::new);

    public SilverwoodSaplingBlock(BlockBehaviour.Properties properties) {
        super(SilverwoodTreeGrower.SILVERWOOD, properties
                .mapColor(MapColor.COLOR_LIGHT_BLUE)
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
