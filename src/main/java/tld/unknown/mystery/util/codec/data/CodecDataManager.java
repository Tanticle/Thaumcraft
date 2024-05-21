package tld.unknown.mystery.util.codec.data;

import com.google.common.collect.HashBiMap;
import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.UnboundedMapCodec;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.configuration.ServerConfigurationPacketListener;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.neoforged.neoforge.network.configuration.ICustomConfigurationTask;
import tld.unknown.mystery.Thaumcraft;
import tld.unknown.mystery.networking.clientbound.DataConfigPacket;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class CodecDataManager<T> extends SimpleJsonResourceReloadListener implements ICustomConfigurationTask {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    private final String name;
    private final Codec<T> codec;
    private final UnboundedMapCodec<ResourceLocation, T> syncCodec;
    protected final HashBiMap<ResourceLocation, T> values;
    private final ImmutableMap<ResourceLocation, T> defaults;
    private final Predicate<ResourceLocation> test;

    public CodecDataManager(Codec<T> codec, String name, String directory, ImmutableMap<ResourceLocation, T> defaults, Predicate<ResourceLocation> test) {
        super(GSON, directory);
        this.name = name;
        this.codec = codec;
        this.syncCodec = Codec.unboundedMap(ResourceLocation.CODEC, codec);
        this.values = HashBiMap.create();
        this.test = test;

        if(defaults != null)
            this.defaults = ImmutableMap.copyOf(defaults);
        else
            this.defaults = ImmutableMap.of();
    }

    public Set<ResourceLocation> getKeys() {
        return values.keySet();
    }

    public T get(ResourceLocation loc) {
        return values.get(loc);
    }

    public Optional<T> getOptional(ResourceLocation loc) {
        if(entryPresent(loc))
            return Optional.of(get(loc));
        return Optional.empty();
    }

    public ResourceLocation getIdentifier(T value) {
        return values.inverse().get(value);
    }

    public boolean entryPresent(ResourceLocation location) {
        return values.containsKey(location);
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> pObject, ResourceManager pResourceManager, ProfilerFiller pProfiler) {
        values.clear();
        values.putAll(defaults);
        pObject.forEach((id, json) -> {
            if(test.test(id)) {
                codec.decode(JsonOps.INSTANCE, json)
                        .resultOrPartial(s -> Thaumcraft.error("Failed to parse %s for resource \"%s\": %s", this.name, id, s))
                        .ifPresent(result -> values.put(id, result.getFirst()));
            }
        });
        postApply();
        Thaumcraft.info("Loaded %d entries for %s data.", values.size(), name);
    }

    protected void postApply() { }

    @Override
    public String getName() { return this.name; }

    public void serialize(CompoundTag tag) {
        syncCodec.encodeStart(NbtOps.INSTANCE, values)
                .resultOrPartial(s -> Thaumcraft.error("Failed to encode sync data for manager %s: %s", this.name, s))
                .ifPresent(result -> tag.put("values", result));
    }

    public void deserialize(CompoundTag data) {
        syncCodec.decode(NbtOps.INSTANCE, data)
                .resultOrPartial(s -> Thaumcraft.error("Failed to decode sync data for manager %s: %s", this.name, s))
                .ifPresent(result -> {
                    values.clear();
                    values.putAll(result.getFirst());
                });
        postApply();
    }

    private ServerConfigurationPacketListener listener;

    public void setSyncListener(ServerConfigurationPacketListener packet) {
        this.listener = packet;
    }

    @Override
    public void run(Consumer<CustomPacketPayload> sender) {
        sender.accept(DataConfigPacket.fromDataManager(this));
        this.listener.finishCurrentTask(type());
    }

    @Override
    public Type type() { return new Type(getName()); }

    public void printRegistry() {
        values.forEach((k, v) -> Thaumcraft.info("%s: %s", k, v));
    }
}
