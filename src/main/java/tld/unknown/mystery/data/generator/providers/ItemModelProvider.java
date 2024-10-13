package tld.unknown.mystery.data.generator.providers;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import tld.unknown.mystery.Thaumcraft;
import tld.unknown.mystery.util.better.BetterItemModelProvider;

import static tld.unknown.mystery.api.ThaumcraftData.*;

public class ItemModelProvider extends BetterItemModelProvider {

    public ItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, Thaumcraft.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        basicItem(Blocks.TUBE);
        basicItem(Items.JAR_BRACE);
        basicItem(Items.VIS_CRYSTAL);
        basicItem(Blocks.CRYSTAL_COLONY);

        // Resources
        batchItems(null, Items.INGOT_BRASS, Items.INGOT_THAUMIUM, Items.INGOT_VOID);

        basicItem(Items.PHIAL)
                .override()
                .predicate(ItemProperties.ASPECT_HOLDER_PRESENT, 1F)
                .model(withExistingParent(Items.PHIAL.getPath() + "_filled", "item/generated")
                        .texture("layer0", "item/phial")
                        .texture("layer1", "item/phial_overlay"))
                .end();
        basicItem(Items.JAR_LABEL)
                .override()
                .predicate(ItemProperties.ASPECT_HOLDER_PRESENT, 1F)
                .model(withExistingParent(Items.JAR_LABEL.getPath() + "_filled", "item/generated")
                        .texture("layer0", "item/jar_label")
                        .texture("layer1", "item/jar_label_overlay"))
                .end();

        // Tools
        batchItems("tools",
                Items.ELEMENTAL_AXE, Items.ELEMENTAL_HOE, Items.ELEMENTAL_PICKAXE, Items.ELEMENTAL_SHOVEL, Items.ELEMENTAL_SWORD,
                Items.THAUMIUM_AXE, Items.THAUMIUM_HOE, Items.THAUMIUM_PICKAXE, Items.THAUMIUM_SHOVEL, Items.THAUMIUM_SWORD,
                Items.VOID_AXE, Items.VOID_HOE, Items.VOID_PICKAXE, Items.VOID_SHOVEL, Items.VOID_SWORD,
                Items.CRIMSON_BLADE, Items.PRIMAL_CRUSHER);
        batchItems("tools",
                Items.ESSENTIA_RESONATOR, Items.SANITY_CHECKER, Items.SCRIBING_TOOLS);

        // Special Block Items that don't use the built-in Block Item Model
        basicItem(Blocks.LAMPLIGHT, "misc");
    }
}
