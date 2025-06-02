package tld.unknown.mystery.events.client.handlers;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterClientTooltipComponentFactoriesEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import tld.unknown.mystery.Thaumcraft;
import tld.unknown.mystery.api.ThaumcraftData;
import tld.unknown.mystery.client.rendering.ber.*;
import tld.unknown.mystery.client.rendering.ui.AspectTooltip;
import tld.unknown.mystery.client.screens.ArcaneWorkbenchScreen;
import tld.unknown.mystery.client.tints.AspectItemTintSource;
import tld.unknown.mystery.registries.*;
import tld.unknown.mystery.util.RegistryUtils;

@EventBusSubscriber(modid = Thaumcraft.MOD_ID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public class RegistrationEvents {

    @SubscribeEvent
    public static void onMenuScreenRegister(RegisterMenuScreensEvent e) {
        e.register(ConfigMenus.ARCANE_WORKBENCH.get(), ArcaneWorkbenchScreen::new);
    }

    @SubscribeEvent
    public static void onTooltipComponentRegister(RegisterClientTooltipComponentFactoriesEvent e) {
        e.register(AspectTooltip.Data.class, d -> new AspectTooltip(d.aspects()));
    }

    @SubscribeEvent
    public static void onBlockEntityRendererRegister(EntityRenderersEvent.RegisterRenderers e) {
        e.registerBlockEntityRenderer(ConfigBlockEntities.CRUCIBLE.entityType(), CrucibleBER::new);
        e.registerBlockEntityRenderer(ConfigBlockEntities.RUNIC_MATRIX.entityType(), RunicMatrixBER::new);
        e.registerBlockEntityRenderer(ConfigBlockEntities.PEDESTAL.entityType(), PedestalBER::new);
        e.registerBlockEntityRenderer(ConfigBlockEntities.CREATIVE_ASPECT_SOURCE.entityType(), CreativeAspectSourceBER::new);
        e.registerBlockEntityRenderer(ConfigBlockEntities.JAR.entityType(), JarBER::new);

        //e.registerEntityRenderer(ConfigEntities.LIVING_TRUNK.entityType(), TrunkEntityRenderer::new);
    }

    @SubscribeEvent
    public static void onBlockColorTintingRegister(RegisterColorHandlersEvent.Block e) {
        ConfigBlocks.CRYSTAL_COLONY.forEach((aspect, block) -> e.register((bs, level, pos, tintIndex) -> ConfigDataRegistries.ASPECTS.get(RegistryUtils.access(), aspect.getId()).colour().argb32(true), block.block()));
    }

    @SubscribeEvent
    public static void onItemColorTintingRegister(RegisterColorHandlersEvent.ItemTintSources e) {
        e.register(ThaumcraftData.TintSources.ASPECT_ITEM, AspectItemTintSource.CODEC);
    }
}
