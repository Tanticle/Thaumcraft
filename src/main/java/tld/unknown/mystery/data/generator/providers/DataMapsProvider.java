package tld.unknown.mystery.data.generator.providers;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.DataMapProvider;
import tld.unknown.mystery.data.DataMapEntries;
import tld.unknown.mystery.registries.ConfigDataMaps;

import java.util.concurrent.CompletableFuture;

public class DataMapsProvider extends DataMapProvider {

    public DataMapsProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(packOutput, lookupProvider);
    }

    @Override
    protected void gather(HolderLookup.Provider provider) {
        this.builder(ConfigDataMaps.INFUSION_STABILIZER)
                .add(Tags.Blocks.SKULLS.location(), new DataMapEntries.InfusionStabilizerData(0.1F, 0.0F), false)
                .add(BlockTags.CANDLES.location(),  new DataMapEntries.InfusionStabilizerData(0.1F, 0.0F), false);
    }
}
