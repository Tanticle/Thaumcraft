package art.arcane.thaumcraft.registries;

import art.arcane.thaumcraft.api.components.FortressFaceplateComponent;
import art.arcane.thaumcraft.api.components.GolemConfiguration;
import art.arcane.thaumcraft.data.golemancy.SealType;
import com.mojang.serialization.Codec;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.UUIDUtil;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.Unit;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.network.codec.NeoForgeStreamCodecs;
import net.neoforged.neoforge.registries.DeferredRegister;
import art.arcane.thaumcraft.Thaumcraft;
import art.arcane.thaumcraft.api.aspects.Aspect;
import art.arcane.thaumcraft.api.components.InfusionEnchantmentComponent;

import java.util.UUID;

import static art.arcane.thaumcraft.api.ThaumcraftData.ItemComponents;

public class ConfigItemComponents {

    private static final DeferredRegister<DataComponentType<?>> REGISTRY = DeferredRegister.create(BuiltInRegistries.DATA_COMPONENT_TYPE, Thaumcraft.MOD_ID);

    /* -------------------------------------------------------------------------------------------------------------- */

    public static final Holder<DataComponentType<InfusionEnchantmentComponent>> INFUSION_ENCHANTMENT = register(ItemComponents.INFUSION_ENCHANTMENTS,
            InfusionEnchantmentComponent.CODEC, InfusionEnchantmentComponent.STREAM_CODEC);

    public static final Holder<DataComponentType<Holder<Aspect>>> ASPECT_HOLDER = register(ItemComponents.ASPECT_HOLDER,
            Aspect.REGISTRY_CODEC, Aspect.REGISTRY_STREAM_CODEC);


    public static final Holder<DataComponentType<UUID>> COLLECTOR_MARKER = marker(ItemComponents.COLLECTOR_MARKER, UUIDUtil.STREAM_CODEC);

    public static final Holder<DataComponentType<Direction.Axis>> AXIS = register(ItemComponents.AXIS, Direction.Axis.CODEC, NeoForgeStreamCodecs.enumCodec(Direction.Axis.class));

    public static final Holder<DataComponentType<Integer>> WARPING = register(ItemComponents.WARPING, ExtraCodecs.POSITIVE_INT, ByteBufCodecs.VAR_INT);
    public static final Holder<DataComponentType<Float>> VIS_COST_MODIFIER = register(ItemComponents.VIS_COST_MODIFIER, Codec.FLOAT, ByteBufCodecs.FLOAT);

	public static final Holder<DataComponentType<Integer>> VIS_CHARGE_MAX = register(ItemComponents.VIS_CHARGE_MAX, ExtraCodecs.POSITIVE_INT, ByteBufCodecs.VAR_INT);
	public static final Holder<DataComponentType<Integer>> VIS_CHARGE = register(ItemComponents.VIS_CHARGE, ExtraCodecs.NON_NEGATIVE_INT, ByteBufCodecs.VAR_INT);

	public static final Holder<DataComponentType<Integer>> TIMER = register(ItemComponents.TIMER, ExtraCodecs.NON_NEGATIVE_INT, ByteBufCodecs.VAR_INT);
	public static final Holder<DataComponentType<Integer>> DYE_COLOR = register(ItemComponents.DYE_COLOR, Codec.INT, ByteBufCodecs.VAR_INT);

	public static final Holder<DataComponentType<FortressFaceplateComponent>> ARMOR_FORTRESS_FACEPLATE = register(ItemComponents.ARMOR_FORTRESS_FACEPLATE, FortressFaceplateComponent.CODEC, FortressFaceplateComponent.STREAM_CODEC);
	public static final Holder<DataComponentType<Unit>> GOGGLE_SIGHT = register(ItemComponents.GOGGLE_SIGHT, Codec.unit(Unit.INSTANCE), StreamCodec.unit(Unit.INSTANCE));

	public static final Holder<DataComponentType<GolemConfiguration>> GOLEM_CONFIG = register(ItemComponents.GOLEM_CONFIG, GolemConfiguration.CODEC, GolemConfiguration.STREAM_CODEC);
	public static final Holder<DataComponentType<ResourceKey<SealType>>> SEAL_TYPE = register(ItemComponents.SEAL_TYPE,
			ResourceKey.codec(art.arcane.thaumcraft.api.ThaumcraftData.Registries.SEAL_TYPE),
			ResourceKey.streamCodec(art.arcane.thaumcraft.api.ThaumcraftData.Registries.SEAL_TYPE));

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
