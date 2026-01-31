package art.arcane.thaumcraft.api.capabilities;

import net.minecraft.resources.ResourceLocation;

import java.util.Map;

public interface IInfusionAttachment {

    boolean hasEnchantment(ResourceLocation id);

    int getEnchantmentLevel(ResourceLocation id);

    Map<ResourceLocation, Integer> getEnchantments();

    IInfusionAttachment addEnchantment(ResourceLocation id, int level);

    boolean removeEnchantment(ResourceLocation id);
}
