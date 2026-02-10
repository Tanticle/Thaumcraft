package art.arcane.thaumcraft.data.providers;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.DataMapProvider;
import art.arcane.thaumcraft.data.DataMapEntries;
import art.arcane.thaumcraft.registries.ConfigDataMaps;

import java.util.concurrent.CompletableFuture;

public class DataMapsProvider extends DataMapProvider {

    public DataMapsProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(packOutput, lookupProvider);
    }

    @Override
    protected void gather(HolderLookup.Provider provider) {
        this.builder(ConfigDataMaps.INFUSION_STABILIZER)
                .add(Tags.Blocks.SKULLS, new DataMapEntries.InfusionStabilizerData(0.1F, 0.0F), false)
                .add(BlockTags.CANDLES,  new DataMapEntries.InfusionStabilizerData(0.1F, 0.0F), false);
    }
}
