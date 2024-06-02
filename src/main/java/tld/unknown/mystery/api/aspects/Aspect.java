package tld.unknown.mystery.api.aspects;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lombok.AllArgsConstructor;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.RegistryFileCodec;
import net.minecraft.resources.ResourceLocation;
import org.apache.commons.compress.utils.Lists;
import tld.unknown.mystery.api.ThaumcraftData;
import tld.unknown.mystery.data.aspects.PrimalAspects;
import tld.unknown.mystery.util.Colour;
import tld.unknown.mystery.util.FallbackHolder;
import tld.unknown.mystery.util.RegistryUtils;
import tld.unknown.mystery.util.codec.EnumCodec;

import java.util.List;

@AllArgsConstructor
public class Aspect {

    public static final Aspect UNKNOWN = new Aspect(Colour.fromInteger(0xFFFFFF, false), Lists.newArrayList());
    public static final Holder<Aspect> UNKNOQN_HOLDER = new FallbackHolder<>(ThaumcraftData.Registries.ASPECT, ThaumcraftData.Aspects.UNKNOWN, UNKNOWN);

    private final Colour colour;
    private final List<ResourceLocation> components;

    public static Component getName(Holder<Aspect> holder, boolean pureColor, boolean primalColor) {
        if(holder == null)
            return Component.translatable("aspect.thaumcraft.untyped");
        ResourceLocation id = RegistryUtils.getKey(holder);
        MutableComponent c = Component.translatable("aspect." + id.getNamespace() + "." + id.getPath());
        Aspect a = holder.value();
        if(pureColor)
            return c.setStyle(Style.EMPTY.withColor(a.colour().rgba32(false)));
        else if(primalColor && a instanceof PrimalAspects pa)
            return c.withStyle(pa.getFormatting());
        else
            return c;
    }

    public Colour colour() {
        return colour;
    }

    public List<ResourceLocation> components() {
        return components;
    }

    public static final MapCodec<Aspect> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
            Colour.CODEC.fieldOf("colour").forGetter(Aspect::colour),
            ResourceLocation.CODEC.listOf().optionalFieldOf("origin", Lists.newArrayList()).forGetter(Aspect::components)).apply(i, Aspect::new));
    public static final RegistryFileCodec<Aspect> REGISTRY_CODEC = RegistryFileCodec.create(ThaumcraftData.Registries.ASPECT, CODEC.codec());

    public static final StreamCodec<RegistryFriendlyByteBuf, Aspect> STREAM_CODEC = StreamCodec.composite(
        Colour.STREAM_CODEC, Aspect::colour, ResourceLocation.STREAM_CODEC.apply(ByteBufCodecs.list()), Aspect::components, Aspect::new);
    public static final StreamCodec<RegistryFriendlyByteBuf, Holder<Aspect>> REGISTRY_STREAM_CODEC = ByteBufCodecs.holder(ThaumcraftData.Registries.ASPECT, STREAM_CODEC);

    @AllArgsConstructor
    public enum Primal implements EnumCodec.Values {
        CHAOS(ThaumcraftData.Aspects.CHAOS),
        ORDER(ThaumcraftData.Aspects.ORDER),
        WATER(ThaumcraftData.Aspects.WATER),
        AIR(ThaumcraftData.Aspects.AIR),
        FIRE(ThaumcraftData.Aspects.FIRE),
        EARTH(ThaumcraftData.Aspects.EARTH);

        private final ResourceLocation id;

        @Override
        public String getSerializedName() {
            return id.getPath();
        }
    }
}
