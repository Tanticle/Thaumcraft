package art.arcane.thaumcraft.registries;

import com.mojang.serialization.Codec;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;
import art.arcane.thaumcraft.Thaumcraft;
import art.arcane.thaumcraft.api.ThaumcraftData;
import art.arcane.thaumcraft.api.aspects.Aspect;
import art.arcane.thaumcraft.data.aspects.AspectList;
import art.arcane.thaumcraft.data.aspects.AspectRegistry;
import art.arcane.thaumcraft.data.aura.AuraBiomeInfo;
import art.arcane.thaumcraft.data.golemancy.GolemMaterial;
import art.arcane.thaumcraft.data.golemancy.GolemPart;
import art.arcane.thaumcraft.data.golemancy.GolemTrait;
import art.arcane.thaumcraft.data.golemancy.SealType;
import art.arcane.thaumcraft.data.research.ResearchCategory;
import art.arcane.thaumcraft.data.research.ResearchEntry;
import art.arcane.thaumcraft.util.ReflectionUtils;

import java.util.stream.Stream;

@EventBusSubscriber(modid = Thaumcraft.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public final class ConfigDataRegistries {

    /* -------------------------------------------------------------------------------------------------------------- */

    public static final AspectRegistry ASPECT_REGISTRY = new AspectRegistry();

    public static final DataRegistry<Aspect> ASPECTS = new DataRegistry<>(ThaumcraftData.Registries.ASPECT,
            Aspect.CODEC.codec(), Aspect.CODEC.codec(),
            ThaumcraftData.Aspects.UNKNOWN.location());

    public static final DataRegistry<ResearchCategory> RESEARCH_CATEGORIES = new DataRegistry<>(ThaumcraftData.Registries.RESEARCH_CATEGORY,
            ResearchCategory.CODEC.codec(), ResearchCategory.CODEC.codec(),
            ThaumcraftData.ResearchCategories.UNKNOWN.location());

    public static final DataRegistry<ResearchEntry> RESEARCH_ENTRIES = new DataRegistry<>(ThaumcraftData.Registries.RESEARCH_ENTRY,
            ResearchEntry.CODEC.codec(), ResearchEntry.CODEC.codec(),
            ThaumcraftData.ResearchCategories.UNKNOWN.location());

    public static final DataRegistry<AuraBiomeInfo> AURA_BIOME_INFO = new DataRegistry<>(ThaumcraftData.Registries.AURA_BIOME_INFO,
            AuraBiomeInfo.CODEC.codec(), AuraBiomeInfo.CODEC.codec(),
            Thaumcraft.id("unknown"));

    public static final DataRegistry<GolemTrait> GOLEM_TRAITS = new DataRegistry<>(ThaumcraftData.Registries.GOLEM_TRAIT,
            GolemTrait.CODEC.codec(), GolemTrait.CODEC.codec(),
            Thaumcraft.id("smart"));

    public static final DataRegistry<GolemMaterial> GOLEM_MATERIALS = new DataRegistry<>(ThaumcraftData.Registries.GOLEM_MATERIAL,
            GolemMaterial.CODEC.codec(), GolemMaterial.CODEC.codec(),
            Thaumcraft.id("wood"));

    public static final DataRegistry<GolemPart> GOLEM_PARTS = new DataRegistry<>(ThaumcraftData.Registries.GOLEM_PART,
            GolemPart.CODEC.codec(), GolemPart.CODEC.codec(),
            Thaumcraft.id("head_basic"));

    public static final DataRegistry<SealType> SEAL_TYPES = new DataRegistry<>(ThaumcraftData.Registries.SEAL_TYPE,
            SealType.CODEC.codec(), SealType.CODEC.codec(),
            Thaumcraft.id("guard"));

    /* -------------------------------------------------------------------------------------------------------------- */

    @SubscribeEvent
    public static void onDatapackRegister(final DataPackRegistryEvent.NewRegistry event) {
        ReflectionUtils.getAllStaticsOfType(ConfigDataRegistries.class, DataRegistry.class).forEach(dataRegistry -> dataRegistry.register(event));
        event.dataPackRegistry(ThaumcraftData.Registries.ASPECT_REGISTRY, AspectList.CODEC.codec(), AspectList.CODEC.codec());
    }

    public record DataRegistry<T>(ResourceKey<Registry<T>> key, Codec<T> codec, Codec<T> syncCodec, ResourceLocation missingId) {

        public T get(HolderLookup.Provider registries, ResourceKey<T> id) {
            return getHolder(registries, id).value();
        }

        public Holder<T> getHolder(HolderLookup.Provider registries, ResourceKey<T> id) {
            return registries.lookupOrThrow(key).get(id).orElse(defaultValue(registries));
        }

        public Stream<ResourceKey<T>> keys(HolderLookup.Provider access) {
            return access.lookupOrThrow(key).listElementIds();
        }

        public Stream<Holder.Reference<T>> holderStream(HolderLookup.Provider access) {
            return access.lookupOrThrow(key).listElements();
        }

        private void register(DataPackRegistryEvent.NewRegistry event) {
            event.dataPackRegistry(key, codec, syncCodec, builder -> builder.defaultKey(missingId));
        }

        private Holder.Reference<T> defaultValue(HolderLookup.Provider registries) {
            return registries.lookupOrThrow(key).get(ResourceKey.create(key, missingId)).get();
        }
    }
}
