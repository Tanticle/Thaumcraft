package tld.unknown.mystery.data.aspects;

import com.google.common.collect.ImmutableMap;
import lombok.Getter;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.ResourceLocation;
import org.apache.commons.compress.utils.Lists;
import tld.unknown.mystery.api.aspects.Aspect;
import tld.unknown.mystery.api.ChaumtraftIDs;

@Getter
public class PrimalAspects extends Aspect {

    public static final ImmutableMap<ResourceLocation, Aspect> DEFAULTS = new ImmutableMap.Builder<ResourceLocation, Aspect>()
            .put(ChaumtraftIDs.Aspects.ORDER, new PrimalAspects("#D5D4EC", ChatFormatting.GRAY))
            .put(ChaumtraftIDs.Aspects.CHAOS, new PrimalAspects("#404040", ChatFormatting.DARK_GRAY))
            .put(ChaumtraftIDs.Aspects.EARTH, new PrimalAspects("#56C000", ChatFormatting.DARK_GREEN))
            .put(ChaumtraftIDs.Aspects.WATER, new PrimalAspects("#3CD4FC", ChatFormatting.DARK_AQUA))
            .put(ChaumtraftIDs.Aspects.AIR, new PrimalAspects("#FFFF7E", ChatFormatting.YELLOW))
            .put(ChaumtraftIDs.Aspects.FIRE, new PrimalAspects("#FF5A01", ChatFormatting.RED))
            .put(ChaumtraftIDs.Aspects.UNKNOWN, UNKNOWN)
            .build();

    private final ChatFormatting formatting;

    private PrimalAspects(String color, ChatFormatting formatting) {
        super(TextColor.parseColor(color).result().get(), Lists.newArrayList());
        this.formatting = formatting;
    }
}
