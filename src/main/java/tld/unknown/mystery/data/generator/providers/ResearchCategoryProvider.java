package tld.unknown.mystery.data.generator.providers;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import tld.unknown.mystery.Thaumcraft;
import tld.unknown.mystery.api.ThaumcraftData;
import tld.unknown.mystery.data.DataRegistries;
import tld.unknown.mystery.data.research.ResearchCategory;
import tld.unknown.mystery.data.research.ResearchEntry;
import tld.unknown.mystery.util.DataResource;
import tld.unknown.mystery.util.codec.data.CodecDataProvider;
import tld.unknown.mystery.util.IconTexture;

import java.util.Arrays;
import java.util.List;

public class ResearchCategoryProvider extends CodecDataProvider<ResearchCategory> {

    public ResearchCategoryProvider(PackOutput generator) {
        super(generator, "ResearchCategories", "research", ResearchCategory.CODEC);
    }

    @Override
    protected void createEntries() {
        registerCategory(ThaumcraftData.Research.CATEGORY_DEBUG, "debug", ThaumcraftData.Research.UNLOCK_DEBUG);
        registerCategory(ThaumcraftData.Research.CATEGORY_FUNDAMENTALS, "fundamentals");
        registerCategory(ThaumcraftData.Research.CATEGORY_ARTIFICE, "artifice", ThaumcraftData.Research.UNLOCK_ARTIFICE);
    }

    private void registerCategory(String id, String texture, ResourceLocation... requirements) {
        List<DataResource<ResearchEntry>> list = Arrays.stream(requirements).map(rl -> DataResource.of(DataRegistries.RESEARCH_ENTRIES, rl)).toList();
        register(Thaumcraft.id(id), ResearchCategory.builder()
                .icon(new IconTexture(Thaumcraft.id("textures/ui/research/categories/" + texture + ".png")))
                .requirements(list)
                .build());
    }
}
