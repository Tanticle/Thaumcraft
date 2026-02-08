package art.arcane.thaumcraft.data.providers;

import art.arcane.thaumcraft.data.loot.LootBagLootProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class ThaumcraftLootProvider {
    public static LootTableProvider create(PackOutput output, CompletableFuture<net.minecraft.core.HolderLookup.Provider> registries) {
        return new LootTableProvider(output, Set.of(), List.of(
                new LootTableProvider.SubProviderEntry(LootBagLootProvider::new, LootContextParamSets.GIFT)
        ), registries);
    }
}
