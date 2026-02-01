package art.arcane.thaumcraft.blocks.world;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;

public class GreatwoodLogBlock extends RotatedPillarBlock {

    public static final MapCodec<GreatwoodLogBlock> CODEC = simpleCodec(GreatwoodLogBlock::new);

    public GreatwoodLogBlock(BlockBehaviour.Properties properties) {
        super(properties
                .mapColor(MapColor.PODZOL)
                .strength(2.0F, 5.0F)
                .sound(SoundType.WOOD)
                .ignitedByLava());
    }

    @Override
    public MapCodec<? extends RotatedPillarBlock> codec() {
        return CODEC;
    }
}
