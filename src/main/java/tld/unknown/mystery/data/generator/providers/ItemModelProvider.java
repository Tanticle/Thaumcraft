package tld.unknown.mystery.data.generator.providers;

import net.minecraft.client.color.item.Constant;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.model.*;
import net.minecraft.client.renderer.block.model.ItemModelGenerator;
import net.minecraft.client.renderer.item.BlockModelWrapper;
import net.minecraft.client.renderer.item.ConditionalItemModel;
import net.minecraft.core.Holder;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import tld.unknown.mystery.Thaumcraft;
import tld.unknown.mystery.client.tints.AspectItemTintSource;
import tld.unknown.mystery.items.ItemModelProperties;
import tld.unknown.mystery.registries.ConfigBlocks;
import tld.unknown.mystery.registries.ConfigItems;
import tld.unknown.mystery.util.RegistryUtils;

import java.util.stream.Stream;


public class ItemModelProvider extends ModelProvider {

    private ItemModelGenerators items;

    public ItemModelProvider(PackOutput output) {
        super(output, Thaumcraft.MOD_ID);
    }

    @Override
    protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        items = itemModels;

        tintableAspect2LayerItem(ConfigItems.JAR_LABEL, "alchemy");
        simpleItem(ConfigItems.JAR_BRACE, "alchemy");

        // Resources
        batchItems("resources", ConfigItems.INGOT_BRASS, ConfigItems.INGOT_THAUMIUM, ConfigItems.INGOT_VOID);

        tintableAspectItem(ConfigItems.VIS_CRYSTAL, "resources");
        tintableAspect2LayerItem(ConfigItems.PHIAL, "resources");

        // Tools
        batchItems("tools",
                ConfigItems.ELEMENTAL_AXE, ConfigItems.ELEMENTAL_HOE, ConfigItems.ELEMENTAL_PICKAXE, ConfigItems.ELEMENTAL_SHOVEL, ConfigItems.ELEMENTAL_SWORD,
                ConfigItems.THAUMIUM_AXE, ConfigItems.THAUMIUM_HOE, ConfigItems.THAUMIUM_PICKAXE, ConfigItems.THAUMIUM_SHOVEL, ConfigItems.THAUMIUM_SWORD,
                ConfigItems.VOID_AXE, ConfigItems.VOID_HOE, ConfigItems.VOID_PICKAXE, ConfigItems.VOID_SHOVEL, ConfigItems.VOID_SWORD,
                ConfigItems.CRIMSON_BLADE, ConfigItems.PRIMAL_CRUSHER);
        batchItems("tools",
                ConfigItems.ESSENTIA_RESONATOR, ConfigItems.SANITY_CHECKER, ConfigItems.SCRIBING_TOOLS);
    }

    protected void simpleItem(Holder<? extends Item> item, String... parentFolder) {
        ResourceLocation location = RegistryUtils.getItemModelLocation(item, parentFolder);
        ResourceLocation model = ModelTemplates.FLAT_ITEM.create(location, TextureMapping.layer0(location), items.modelOutput);
        items.itemModelOutput.accept(item.value(), ItemModelUtils.plainModel(model));
    }

    protected void batchItems(String folder, Holder<? extends Item>... items) {
        for (Holder<? extends Item> item : items) {
            simpleItem(item, folder);
        }
    }

    protected void tintableAspectItem(Holder<? extends Item> item, String... parentFolder) {
        ResourceLocation location = RegistryUtils.getItemModelLocation(item, parentFolder);

        ResourceLocation model = ModelTemplates.FLAT_ITEM.create(location,
                TextureMapping.layer0(location),
                items.modelOutput);

        items.itemModelOutput.accept(item.value(), ItemModelUtils.tintedModel(model, new AspectItemTintSource()));
    }


    protected void tintableAspect2LayerItem(Holder<? extends Item> item, String... parentFolder) {
        ResourceLocation location = RegistryUtils.getItemModelLocation(item, parentFolder);

        ResourceLocation emptyModel = ModelTemplates.FLAT_ITEM.create(location.withSuffix("_empty"),
                TextureMapping.layer0(location),
                items.modelOutput);
        ResourceLocation filledModel = ModelTemplates.TWO_LAYERED_ITEM.create(location.withSuffix("_filled"),
                TextureMapping.layered(location, location.withSuffix("_overlay")),
                items.modelOutput);

        items.itemModelOutput.accept(item.value(), ItemModelUtils.conditional(
                new ItemModelProperties.HasData(),
                ItemModelUtils.tintedModel(filledModel, new Constant(0xFFFFFFFF), new AspectItemTintSource()),
                ItemModelUtils.plainModel(emptyModel)
        ));
    }

    @Override
    protected Stream<? extends Holder<Block>> getKnownBlocks() {
        return Stream.empty();
    }

    @Override
    public String getName() {
        return "Item Data";
    }
}
