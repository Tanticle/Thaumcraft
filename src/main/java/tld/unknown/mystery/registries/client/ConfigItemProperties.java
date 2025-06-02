package tld.unknown.mystery.registries.client;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.MapCodec;
import net.minecraft.client.renderer.item.properties.conditional.ConditionalItemModelProperty;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterConditionalItemModelPropertyEvent;
import tld.unknown.mystery.Thaumcraft;
import tld.unknown.mystery.api.ThaumcraftData;
import tld.unknown.mystery.items.ItemModelProperties;
import tld.unknown.mystery.util.ReflectionUtils;

@OnlyIn(Dist.CLIENT)
@EventBusSubscriber(modid = Thaumcraft.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public final class ConfigItemProperties {

    /* -------------------------------------------------------------------------------------------------------------- */

    public static final Pair<ResourceLocation, MapCodec<? extends ItemModelProperties.HasData>> HAS_ASPECT = Pair.of(ThaumcraftData.ItemProperties.HAS_ASPECT, ItemModelProperties.HasData.CODEC);

    /* -------------------------------------------------------------------------------------------------------------- */

    @SubscribeEvent
    public static void onConditionalPropertyRegisterEvent(RegisterConditionalItemModelPropertyEvent event) {
        /*ReflectionUtils.getAllStaticsOfType(ConfigItemProperties.class, Pair.class).forEach(p -> {
            if(p.getSecond() instanceof ConditionalItemModelProperty property)
                event.register((ResourceLocation)p.getFirst(), property.type());
        });*/
        event.register(HAS_ASPECT.getFirst(), HAS_ASPECT.getSecond());
    }

}
