package tld.unknown.mystery.registries.client;

import lombok.Getter;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.client.renderer.item.ItemPropertyFunction;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import tld.unknown.mystery.api.ThaumcraftData;
import tld.unknown.mystery.registries.ConfigItems;
import tld.unknown.mystery.util.ReflectionUtils;
import tld.unknown.mystery.util.simple.DataDependentItem;

@OnlyIn(Dist.CLIENT)
public final class ConfigItemProperties {

    /* -------------------------------------------------------------------------------------------------------------- */

    public static final PropertyObject ASPECT_HOLDER_PRESENT = register(ThaumcraftData.ItemProperties.ASPECT_HOLDER_PRESENT, ConfigItems.PHIAL, DataDependentItem.HAS_META_GETTER);

    /* -------------------------------------------------------------------------------------------------------------- */

    public static void init(FMLClientSetupEvent e) {
        e.enqueueWork(() -> ReflectionUtils.getAllStaticsOfType(ConfigItemProperties.class, PropertyObject.class).forEach(o -> ItemProperties.register(o.getItem().value(), o.getId(), o.getFunction())));
    }

    private static PropertyObject register(ResourceLocation id, Holder<? extends Item> item, ItemPropertyFunction function) {
        return new PropertyObject(id, item, function);
    }

    @Getter
    public static class PropertyObject {

        private final ResourceLocation id;
        private final Holder<? extends Item> item;
        private final ItemPropertyFunction function;

        private PropertyObject(ResourceLocation id, Holder<? extends Item> item, ItemPropertyFunction function) {
            this.id = id;
            this.item = item;
            this.function = function;
        }
    }
}
