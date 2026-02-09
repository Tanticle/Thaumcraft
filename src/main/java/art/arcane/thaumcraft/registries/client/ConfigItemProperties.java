package art.arcane.thaumcraft.registries.client;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.MapCodec;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterConditionalItemModelPropertyEvent;
import art.arcane.thaumcraft.Thaumcraft;
import art.arcane.thaumcraft.api.ThaumcraftData;
import art.arcane.thaumcraft.items.ItemModelProperties;

@OnlyIn(Dist.CLIENT)
@EventBusSubscriber(modid = Thaumcraft.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public final class ConfigItemProperties {

    /* -------------------------------------------------------------------------------------------------------------- */

    public static final Pair<ResourceLocation, MapCodec<? extends ItemModelProperties.HasData>> HAS_ASPECT = Pair.of(ThaumcraftData.ItemProperties.HAS_ASPECT, ItemModelProperties.HasData.CODEC);
    public static final Pair<ResourceLocation, MapCodec<? extends ItemModelProperties.SealTypeCheck>> SEAL_TYPE_CHECK = Pair.of(ThaumcraftData.ItemProperties.SEAL_TYPE_CHECK, ItemModelProperties.SealTypeCheck.CODEC);

    /* -------------------------------------------------------------------------------------------------------------- */

    @SubscribeEvent
    public static void onConditionalPropertyRegisterEvent(RegisterConditionalItemModelPropertyEvent event) {
        event.register(HAS_ASPECT.getFirst(), HAS_ASPECT.getSecond());
        event.register(SEAL_TYPE_CHECK.getFirst(), SEAL_TYPE_CHECK.getSecond());
    }

}
