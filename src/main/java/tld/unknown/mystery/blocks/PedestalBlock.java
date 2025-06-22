package tld.unknown.mystery.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import tld.unknown.mystery.api.capabilities.IInfusionModifierCapability;
import tld.unknown.mystery.blocks.entities.PedestalBlockEntity;
import tld.unknown.mystery.registries.ConfigBlockEntities;
import tld.unknown.mystery.util.simple.SimpleBlockMaterials;
import tld.unknown.mystery.util.simple.SimpleEntityBlock;

//TODO: Infusion Stabilization
public class PedestalBlock extends SimpleEntityBlock<PedestalBlockEntity> implements IInfusionModifierCapability {

    private static final VoxelShape SHAPE = Shapes.or(
            Block.box(0, 0, 0, 16, 4, 16),
            Block.box(4, 4, 4, 12, 12, 12),
            Block.box(2, 12, 2, 14, 16, 14));

    public static final IntegerProperty STABILIZED = IntegerProperty.create("stabilized", 0, 15);

    private final float infusionCostModifier;

    public PedestalBlock(BlockBehaviour.Properties props, float infusionCostModifier) {
        super(SimpleBlockMaterials.stone(props), ConfigBlockEntities.PEDESTAL.entityTypeObject());
        this.infusionCostModifier = infusionCostModifier;
        registerDefaultState(this.getStateDefinition().any()
                .setValue(STABILIZED, 0));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(STABILIZED);
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return SHAPE;
    }

    @Override
    public boolean skipRendering(BlockState pState, BlockState pAdjacentBlockState, Direction pDirection) {
        return pDirection == Direction.DOWN;
    }

    @Override
    protected InteractionResult useItemOn(ItemStack pStack, BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHitResult) {
        if (pPlayer.isCrouching() || pHand != InteractionHand.MAIN_HAND)
            return InteractionResult.TRY_WITH_EMPTY_HAND;
        if (!(pLevel.getBlockEntity(pPos) instanceof PedestalBlockEntity be))
            return InteractionResult.FAIL;

        ItemStack pedestalStack = be.getItemStack();
        ItemStack heldStack = pPlayer.getItemInHand(pHand);

        if (!pLevel.isClientSide()) {
            if (!pedestalStack.isEmpty() && !pPlayer.getInventory().add(pedestalStack))
                pLevel.addFreshEntity(new ItemEntity(pLevel, pPos.getX() + 0.5, pPos.getY() + 1, pPos.getZ() + 0.5, pedestalStack));
            be.setItemStack(heldStack.split(1));
            be.sync();
            pLevel.playSound(null, pPos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS);
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, BlockHitResult pHitResult) {
        if (!(pLevel.getBlockEntity(pPos) instanceof PedestalBlockEntity be))
            return InteractionResult.FAIL;
        if (pPlayer.isCrouching() || be.getItemStack().isEmpty()) //TODO - BE, fix pickup logic happening with empty content.
            return InteractionResult.PASS;

        if(!pLevel.isClientSide()) {
            ItemStack pedestalStack = be.getItemStack();
            if (!pPlayer.getInventory().add(pedestalStack))
                pLevel.addFreshEntity(new ItemEntity(pLevel, pPos.getX() + 0.5, pPos.getY() + 1, pPos.getZ() + 0.5, pedestalStack));
            be.setItemStack(ItemStack.EMPTY);
            be.sync();
            pLevel.playSound(null, pPos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS);
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    public float getCostModifier(Level level, BlockPos pos) {
        return this.infusionCostModifier;
    }

    @Override
    public int getCycleModifier(Level level, BlockPos pos) {
        return 0;
    }
}
