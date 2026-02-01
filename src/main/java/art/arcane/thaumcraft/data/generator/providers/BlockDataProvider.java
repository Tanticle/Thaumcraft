package art.arcane.thaumcraft.data.generator.providers;

import com.google.common.collect.ImmutableMap;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.blockstates.*;
import net.minecraft.client.data.models.model.*;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.neoforged.neoforge.client.model.generators.loaders.ObjModelBuilder;
import net.neoforged.neoforge.client.model.generators.template.ExtendedModelTemplateBuilder;
import net.neoforged.neoforge.registries.DeferredBlock;
import art.arcane.thaumcraft.Thaumcraft;
import art.arcane.thaumcraft.blocks.CrystalBlock;
import art.arcane.thaumcraft.blocks.InfusionPillarBlock;
import art.arcane.thaumcraft.blocks.alchemy.CreativeAspectSourceBlock;
import art.arcane.thaumcraft.blocks.alchemy.JarBlock;
import art.arcane.thaumcraft.blocks.alchemy.TubeBlock;
import art.arcane.thaumcraft.blocks.DioptraBlock;
import art.arcane.thaumcraft.client.tints.AspectItemTintSource;
import art.arcane.thaumcraft.registries.ConfigBlocks;
import art.arcane.thaumcraft.util.RegistryUtils;

import java.util.*;
import java.util.stream.Stream;

@SuppressWarnings("unchecked")
public class BlockDataProvider extends ModelProvider {

    private BlockModelGenerators blocks;
    private ItemModelGenerators items;

    public BlockDataProvider(PackOutput gen) {
        super(gen, Thaumcraft.MOD_ID);
    }

    @Override
    protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        this.blocks = blockModels;
        this.items = itemModels;

        registerAxisTexturedBlock(ConfigBlocks.ARCANE_STONE, false);
        registerStairAndSlab(ConfigBlocks.ARCANE_STONE, ConfigBlocks.ARCANE_STONE_STAIRS, ConfigBlocks.ARCANE_STONE_SLAB, true);
        simpleBlock(ConfigBlocks.ARCANE_STONE_BRICK);
        registerStairAndSlab(ConfigBlocks.ARCANE_STONE_BRICK, ConfigBlocks.ARCANE_STONE_BRICK_STAIRS, ConfigBlocks.ARCANE_STONE_BRICK_SLAB, false);
        registerAxisTexturedBlock(ConfigBlocks.ANCIENT_STONE, true);
        registerStairAndSlab(ConfigBlocks.ANCIENT_STONE, ConfigBlocks.ANCIENT_STONE_STAIRS, ConfigBlocks.ANCIENT_STONE_SLAB, true);
        simpleBlock(ConfigBlocks.ANCIENT_STONE_TILE);
        registerStairAndSlab(ConfigBlocks.ANCIENT_STONE_TILE, ConfigBlocks.ANCIENT_STONE_TILE_STAIRS, ConfigBlocks.ANCIENT_STONE_TILE_SLAB, false);
        registerAxisTexturedBlock(ConfigBlocks.ELDRITCH_STONE, false);
        registerStairAndSlab(ConfigBlocks.ELDRITCH_STONE, ConfigBlocks.ELDRITCH_STONE_STAIRS, ConfigBlocks.ELDRITCH_STONE_SLAB, true);

        simpleExistingBlock(ConfigBlocks.CRUCIBLE);
        simpleExistingBlock(ConfigBlocks.ARCANE_WORKBENCH);
        batchSimpleExistingBlock(ConfigBlocks.ARCANE_PEDESTAL, ConfigBlocks.ANCIENT_PEDESTAL, ConfigBlocks.ELDRITCH_PEDESTAL);

        registerDirectionalMultipart(ConfigBlocks.TUBE, TubeBlock.BY_DIRECTION, Thaumcraft.id("block/tube_generic_center"), Thaumcraft.id("block/tube_generic_side"), true);

        registerJars();
        registerAspectSource();
        registerInfusionPillar(ConfigBlocks.INFUSION_PILLAR_ARCANE, RegistryUtils.getBlockLocation(ConfigBlocks.ARCANE_STONE.blockSupplier()).withSuffix("_0"));
        registerInfusionPillar(ConfigBlocks.INFUSION_PILLAR_ANCIENT, RegistryUtils.getBlockLocation(ConfigBlocks.ANCIENT_STONE.blockSupplier()).withSuffix("_0"));
        registerInfusionPillar(ConfigBlocks.INFUSION_PILLAR_ELDRITCH, RegistryUtils.getBlockLocation(ConfigBlocks.ELDRITCH_STONE.blockSupplier()).withSuffix("_0"));

