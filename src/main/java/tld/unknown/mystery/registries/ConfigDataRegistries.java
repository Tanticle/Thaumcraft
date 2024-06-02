package tld.unknown.mystery.registries;

import com.mojang.serialization.Codec;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;
import tld.unknown.mystery.Thaumcraft;
import tld.unknown.mystery.api.ThaumcraftData;
import tld.unknown.mystery.api.aspects.Aspect;
import tld.unknown.mystery.data.aspects.AspectList;
import tld.unknown.mystery.data.aura.AuraBiomeInfo;
import tld.unknown.mystery.data.research.ResearchCategory;
import tld.unknown.mystery.data.research.ResearchEntry;
import tld.unknown.mystery.util.ReflectionUtils;

import java.util.Set;
import java.util.stream.Stream;

@EventBusSubscriber(modid = Thaumcraft.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public final class ConfigDataRegistries {

    /* -------------------------------------------------------------------------------------------------------------- */

    public static final DataRegistry<Aspect> ASPECTS = new DataRegistry<>(ThaumcraftData.Registries.ASPECT,
            Aspect.CODEC.codec(), Aspect.CODEC.codec(),
            ThaumcraftData.Aspects.UNKNOWN, Aspect.UNKNOWN);

    public static final DataRegistry<AspectList> ASPECT_REGISTRY = new DataRegistry<>(ThaumcraftData.Registries.ASPECT_REGISTRY,
            AspectList.CODEC.codec(), AspectList.CODEC.codec(),
            Thaumcraft.id("unknown"), new AspectList());

    public static final DataRegistry<ResearchCategory> RESEARCH_CATEGORIES = new DataRegistry<>(ThaumcraftData.Registries.RESEARCH_CATEGORY,
            ResearchCategory.CODEC.codec(), ResearchCategory.CODEC.codec(),
            ThaumcraftData.Research.CATEGORY_UNKNOWN, ResearchCategory.UNKNOWN);

    public static final DataRegistry<ResearchEntry> RESEARCH_ENTRIES = new DataRegistry<>(ThaumcraftData.Registries.RESEARCH_ENTRY,
            ResearchEntry.CODEC.codec(), ResearchEntry.CODEC.codec(),
            ThaumcraftData.Research.UNKNOWN, ResearchEntry.EMPTY);

    public static final DataRegistry<AuraBiomeInfo> AURA_BIOME_INFO = new DataRegistry<>(ThaumcraftData.Registries.AURA_BIOME_INFO,
            AuraBiomeInfo.CODEC.codec(), AuraBiomeInfo.CODEC.codec(),
            Thaumcraft.id("unknown"), AuraBiomeInfo.UNKNOWN);
    /* -------------------------------------------------------------------------------------------------------------- */

    public static void onDatapackRegister(final DataPackRegistryEvent.NewRegistry event) {
        ReflectionUtils.getAllStaticsOfType(ConfigDataRegistries.class, DataRegistry.class).forEach(dataRegistry -> dataRegistry.register(event));
    }

    public record DataRegistry<T>(ResourceKey<Registry<T>> key, Codec<T> codec, Codec<T> syncCodec, ResourceLocation missingId, T missingValue) {
        public T get(RegistryAccess registries, ResourceLocation id) {
            return registries.registryOrThrow(key).getOptional(id).orElse(missingValue);
        }

        public Holder<T> getHolder(RegistryAccess registries, ResourceLocation id) {
            return registries.registryOrThrow(key).getHolder(id).get();
        }

        public ResourceLocation getKey(RegistryAccess access, T value) {
            ResourceLocation key = access.registryOrThrow(this.key).getKey(value);
            return key == null ? missingId : key;
        }

        public Set<ResourceLocation> keys(RegistryAccess access) {
            return access.registryOrThrow(key).keySet();
        }

        public Stream<Holder.Reference<T>> holderStream(RegistryAccess access) {
            return access.registryOrThrow(key).holders();
        }

        private void register(DataPackRegistryEvent.NewRegistry event) {
            if(syncCodec == null)
                event.dataPackRegistry(key, codec);
            else
                event.dataPackRegistry(key, codec, syncCodec);
        }
    }
}
