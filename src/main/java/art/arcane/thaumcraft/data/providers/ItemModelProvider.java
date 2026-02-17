package art.arcane.thaumcraft.data.providers;

import art.arcane.thaumcraft.Thaumcraft;
import art.arcane.thaumcraft.client.rendering.entity.models.ArmorRobe;
import art.arcane.thaumcraft.client.tints.AspectItemTintSource;
import art.arcane.thaumcraft.client.tints.GolemMaterialItemTintSource;
import art.arcane.thaumcraft.items.ItemModelProperties;
import art.arcane.thaumcraft.registries.ConfigItems;
import art.arcane.thaumcraft.util.RegistryUtils;
import net.minecraft.client.color.item.Constant;
import net.minecraft.client.color.item.Dye;
import net.minecraft.client.color.item.ItemTintSource;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.model.*;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.core.Holder;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.loaders.ObjModelBuilder;
import net.neoforged.neoforge.client.model.generators.template.ExtendedModelTemplateBuilder;
import net.neoforged.neoforge.client.model.generators.template.TransformVecBuilder;

import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Stream;


public class ItemModelProvider extends ModelProvider {

	private ItemModelGenerators items;

	public ItemModelProvider(PackOutput output) {
		super(output, Thaumcraft.MOD_ID);
	}

	@Override
	protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
		items = itemModels;

		phialItem(ConfigItems.JAR_LABEL, "alchemy");
		simpleItem(ConfigItems.JAR_BRACE, "alchemy");

		// Resources
		batchItems("resources", ConfigItems.INGOT_BRASS, ConfigItems.INGOT_THAUMIUM, ConfigItems.INGOT_VOID, ConfigItems.AMBER, ConfigItems.CINNABAR, ConfigItems.QUICKSILVER, ConfigItems.SALIS_MUNDUS);

		// Plates
		batchItems("resources", ConfigItems.PLATE_BRASS, ConfigItems.PLATE_IRON, ConfigItems.PLATE_THAUMIUM, ConfigItems.PLATE_VOID);

		// Nuggets
		batchItems("resources", ConfigItems.NUGGET_BRASS, ConfigItems.NUGGET_QUICKSILVER, ConfigItems.NUGGET_THAUMIUM, ConfigItems.NUGGET_VOID, ConfigItems.NUGGET_QUARTZ);

		// Crafting Components
		batchItems("resources", ConfigItems.FILTER, ConfigItems.FABRIC, ConfigItems.TALLOW, ConfigItems.ALUMENTUM);

		// Misc Resources
		simpleItem(ConfigItems.VOID_SEED, "resources");
		simpleItem(ConfigItems.MAGIC_DUST, "resources");
		simpleItem(ConfigItems.PECH_WAND, "resources");

		// Clusters
		ResourceLocation clusterTexture = Thaumcraft.id("item/resources/cluster");
		tintedItemSharedTexture(ConfigItems.CLUSTER_IRON, clusterTexture, 0xD8AF93, "resources");
		tintedItemSharedTexture(ConfigItems.CLUSTER_GOLD, clusterTexture, 0xFCEE4B, "resources");
		tintedItemSharedTexture(ConfigItems.CLUSTER_COPPER, clusterTexture, 0xE77C56, "resources");
		tintedItemSharedTexture(ConfigItems.CLUSTER_CINNABAR, clusterTexture, 0xE03030, "resources");

		simpleItem(ConfigItems.LOOT_BAG_COMMON);
		simpleItem(ConfigItems.LOOT_BAG_UNCOMMON);
		simpleItem(ConfigItems.LOOT_BAG_RARE);

		// Consumables
		batchItems("consumables", ConfigItems.ZOMBIE_BRAIN, ConfigItems.TRIPLE_MEAT_TREAT);

		tintableAspectItem(ConfigItems.VIS_CRYSTAL, "resources");
		phialItem(ConfigItems.PHIAL, "resources");

