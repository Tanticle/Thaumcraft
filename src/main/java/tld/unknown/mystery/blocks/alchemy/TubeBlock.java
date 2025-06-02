package tld.unknown.mystery.blocks.alchemy;

import com.google.common.collect.ImmutableMap;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.redstone.Orientation;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import tld.unknown.mystery.blocks.entities.TubeBlockEntity;
import tld.unknown.mystery.registries.ConfigCapabilities;
import tld.unknown.mystery.util.simple.SimpleBlockMaterials;

import java.util.EnumMap;
import java.util.Map;

public class TubeBlock extends DirectionalBlock implements EntityBlock {

    public static final BooleanProperty NORTH = BooleanProperty.create("north");
    public static final BooleanProperty EAST = BooleanProperty.create("east");
    public static final BooleanProperty SOUTH = BooleanProperty.create("south");
    public static final BooleanProperty WEST = BooleanProperty.create("west");
    public static final BooleanProperty UP = BooleanProperty.create("up");
    public static final BooleanProperty DOWN = BooleanProperty.create("down");

    public static final Map<Direction, BooleanProperty> BY_DIRECTION = new EnumMap<>(ImmutableMap.of(
            Direction.NORTH, NORTH,
            Direction.EAST, EAST,
            Direction.SOUTH, SOUTH,
            Direction.WEST, WEST,
            Direction.UP, UP,
            Direction.DOWN, DOWN));

    private static final MapCodec<TubeBlock> CODEC = simpleCodec(TubeBlock::new);

    public TubeBlock(BlockBehaviour.Properties props) {
        super(SimpleBlockMaterials.metal(props));

        registerDefaultState(this.getStateDefinition().any()
                .setValue(NORTH, false)
                .setValue(EAST, false)
                .setValue(SOUTH, false)
                .setValue(WEST, false)
                .setValue(UP, false)
                .setValue(DOWN, false));
    }

    @Override
    protected MapCodec<? extends DirectionalBlock> codec() {
        return CODEC;
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return box(
            pState.getValue(WEST) ? 0 : 5, pState.getValue(DOWN) ? 0 : 5, pState.getValue(NORTH) ? 0 : 5,
            pState.getValue(EAST) ? 16 : 11, pState.getValue(UP) ? 16 : 11, pState.getValue(SOUTH) ? 16 : 11);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(NORTH, EAST, SOUTH, WEST, UP, DOWN);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new TubeBlockEntity(pPos, pState);
    }

    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return determineConnections(pContext.getLevel(), this.defaultBlockState(), pContext.getClickedPos());
    }

    @Override
    public void onPlace(BlockState pState, Level pLevel, BlockPos pPos, BlockState pOldState, boolean pIsMoving) {
        if (!pOldState.is(pState.getBlock()) && !pLevel.isClientSide)
            for(Direction dir : Direction.values())
                pLevel.updateNeighborsAt(pPos.relative(dir), this);
    }

    @Override
    protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock, @Nullable Orientation orientation, boolean movedByPiston) {
        level.setBlock(pos, determineConnections(level, state, pos), 1 | 2);
    }

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        if (!pIsMoving && !pState.is(pNewState.getBlock())) {
            super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
            if (!pLevel.isClientSide)
                for(Direction dir : Direction.values())
                    pLevel.updateNeighborsAt(pPos.relative(dir), this);
        }
    }

    private BlockState determineConnections(Level level, BlockState state, BlockPos pos) {
        for(Direction dir : Direction.values())
            state = state.setValue(BY_DIRECTION.get(dir), level.getCapability(ConfigCapabilities.ESSENTIA, pos.offset(dir.getUnitVec3i()), dir.getOpposite()) != null);
        return state;
    }
}
