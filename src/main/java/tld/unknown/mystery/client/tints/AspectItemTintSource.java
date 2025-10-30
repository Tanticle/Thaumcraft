package tld.unknown.mystery.client.tints;

import com.mojang.serialization.MapCodec;
import net.minecraft.client.color.item.ItemTintSource;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import tld.unknown.mystery.api.ThaumcraftData;
import tld.unknown.mystery.api.aspects.Aspect;
import tld.unknown.mystery.api.aspects.AspectContainerItem;
import tld.unknown.mystery.data.aspects.AspectList;
import tld.unknown.mystery.data.aspects.AspectRegistry;
import tld.unknown.mystery.items.AbstractAspectItem;
import tld.unknown.mystery.registries.ConfigDataRegistries;
import tld.unknown.mystery.util.Colour;

public class AspectItemTintSource implements ItemTintSource {

    public static final MapCodec<AspectItemTintSource> CODEC = MapCodec.unit(AspectItemTintSource::new);

    @Override
    public int calculate(ItemStack itemStack, @Nullable ClientLevel clientLevel, @Nullable LivingEntity livingEntity) {
        if(itemStack.getItem() instanceof AbstractAspectItem item) {
            Holder<Aspect> aspect = item.getHolder(itemStack);
            Colour c = aspect.value().colour();
            return c.argb32(true);
        } else if (itemStack.getItem() instanceof AspectContainerItem item) {
            AspectList list = item.getAspects(itemStack);
            Colour c = ConfigDataRegistries.ASPECTS.get(clientLevel.registryAccess(), list.aspectsPresent().get(0)).colour();
            return c.argb32(true);
        }
        return 0xFFFFFFFF;
    }

    @Override
    public MapCodec<? extends ItemTintSource> type() {
        return CODEC;
    }
}
