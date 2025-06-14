package tld.unknown.mystery.blocks;

import com.mojang.serialization.MapCodec;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.client.data.models.blockstates.VariantProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import tld.unknown.mystery.util.simple.SimpleBlockMaterials;

public class InfusionPillarBlock extends Block {

    public static final EnumProperty<PillarDirection> POINTING = EnumProperty.create("pointing", PillarDirection.class);

    private static final MapCodec<InfusionPillarBlock> CODEC = simpleCodec(InfusionPillarBlock::new);

    public InfusionPillarBlock(Properties props) {
        super(SimpleBlockMaterials.stone(props));
        registerDefaultState(getStateDefinition().any().setValue(POINTING, PillarDirection.NORTH_EAST));
    }

    @Override
    protected MapCodec<? extends Block> codec() {
        return CODEC;
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return box(0, 0, 0, 16, 32, 16);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(POINTING);
    }

    @Override
    public ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state, boolean includeData, Player player) {
        return ItemStack.EMPTY;
    }

    @Getter
    @AllArgsConstructor
    public enum PillarDirection implements StringRepresentable {
        SOUTH_EAST("south_east", VariantProperties.Rotation.R0),
        NORTH_EAST("north_east", VariantProperties.Rotation.R90),
        NORTH_WEST("north_west", VariantProperties.Rotation.R180),
        SOUTH_WEST("south_west", VariantProperties.Rotation.R270);

        private final String id;
        private final VariantProperties.Rotation blockRotation;

        @Override
        public String getSerializedName() {
            return this.id;
        }
    }
}
