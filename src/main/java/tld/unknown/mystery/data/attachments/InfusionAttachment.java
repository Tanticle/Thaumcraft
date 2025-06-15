package tld.unknown.mystery.data.attachments;

import com.mojang.serialization.Codec;
import lombok.AllArgsConstructor;
import net.minecraft.resources.ResourceLocation;
import tld.unknown.mystery.api.capabilities.IInfusionAttachment;

import java.util.Map;

@AllArgsConstructor
public class InfusionAttachment implements IInfusionAttachment {

    private final Map<ResourceLocation, Integer> enchantments;

    @Override
    public boolean hasEnchantment(ResourceLocation id) {
        return getEnchantmentLevel(id) > 0;
    }

    @Override
    public int getEnchantmentLevel(ResourceLocation id) {
        return enchantments.getOrDefault(id, 0);
    }

    @Override
    public Map<ResourceLocation, Integer> getEnchantments() {
        return enchantments;
    }

    @Override
    public IInfusionAttachment addEnchantment(ResourceLocation id, int level) {
        enchantments.put(id, level);
        return this;
    }

    @Override
    public boolean removeEnchantment(ResourceLocation id) {
        return enchantments.remove(id) != null;
    }

    public static final Codec<InfusionAttachment> CODEC = Codec.unboundedMap(ResourceLocation.CODEC, Codec.INT).xmap(InfusionAttachment::new, a -> a.enchantments);
}
