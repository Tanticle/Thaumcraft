package art.arcane.thaumcraft.data.providers;

import net.minecraft.core.RegistrySetBuilder;
import art.arcane.thaumcraft.api.ThaumcraftData;
import art.arcane.thaumcraft.data.research.ResearchEntry;
import art.arcane.thaumcraft.util.simple.SimpleDataProvider;

public class ResearchEntryProvider extends SimpleDataProvider<ResearchEntry> {

    public ResearchEntryProvider(RegistrySetBuilder builder) {
        super("ResearchEntries", ThaumcraftData.Registries.RESEARCH_ENTRY, builder);
    }

    @Override
    public void createEntries() { }
}
