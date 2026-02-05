package art.arcane.thaumcraft.blocks;

import art.arcane.thaumcraft.blocks.entities.NitorBlockEntity;
import art.arcane.thaumcraft.registries.ConfigBlockEntities;
import art.arcane.thaumcraft.registries.ConfigItemComponents;
import art.arcane.thaumcraft.util.simple.TickableEntityBlock;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class NitorBlock extends TickableEntityBlock<NitorBlockEntity> {

    private static final VoxelShape SHAPE = Block.box(5.28, 5.28, 5.28, 10.72, 10.72, 10.72);
    private static final MapCodec<NitorBlock> CODEC = simpleCodec(NitorBlock::new);

    public static final EnumProperty<DyeColor> COLOR = EnumProperty.create("color", DyeColor.class);

    public NitorBlock(BlockBehaviour.Properties props) {
        super(props.strength(0.1f)
                .sound(SoundType.WOOL)
                .lightLevel(bs -> 15)
                .noOcclusion()
                .noCollission()
                .mapColor(state -> state.getValue(COLOR).getMapColor()), ConfigBlockEntities.NITOR::entityType);
        registerDefaultState(stateDefinition.any().setValue(COLOR, DyeColor.YELLOW));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(COLOR);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return Shapes.empty();
    }

    @Override
    public VoxelShape getBlockSupportShape(BlockState state, BlockGetter level, BlockPos pos) {
        return Shapes.empty();
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.INVISIBLE;
    }

    @Override
    public boolean isCollisionShapeFullBlock(BlockState state, BlockGetter level, BlockPos pos) {
        return false;
    }

    @Override
    public ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state, boolean includeData, Player player) {
        ItemStack stack = new ItemStack(this);
        stack.set(ConfigItemComponents.DYE_COLOR.value(), state.getValue(COLOR));
        return stack;
    }

    @Override
    protected MapCodec<? extends NitorBlock> codec() {
        return CODEC;
    }
}