        registerCrystalColonies();

        registerEmptyBlock(ConfigBlocks.RUNIC_MATRIX, RegistryUtils.getBlockItemLocation(ConfigBlocks.RUNIC_MATRIX.blockSupplier()));
        registerFakeBlock(ConfigBlocks.LAMPLIGHT);

        simpleBlock(ConfigBlocks.INFUSION_STONE_COST);
        simpleBlock(ConfigBlocks.INFUSION_STONE_SPEED);

        registerDioptra();

        simpleBlock(ConfigBlocks.ORE_AMBER);
        simpleBlock(ConfigBlocks.ORE_CINNABAR);
        simpleBlock(ConfigBlocks.ORE_QUARTZ);
        simpleBlockWithTexture(ConfigBlocks.DEEPSLATE_ORE_AMBER, Thaumcraft.id("block/ore_amber"));
        simpleBlockWithTexture(ConfigBlocks.DEEPSLATE_ORE_CINNABAR, Thaumcraft.id("block/ore_cinnabar"));
        simpleBlockWithTexture(ConfigBlocks.DEEPSLATE_ORE_QUARTZ, Thaumcraft.id("block/ore_quartz"));
    }

    @Override
    protected Stream<? extends Holder<Item>> getKnownItems() {
        return super.getKnownItems().filter(holder -> holder.value() instanceof BlockItem);
    }

    public void simpleBlock(ConfigBlocks.BlockObject<? extends Block> block) {
        ResourceLocation id = RegistryUtils.getBlockLocation(block.blockSupplier());
        TextureMapping mapping = new TextureMapping().put(TextureSlot.ALL, id).put(TextureSlot.PARTICLE, id);
        TexturedModel.Provider model = TexturedModel.createDefault(b -> mapping, ModelTemplates.CUBE_ALL);
        blocks.createTrivialBlock(block.block(), model);
        blockParentItem(block, id);
    }

    public void simpleBlockWithTexture(ConfigBlocks.BlockObject<? extends Block> block, ResourceLocation texture) {
        TextureMapping mapping = new TextureMapping().put(TextureSlot.ALL, texture).put(TextureSlot.PARTICLE, texture);
        ResourceLocation modelLocation = RegistryUtils.getBlockLocation(block.blockSupplier());
        ResourceLocation model = ModelTemplates.CUBE_ALL.create(block.block(), mapping, blocks.modelOutput);
        blocks.blockStateOutput.accept(MultiVariantGenerator.multiVariant(block.block(), Variant.variant().with(VariantProperties.MODEL, model)));
        blockParentItem(block, model);
    }

    private void registerDirectionalMultipart(ConfigBlocks.BlockObject<? extends Block> block, Map<Direction, BooleanProperty> dirProperties, ResourceLocation centerPart, ResourceLocation sidePart, boolean hideCenter) {
        MultiPartGenerator generator = MultiPartGenerator.multiPart(block.block());
        Variant center = Variant.variant().with(VariantProperties.MODEL, centerPart);

        for(Direction dir : Direction.values()) {
            Condition dirCondition = Condition.condition().term(dirProperties.get(dir), true);
            MultiPartRotation rot = MultiPartRotation.BY_DIRECTION.get(dir);
            generator.with(dirCondition, Variant.variant()
                    .with(VariantProperties.MODEL, sidePart)
                    .with(VariantProperties.X_ROT, rot.xRotation)
                    .with(VariantProperties.Y_ROT, rot.yRotation));
        }

        if(hideCenter) {
            generator.with(Condition.or(Arrays.stream(Direction.values()).map(d -> Condition.condition().term(dirProperties.get(d), false)).toList().toArray(new Condition[0])), center);
        } else
            generator.with(center);

        blocks.blockStateOutput.accept(generator);
        blockTextureItem(block, RegistryUtils.getBlockItemLocation(block.blockSupplier()));
    }

    private void registerJars() {
        Set.of(ConfigBlocks.WARDED_JAR, ConfigBlocks.VOID_JAR).forEach(block -> {
            blocks.blockStateOutput.accept(MultiPartGenerator.multiPart(block.block())
                    .with(Variant.variant().with(VariantProperties.MODEL,
                            ModelLocationUtils.getModelLocation(block.block(), "_body")))
                    .with(Condition.condition().term(JarBlock.BRACED, true),
                            Variant.variant().with(VariantProperties.MODEL, Thaumcraft.id("block/jar_brace")))
                    .with(Condition.condition().term(JarBlock.CONNECTED, true),
                            Variant.variant().with(VariantProperties.MODEL, Thaumcraft.id("block/jar_tube"))));

            blockParentItem(block, ModelLocationUtils.getModelLocation(block.block()).withSuffix("_body"));
        });
    }

    protected void registerCrystalColonies() {
        ResourceLocation itemTexture = Thaumcraft.id("item/block/crystal_colony");
        ResourceLocation itemModel = ModelTemplates.FLAT_ITEM.create(itemTexture, TextureMapping.layer0(itemTexture), items.modelOutput);
        ConfigBlocks.CRYSTAL_COLONY.forEach((aspect, blockObject) -> {
            Block block = blockObject.block();

            MultiVariantGenerator generator = MultiVariantGenerator.multiVariant(block);
            generator.with(PropertyDispatch.properties(CrystalBlock.FACING, CrystalBlock.SIZE).generate((dir, size) -> {
                ResourceLocation model = Thaumcraft.id("block/crystal_colony_" + size);
                VariantProperties.Rotation xRot = switch(dir.getAxis()) {
                    case X -> VariantProperties.Rotation.R90;
                    case Y -> dir.getAxisDirection() == Direction.AxisDirection.POSITIVE
                            ? VariantProperties.Rotation.R180
                            : VariantProperties.Rotation.R0;
                    case Z -> dir.getAxisDirection() == Direction.AxisDirection.POSITIVE
                            ? VariantProperties.Rotation.R270
                            : VariantProperties.Rotation.R90;
                };
                Variant variant = Variant.variant().with(VariantProperties.MODEL, model).with(VariantProperties.X_ROT, xRot);
                if(dir.getAxis() == Direction.Axis.X) {
                    variant.with(VariantProperties.Y_ROT,
                            dir.getAxisDirection() == Direction.AxisDirection.POSITIVE
                                    ? VariantProperties.Rotation.R90
                                    : VariantProperties.Rotation.R270);
                }
                return variant;
            }));
            blocks.blockStateOutput.accept(generator);
            items.itemModelOutput.accept(blockObject.item(), ItemModelUtils.tintedModel(itemModel, new AspectItemTintSource()));
        });
    }

    protected void registerAspectSource() {
        Block block = ConfigBlocks.CREATIVE_ASPECT_SOURCE.block();
        ResourceLocation texture = TextureMapping.getBlockTexture(block);
        MultiVariantGenerator generator = MultiVariantGenerator.multiVariant(block);

        generator.with(PropertyDispatch.property(CreativeAspectSourceBlock.HAS_ASPECT).generate(b -> {
            TextureMapping mapping = new TextureMapping()
                    .put(TextureSlot.PARTICLE, texture.withSuffix(b ? "_filled" : "_empty"))
                    .put(TextureSlot.SIDE, texture.withSuffix(b ? "_filled" : "_empty"))
                    .put(TextureSlot.UP, texture.withSuffix("_empty"))
                    .put(TextureSlot.DOWN, texture.withSuffix(b ? "_filled" : "_empty"));
            return Variant.variant().with(VariantProperties.MODEL, ModelTemplates.CUBE.create(ModelLocationUtils.getModelLocation(block, b ? "_filled" : "_empty"), mapping, blocks.modelOutput));
        }));

        blocks.blockStateOutput.accept(generator);
        blockParentItem(ConfigBlocks.CREATIVE_ASPECT_SOURCE, ModelLocationUtils.getModelLocation(block, "_empty"));
    }

	private static final ModelTemplate DIOPTRA = ModelTemplates.create("thaumcraft:dioptra", TextureSlot.PARTICLE, TextureSlot.TOP, TextureSlot.BOTTOM, TextureSlot.SIDE);

    protected void registerDioptra() {
        Block block = ConfigBlocks.DIOPTRA.block();
		ResourceLocation texture = TextureMapping.getBlockTexture(block);
        MultiVariantGenerator generator = MultiVariantGenerator.multiVariant(block);

        generator.with(PropertyDispatch.property(DioptraBlock.DISPLAY_VIS).generate(displayVis -> {
			ResourceLocation sideTexture = texture.withSuffix("_side_").withSuffix(displayVis ? "vis" : "flux");
			TextureMapping mapping = new TextureMapping()
					.put(TextureSlot.PARTICLE, sideTexture)
					.put(TextureSlot.SIDE, sideTexture)
					.put(TextureSlot.TOP, texture.withSuffix("_top"))
					.put(TextureSlot.BOTTOM, texture.withSuffix("_bottom"));
            return Variant.variant().with(VariantProperties.MODEL, DIOPTRA.create(ModelLocationUtils.getModelLocation(block, displayVis ? "_vis" : "_flux"), mapping, blocks.modelOutput));
        }));

        blocks.blockStateOutput.accept(generator);
        blockParentItem(ConfigBlocks.DIOPTRA, ModelLocationUtils.getModelLocation(block));
    }

    private void simpleExistingBlock(ConfigBlocks.BlockObject<? extends Block> block) {
        ResourceLocation model = ModelLocationUtils.getModelLocation(block.block());
        blocks.blockStateOutput.accept(MultiVariantGenerator.multiVariant(block.block(), Variant.variant().with(VariantProperties.MODEL, model)));
        blockParentItem(block, model);
    }

    private void registerStairAndSlab(ConfigBlocks.BlockObject<? extends Block> block, ConfigBlocks.BlockObject<? extends StairBlock> stairs, ConfigBlocks.BlockObject<? extends SlabBlock> slab, boolean uniqueTextures) {
        ResourceLocation texture = RegistryUtils.getBlockLocation(block.blockSupplier());
        TextureMapping mapping = new TextureMapping().put(TextureSlot.PARTICLE, texture.withSuffix(uniqueTextures ? "_0" : ""))
                .put(TextureSlot.BOTTOM, texture.withSuffix(uniqueTextures ? "_0" : ""))
                .put(TextureSlot.TOP, texture.withSuffix(uniqueTextures ? "_1" : ""))
                .put(TextureSlot.SIDE, texture.withSuffix(uniqueTextures ? "_2" : ""));
        ResourceLocation stairsStraight = ModelTemplates.STAIRS_STRAIGHT.create(stairs.block(), mapping, blocks.modelOutput);
        ResourceLocation innerStairs = ModelTemplates.STAIRS_INNER.create(stairs.block(), mapping, blocks.modelOutput);
        ResourceLocation outerStairs = ModelTemplates.STAIRS_OUTER.create(stairs.block(), mapping, blocks.modelOutput);
        blocks.blockStateOutput.accept(BlockModelGenerators.createStairs(stairs.block(), innerStairs, stairsStraight, outerStairs));
        ResourceLocation bottomSlab = ModelTemplates.SLAB_BOTTOM.create(slab.block(), mapping, blocks.modelOutput);
        ResourceLocation topSlab = ModelTemplates.SLAB_TOP.create(slab.block(), mapping, blocks.modelOutput);
        blocks.blockStateOutput.accept(BlockModelGenerators.createSlab(slab.block(), bottomSlab, topSlab, RegistryUtils.getBlockLocation(block.blockSupplier())));
        blockParentItem(stairs, stairsStraight);
        blockParentItem(slab, bottomSlab);
    }

    private void registerAxisTexturedBlock(ConfigBlocks.BlockObject<? extends Block> block, boolean allSides) {
        ResourceLocation id = RegistryUtils.getBlockLocation(block.blockSupplier());
        TextureMapping mapping = new TextureMapping().put(TextureSlot.PARTICLE, id.withSuffix("_0"))
                .put(TextureSlot.UP, id.withSuffix("_0"))
                .put(TextureSlot.EAST, id.withSuffix("_1"))
                .put(TextureSlot.NORTH, id.withSuffix("_2"))
                .put(TextureSlot.DOWN, id.withSuffix(allSides ? "_3" : "_0"))
                .put(TextureSlot.WEST, id.withSuffix(allSides ? "_4" : "_1"))
                .put(TextureSlot.SOUTH, id.withSuffix(allSides ? "_5" : "_2"));

        ResourceLocation model = ModelTemplates.CUBE_DIRECTIONAL.create(block.block(), mapping, blocks.modelOutput);
        Variant standard = Variant.variant().with(VariantProperties.MODEL, model);
        Variant x = Variant.variant().with(VariantProperties.MODEL, model).with(VariantProperties.X_ROT , VariantProperties.Rotation.R90);
        Variant y = Variant.variant().with(VariantProperties.MODEL, model).with(VariantProperties.Y_ROT , VariantProperties.Rotation.R90);
        Variant xy = Variant.variant().with(VariantProperties.MODEL, model).with(VariantProperties.X_ROT , VariantProperties.Rotation.R90).with(VariantProperties.Y_ROT , VariantProperties.Rotation.R90);
        blocks.blockStateOutput.accept(MultiVariantGenerator.multiVariant(block.block(), standard, x, y, xy));
        blockParentItem(block, id);
    }

    private void registerFakeBlock(DeferredBlock<?> block) {
        ResourceLocation model = Thaumcraft.id("block/empty");
        blocks.blockStateOutput.accept(MultiVariantGenerator.multiVariant(block.value(), Variant.variant().with(VariantProperties.MODEL, model)));
    }

    private void registerEmptyBlock(ConfigBlocks.BlockObject<? extends Block> block, ResourceLocation itemModel) {
        ResourceLocation model = Thaumcraft.id("block/empty");
        blocks.blockStateOutput.accept(MultiVariantGenerator.multiVariant(block.block(), Variant.variant().with(VariantProperties.MODEL, model)));
        if(itemModel != null)
            blockParentItem(block, itemModel);
    }

    private static final ResourceLocation PILLAR_MODEL = Thaumcraft.id("models/block/infusion_pillar.obj");

    private void registerInfusionPillar(ConfigBlocks.BlockObject<InfusionPillarBlock> block, ResourceLocation particle) {
        ResourceLocation model = TexturedModel.createDefault(
                b -> new TextureMapping()
                        .put(TextureSlot.ALL, TextureMapping.getBlockTexture(b))
                        .put(TextureSlot.PARTICLE, particle),
                ExtendedModelTemplateBuilder.builder()
                    .customLoader(ObjModelBuilder::new, loader -> {
                        loader.modelLocation(PILLAR_MODEL);
                    }).requiredTextureSlot(TextureSlot.TEXTURE).requiredTextureSlot(TextureSlot.PARTICLE).build()).create(block.block(), blocks.modelOutput);

        MultiVariantGenerator generator = MultiVariantGenerator.multiVariant(block.block());
        generator.with(PropertyDispatch.property(InfusionPillarBlock.POINTING).generate(dir -> Variant.variant().with(VariantProperties.MODEL, model).with(VariantProperties.Y_ROT, dir.getBlockRotation())));
        blocks.blockStateOutput.accept(generator);
    }

    private void blockParentItem(ConfigBlocks.BlockObject<? extends Block> block, ResourceLocation blockModel) {
        items.itemModelOutput.accept(block.item(), ItemModelUtils.plainModel(blockModel));
    }

    private void blockTextureItem(ConfigBlocks.BlockObject<? extends Block> block, ResourceLocation itemModel) {
        ResourceLocation model = ModelTemplates.FLAT_ITEM.create(itemModel, TextureMapping.layer0(itemModel), items.modelOutput);
        items.itemModelOutput.accept(block.item(), ItemModelUtils.plainModel(model));
    }

    private void batchSimpleExistingBlock(ConfigBlocks.BlockObject<? extends Block>... blocks) {
        for (ConfigBlocks.BlockObject<? extends Block> block : blocks)
            simpleExistingBlock(block);
    }

    private record MultiPartRotation(VariantProperties.Rotation xRotation, VariantProperties.Rotation yRotation) {
        public static final Map<Direction, MultiPartRotation> BY_DIRECTION = new EnumMap<>(ImmutableMap.of(
                Direction.NORTH, new MultiPartRotation(VariantProperties.Rotation.R270, VariantProperties.Rotation.R0),
                Direction.EAST, new MultiPartRotation(VariantProperties.Rotation.R270, VariantProperties.Rotation.R90),
                Direction.SOUTH, new MultiPartRotation(VariantProperties.Rotation.R90, VariantProperties.Rotation.R0),
                Direction.WEST, new MultiPartRotation(VariantProperties.Rotation.R90, VariantProperties.Rotation.R90),
                Direction.UP, new MultiPartRotation(VariantProperties.Rotation.R180, VariantProperties.Rotation.R0),
                Direction.DOWN, new MultiPartRotation(VariantProperties.Rotation.R0, VariantProperties.Rotation.R0)));
    }

    @Override
    public String getName() {
        return "Block Data";
    }
}
