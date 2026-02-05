package art.arcane.thaumcraft.blocks.devices;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import art.arcane.thaumcraft.blocks.entities.EverfullUrnBlockEntity;
import art.arcane.thaumcraft.registries.ConfigBlockEntities;
import art.arcane.thaumcraft.util.simple.SimpleBlockMaterials;
import art.arcane.thaumcraft.util.simple.SimpleEntityBlock;
import art.arcane.thaumcraft.util.simple.TickableBlockEntity;
import org.jetbrains.annotations.Nullable;

public class EverfullUrnBlock extends SimpleEntityBlock<EverfullUrnBlockEntity> {

    private static final VoxelShape SHAPE = Block.box(3.0, 0.0, 3.0, 13.0, 16.0, 13.0);

    public EverfullUrnBlock(BlockBehaviour.Properties props) {
        super(SimpleBlockMaterials.stone(props).strength(2.0F).noOcclusion(), ConfigBlockEntities.EVERFULL_URN.entityTypeObject());
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        }

        EverfullUrnBlockEntity entity = getEntity(level, pos);

        if (stack.is(Items.BUCKET)) {
            if (entity.drainWater(EverfullUrnBlockEntity.BUCKET_COST)) {
                if (!player.getAbilities().instabuild) {
                    stack.shrink(1);
                }
                ItemStack waterBucket = new ItemStack(Items.WATER_BUCKET);
                if (stack.isEmpty()) {
                    player.setItemInHand(hand, waterBucket);
                } else if (!player.getInventory().add(waterBucket)) {
                    player.drop(waterBucket, false);
                }
                if (player instanceof ServerPlayer serverPlayer) {
                    serverPlayer.containerMenu.broadcastChanges();
                }
                level.playSound(null, pos, SoundEvents.BUCKET_FILL, SoundSource.BLOCKS, 0.33F,
                        1.0F + (level.random.nextFloat() - level.random.nextFloat()) * 0.3F);
                return InteractionResult.CONSUME;
            }
        } else if (stack.is(Items.GLASS_BOTTLE)) {
            if (entity.drainWater(EverfullUrnBlockEntity.BOTTLE_COST)) {
                if (!player.getAbilities().instabuild) {
                    stack.shrink(1);
                }
                ItemStack waterBottle = PotionContents.createItemStack(Items.POTION, Potions.WATER);
                if (stack.isEmpty()) {
                    player.setItemInHand(hand, waterBottle);
                } else if (!player.getInventory().add(waterBottle)) {
                    player.drop(waterBottle, false);
                }
                if (player instanceof ServerPlayer serverPlayer) {
                    serverPlayer.containerMenu.broadcastChanges();
                }
                level.playSound(null, pos, SoundEvents.BOTTLE_FILL, SoundSource.BLOCKS, 0.33F,
                        1.0F + (level.random.nextFloat() - level.random.nextFloat()) * 0.3F);
                return InteractionResult.CONSUME;
            }
        }

        return InteractionResult.PASS;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        return InteractionResult.PASS;
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (level.getBlockEntity(pos) instanceof EverfullUrnBlockEntity entity && entity.isFull()) {
            if (random.nextInt(3) == 0) {
                double x = pos.getX() + 0.5 + (random.nextDouble() - 0.5) * 0.2;
                double y = pos.getY() + 1.0;
                double z = pos.getZ() + 0.5 + (random.nextDouble() - 0.5) * 0.2;
                level.addParticle(ParticleTypes.SPLASH, x, y, z, 0, 0, 0);
            }
        }
    }

    @Nullable
    @Override
    public <E extends BlockEntity> BlockEntityTicker<E> getTicker(Level level, BlockState state, BlockEntityType<E> beType) {
        if (level.isClientSide) {
            return null;
        }
        return (l, p, s, be) -> {
            if (be instanceof TickableBlockEntity tickable && tickable.getTickSetting().isTickServer()) {
                tickable.onServerTick();
            }
        };
    }
}
