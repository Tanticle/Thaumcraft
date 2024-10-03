package tld.unknown.mystery.util.better;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.client.model.generators.ModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.Arrays;

public abstract class BetterItemModelProvider extends ModelProvider<ItemModelBuilder> {

    public BetterItemModelProvider(PackOutput output, String modid, ExistingFileHelper existingFileHelper) {
        super(output, modid, "item", ItemModelBuilder::new, existingFileHelper);
    }

    public ItemModelBuilder basicItem(ResourceLocation location) {
        return basicItem(location, null);
    }

    public ItemModelBuilder basicItem(ResourceLocation item, String textureSubFolder) {
        return getBuilder(item.toString())
                .parent(new ModelFile.UncheckedModelFile("item/generated"))
                .texture("layer0", ResourceLocation.tryBuild(item.getNamespace(), "item/" + (textureSubFolder != null ? textureSubFolder + "/" : "") + item.getPath()));
    }

    public void batchItems(String texturePrefix, ResourceLocation... items) {
        Arrays.stream(items).forEach(item -> basicItem(item, texturePrefix));
    }


    @Override
    public String getName() {
        return "Item Models: " + modid;
    }
}
