package art.arcane.thaumcraft.blocks.world;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public class VishroomBlock extends BushBlock {

    public static final MapCodec<VishroomBlock> CODEC = simpleCodec(VishroomBlock::new);

    public VishroomBlock(BlockBehaviour.Properties properties) {
        super(properties
                .mapColor(MapColor.COLOR_PURPLE)
                .noCollission()
                .instabreak()
                .sound(SoundType.GRASS)
                .lightLevel(state -> 6)
                .pushReaction(PushReaction.DESTROY));
    }

    @Override
    public MapCodec<? extends BushBlock> codec() {
        return CODEC;
    }

    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
        return state.isSolid();
    }

    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (!level.isClientSide() && entity instanceof LivingEntity livingEntity && level.random.nextInt(5) == 0) {
            livingEntity.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 200, 0));
        }
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (random.nextInt(3) == 0) {
            double x = pos.getX() + 0.5 + (random.nextFloat() - random.nextFloat()) * 0.4;
            double y = pos.getY() + 0.3;
            double z = pos.getZ() + 0.5 + (random.nextFloat() - random.nextFloat()) * 0.4;
            level.addParticle(
                    net.minecraft.core.particles.ParticleTypes.WITCH,
                    x, y, z,
                    0.0, 0.02, 0.0
            );
        }
    }
}
