package art.arcane.thaumcraft.data.generator.providers;

import com.google.common.collect.Lists;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.resources.ResourceKey;
import art.arcane.thaumcraft.Thaumcraft;
import art.arcane.thaumcraft.api.ThaumcraftData;
import art.arcane.thaumcraft.data.research.ResearchCategory;
import art.arcane.thaumcraft.data.research.ResearchEntry;
import art.arcane.thaumcraft.util.IconTexture;
import art.arcane.thaumcraft.util.simple.SimpleDataProvider;

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
