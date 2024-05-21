package tld.unknown.mystery.data.generator.providers;

import com.google.common.collect.ImmutableMap;
import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.data.models.model.TextureMapping;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.neoforged.neoforge.client.model.generators.*;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredBlock;
import tld.unknown.mystery.Thaumcraft;
import tld.unknown.mystery.api.ThaumcraftData;
import tld.unknown.mystery.blocks.CreativeAspectSourceBlock;
import tld.unknown.mystery.blocks.CrystalBlock;
import tld.unknown.mystery.blocks.JarBlock;
import tld.unknown.mystery.blocks.TubeBlock;
import tld.unknown.mystery.registries.ConfigBlocks;

import java.util.EnumMap;
import java.util.Map;

public class BlockDataProvider extends BlockStateProvider {

    public BlockDataProvider(PackOutput gen, ExistingFileHelper exFileHelper) {
        super(gen, Thaumcraft.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        simpleBlock(ConfigBlocks.CRUCIBLE.block(), models().getExistingFile(Thaumcraft.id("block/crucible")));
        simpleBlockItem(ConfigBlocks.CRUCIBLE.block(), models().getExistingFile(Thaumcraft.id("block/crucible")));
        simpleBlock(ConfigBlocks.ARCANE_WORKBENCH.block(), models().getExistingFile(Thaumcraft.id("block/arcane_workbench")));
        simpleBlockItem(ConfigBlocks.ARCANE_WORKBENCH.block(), models().getExistingFile(Thaumcraft.id("block/arcane_workbench")));

        simpleBlock(ConfigBlocks.ARCANE_PEDESTAL.block(), models().getExistingFile(Thaumcraft.id("block/pedestal_arcane")));
        simpleBlockItem(ConfigBlocks.ARCANE_PEDESTAL.block(), models().getExistingFile(Thaumcraft.id("block/pedestal_arcane")));
        simpleBlock(ConfigBlocks.ANCIENT_PEDESTAL.block(), models().getExistingFile(Thaumcraft.id("block/pedestal_ancient")));
        simpleBlockItem(ConfigBlocks.ANCIENT_PEDESTAL.block(), models().getExistingFile(Thaumcraft.id("block/pedestal_ancient")));
        simpleBlock(ConfigBlocks.ELDRITCH_PEDESTAL.block(), models().getExistingFile(Thaumcraft.id("block/pedestal_eldritch")));
        simpleBlockItem(ConfigBlocks.ELDRITCH_PEDESTAL.block(), models().getExistingFile(Thaumcraft.id("block/pedestal_eldritch")));

        registerDirectionalMultipart(ConfigBlocks.TUBE.block(), TubeBlock.BY_DIRECTION, Thaumcraft.id("block/tube_generic_center"), Thaumcraft.id("block/tube_generic_side"), true);

        generateAspectSource();
        simpleBlockItem(ConfigBlocks.CREATIVE_ASPECT_SOURCE.block(), models().getExistingFile(Thaumcraft.id("block/creative_aspect_source_empty")));

        generateJar(ConfigBlocks.WARDED_JAR.block());
        generateJar(ConfigBlocks.VOID_JAR.block());

        ModelFile CRYSTAL_0 = models().getExistingFile(Thaumcraft.id("block/vis_crystals_ground_stage0"));
        ModelFile CRYSTAL_1 = models().getExistingFile(Thaumcraft.id("block/vis_crystals_ground_stage1"));
        ModelFile CRYSTAL_2 = models().getExistingFile(Thaumcraft.id("block/vis_crystals_ground_stage2"));

        getVariantBuilder(ConfigBlocks.CRYSTAL_COLONY.block()).forAllStates((bs) -> getCrystalModels(bs.getValue(CrystalBlock.FACING), switch(bs.getValue(CrystalBlock.SIZE)) {
            case 1 -> CRYSTAL_1;
            case 2 -> CRYSTAL_2;
            default -> CRYSTAL_0;
        }));

    }

    private void generateAspectSource() {
        ResourceLocation filled = TextureMapping.getBlockTexture(ConfigBlocks.CREATIVE_ASPECT_SOURCE.block());
        ResourceLocation top = TextureMapping.getBlockTexture(ConfigBlocks.CREATIVE_ASPECT_SOURCE.block(), "_top");
        getVariantBuilder(ConfigBlocks.CREATIVE_ASPECT_SOURCE.block()).forAllStates(s -> {
            if(s.getValue(CreativeAspectSourceBlock.HAS_ASPECT)) {
                return ConfiguredModel.builder().modelFile(models().cubeTop(ThaumcraftData.Blocks.CREATIVE_ASPECT_SOURCE + "_filled", filled, top)).build();
            } else {
                return ConfiguredModel.builder().modelFile(models().cubeTop(ThaumcraftData.Blocks.CREATIVE_ASPECT_SOURCE + "_empty", top, top)).build();
            }
        });
    }

    private void generateJar(JarBlock block) {
        MultiPartBlockStateBuilder builder = getMultipartBuilder(block);
        ResourceLocation base = block.isVoid() ? Thaumcraft.id("block/void_jar_body") : Thaumcraft.id("block/warded_jar_body");
        ResourceLocation brace = Thaumcraft.id("block/jar_brace");
        ResourceLocation tube = Thaumcraft.id("block/jar_tube");

        builder.part().modelFile(models().getExistingFile(base)).addModel().end();
        builder.part().modelFile(models().getExistingFile(brace)).addModel().condition(JarBlock.BRACED, true).end();
        builder.part().modelFile(models().getExistingFile(tube)).addModel().condition(JarBlock.CONNECTED, true).end();

        simpleBlockItem(block, models().getExistingFile(base));
    }

    private ConfiguredModel[] getCrystalModels(Direction dir, ModelFile model) {
        ConfiguredModel.Builder<?> builder = ConfiguredModel.builder().modelFile(model);
        Direction.Axis axis = dir.getAxis();
        switch(axis) {
            case X -> builder.rotationX(90).rotationY(dir.getAxisDirection() == Direction.AxisDirection.POSITIVE ? 90 : -90);
            case Y -> builder.rotationX(dir.getAxisDirection() == Direction.AxisDirection.POSITIVE ? 180 : 0);
            case Z -> builder.rotationX(dir.getAxisDirection() == Direction.AxisDirection.POSITIVE ? -90 : 90);
        }
        return builder.build();
    }

    private void registerSimpleBlock(DeferredBlock<? extends Block> block) {
        simpleBlock(block.value());
        simpleBlockItem(block.value(), itemModels().getExistingFile(new ResourceLocation(block.getId().getNamespace(), "block/" + block.getId().getPath())));
    }

    private void registerStairSlab(DeferredBlock<? extends StairBlock> stairs, DeferredBlock<? extends SlabBlock> slab, ResourceLocation texture) {
        texture = new ResourceLocation(texture.getNamespace(), "block/" + texture.getPath());
        stairsBlock(stairs.get(), texture);
        simpleBlockItem(stairs.get(), itemModels().getExistingFile(new ResourceLocation(stairs.getId().getNamespace(), "block/" + stairs.getId().getPath())));
        slabBlock(slab.get(), texture, texture);
        simpleBlockItem(slab.get(), itemModels().getExistingFile(new ResourceLocation(slab.getId().getNamespace(), "block/" + slab.getId().getPath())));
    }

    public ItemModelBuilder basicBlockItem(ResourceLocation item) {
        return itemModels().getBuilder(item.toString())
                .parent(new ModelFile.UncheckedModelFile("item/generated"))
                .texture("layer0", new ResourceLocation(item.getNamespace(), "block/" + item.getPath()));
    }

    private void registerDirectionalMultipart(Block block, Map<Direction, BooleanProperty> dirProperties, ResourceLocation centerPart, ResourceLocation sidePart, boolean hideCenter) {
        MultiPartBlockStateBuilder multipartBuilder = getMultipartBuilder(block);

        for(Direction dir : Direction.values()) {
            MultiPartRotation rot = MultiPartRotation.BY_DIRECTION.get(dir);
            multipartBuilder.part()
                    .modelFile(models().getExistingFile(sidePart))
                    .rotationX(rot.xRotation()).rotationY(rot.yRotation())
                    .addModel()
                    .condition(dirProperties.get(dir), true)
                    .end();
        }

        MultiPartBlockStateBuilder.PartBuilder builder = multipartBuilder.part()
                .modelFile(models().getExistingFile(centerPart))
                .addModel();
        if(hideCenter) {
            builder.useOr();
            for(Direction dir : Direction.values()) {
                builder.condition(dirProperties.get(dir), false);
            }
        }
        builder.end();
    }

    private record MultiPartRotation(int xRotation, int yRotation) {
        public static final Map<Direction, MultiPartRotation> BY_DIRECTION = new EnumMap<>(ImmutableMap.of(
                Direction.NORTH, new MultiPartRotation(-90, 0),
                Direction.EAST, new MultiPartRotation(-90, 90),
                Direction.SOUTH, new MultiPartRotation(90, 0),
                Direction.WEST, new MultiPartRotation(90, 90),
                Direction.UP, new MultiPartRotation(180, 0),
                Direction.DOWN, new MultiPartRotation(0, 0)));
    }
}
