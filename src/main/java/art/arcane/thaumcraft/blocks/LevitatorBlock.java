package art.arcane.thaumcraft.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.redstone.Orientation;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import art.arcane.thaumcraft.api.ThaumcraftData;
import art.arcane.thaumcraft.blocks.entities.LevitatorBlockEntity;
import art.arcane.thaumcraft.config.ThaumcraftConfig;
import art.arcane.thaumcraft.registries.ConfigBlockEntities;
import art.arcane.thaumcraft.util.simple.SimpleBlockMaterials;
import art.arcane.thaumcraft.util.simple.TickableEntityBlock;

import java.util.EnumMap;
import java.util.Map;

public class LevitatorBlock extends TickableEntityBlock<LevitatorBlockEntity> {

    public static final EnumProperty<Direction> FACING = BlockStateProperties.FACING;
    public static final BooleanProperty ENABLED = BlockStateProperties.ENABLED;

    private static final Map<Direction, VoxelShape> SHAPES = createShapes();

    public LevitatorBlock(BlockBehaviour.Properties props) {
        super(SimpleBlockMaterials.metal(props), ConfigBlockEntities.LEVITATOR.entityTypeObject());
        registerDefaultState(getStateDefinition().any()
                .setValue(FACING, Direction.UP)
                .setValue(ENABLED, true));
    }

    private static Map<Direction, VoxelShape> createShapes() {
        Map<Direction, VoxelShape> shapes = new EnumMap<>(Direction.class);
        VoxelShape base = box(2, 0, 2, 14, 14, 14);
        VoxelShape nozzleUp = box(6, 14, 6, 10, 16, 10);
        VoxelShape nozzleDown = box(6, 0, 6, 10, 2, 10);
        VoxelShape nozzleNorth = box(6, 6, 0, 10, 10, 2);
        VoxelShape nozzleSouth = box(6, 6, 14, 10, 10, 16);
        VoxelShape nozzleEast = box(14, 6, 6, 16, 10, 10);
        VoxelShape nozzleWest = box(0, 6, 6, 2, 10, 10);

        shapes.put(Direction.UP, Shapes.or(box(2, 0, 2, 14, 14, 14), nozzleUp));
        shapes.put(Direction.DOWN, Shapes.or(box(2, 2, 2, 14, 16, 14), nozzleDown));
        shapes.put(Direction.NORTH, Shapes.or(box(2, 2, 2, 14, 14, 16), nozzleNorth));
        shapes.put(Direction.SOUTH, Shapes.or(box(2, 2, 0, 14, 14, 14), nozzleSouth));
        shapes.put(Direction.EAST, Shapes.or(box(0, 2, 2, 14, 14, 14), nozzleEast));
        shapes.put(Direction.WEST, Shapes.or(box(2, 2, 2, 16, 14, 14), nozzleWest));

        return shapes;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, ENABLED);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return defaultBlockState().setValue(FACING, context.getNearestLookingDirection().getOpposite());
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPES.get(state.getValue(FACING));
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (!level.isClientSide()) {
            LevitatorBlockEntity entity = getEntity(level, pos);
            entity.cycleRange();
            int range = entity.getCurrentRange();
            float cost = entity.getVisCost();
            player.displayClientMessage(Component.translatable("tc.levitator", range, String.format("%.2f", cost)), true);
            level.playSound(null, pos, ThaumcraftData.Sounds.KNOB_TWISTING, net.minecraft.sounds.SoundSource.BLOCKS, 1.0F, 1.0F);
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock, @Nullable Orientation orientation, boolean movedByPiston) {
        if (!level.isClientSide()) {
            boolean powered = level.hasNeighborSignal(pos);
            boolean enabled = state.getValue(ENABLED);
            if (powered == enabled) {
                level.setBlock(pos, state.setValue(ENABLED, !powered), 2);
            }
        }
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
        LevitatorBlockEntity entity = getEntity(level, pos);
        float maxVis = ThaumcraftConfig.LEVITATOR_MAX_VIS.get().floatValue();
        return Math.min(15, (int) (entity.getVis() / maxVis * 15F));
    }
}
