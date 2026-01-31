package art.arcane.thaumcraft.util.simple;

import net.minecraft.core.Registry;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import art.arcane.thaumcraft.Thaumcraft;
import art.arcane.thaumcraft.extensions.RegistriesDatapackGeneratorExt;

import java.util.Set;

public abstract class SimpleDataProvider<T> {

    private final String name;
    private final ResourceKey<? extends Registry<T>> key;
    private final RegistrySetBuilder builder;

    protected BootstrapContext<T> context;

    public SimpleDataProvider(String name, ResourceKey<? extends Registry<T>> key, RegistrySetBuilder builder) {
        this.name = name;
        this.key = key;
        this.builder = builder;
    }

    public abstract void createEntries();

    public DatapackBuiltinEntriesProvider build(GatherDataEvent e) {
        builder.add(this.key, ctx -> {
            this.context = ctx;
            createEntries();
        });
        DatapackBuiltinEntriesProvider provider = new DatapackBuiltinEntriesProvider(e.getGenerator().getPackOutput(), e.getLookupProvider(), builder, Set.of(Thaumcraft.MOD_ID, ResourceLocation.DEFAULT_NAMESPACE, "c"));
        ((RegistriesDatapackGeneratorExt)provider).setName(this.name);
        return provider;
    }
}
