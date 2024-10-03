package tld.unknown.mystery.data.generator.providers;

import com.google.common.collect.Lists;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import tld.unknown.mystery.Thaumcraft;
import tld.unknown.mystery.api.ThaumcraftData;
import tld.unknown.mystery.data.research.ResearchCategory;
import tld.unknown.mystery.data.research.ResearchEntry;
import tld.unknown.mystery.util.IconTexture;
import tld.unknown.mystery.util.codec.data.CodecDataProvider;
import tld.unknown.mystery.util.simple.SimpleDataProvider;

public class ResearchCategoryProvider extends SimpleDataProvider<ResearchCategory> {

    public ResearchCategoryProvider(RegistrySetBuilder builder) {
        super("Research Categories", ThaumcraftData.Registries.RESEARCH_CATEGORY, builder);
    }

    @Override
    public void createEntries() {
        registerCategory(ThaumcraftData.ResearchCategories.FUNDAMENTALS, "fundamentals");
        registerCategory(ThaumcraftData.ResearchCategories.ARTIFICE, "artifice", ThaumcraftData.ResearchEntries.UNLOCK_ARTIFICE);
    }

    private void registerCategory(ResourceKey<ResearchCategory> id, String texture, ResourceKey<ResearchEntry>... requirements) {
        context.register(id, ResearchCategory.builder()
                .icon(new IconTexture(Thaumcraft.id("textures/ui/research/categories/" + texture + ".png")))
                .requirements(Lists.newArrayList(requirements))
                .build());
    }
}
