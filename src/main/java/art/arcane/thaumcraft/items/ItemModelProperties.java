package art.arcane.thaumcraft.items;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.properties.conditional.ConditionalItemModelProperty;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import art.arcane.thaumcraft.data.golemancy.SealType;
import art.arcane.thaumcraft.registries.ConfigItemComponents;
import art.arcane.thaumcraft.util.simple.DataDependentItem;

public final class ItemModelProperties {

    public static class HasData implements ConditionalItemModelProperty {

        public static final MapCodec<HasData> CODEC = MapCodec.unit(HasData::new);

        @Override
        public boolean get(ItemStack itemStack, @Nullable ClientLevel clientLevel, @Nullable LivingEntity livingEntity, int i, ItemDisplayContext itemDisplayContext) {
            return itemStack.getItem() instanceof DataDependentItem<?> item && item.hasData(itemStack);
        }

        @Override
        public MapCodec<? extends ConditionalItemModelProperty> type() {
            return CODEC;
        }
    }

    public static class SealTypeCheck implements ConditionalItemModelProperty {

        public static final MapCodec<SealTypeCheck> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
                com.mojang.serialization.Codec.STRING.fieldOf("seal_type").forGetter(s -> s.sealTypeName)
        ).apply(i, SealTypeCheck::new));

        private final String sealTypeName;

        public SealTypeCheck(String sealTypeName) {
            this.sealTypeName = sealTypeName;
        }

        @Override
        public boolean get(ItemStack itemStack, @Nullable ClientLevel clientLevel, @Nullable LivingEntity livingEntity, int i, ItemDisplayContext itemDisplayContext) {
            ResourceKey<SealType> sealType = itemStack.get(ConfigItemComponents.SEAL_TYPE.value());
            return sealType != null && sealType.location().getPath().equals(sealTypeName);
        }

        @Override
        public MapCodec<? extends ConditionalItemModelProperty> type() {
            return CODEC;
        }
    }
}