        // Tools
        batchItems("tools",
                ConfigItems.GAUNTLET,
                ConfigItems.ELEMENTAL_AXE, ConfigItems.ELEMENTAL_HOE, ConfigItems.ELEMENTAL_PICKAXE, ConfigItems.ELEMENTAL_SHOVEL, ConfigItems.ELEMENTAL_SWORD,
                ConfigItems.THAUMIUM_AXE, ConfigItems.THAUMIUM_HOE, ConfigItems.THAUMIUM_PICKAXE, ConfigItems.THAUMIUM_SHOVEL, ConfigItems.THAUMIUM_SWORD,
                ConfigItems.VOID_AXE, ConfigItems.VOID_HOE, ConfigItems.VOID_PICKAXE, ConfigItems.VOID_SHOVEL, ConfigItems.VOID_SWORD,
                ConfigItems.CRIMSON_BLADE, ConfigItems.PRIMAL_CRUSHER);
        batchItems("tools",
                ConfigItems.ESSENTIA_RESONATOR, ConfigItems.SANITY_CHECKER, ConfigItems.SCRIBING_TOOLS);

		// Armor
		armorSet(ConfigItems.ARMOR_CRIMSON_LEADER);
		armorSet(ConfigItems.ARMOR_CRIMSON_PLATE);
		armorSet(ConfigItems.ARMOR_CRIMSON_ROBE);
		armorSet(ConfigItems.ARMOR_FORTRESS);

		tintableItemLayer(ConfigItems.ARMOR_VOID_ROBE.head(), () -> new Dye(ArmorRobe.VOID_DEFAULT_COLOUR), "armor");
		tintableItem2Layer(ConfigItems.ARMOR_VOID_ROBE.chest(), () -> new Dye(ArmorRobe.VOID_DEFAULT_COLOUR), "armor");
		tintableItem2Layer(ConfigItems.ARMOR_VOID_ROBE.legs(), () -> new Dye(ArmorRobe.VOID_DEFAULT_COLOUR), "armor");

		simpleItem(ConfigItems.ARMOR_CRIMSON_BOOTS, "armor");
		simpleItem(ConfigItems.ARMOR_TRAVELLER_BOOTS, "armor");

		tintableItem2Layer(ConfigItems.ARMOR_THAUMATURGE_CHEST, () -> new Dye(EquipmentInfoProvider.COLOUR_THAUMATURGE), "armor");
		tintableItem2Layer(ConfigItems.ARMOR_THAUMATURGE_PANTS, () -> new Dye(EquipmentInfoProvider.COLOUR_THAUMATURGE), "armor");
		tintableItem2Layer(ConfigItems.ARMOR_THAUMATURGE_BOOTS, () -> new Dye(EquipmentInfoProvider.COLOUR_THAUMATURGE), "armor");

		simpleItem(ConfigItems.ARMOR_THAUMIUM_HELMET, "armor");
		simpleItem(ConfigItems.ARMOR_THAUMIUM_CHEST, "armor");
		simpleItem(ConfigItems.ARMOR_THAUMIUM_PANTS, "armor");
		simpleItem(ConfigItems.ARMOR_THAUMIUM_BOOTS, "armor");

		simpleItem(ConfigItems.ARMOR_VOID_HELMET, "armor");
		simpleItem(ConfigItems.ARMOR_VOID_CHEST, "armor");
		simpleItem(ConfigItems.ARMOR_VOID_PANTS, "armor");
		simpleItem(ConfigItems.ARMOR_VOID_BOOTS, "armor");

