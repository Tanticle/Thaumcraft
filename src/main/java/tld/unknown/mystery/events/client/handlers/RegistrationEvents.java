package tld.unknown.mystery.events.client.handlers;

import net.minecraft.core.Holder;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterClientTooltipComponentFactoriesEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import tld.unknown.mystery.Thaumcraft;
import tld.unknown.mystery.api.aspects.Aspect;
import tld.unknown.mystery.client.rendering.ber.*;
import tld.unknown.mystery.client.rendering.entity.TrunkEntityRenderer;
import tld.unknown.mystery.client.rendering.ui.AspectTooltip;
import tld.unknown.mystery.client.screens.ArcaneWorkbenchScreen;
import tld.unknown.mystery.items.AbstractAspectItem;
import tld.unknown.mystery.registries.*;
import tld.unknown.mystery.util.Colour;
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

        e.registerEntityRenderer(ConfigEntities.LIVING_TRUNK.entityType(), TrunkEntityRenderer::new);
    }

    @SubscribeEvent
    public static void onBlockColorTintingRegister(RegisterColorHandlersEvent.Block e) {
        ConfigBlocks.CRYSTAL_COLONY.forEach((aspect, block) -> e.register((bs, level, pos, tintIndex) -> ConfigDataRegistries.ASPECTS.get(RegistryUtils.access(), aspect.getId()).colour().argb32(true), block.block()));
    }

    @SubscribeEvent
    public static void onItemColorTintingRegister(RegisterColorHandlersEvent.Item e) {
        e.register((stack, tintIndex) -> {
            if(tintIndex == 1) {
                Holder<Aspect> aspect = ((AbstractAspectItem)stack.getItem()).getHolder(stack);
                Colour c = aspect.value().colour();;
                return c.argb32(true);
            }
            return -1;
        }, ConfigItems.PHIAL.value());
        e.register((stack, tintIndex) -> ((AbstractAspectItem)stack.getItem()).getData(stack).colour().argb32(true), ConfigItems.VIS_CRYSTAL.value());
        ConfigBlocks.CRYSTAL_COLONY.forEach((aspect, block) -> e.register((stack, index) -> ConfigDataRegistries.ASPECTS.get(RegistryUtils.access(), aspect.getId()).colour().argb32(true), block.item()));
    }

}
