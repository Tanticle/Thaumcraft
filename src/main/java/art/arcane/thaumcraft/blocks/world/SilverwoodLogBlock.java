package art.arcane.thaumcraft.blocks.world;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;

public class SilverwoodLogBlock extends RotatedPillarBlock {

    public static final MapCodec<SilverwoodLogBlock> CODEC = simpleCodec(SilverwoodLogBlock::new);

    public SilverwoodLogBlock(BlockBehaviour.Properties properties) {
        super(properties
                .mapColor(MapColor.QUARTZ)
                .strength(2.0F, 5.0F)
                .sound(SoundType.WOOD)
                .lightLevel(state -> 5)
                .ignitedByLava());
    }

    @Override
    public MapCodec<? extends RotatedPillarBlock> codec() {
        return CODEC;
    }
}
