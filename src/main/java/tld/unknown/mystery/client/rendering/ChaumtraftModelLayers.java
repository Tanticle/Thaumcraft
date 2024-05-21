package tld.unknown.mystery.client.rendering;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import tld.unknown.mystery.Thaumcraft;
import tld.unknown.mystery.api.ChaumtraftIDs;
import tld.unknown.mystery.client.rendering.ber.models.RunicMatrixModel;
import tld.unknown.mystery.client.rendering.entity.models.TrunkModel;

@EventBusSubscriber(modid = Thaumcraft.MOD_ID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public final class ChaumtraftModelLayers {

    public static ModelLayerLocation TRUNK = new ModelLayerLocation(ChaumtraftIDs.Entities.TRAVELING_TRUNK, "main");
    public static ModelLayerLocation RUNIC_MATRIX = new ModelLayerLocation(ChaumtraftIDs.Blocks.RUNIC_MATRIX, "main");

    @SubscribeEvent
    public static void onLayerBake(EntityRenderersEvent.RegisterLayerDefinitions e) {
        e.registerLayerDefinition(TRUNK, TrunkModel::createBodyLayer);
        e.registerLayerDefinition(RUNIC_MATRIX, RunicMatrixModel::createBodyLayer);
    }
}
