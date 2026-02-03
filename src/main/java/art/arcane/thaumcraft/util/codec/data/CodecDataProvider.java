package art.arcane.thaumcraft.util.codec.data;

import com.google.gson.JsonElement;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import art.arcane.thaumcraft.Thaumcraft;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public abstract class CodecDataProvider<T> implements DataProvider {

    private final String name;
    private final PackOutput.PathProvider provider;
    private final MapCodec<T> codec;
    private final Map<ResourceLocation, T> entries = new HashMap<>();
    private final CompletableFuture<HolderLookup.Provider> lookupProvider;

    public CodecDataProvider(PackOutput output, String name, String path, MapCodec<T> codec) {
        this(output, name, path, codec, null);
    }

    public CodecDataProvider(PackOutput output, String name, String path, MapCodec<T> codec, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        this.name = name;
        this.provider = output.createPathProvider(PackOutput.Target.DATA_PACK, path);
        this.codec = codec;
        this.lookupProvider = lookupProvider;
    }

    protected void init(HolderLookup.Provider registries) {
        createEntries(registries);
    }

    protected abstract void createEntries(HolderLookup.Provider registries);

    @Override
    public CompletableFuture<Void> run(CachedOutput pOutput) {
        if (lookupProvider != null) {
            return lookupProvider.thenCompose(registries -> {
                init(registries);
                return runWithRegistries(pOutput, registries);
            });
        } else {
            init(null);
            return runWithRegistries(pOutput, null);
        }
    }

    private CompletableFuture<Void> runWithRegistries(CachedOutput pOutput, HolderLookup.Provider registries) {
        DynamicOps<JsonElement> ops = registries != null
                ? RegistryOps.create(JsonOps.INSTANCE, registries)
                : JsonOps.INSTANCE;

        entries.forEach((id, obj) -> {
            Path p = provider.json(id);
            DataResult<JsonElement> result = ops.withEncoder(codec.codec()).apply(obj);
            result.resultOrPartial(s -> Thaumcraft.error("Failed to save data object: " + s))
                    .ifPresent(json -> {
                        processJson(json);
                        Thaumcraft.info("Generating %s for %s", name, id);
                        try {
                            DataProvider.saveStable(pOutput, json, p).get();
                        } catch (InterruptedException | ExecutionException e) {
                            Thaumcraft.error(e, "Error generating %s for %s: ", name, id);
                        }
                    });
        });
        return CompletableFuture.completedFuture(null);
    }

    protected void processJson(JsonElement element) { }

    protected void register(ResourceKey<T> key, T object) {
        if(entries.containsKey(key))
            throw new IllegalStateException("Tried to register duplicate data at " + key.location() + " for data type " + name + "!");
        entries.put(key.location(), object);
    }

    protected void register(ResourceLocation key, T object) {
        if(entries.containsKey(key))
            throw new IllegalStateException("Tried to register duplicate data at " + key + " for data type " + name + "!");
        entries.put(key, object);
    }

    @Override
    public String getName() { return name; }
}
