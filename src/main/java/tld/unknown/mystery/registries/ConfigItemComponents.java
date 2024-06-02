package tld.unknown.mystery.registries;

import com.mojang.serialization.Codec;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import tld.unknown.mystery.Thaumcraft;
import tld.unknown.mystery.api.aspects.Aspect;
import tld.unknown.mystery.items.components.CollectorMarkerComponent;
import tld.unknown.mystery.items.components.CrystalAspectComponent;
import tld.unknown.mystery.items.components.InfusionEnchantmentComponent;

import static tld.unknown.mystery.api.ThaumcraftData.ItemComponents;

public class ConfigItemComponents {

    private static final DeferredRegister<DataComponentType<?>> REGISTRY = DeferredRegister.create(BuiltInRegistries.DATA_COMPONENT_TYPE, Thaumcraft.MOD_ID);

    /* -------------------------------------------------------------------------------------------------------------- */

    public static final Holder<DataComponentType<InfusionEnchantmentComponent>> INFUSION_ENCHANTMENT = register(ItemComponents.INFUSION_ENCHANTMENTS,
            InfusionEnchantmentComponent.CODEC, InfusionEnchantmentComponent.STREAM_CODEC);

    public static final Holder<DataComponentType<Holder<Aspect>>> ASPECT_HOLDER = register(ItemComponents.ASPECT_HOLDER,
            Aspect.REGISTRY_CODEC, Aspect.REGISTRY_STREAM_CODEC);

    public static final Holder<DataComponentType<CrystalAspectComponent>> CRYSTAL_ASPECT = register(ItemComponents.CRYSTAL_ASPECT,
            CrystalAspectComponent.CODEC, CrystalAspectComponent.STREAM_CODEC);

    public static final Holder<DataComponentType<CollectorMarkerComponent>> COLLECTOR_MARKER = marker(ItemComponents.COLLECTOR_MARKER, CollectorMarkerComponent.STREAM_CODEC);

    /* -------------------------------------------------------------------------------------------------------------- */

    public static void init(IEventBus bus) { REGISTRY.register(bus); }

    @SuppressWarnings("unchecked")
    private static <E, T extends DataComponentType<E>> Holder<T> register(ResourceLocation id, Codec<E> codec, StreamCodec<? super RegistryFriendlyByteBuf, E> streamCodec) {
        return (Holder<T>)REGISTRY.register(id.getPath(), () -> DataComponentType.<E>builder().persistent(codec).networkSynchronized(streamCodec).build());
    }

    @SuppressWarnings("unchecked")
    private static <E, T extends DataComponentType<E>> Holder<T> marker(ResourceLocation id, StreamCodec<? super RegistryFriendlyByteBuf, E> streamCodec) {
        return (Holder<T>)REGISTRY.register(id.getPath(), () -> DataComponentType.<E>builder().networkSynchronized(streamCodec).build());
    }
}
