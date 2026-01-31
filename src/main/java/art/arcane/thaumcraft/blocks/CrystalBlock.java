package art.arcane.thaumcraft.blocks;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import art.arcane.thaumcraft.api.ThaumcraftData;
import art.arcane.thaumcraft.api.aspects.Aspect;
import art.arcane.thaumcraft.registries.ConfigDataRegistries;
import art.arcane.thaumcraft.util.simple.SimpleBlockMaterials;

//TODO: Crystal material
public class CrystalBlock extends DirectionalBlock {

    public static final IntegerProperty SIZE = IntegerProperty.create("size", 0, 2);

    private static final VoxelShape COLLISION = Block.box(2.0D, 0.0D, 2.0D, 14.0D, 12.0D, 14.0D);
    private static final MapCodec<CrystalBlock> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
            propertiesCodec(),
            StringRepresentable.fromValues(CrystalAspect::values).fieldOf("aspect").forGetter(b -> b.aspect)
    ).apply(i, CrystalBlock::new));

    private final CrystalBlock.CrystalAspect aspect;

    public CrystalBlock(CrystalBlock.CrystalAspect aspect, BlockBehaviour.Properties props) {
        this(SimpleBlockMaterials.glass(props).mapColor(aspect.color), aspect);
    }

    public Aspect getAspect(RegistryAccess access) {
        return ConfigDataRegistries.ASPECTS.get(access, this.aspect.getId());
    }

    public CrystalBlock(BlockBehaviour.Properties properties, CrystalAspect aspect) {
        super(properties);
        this.aspect = aspect;
        registerDefaultState(this.getStateDefinition().any()
                .setValue(FACING, Direction.UP)
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
        pBuilder.add(FACING, SIZE);
    }

    @Override
    protected MapCodec<? extends DirectionalBlock> codec() {
        return CODEC;
    }

    @AllArgsConstructor
    public enum CrystalAspect implements StringRepresentable {
        ORDER(ThaumcraftData.Aspects.ORDER, MapColor.COLOR_LIGHT_GRAY),
        DESTRUCTION(ThaumcraftData.Aspects.CHAOS, MapColor.COLOR_GRAY),
        EARTH(ThaumcraftData.Aspects.EARTH, MapColor.COLOR_GREEN),
        WATER(ThaumcraftData.Aspects.WATER, MapColor.COLOR_CYAN),
        AIR(ThaumcraftData.Aspects.AIR, MapColor.COLOR_YELLOW),
        FIRE(ThaumcraftData.Aspects.FIRE, MapColor.COLOR_RED),
        TAINT(ThaumcraftData.Aspects.TAINT, MapColor.COLOR_PURPLE);

        @Getter
        private final ResourceKey<Aspect> id;
        private final MapColor color;

        @Override
        public String getSerializedName() {
            return id.location().getPath();
        }
    }
}
