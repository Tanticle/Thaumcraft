package art.arcane.thaumcraft.registries.client;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import art.arcane.thaumcraft.Thaumcraft;
import art.arcane.thaumcraft.api.ThaumcraftData;
import art.arcane.thaumcraft.client.rendering.ber.models.RunicMatrixModel;

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
