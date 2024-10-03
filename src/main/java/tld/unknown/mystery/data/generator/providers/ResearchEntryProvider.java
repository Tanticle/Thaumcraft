package tld.unknown.mystery.data.generator.providers;

import net.minecraft.core.RegistrySetBuilder;
import tld.unknown.mystery.api.ThaumcraftData;
import tld.unknown.mystery.data.research.ResearchEntry;
import tld.unknown.mystery.util.simple.SimpleDataProvider;

public class ResearchEntryProvider extends SimpleDataProvider<ResearchEntry> {

    public ResearchEntryProvider(RegistrySetBuilder builder) {
        super("ResearchEntries", ThaumcraftData.Registries.RESEARCH_ENTRY, builder);
    }

    @Override
    public void createEntries() { }
}
