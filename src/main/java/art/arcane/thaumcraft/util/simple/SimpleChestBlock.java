package art.arcane.thaumcraft.util.simple;

import art.arcane.thaumcraft.util.better.BetterLidBlockEntity;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.stats.Stat;
import net.minecraft.stats.Stats;
import net.minecraft.util.RandomSource;
import net.minecraft.world.*;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Supplier;

public class SimpleChestBlock<B extends BaseContainerBlockEntity & BetterLidBlockEntity> extends SimpleEntityBlock<B> implements SimpleWaterloggedBlock {

	public static final EnumProperty<Direction> FACING = HorizontalDirectionalBlock.FACING;
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

	protected static final VoxelShape AABB = Block.box(1.0, 0.0, 1.0, 15.0, 14.0, 15.0);

	private final Supplier<BlockEntityType<B>> blockEntityType;

	public SimpleChestBlock(Supplier<BlockEntityType<B>> blockEntityType, BlockBehaviour.Properties properties) {
		super(properties, blockEntityType);
		this.blockEntityType = blockEntityType;
		this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(WATERLOGGED, Boolean.valueOf(false)));
	}

	@Override
	protected MapCodec<? extends Block> codec() {
		return simpleCodec(p -> new SimpleChestBlock<>(blockEntityType, p));
	}

	@Override
	protected BlockState updateShape(
			BlockState state,
			LevelReader level,
			ScheduledTickAccess scheduledTickAccess,
			BlockPos pos,
			Direction direction,
			BlockPos neighborPos,
			BlockState neighborState,
			RandomSource random
	) {
		if (state.getValue(WATERLOGGED)) {
			scheduledTickAccess.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
		}

		return state;
	}

	@Override
	protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return AABB;
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		Direction direction = context.getHorizontalDirection().getOpposite();
		FluidState fluidstate = context.getLevel().getFluidState(context.getClickedPos());

		return this.defaultBlockState()
				.setValue(FACING, direction)
				.setValue(WATERLOGGED, Boolean.valueOf(fluidstate.getType() == Fluids.WATER));
	}

	@Override
	protected FluidState getFluidState(BlockState state) {
		return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
	}

	@Override
	protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
		Containers.dropContentsOnDestroy(state, newState, level, pos);
		super.onRemove(state, level, pos, newState, isMoving);
	}

	@Override
	protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult blockHitResult) {
		if (level instanceof ServerLevel serverlevel) {
			if(isChestBlockedAt(serverlevel, pos.above()))
				return InteractionResult.CONSUME;
			MenuProvider menuprovider = this.getMenuProvider(state, level, pos);
			if (menuprovider != null) {
				player.openMenu(menuprovider);
				player.awardStat(this.getOpenChestStat());
			}
		}

		return InteractionResult.SUCCESS;
	}

	protected Stat<ResourceLocation> getOpenChestStat() {
		return Stats.CUSTOM.get(Stats.OPEN_CHEST);
	}

	public BlockEntityType<B> blockEntityType() {
		return this.blockEntityType.get();
	}

	@Nullable
	@Override
	protected MenuProvider getMenuProvider(BlockState state, Level level, BlockPos pos) {
		return new MenuProvider() {
			@Override
			public Component getDisplayName() {
				return state.getBlock().getName();
			}

			@Override
			public @org.jetbrains.annotations.Nullable AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
				return getEntity(level, pos).createMenu(containerId, playerInventory, player);
			}
		};
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
		if(!level.isClientSide || !blockEntityType.equals(this.blockEntityType()))
			return null;
		return (l, pos, st, be) -> getEntity(l, pos).lidAnimateTick(l, pos, st);
	}

	public static boolean isChestBlockedAt(LevelAccessor level, BlockPos pos) {
		return isBlockedChestByBlock(level, pos) || isCatSittingOnChest(level, pos);
	}

	private static boolean isBlockedChestByBlock(BlockGetter level, BlockPos pos) {
		BlockPos blockpos = pos.above();
		return level.getBlockState(blockpos).isRedstoneConductor(level, blockpos);
	}

	private static boolean isCatSittingOnChest(LevelAccessor level, BlockPos pos) {
		List<Cat> list = level.getEntitiesOfClass(
				Cat.class,
				new AABB(
						pos.getX(),
						pos.getY() + 1,
						pos.getZ(),
						pos.getX() + 1,
						pos.getY() + 2,
						pos.getZ() + 1
				)
		);
		if (!list.isEmpty()) {
			for (Cat cat : list) {
				if (cat.isInSittingPose()) {
					return true;
				}
			}
		}

		return false;
	}

	@Override
	protected boolean hasAnalogOutputSignal(BlockState state) {
		return true;
	}

	@Override
	protected int getAnalogOutputSignal(BlockState blockState, Level level, BlockPos pos) {
		return AbstractContainerMenu.getRedstoneSignalFromContainer(getEntity(level, pos));
	}

	@Override
	protected BlockState rotate(BlockState state, Rotation rotation) {
		return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
	}

	@Override
	protected BlockState mirror(BlockState state, Mirror mirror) {
		return state.rotate(mirror.getRotation(state.getValue(FACING)));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING, WATERLOGGED);
	}

	protected RenderShape getRenderShape(BlockState state) {
		return RenderShape.INVISIBLE;
	}

	@Override
	protected boolean isPathfindable(BlockState p_51522_, PathComputationType p_51525_) {
		return false;
	}

	@Override
	protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
		getEntity(level, pos).recheckOpen();
	}
}
