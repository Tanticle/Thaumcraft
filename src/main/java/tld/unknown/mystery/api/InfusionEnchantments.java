package tld.unknown.mystery.api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import tld.unknown.mystery.util.codec.EnumCodec;

import java.util.Arrays;

import static tld.unknown.mystery.api.ThaumcraftData.Enchantments;

@Getter
@AllArgsConstructor
public enum InfusionEnchantments implements EnumCodec.Values {

    COLLECTOR(Enchantments.COLLECTOR, 1),
    BURROWING(Enchantments.BURROWING, 1),
    REFINING(Enchantments.REFINING, 4),
    SOUNDING(Enchantments.SOUNDING, 4),
    DESTRUCTIVE(Enchantments.DESTRUCTIVE, 1),
    ARCING(Enchantments.ARCING, 4),
    HARVESTER(Enchantments.HARVESTER, 5),
    LAMPLIGHT(Enchantments.LAMPLIGHT, 1);

    private final ResourceLocation id;
    private final int maxLevel;

    public static int getMaxLevel(ResourceLocation id) {
        return Arrays.stream(values()).filter(e -> e.id.equals(id)).map(e -> e.maxLevel).findFirst().orElse(0);
    }

    public MutableComponent getTranslationKey() {
        return Component.translatable(String.format("enchantment.%s.%s", this.id.getNamespace(), this.id.getPath()));
    }

    @Override
    public String getSerializedName() {
        return id.toString();
    }
}
