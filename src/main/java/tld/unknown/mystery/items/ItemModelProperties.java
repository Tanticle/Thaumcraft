package tld.unknown.mystery.items;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.properties.conditional.ConditionalItemModelProperty;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import tld.unknown.mystery.util.simple.DataDependentItem;

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
}
