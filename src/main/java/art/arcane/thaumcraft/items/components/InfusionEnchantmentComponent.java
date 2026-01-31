package art.arcane.thaumcraft.items.components;

import com.mojang.serialization.Codec;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.StringRepresentable;
import net.neoforged.neoforge.network.codec.NeoForgeStreamCodecs;
import art.arcane.thaumcraft.api.enums.InfusionEnchantments;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public record InfusionEnchantmentComponent(Map<InfusionEnchantments, Byte> enchantments) {
    public static final Codec<InfusionEnchantmentComponent> CODEC = Codec
            .unboundedMap(StringRepresentable.fromValues(InfusionEnchantments::values), Codec.BYTE)
            .xmap(InfusionEnchantmentComponent::new, InfusionEnchantmentComponent::enchantments);
    public static final StreamCodec<FriendlyByteBuf, InfusionEnchantmentComponent> STREAM_CODEC = ByteBufCodecs
            .<FriendlyByteBuf, InfusionEnchantments, Byte, Map<InfusionEnchantments, Byte>>map(
                    HashMap::new,
                    NeoForgeStreamCodecs.enumCodec(InfusionEnchantments.class),
                    ByteBufCodecs.BYTE)
            .map(InfusionEnchantmentComponent::new, InfusionEnchantmentComponent::enchantments);

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InfusionEnchantmentComponent that)) return false;
        return Objects.equals(enchantments, that.enchantments);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(enchantments);
    }
}
