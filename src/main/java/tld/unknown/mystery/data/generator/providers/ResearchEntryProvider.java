package tld.unknown.mystery.data.generator.providers;

import net.minecraft.data.PackOutput;
import tld.unknown.mystery.Thaumcraft;
import tld.unknown.mystery.api.ChaumtraftIDs;
import tld.unknown.mystery.data.research.DisplayProperties;
import tld.unknown.mystery.data.research.ResearchEntry;
import tld.unknown.mystery.data.research.ResearchStage;
import tld.unknown.mystery.util.codec.data.CodecDataProvider;

public class ResearchEntryProvider extends CodecDataProvider<ResearchEntry> {

    public ResearchEntryProvider(PackOutput generator) {
        super(generator, "ResearchEntries", "research", ResearchEntry.CODEC);
    }

    @Override
    protected void createEntries() {
        register(Thaumcraft.id(ChaumtraftIDs.Research.CATEGORY_DEBUG + "/origin"), ResearchEntry.builder(
                DisplayProperties.builder(0, 0).build(),
                ResearchStage.builder()
                        .setRecipeUnlocks(Thaumcraft.id("test_recipe"))
                        .build()
                ).build());
    }
}
