package tld.unknown.mystery.data.aspects;

import com.google.common.collect.ImmutableMap;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.ChatFormatting;
import net.minecraft.resources.ResourceKey;
import org.apache.commons.compress.utils.Lists;
import tld.unknown.mystery.api.ThaumcraftData;
import tld.unknown.mystery.api.aspects.Aspect;
import tld.unknown.mystery.util.Colour;

@AllArgsConstructor
public enum PrimalAspects {

    EARTH(ChatFormatting.DARK_GREEN, ThaumcraftData.Aspects.EARTH),
    WATER(ChatFormatting.DARK_AQUA, ThaumcraftData.Aspects.WATER),
    FIRE(ChatFormatting.RED, ThaumcraftData.Aspects.FIRE),
    AIR(ChatFormatting.YELLOW, ThaumcraftData.Aspects.AIR),
    ORDER(ChatFormatting.GRAY, ThaumcraftData.Aspects.ORDER),
    CHAOS(ChatFormatting.DARK_GRAY, ThaumcraftData.Aspects.CHAOS);

    private final ChatFormatting formatting;
    private final ResourceKey<Aspect> resourceKey;

    public static ChatFormatting getPrimalFormatting(ResourceKey<Aspect> aspect) {
        for (PrimalAspects value : values()) {
            if(value.resourceKey.equals(aspect))
                return value.formatting;
        }
        return ChatFormatting.WHITE;
    }
}
