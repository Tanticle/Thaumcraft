package tld.unknown.mystery.registries.client;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import tld.unknown.mystery.Thaumcraft;
import tld.unknown.mystery.api.ThaumcraftData;
import tld.unknown.mystery.client.rendering.ber.models.RunicMatrixModel;

@EventBusSubscriber(modid = Thaumcraft.MOD_ID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public final class ConfigModelLayers {

    public static ModelLayerLocation TRUNK = new ModelLayerLocation(ThaumcraftData.Entities.TRAVELING_TRUNK.location(), "main");
    public static ModelLayerLocation RUNIC_MATRIX = new ModelLayerLocation(ThaumcraftData.Blocks.RUNIC_MATRIX, "main");

    @SubscribeEvent
    public static void onLayerBake(EntityRenderersEvent.RegisterLayerDefinitions e) {
        //e.registerLayerDefinition(TRUNK, TrunkModel::createBodyLayer);
        e.registerLayerDefinition(RUNIC_MATRIX, RunicMatrixModel::createBodyLayer);
    }
}
