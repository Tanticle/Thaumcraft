package tld.unknown.mystery.util.codec.data;

import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import tld.unknown.mystery.Thaumcraft;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public abstract class CodecDataProvider<T> implements DataProvider {

    private final String name;
    private final PackOutput.PathProvider provider;
    private final Codec<T> codec;
    private final Map<ResourceLocation, T> entries = new HashMap<>();

    public CodecDataProvider(PackOutput output, String name, String path, Codec<T> codec) {
        this.name = name;
        this.provider = output.createPathProvider(PackOutput.Target.DATA_PACK, path);
        this.codec = codec;
        createEntries();
    }

    protected abstract void createEntries();

    @Override
    public CompletableFuture<Void> run(CachedOutput pOutput) {
        entries.forEach((id, obj) -> {
            Path p = provider.json(id);
            DataResult<JsonElement> result = JsonOps.INSTANCE.withEncoder(codec).apply(obj);
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

    protected void register(ResourceLocation path, T object) {
        if(entries.containsKey(path))
            throw new IllegalStateException("Tried to register duplicate data at " + path + " for data type " + name + "!");
        entries.put(path, object);
    }

    @Override
    public String getName() { return name; }
}
