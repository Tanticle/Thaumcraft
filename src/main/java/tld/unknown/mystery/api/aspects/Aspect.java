package tld.unknown.mystery.api.aspects;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lombok.AllArgsConstructor;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.RegistryFileCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;
import org.apache.commons.compress.utils.Lists;
import tld.unknown.mystery.api.ThaumcraftData;
import tld.unknown.mystery.data.aspects.PrimalAspects;
import tld.unknown.mystery.registries.ConfigDataRegistries;
import tld.unknown.mystery.util.Colour;
import tld.unknown.mystery.util.FallbackHolder;

import java.util.List;

@AllArgsConstructor
public class Aspect {

    public static final Aspect UNKNOWN = new Aspect(Colour.fromInteger(0xFFFFFF, false), Lists.newArrayList());
    public static final Holder<Aspect> UNKNOWN_HOLDER = new FallbackHolder<>(ThaumcraftData.Aspects.UNKNOWN, UNKNOWN);

    private final Colour colour;
    private final List<ResourceKey<Aspect>> components;

    public static Component getName(HolderLookup.Provider provider, ResourceKey<Aspect> key, boolean pureColor, boolean primalColor) {
        if (key == null)
            return Component.translatable("aspect.thaumcraft.untyped");
        ResourceLocation id = key.location();
        MutableComponent c = Component.translatable(id.toLanguageKey("aspect"));
        if (pureColor || primalColor) {
            Aspect a = ConfigDataRegistries.ASPECTS.get(provider, key);
            if (pureColor)
                return c.setStyle(Style.EMPTY.withColor(a.colour().argb32(false)));
            else
                return c.withStyle(PrimalAspects.getPrimalFormatting(key));
        }
        return c;
    }

    public Colour colour() {
        return colour;
    }

    public List<ResourceKey<Aspect>> components() {
        return components;
    }

    public static final MapCodec<Aspect> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
            Colour.CODEC.fieldOf("colour").forGetter(Aspect::colour),
            ResourceKey.codec(ThaumcraftData.Registries.ASPECT).listOf().optionalFieldOf("origin", Lists.newArrayList()).forGetter(Aspect::components)).apply(i, Aspect::new));
    public static final RegistryFileCodec<Aspect> REGISTRY_CODEC = RegistryFileCodec.create(ThaumcraftData.Registries.ASPECT, CODEC.codec());

    public static final StreamCodec<RegistryFriendlyByteBuf, Aspect> STREAM_CODEC = StreamCodec.composite(
            Colour.STREAM_CODEC, Aspect::colour, ResourceKey.streamCodec(ThaumcraftData.Registries.ASPECT).apply(ByteBufCodecs.list()), Aspect::components, Aspect::new);
    public static final StreamCodec<RegistryFriendlyByteBuf, Holder<Aspect>> REGISTRY_STREAM_CODEC = ByteBufCodecs.holder(ThaumcraftData.Registries.ASPECT, STREAM_CODEC);

    @AllArgsConstructor
    public enum Primal implements StringRepresentable {
        CHAOS(ThaumcraftData.Aspects.CHAOS),
        ORDER(ThaumcraftData.Aspects.ORDER),
        WATER(ThaumcraftData.Aspects.WATER),
        AIR(ThaumcraftData.Aspects.AIR),
        FIRE(ThaumcraftData.Aspects.FIRE),
        EARTH(ThaumcraftData.Aspects.EARTH);

        private final ResourceKey<Aspect> id;

        @Override
        public String getSerializedName() {
            return id.location().getPath();
        }
    }
}
