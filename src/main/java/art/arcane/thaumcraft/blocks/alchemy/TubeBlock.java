package art.arcane.thaumcraft.blocks.alchemy;

import art.arcane.thaumcraft.api.aspects.Aspect;
import art.arcane.thaumcraft.api.aspects.AspectContainerItem;
import art.arcane.thaumcraft.blocks.entities.TubeBufferBlockEntity;
import art.arcane.thaumcraft.blocks.entities.TubeFilterBlockEntity;
import com.google.common.collect.ImmutableMap;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.redstone.Orientation;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import art.arcane.thaumcraft.blocks.entities.TubeBlockEntity;
import art.arcane.thaumcraft.blocks.entities.TubeValveBlockEntity;
import art.arcane.thaumcraft.registries.ConfigCapabilities;
import art.arcane.thaumcraft.registries.ConfigItems;
import art.arcane.thaumcraft.registries.ConfigSounds;
import art.arcane.thaumcraft.util.simple.SimpleBlockMaterials;
import art.arcane.thaumcraft.util.simple.TickableBlockEntity;

import java.util.EnumMap;
import java.util.List;
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
        super(SimpleBlockMaterials.metal(props).noOcclusion());

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

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return (l, p, s, be) -> {
            if (be instanceof TickableBlockEntity tickable) {
                if (l.isClientSide() && tickable.getTickSetting().isTickClient()) {
                    tickable.onClientTick();
                }
                if (!l.isClientSide() && tickable.getTickSetting().isTickServer()) {
                    tickable.onServerTick();
                }
            }
        };
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
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(level, pos, state, placer, stack);
        if (!level.isClientSide() && placer != null && level.getBlockEntity(pos) instanceof TubeBlockEntity tube && tube.supportsFacingControl()) {
            tube.setFacing(placer.getDirection().getOpposite());
            tube.sync();
            level.setBlock(pos, determineConnections(level, state, pos), 1 | 2);
        }
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

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        // Tube configuration is gauntlet-driven for TC6-style behavior.
        return InteractionResult.PASS;
    }

    @Override
    protected InteractionResult useItemOn(ItemStack pStack, BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHitResult) {
        if (pStack.is(ConfigItems.GAUNTLET.value())) {
            return configureWithGauntlet(pLevel, pPos, pPlayer, pHitResult.getDirection(), pHitResult.getLocation());
        }
        if (pHand != InteractionHand.MAIN_HAND) {
            return InteractionResult.PASS;
        }
        if (pLevel.getBlockEntity(pPos) instanceof TubeFilterBlockEntity filter
                && filter.getFilterAspect() == null
                && pStack.getItem() instanceof AspectContainerItem aspectContainerItem) {
            List<ResourceKey<Aspect>> aspects = aspectContainerItem.getAspects(pStack).aspectsPresent();
            if (!aspects.isEmpty()) {
                if (!pLevel.isClientSide()) {
                    filter.setFilterAspect(aspects.get(0));
                    pLevel.playSound(null, pPos, ConfigSounds.KNOB_TWISTING.value(), SoundSource.BLOCKS, 0.7F, 1.2F + pLevel.random.nextFloat() * 0.2F);
                }
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.PASS;
    }

    public InteractionResult configureWithGauntlet(Level level, BlockPos pos, Player player, Direction clickedFace, Vec3 clickLocation) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (!(blockEntity instanceof TubeBlockEntity tube)) {
            return InteractionResult.PASS;
        }

        Direction side = resolveConfiguredSide(pos, clickedFace, clickLocation);

        if (!level.isClientSide()) {
            // Valve behavior: normal use toggles flow; sneak uses side editing.
            if (blockEntity instanceof TubeValveBlockEntity valve && !player.isCrouching()) {
                valve.toggleFlowAllowed();
                level.playSound(null, pos, ConfigSounds.KNOB_TWISTING.value(), SoundSource.BLOCKS, 0.7F, 0.9F + level.random.nextFloat() * 0.2F);
                player.displayClientMessage(
                        Component.translatable(valve.isFlowAllowed() ? "msg.thaumcraft.gauntlet.valve.opened" : "msg.thaumcraft.gauntlet.valve.closed"),
                        true
                );
                return InteractionResult.SUCCESS;
            }

            // Buffer behavior: sneak cycles per-side choke (open/half/closed).
            if (blockEntity instanceof TubeBufferBlockEntity buffer && player.isCrouching()) {
                buffer.cycleChoke(side);
                level.playSound(null, pos, ConfigSounds.KNOB_TWISTING.value(), SoundSource.BLOCKS, 0.7F, 1.05F + level.random.nextFloat() * 0.15F);
                player.displayClientMessage(
                        Component.translatable(
                                "msg.thaumcraft.gauntlet.choke",
                                Component.translatable("direction.minecraft." + side.getSerializedName()),
                                Component.translatable(switch (buffer.getChoke(side)) {
                                    case 1 -> "msg.thaumcraft.gauntlet.choke.half";
                                    case 2 -> "msg.thaumcraft.gauntlet.choke.closed";
                                    default -> "msg.thaumcraft.gauntlet.choke.open";
                                })),
                        true
                );
                return InteractionResult.SUCCESS;
            }

            // Directional behavior (e.g. one-way): sneak cycles facing.
            if (!(blockEntity instanceof TubeValveBlockEntity) && !(blockEntity instanceof TubeBufferBlockEntity)
                    && tube.supportsFacingControl() && player.isCrouching()) {
                tube.cycleFacing();
                level.playSound(null, pos, ConfigSounds.KNOB_TWISTING.value(), SoundSource.BLOCKS, 0.7F, 1.0F + level.random.nextFloat() * 0.2F);
                player.displayClientMessage(
                        Component.translatable("msg.thaumcraft.gauntlet.facing", Component.translatable("direction.minecraft." + tube.getFacing().getSerializedName())),
                        true
                );
                return InteractionResult.SUCCESS;
            }

            if (blockEntity instanceof TubeFilterBlockEntity filter
                    && filter.getFilterAspect() != null
                    && !pStateHasConnection(level, pos, side)) {
                filter.clearFilterAspect();
            }

            tube.toggleSide(side);
            level.playSound(null, pos, ConfigSounds.KNOB_TWISTING.value(), SoundSource.BLOCKS, 0.7F, 0.95F + level.random.nextFloat() * 0.15F);
            player.displayClientMessage(
                    Component.translatable(
                            tube.isConnectable(side) ? "msg.thaumcraft.gauntlet.side.opened" : "msg.thaumcraft.gauntlet.side.closed",
                            Component.translatable("direction.minecraft." + side.getSerializedName())),
                    true
            );
        }
        return InteractionResult.SUCCESS;
    }

    private boolean pStateHasConnection(Level level, BlockPos pos, Direction side) {
        BlockState state = level.getBlockState(pos);
        return state.hasProperty(BY_DIRECTION.get(side)) && state.getValue(BY_DIRECTION.get(side));
    }

    private Direction resolveConfiguredSide(BlockPos pos, Direction clickedFace, Vec3 clickLocation) {
        Direction fallback = clickedFace == null ? Direction.UP : clickedFace;
        if (clickLocation == null) {
            return fallback;
        }

        double dx = clickLocation.x - (pos.getX() + 0.5);
        double dy = clickLocation.y - (pos.getY() + 0.5);
        double dz = clickLocation.z - (pos.getZ() + 0.5);
        double ax = Math.abs(dx);
        double ay = Math.abs(dy);
        double az = Math.abs(dz);

        // Inside the center core, use the looked-at face.
        if (ax < 0.15625 && ay < 0.15625 && az < 0.15625) {
            return fallback;
        }
        if (ax >= ay && ax >= az) {
            return dx >= 0 ? Direction.EAST : Direction.WEST;
        }
        if (ay >= ax && ay >= az) {
            return dy >= 0 ? Direction.UP : Direction.DOWN;
        }
        return dz >= 0 ? Direction.SOUTH : Direction.NORTH;
    }

    public BlockState determineConnections(Level level, BlockState state, BlockPos pos) {
        TubeBlockEntity tube = level.getBlockEntity(pos) instanceof TubeBlockEntity tubeEntity ? tubeEntity : null;
        for(Direction dir : Direction.values()) {
            boolean open = tube == null || tube.isConnectable(dir);
            boolean connected = open && level.getCapability(ConfigCapabilities.ESSENTIA, pos.offset(dir.getUnitVec3i()), dir.getOpposite()) != null;
            state = state.setValue(BY_DIRECTION.get(dir), connected);
        }
        return state;
    }
}
