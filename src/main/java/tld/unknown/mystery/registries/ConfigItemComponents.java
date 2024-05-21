package tld.unknown.mystery.registries;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import tld.unknown.mystery.Thaumcraft;
import tld.unknown.mystery.items.components.AspectHolderComponent;
import tld.unknown.mystery.items.components.InfusionEnchantmentComponent;
import static tld.unknown.mystery.api.ThaumcraftData.ItemComponents;

public class ConfigItemComponents {

    private static final DeferredRegister<DataComponentType<?>> REGISTRY = DeferredRegister.create(BuiltInRegistries.DATA_COMPONENT_TYPE, Thaumcraft.MOD_ID);

    /* -------------------------------------------------------------------------------------------------------------- */

    public static final Holder<DataComponentType<InfusionEnchantmentComponent>> INFUSION_ENCHANTMENT = register(ItemComponents.INFUSION_ENCHANTMENTS,
            InfusionEnchantmentComponent.CODEC, InfusionEnchantmentComponent.STREAM_CODEC);
    public static final Holder<DataComponentType<AspectHolderComponent>> ASPECT_HOLDER = register(ItemComponents.ASPECT_HOLDER,
            AspectHolderComponent.CODEC, AspectHolderComponent.STREAM_CODEC);

    /* -------------------------------------------------------------------------------------------------------------- */

    public static void init(IEventBus bus) { REGISTRY.register(bus); }

    private static <E, T extends DataComponentType<E>> Holder<T> register(ResourceLocation id, Codec<E> codec, StreamCodec<? extends ByteBuf, E> streamCodec) {
        return REGISTRY.register(id.getPath(), () -> DataComponentType.builder().persistent(codec).networkSynchronized(streamCodec).build());
    }
}
