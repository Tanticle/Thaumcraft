package tld.unknown.mystery.blocks;

import com.mojang.serialization.MapCodec;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import tld.unknown.mystery.api.ChaumtraftIDs;
import tld.unknown.mystery.util.simple.SimpleBlockMaterials;

import java.util.Arrays;
import java.util.Optional;

//TODO: Crystal material
public class CrystalBlock extends DirectionalBlock {

    public static final EnumProperty<CrystalAspect> ASPECT = EnumProperty.create("aspect", CrystalAspect.class);
    public static final IntegerProperty SIZE = IntegerProperty.create("size", 0, 2);

    private static final VoxelShape COLLISION = Block.box(2.0D, 0.0D, 2.0D, 14.0D, 12.0D, 14.0D);
    private static final MapCodec<CrystalBlock> CODEC = simpleCodec(CrystalBlock::new);

    public CrystalBlock() {
        this(SimpleBlockMaterials.GLASS);
    }

    public CrystalBlock(BlockBehaviour.Properties properties) {
        super(properties);
        registerDefaultState(this.getStateDefinition().any()
                .setValue(FACING, Direction.UP)
                .setValue(ASPECT, CrystalAspect.ORDER)
                .setValue(SIZE, 0));
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return COLLISION;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(FACING, ASPECT, SIZE);
    }

    @Override
    protected MapCodec<? extends DirectionalBlock> codec() {
        return CODEC;
    }

    @AllArgsConstructor
    public enum CrystalAspect implements StringRepresentable {
        ORDER(ChaumtraftIDs.Aspects.ORDER, MapColor.COLOR_LIGHT_GRAY),
        DESTRUCTION(ChaumtraftIDs.Aspects.CHAOS, MapColor.COLOR_GRAY),
        EARTH(ChaumtraftIDs.Aspects.EARTH, MapColor.COLOR_GREEN),
        WATER(ChaumtraftIDs.Aspects.WATER, MapColor.COLOR_CYAN),
        AIR(ChaumtraftIDs.Aspects.AIR, MapColor.COLOR_YELLOW),
        FIRE(ChaumtraftIDs.Aspects.FIRE, MapColor.COLOR_RED),
        TAINT(ChaumtraftIDs.Aspects.TAINT, MapColor.COLOR_PURPLE);

        @Getter
        private final ResourceLocation id;
        private final MapColor color;

        @Override
        public String getSerializedName() {
            return id.getPath();
        }

        public static Optional<CrystalAspect> getFromId(ResourceLocation id) {
            return Arrays.stream(values()).filter(a -> a.id == id).findFirst();
        }
    }
}
