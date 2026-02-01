package art.arcane.thaumcraft.events.client.handlers;

import art.arcane.thaumcraft.client.fx.particles.SmokeSpiralParticle;
import art.arcane.thaumcraft.client.rendering.ber.*;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.entity.ItemEntityRenderer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterClientTooltipComponentFactoriesEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import art.arcane.thaumcraft.Thaumcraft;
import art.arcane.thaumcraft.api.ThaumcraftData;
import art.arcane.thaumcraft.client.rendering.ui.AspectTooltip;
import art.arcane.thaumcraft.client.screens.ArcaneWorkbenchScreen;
import art.arcane.thaumcraft.client.tints.AspectItemTintSource;
import art.arcane.thaumcraft.registries.ConfigBlockEntities;
import art.arcane.thaumcraft.registries.ConfigBlocks;
import art.arcane.thaumcraft.registries.ConfigDataRegistries;
import art.arcane.thaumcraft.registries.ConfigEntities;
import art.arcane.thaumcraft.registries.ConfigMenus;
import art.arcane.thaumcraft.registries.ConfigParticles;
import art.arcane.thaumcraft.util.RegistryUtils;

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
        e.registerBlockEntityRenderer(ConfigBlockEntities.DIOPTRA.entityType(), DioptraBER::new);

        //e.registerEntityRenderer(ConfigEntities.LIVING_TRUNK.entityType(), TrunkEntityRenderer::new);
        e.registerEntityRenderer(ConfigEntities.MOVING_ITEM.entityType(), ItemEntityRenderer::new);
    }

    @SubscribeEvent
    public static void onBlockColorTintingRegister(RegisterColorHandlersEvent.Block e) {
        ConfigBlocks.CRYSTAL_COLONY.forEach((aspect, block) -> e.register((bs, level, pos, tintIndex) -> ConfigDataRegistries.ASPECTS.get(RegistryUtils.access(), aspect.getId()).colour().argb32(true), block.block()));

        e.register((state, level, pos, tintIndex) -> {
            if (level != null && pos != null) {
                return BiomeColors.getAverageFoliageColor(level, pos);
            }
            return 0x48B518;
        }, ConfigBlocks.GREATWOOD_LEAVES.block());
    }

    @SubscribeEvent
    public static void onItemColorTintingRegister(RegisterColorHandlersEvent.ItemTintSources e) {
        e.register(ThaumcraftData.TintSources.ASPECT_ITEM, AspectItemTintSource.CODEC);
    }

    @SubscribeEvent
    public static void onParticleProvidersRegister(RegisterParticleProvidersEvent e) {
        e.registerSpriteSet(ConfigParticles.SMOKE_SPIRAL.get(), SmokeSpiralParticle.Provider::new);
    }
}
