package art.arcane.thaumcraft.blocks.devices;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import art.arcane.thaumcraft.blocks.entities.HungryChestBlockEntity;
import art.arcane.thaumcraft.menus.HungryChestMenu;
import art.arcane.thaumcraft.registries.ConfigBlockEntities;
import art.arcane.thaumcraft.util.simple.SimpleBlockMaterials;
import art.arcane.thaumcraft.util.simple.SimpleEntityBlock;

public class HungryChestBlock extends SimpleEntityBlock<HungryChestBlockEntity> {

    public static final EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;
    private static final Component CONTAINER_TITLE = Component.translatable("container.thaumcraft.hungry_chest");
    private static final VoxelShape SHAPE = Block.box(1.0, 0.0, 1.0, 15.0, 14.0, 15.0);

    public HungryChestBlock(BlockBehaviour.Properties props) {
        super(SimpleBlockMaterials.wood(props).strength(2.5F).noOcclusion(), ConfigBlockEntities.HUNGRY_CHEST.entityTypeObject());
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
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
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        }
        player.openMenu(state.getMenuProvider(level, pos));
        return InteractionResult.CONSUME;
    }

    @Override
    protected MenuProvider getMenuProvider(BlockState state, Level level, BlockPos pos) {
        HungryChestBlockEntity entity = getEntity(level, pos);
        return new SimpleMenuProvider((id, inv, player) -> new HungryChestMenu(id, inv, entity.getInventory()), CONTAINER_TITLE);
    }

    @Override
    protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (level.isClientSide || !(entity instanceof ItemEntity itemEntity)) {
            return;
        }
        if (itemEntity.isRemoved()) {
            return;
        }
        HungryChestBlockEntity chestEntity = getEntity(level, pos);
        ItemStack stack = itemEntity.getItem();
        ItemStack leftover = chestEntity.insertItem(stack);

        if (leftover.isEmpty() || leftover.getCount() != stack.getCount()) {
            level.playSound(null, pos, SoundEvents.GENERIC_EAT.value(), SoundSource.BLOCKS, 0.25F,
                    (level.random.nextFloat() - level.random.nextFloat()) * 0.2F + 1.0F);
        }

        if (leftover.isEmpty()) {
            itemEntity.discard();
        } else {
            itemEntity.setItem(leftover);
        }
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock())) {
            if (level.getBlockEntity(pos) instanceof HungryChestBlockEntity entity) {
                Containers.dropContents(level, pos, entity.getInventory());
                level.updateNeighbourForOutputSignal(pos, this);
            }
        }
        super.onRemove(state, level, pos, newState, isMoving);
    }

    @Override
    protected boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    protected int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
        if (level.getBlockEntity(pos) instanceof HungryChestBlockEntity entity) {
            return getRedstoneSignalFromContainer(entity.getInventory());
        }
        return 0;
    }

    private static int getRedstoneSignalFromContainer(net.minecraft.world.Container container) {
        int totalItems = 0;
        float fillLevel = 0.0F;

        for (int i = 0; i < container.getContainerSize(); ++i) {
            ItemStack stack = container.getItem(i);
            if (!stack.isEmpty()) {
                fillLevel += (float) stack.getCount() / (float) Math.min(container.getMaxStackSize(), stack.getMaxStackSize());
                ++totalItems;
            }
        }

        fillLevel = fillLevel / (float) container.getContainerSize();
        return (int) Math.floor(fillLevel * 14.0F) + (totalItems > 0 ? 1 : 0);
    }

    @Override
    protected BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    protected BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }
}
