package art.arcane.thaumcraft.blocks.alchemy;

import art.arcane.thaumcraft.blocks.entities.EssentiaOutputBlockEntity;
import art.arcane.thaumcraft.registries.ConfigBlockEntities;
import art.arcane.thaumcraft.util.simple.SimpleBlockMaterials;
import art.arcane.thaumcraft.util.simple.TickableEntityBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.EnumMap;
import java.util.Map;

public class EssentiaOutputBlock extends TickableEntityBlock<EssentiaOutputBlockEntity> {

    public static final EnumProperty<Direction> FACING = BlockStateProperties.FACING;

    private static final Map<Direction, VoxelShape> SHAPES = createShapes();

    public EssentiaOutputBlock(BlockBehaviour.Properties props) {
        super(SimpleBlockMaterials.metal(props).noOcclusion(), ConfigBlockEntities.ESSENTIA_OUTPUT.entityTypeObject());
        this.registerDefaultState(this.getStateDefinition().any().setValue(FACING, Direction.UP));
    }

    private static Map<Direction, VoxelShape> createShapes() {
        Map<Direction, VoxelShape> shapes = new EnumMap<>(Direction.class);
        shapes.put(Direction.UP, Block.box(4, 0, 4, 12, 8, 12));
        shapes.put(Direction.DOWN, Block.box(4, 8, 4, 12, 16, 12));
        shapes.put(Direction.NORTH, Block.box(4, 4, 8, 12, 12, 16));
        shapes.put(Direction.SOUTH, Block.box(4, 4, 0, 12, 12, 8));
        shapes.put(Direction.WEST, Block.box(8, 4, 4, 16, 12, 12));
        shapes.put(Direction.EAST, Block.box(0, 4, 4, 8, 12, 12));
        return shapes;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getClickedFace());
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
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPES.get(state.getValue(FACING));
    }
}