		objItem(ConfigItems.SCANNER, 2, Map.of(
				ItemDisplayContext.FIRST_PERSON_RIGHT_HAND, t ->
						t.translation(-1F, 0F, -6.3F).rotation(90, 0, 0),
				ItemDisplayContext.FIRST_PERSON_LEFT_HAND, t ->
						t.translation(-17F, 0F, -6.3F).rotation(90, 0, 0),
				ItemDisplayContext.THIRD_PERSON_RIGHT_HAND, t ->
						t.translation(0.1F, 0.4F, -0.7F).scale(0.2F).rotation(242, 0, 270),
				ItemDisplayContext.THIRD_PERSON_LEFT_HAND, t ->
						t.translation(0.1F, -2.2F, 0.4F).scale(0.2F).rotation(75, 0, 270),
				ItemDisplayContext.GUI, t ->
						t.translation(3F, -2.7F, 0).scale(0.35F).rotation(90, 0, 0),
				ItemDisplayContext.GROUND, t ->
						t.translation(1.5F, 2F, 1.5F).scale(0.2F).rotation(90, 0, 0),
				ItemDisplayContext.FIXED, t ->
						t.translation(-3F, -3.26F, -2.25F).scale(0.4F).rotation(-90, 180, 0)
		));

		simpleItem(ConfigItems.GOGGLES);
    }

		tintableGolemItem(ConfigItems.GOLEM_PLACER, "golemancy");
		sealPlacerItem();
		simpleItem(ConfigItems.GOLEM_BELL, "golemancy");

		batchItems("golemancy", ConfigItems.MIND_CLOCKWORK, ConfigItems.MIND_BIOTHAUMIC, ConfigItems.MECHANISM_SIMPLE, ConfigItems.MODULE_VISION, ConfigItems.MODULE_AGGRESSION);
	}

	protected void sealPlacerItem() {
		String[] sealTypes = {
				"guard", "guard_advanced", "pickup", "pickup_advanced", "provide", "stock",
				"breaker", "breaker_advanced", "harvest", "lumber", "butcher",
				"fill", "fill_advanced", "empty", "empty_advanced", "use"
		};

		ResourceLocation blankTexture = Thaumcraft.id("item/golemancy/seals/static/seal_blank");
		ResourceLocation blankModelLoc = Thaumcraft.id("item/golemancy/seal_placer_blank");
		ResourceLocation fallbackModel = ModelTemplates.FLAT_ITEM.create(blankModelLoc, TextureMapping.layer0(blankTexture), items.modelOutput);

		ItemModel.Unbaked current = ItemModelUtils.plainModel(fallbackModel);

		for (int i = sealTypes.length - 1; i >= 0; i--) {
			String type = sealTypes[i];
			ResourceLocation texture = Thaumcraft.id("item/golemancy/seals/static/seal_" + type);
			ResourceLocation modelLoc = Thaumcraft.id("item/golemancy/seal_placer_" + type);
			ResourceLocation model = ModelTemplates.FLAT_ITEM.create(modelLoc, TextureMapping.layer0(texture), items.modelOutput);
			current = ItemModelUtils.conditional(new ItemModelProperties.SealTypeCheck(type), ItemModelUtils.plainModel(model), current);
		}

		items.itemModelOutput.accept(ConfigItems.SEAL_PLACER.value(), current);
	}

	protected void simpleItem(Holder<? extends Item> item, String... parentFolder) {
		ResourceLocation location = RegistryUtils.getItemLocation(item, parentFolder);
		ResourceLocation model = ModelTemplates.FLAT_ITEM.create(location, TextureMapping.layer0(location), items.modelOutput);
		items.itemModelOutput.accept(item.value(), ItemModelUtils.plainModel(model));
	}

	protected void objItem(Holder<? extends Item> item, int layers, Map<ItemDisplayContext, Consumer<TransformVecBuilder>> transforms, String... parentFolder) {
		ResourceLocation texture = RegistryUtils.getItemLocation(item, parentFolder);
		TextureMapping mapping = new TextureMapping();
		ExtendedModelTemplateBuilder builder = ExtendedModelTemplateBuilder.builder()
				.customLoader(ObjModelBuilder::new, loader -> loader.flipV(true).modelLocation(RegistryUtils.getItemLocation(item, parentFolder).withPrefix("models/").withSuffix(".obj")))
				.renderType("item_entity_translucent_cull");
		for (int i = 0; i < layers; i++) {
			TextureSlot slot = TextureSlot.create("layer" + i);
			builder.requiredTextureSlot(slot);
			mapping.put(slot, texture.withSuffix("_" + i));
		}

		transforms.forEach(builder::transform);

		items.itemModelOutput.accept(item.value(), ItemModelUtils.plainModel(builder.build().create(item.value(), mapping, items.modelOutput)));
	}

	protected void simpleItemWithTexture(Holder<? extends Item> item, ResourceLocation texture) {
		ResourceLocation location = RegistryUtils.getItemLocation(item);
		ResourceLocation model = ModelTemplates.FLAT_ITEM.create(location, TextureMapping.layer0(texture), items.modelOutput);
		items.itemModelOutput.accept(item.value(), ItemModelUtils.plainModel(model));
	}

	protected void batchItems(String folder, Holder<? extends Item>... items) {
		for (Holder<? extends Item> item : items) {
			simpleItem(item, folder);
		}
	}

	protected void tintableGolemItem(Holder<? extends Item> item, String... parentFolder) {
		ResourceLocation location = RegistryUtils.getItemLocation(item, parentFolder);

		ResourceLocation model = ModelTemplates.FLAT_ITEM.create(location,
				TextureMapping.layer0(location),
				items.modelOutput);

		items.itemModelOutput.accept(item.value(), ItemModelUtils.tintedModel(model, new GolemMaterialItemTintSource()));
	}

	protected void tintableAspectItem(Holder<? extends Item> item, String... parentFolder) {
		ResourceLocation location = RegistryUtils.getItemLocation(item, parentFolder);

		ResourceLocation model = ModelTemplates.FLAT_ITEM.create(location,
				TextureMapping.layer0(location),
				items.modelOutput);

		items.itemModelOutput.accept(item.value(), ItemModelUtils.tintedModel(model, new AspectItemTintSource()));
	}


	protected void phialItem(Holder<? extends Item> item, String... parentFolder) {
		ResourceLocation location = RegistryUtils.getItemLocation(item, parentFolder);

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

	protected void tintableItemLayer(Holder<? extends Item> item, Supplier<? extends ItemTintSource> source, String... parentFolder) {
		ResourceLocation location = RegistryUtils.getItemLocation(item, parentFolder);
		ResourceLocation model = ModelTemplates.FLAT_ITEM.create(location,
				TextureMapping.layer0(location),
				items.modelOutput);
		items.itemModelOutput.accept(item.value(), ItemModelUtils.tintedModel(model, new Constant(0xFFFFFFFF), source.get()));
	}

	protected void tintableItem2Layer(Holder<? extends Item> item, Supplier<? extends ItemTintSource> source, String... parentFolder) {
		ResourceLocation location = RegistryUtils.getItemLocation(item, parentFolder);
		ResourceLocation model = ModelTemplates.TWO_LAYERED_ITEM.create(location,
				TextureMapping.layered(location, location.withSuffix("_overlay")),
				items.modelOutput);
		items.itemModelOutput.accept(item.value(), ItemModelUtils.tintedModel(model, new Constant(0xFFFFFFFF), source.get()));
	}

	protected void tintedItemSharedTexture(Holder<? extends Item> item, ResourceLocation sharedTexture, int color, String... parentFolder) {
		ResourceLocation location = RegistryUtils.getItemLocation(item, parentFolder);
		ResourceLocation model = ModelTemplates.FLAT_ITEM.create(location, TextureMapping.layer0(sharedTexture), items.modelOutput);
		items.itemModelOutput.accept(item.value(), ItemModelUtils.tintedModel(model, new Constant(color)));
	}

	protected void armorSet(ConfigItems.FancyArmorSet armorSet) {
		batchItems("armor", armorSet.head(), armorSet.chest(), armorSet.legs());
	}

	@Override
	protected Stream<? extends Holder<Block>> getKnownBlocks() {
		return Stream.empty();
	}

	@Override
	protected Stream<? extends Holder<Item>> getKnownItems() {
		return super.getKnownItems().filter(holder -> !(holder.value() instanceof BlockItem));
	}

	@Override
	public String getName() {
		return "Item Data";
	}
}
