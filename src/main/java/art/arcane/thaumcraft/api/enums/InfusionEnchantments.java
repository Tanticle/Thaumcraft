package art.arcane.thaumcraft.api.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.ItemStack;
import art.arcane.thaumcraft.api.components.InfusionEnchantmentComponent;
import art.arcane.thaumcraft.registries.ConfigItemComponents;

import java.util.Arrays;

import static art.arcane.thaumcraft.api.ThaumcraftData.Enchantments;

@Getter
@AllArgsConstructor
public enum InfusionEnchantments implements StringRepresentable {

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

    public static boolean hasEnchantment(ItemStack stack, InfusionEnchantments enchantment) {
        InfusionEnchantmentComponent component = stack.get(ConfigItemComponents.INFUSION_ENCHANTMENT.value());
        return component != null && component.enchantments().containsKey(enchantment);
    }

    public static InfusionEnchantments getFromId(ResourceLocation rl) {
        return Arrays.stream(values()).filter(e -> e.id.equals(rl)).findFirst().orElse(null);
    }

    public MutableComponent getTranslationKey() {
        return Component.translatable(String.format("enchantment.%s.%s", this.id.getNamespace(), this.id.getPath()));
    }

    @Override
    public String getSerializedName() {
        return id.toString();
    }
}
