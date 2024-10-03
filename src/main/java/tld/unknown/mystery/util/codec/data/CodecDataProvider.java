package tld.unknown.mystery.util.codec.data;

import com.google.gson.JsonElement;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.MapCodec;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import tld.unknown.mystery.Thaumcraft;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public abstract class CodecDataProvider<T> implements DataProvider {

    private final String name;
    private final PackOutput.PathProvider provider;
    private final MapCodec<T> codec;
    private final Map<ResourceKey<T>, T> entries = new HashMap<>();

    public CodecDataProvider(PackOutput output, String name, String path, MapCodec<T> codec) {
        this.name = name;
        this.provider = output.createPathProvider(PackOutput.Target.DATA_PACK, path);
        this.codec = codec;
        createEntries();
    }

    protected abstract void createEntries();

    @Override
    public CompletableFuture<Void> run(CachedOutput pOutput) {
        entries.forEach((id, obj) -> {
            Path p = provider.json(id.location());
            DataResult<JsonElement> result = JsonOps.INSTANCE.withEncoder(codec.codec()).apply(obj);
            result.resultOrPartial(s -> Thaumcraft.error("Failed to save data object: " + s))
                    .ifPresent(json -> {
                        processJson(json);
                        Thaumcraft.info("Generating %s for %s", name, id);
                        DataProvider.saveStable(pOutput, json, p);
                    });
        });
        return CompletableFuture.completedFuture(null);
    }

    protected void processJson(JsonElement element) { }

    protected void register(ResourceKey<T> key, T object) {
        if(entries.containsKey(key))
            throw new IllegalStateException("Tried to register duplicate data at " + key.location() + " for data type " + name + "!");
        entries.put(key, object);
    }

    @Override
    public String getName() { return name; }
}
