package tld.unknown.mystery.api.aspects;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lombok.AllArgsConstructor;
import lombok.Data;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.apache.commons.compress.utils.Lists;
import tld.unknown.mystery.api.ChaumtraftIDs;
import tld.unknown.mystery.data.ThaumcraftData;
import tld.unknown.mystery.data.aspects.PrimalAspects;
import tld.unknown.mystery.util.codec.EnumCodec;

import java.util.Arrays;
import java.util.List;

@Data
public class Aspect {

    public static final Aspect UNKNOWN = new Aspect(TextColor.fromRgb(0xFFFFFF), Lists.newArrayList());

    private final TextColor color;
    private final List<ResourceLocation> origin;

    public static Component getName(ResourceLocation id, boolean pureColor, boolean primalColor) {
        if(id == null) {
            return Component.translatable("aspect.thaumcraft.untyped");
        }
        MutableComponent c = Component.translatable("aspect." + id.getNamespace() + "." + id.getPath());
        Aspect a = ThaumcraftData.ASPECTS.getOptional(id).orElse(Aspect.UNKNOWN);
        if(pureColor) {
            return c.setStyle(Style.EMPTY.withColor(a.getColor()));
        } else if(primalColor && a instanceof PrimalAspects pa) {
            return c.withStyle(pa.getFormatting());
        } else {
            return c;
        }
    }

    public static ResourceLocation getTexture(ResourceLocation id, boolean sdf) {

        return new ResourceLocation(id.getNamespace(), "textures/aspects/" + id.getPath() + (sdf ? "_sdf.png" : ".png"));
    }

    public static Codec<Aspect> CODEC = RecordCodecBuilder.create(i -> i.group(
            TextColor.CODEC.fieldOf("color").forGetter(Aspect::getColor),
            ResourceLocation.CODEC.listOf().optionalFieldOf("origin", Lists.newArrayList()).forGetter(Aspect::getOrigin)
    ).apply(i, Aspect::new));

    @AllArgsConstructor
    public enum Primal implements EnumCodec.Values {
        CHAOS(ChaumtraftIDs.Aspects.CHAOS),
        ORDER(ChaumtraftIDs.Aspects.ORDER),
        WATER(ChaumtraftIDs.Aspects.WATER),
        AIR(ChaumtraftIDs.Aspects.AIR),
        FIRE(ChaumtraftIDs.Aspects.FIRE),
        EARTH(ChaumtraftIDs.Aspects.EARTH);

        private final ResourceLocation id;

        @Override
        public String getSerializedName() {
            return id.getPath();
        }
    }
}
