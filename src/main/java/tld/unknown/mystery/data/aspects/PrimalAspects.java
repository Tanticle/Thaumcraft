package tld.unknown.mystery.data.aspects;

import com.google.common.collect.ImmutableMap;
import lombok.Getter;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.ResourceLocation;
import org.apache.commons.compress.utils.Lists;
import tld.unknown.mystery.api.aspects.Aspect;
import tld.unknown.mystery.api.ThaumcraftData;

@Getter
public class PrimalAspects extends Aspect {

    public static final ImmutableMap<ResourceLocation, Aspect> DEFAULTS = new ImmutableMap.Builder<ResourceLocation, Aspect>()
            .put(ThaumcraftData.Aspects.ORDER, new PrimalAspects("#D5D4EC", ChatFormatting.GRAY))
            .put(ThaumcraftData.Aspects.CHAOS, new PrimalAspects("#404040", ChatFormatting.DARK_GRAY))
            .put(ThaumcraftData.Aspects.EARTH, new PrimalAspects("#56C000", ChatFormatting.DARK_GREEN))
            .put(ThaumcraftData.Aspects.WATER, new PrimalAspects("#3CD4FC", ChatFormatting.DARK_AQUA))
            .put(ThaumcraftData.Aspects.AIR, new PrimalAspects("#FFFF7E", ChatFormatting.YELLOW))
            .put(ThaumcraftData.Aspects.FIRE, new PrimalAspects("#FF5A01", ChatFormatting.RED))
            .put(ThaumcraftData.Aspects.UNKNOWN, UNKNOWN)
            .build();

    private final ChatFormatting formatting;

    private PrimalAspects(String color, ChatFormatting formatting) {
        super(TextColor.parseColor(color).result().get(), Lists.newArrayList());
        this.formatting = formatting;
    }
}
