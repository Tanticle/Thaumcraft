package art.arcane.thaumcraft.blocks.world;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;

public class GreatwoodLeavesBlock extends LeavesBlock {

    public static final MapCodec<GreatwoodLeavesBlock> CODEC = simpleCodec(GreatwoodLeavesBlock::new);

    public GreatwoodLeavesBlock(BlockBehaviour.Properties properties) {
        super(properties
                .mapColor(MapColor.PLANT)
                .strength(0.2F)
                .randomTicks()
                .sound(SoundType.GRASS)
                .noOcclusion()
                .isValidSpawn((state, level, pos, entity) -> false)
                .isSuffocating((state, level, pos) -> false)
                .isViewBlocking((state, level, pos) -> false)
                .ignitedByLava()
                .pushReaction(PushReaction.DESTROY)
                .isRedstoneConductor((state, level, pos) -> false));
    }

    @Override
    public MapCodec<? extends LeavesBlock> codec() {
        return CODEC;
    }
}
