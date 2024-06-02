package tld.unknown.mystery.events.client.handlers;

import net.minecraft.client.Minecraft;
import net.minecraft.core.RegistryAccess;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterClientTooltipComponentFactoriesEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import tld.unknown.mystery.Thaumcraft;
import tld.unknown.mystery.blocks.CrystalBlock;
import tld.unknown.mystery.client.rendering.ber.*;
import tld.unknown.mystery.client.rendering.entity.TrunkEntityRenderer;
import tld.unknown.mystery.client.rendering.ui.AspectTooltip;
import tld.unknown.mystery.client.screens.ArcaneWorkbenchScreen;
import tld.unknown.mystery.items.AbstractAspectItem;
import tld.unknown.mystery.items.blocks.CrystalBlockItem;
import tld.unknown.mystery.registries.*;

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
        e.register((bs, level, pos, tintIndex) -> ConfigDataRegistries.ASPECTS.get(access(), bs.getValue(CrystalBlock.ASPECT).getId()).colour().rgba32(true), ConfigBlocks.CRYSTAL_COLONY.block());
    }

    @SubscribeEvent
    public static void onItemColorTintingRegister(RegisterColorHandlersEvent.Item e) {
        e.register((stack, tintIndex) -> tintIndex == 1 ? ((AbstractAspectItem)stack.getItem()).getData(stack).colour().rgba32(true) : -1, ConfigItems.PHIAL.value());
        e.register((stack, tintIndex) -> ((AbstractAspectItem)stack.getItem()).getData(stack).colour().rgba32(true), ConfigItems.VIS_CRYSTAL.value());

        e.register((stack, tintIndex) -> {
            CrystalBlock.CrystalAspect aspect = ((CrystalBlockItem)stack.getItem()).getData(stack);
            if(aspect == null) {
                return 0xFFFFFFFF;
            }
            return ConfigDataRegistries.ASPECTS.get(access(), aspect.getId()).colour().rgba32(true);
        }, ConfigBlocks.CRYSTAL_COLONY.item());
    }

    private static RegistryAccess access() {
        return Minecraft.getInstance().getConnection().registryAccess();
    }
}
