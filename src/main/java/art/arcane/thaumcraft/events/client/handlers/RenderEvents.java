package art.arcane.thaumcraft.events.client.handlers;

import art.arcane.thaumcraft.api.ThaumcraftUtils;
import art.arcane.thaumcraft.api.capabilities.IGoggleRendererCapability;
import art.arcane.thaumcraft.api.components.FortressFaceplateComponent;
import art.arcane.thaumcraft.items.VisChargeItem;
import art.arcane.thaumcraft.registries.ConfigCapabilities;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.util.Either;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.neoforged.neoforge.client.event.RenderTooltipEvent;
import org.joml.Matrix4fStack;
import art.arcane.thaumcraft.Thaumcraft;
import art.arcane.thaumcraft.api.aspects.Aspect;
import art.arcane.thaumcraft.client.fx.ArchitectBlockRenderer;
import art.arcane.thaumcraft.client.fx.OreScanRenderer;
import art.arcane.thaumcraft.api.aspects.AspectContainerItem;
import art.arcane.thaumcraft.client.ThaumcraftClient;
import art.arcane.thaumcraft.client.rendering.SealClientData;
import art.arcane.thaumcraft.client.rendering.SealWorldRenderer;
import art.arcane.thaumcraft.client.rendering.ui.AspectTooltip;
import art.arcane.thaumcraft.data.aspects.AspectList;
import art.arcane.thaumcraft.items.AbstractAspectItem;
import art.arcane.thaumcraft.items.golemancy.GolemBellItem;
import art.arcane.thaumcraft.items.golemancy.SealPlacerItem;
import art.arcane.thaumcraft.registries.ConfigDataRegistries;
import art.arcane.thaumcraft.registries.ConfigItemComponents;
import art.arcane.thaumcraft.util.RegistryUtils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

@EventBusSubscriber(modid = Thaumcraft.MOD_ID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.GAME)
public class RenderEvents {

    private static final String KEY_ROMAN_NUMERAL = "enchantment.level.";

    @SubscribeEvent
    public static void onTooltipGather(RenderTooltipEvent.GatherComponents e) {
        //TODO Actual restrictions, equipment, thaumonometer etc
        if(e.getItemStack().has(ConfigItemComponents.INFUSION_ENCHANTMENT.value())) {
            List<Either<FormattedText, TooltipComponent>> components = new ArrayList<>();
            e.getItemStack().get(ConfigItemComponents.INFUSION_ENCHANTMENT.value()).enchantments().forEach((ench, lvl) -> {
                MutableComponent comp = ench.getTranslationKey().withStyle(ChatFormatting.GOLD);
                if(ench.getMaxLevel()> 1)
                    comp.append(Component.literal(" ")).append(Component.translatable(KEY_ROMAN_NUMERAL + lvl));
                components.add(Either.left(comp));
            });
            e.getTooltipElements().addAll(1, components);
        }

        AspectList aspects = ConfigDataRegistries.ASPECT_REGISTRY.getAspects(e.getItemStack());
        if(!aspects.isEmpty())
            e.getTooltipElements().add(Either.right(new AspectTooltip.Data(aspects)));

        if(e.getItemStack().has(ConfigItemComponents.WARPING.value())) {
            String lvl = Component.translatable(KEY_ROMAN_NUMERAL + e.getItemStack().get(ConfigItemComponents.WARPING.value())).getString();
            e.getTooltipElements().add(Either.left(Component.translatable("misc.thaumcraft.tooltip.warping", lvl).withStyle(ChatFormatting.DARK_PURPLE)));
        }

        if(e.getItemStack().has(ConfigItemComponents.VIS_COST_MODIFIER.value())) {
            float modifier = e.getItemStack().get(ConfigItemComponents.VIS_COST_MODIFIER.value());
            String translation = "misc.thaumcraft.tooltip.vis_modifier_" + (modifier <= 0 ? "positive" : "negative");
            DecimalFormat format = new DecimalFormat();
            format.setMaximumFractionDigits(2);
            format.setMinimumFractionDigits(0);
            e.getTooltipElements().add(Either.left(Component.translatable(translation, format.format(Math.abs(modifier * 100))).withStyle(ChatFormatting.DARK_PURPLE)));
        }

		if(e.getItemStack().has(ConfigItemComponents.ARMOR_FORTRESS_FACEPLATE.value())) {
			FortressFaceplateComponent face = e.getItemStack().get(ConfigItemComponents.ARMOR_FORTRESS_FACEPLATE.value());
			if(face.hasGoggles())
				e.getTooltipElements().add(Either.left(Component.translatable("misc.thaumcraft.tooltip.fortress_armor_goggles").withStyle(ChatFormatting.DARK_PURPLE)));
			if(face.type() != FortressFaceplateComponent.Type.NONE)
				e.getTooltipElements().add(Either.left(Component.translatable("misc.thaumcraft.tooltip.fortress_armor_" + face.type().getModelPart()).withStyle(ChatFormatting.GOLD)));
		}

		if(e.getItemStack().getItem() instanceof VisChargeItem i) {
			int charge = i.getCharge(e.getItemStack());
			e.getTooltipElements().add(Either.left(Component.translatable("misc.thaumcraft.tooltip.vis_charge",charge).withStyle(ChatFormatting.YELLOW)));
		}


        if(e.getItemStack().getItem() instanceof AbstractAspectItem l && l.hasData(e.getItemStack())) {
            Holder<Aspect> aspect = l.getHolder(e.getItemStack());
            Component name = Aspect.getName(RegistryUtils.access(), aspect.getKey(), false, false);
            short amount = 0;
            if(l instanceof AspectContainerItem a)
                amount = a.getAspects(e.getItemStack()).getAspect(aspect.getKey());
            MutableComponent component = amount > 0 ? Component.translatable("misc.thaumcraft.tooltip.aspect_complex", amount, name) : Component.translatable("misc.thaumcraft.tooltip.aspect_simple", name);
            e.getTooltipElements().add(1, Either.left(component.withStyle(ChatFormatting.DARK_GRAY)));
        }
    }

    @SubscribeEvent
    public static void onLevelRender(RenderLevelStageEvent e) {
        if(e.getStage() == RenderLevelStageEvent.Stage.AFTER_LEVEL) {
            Matrix4fStack matrix4fstack = RenderSystem.getModelViewStack();
            matrix4fstack.pushMatrix();
            matrix4fstack.mul(e.getModelViewMatrix());
            e.getPoseStack().pushPose();
            if (ThaumcraftClient.TUBE_DEBUG_RENDERER != null && ThaumcraftClient.TUBE_DEBUG_RENDERER.active) {
                ThaumcraftClient.TUBE_DEBUG_RENDERER.render(
                        e.getPoseStack(),
                        Minecraft.getInstance().renderBuffers().bufferSource(),
                        e.getCamera().getPosition().x,
                        e.getCamera().getPosition().y,
                        e.getCamera().getPosition().z);
            }
            e.getPoseStack().popPose();
            matrix4fstack.popMatrix();
        }

        if (e.getStage() == RenderLevelStageEvent.Stage.AFTER_PARTICLES) {
            MultiBufferSource.BufferSource bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();
            OreScanRenderer.render(e.getPoseStack(), e.getCamera(), bufferSource, e.getPartialTick().getGameTimeDeltaPartialTick(true));
            ArchitectBlockRenderer.render(e.getPoseStack(), e.getCamera(), bufferSource);
            if (isHoldingSealDisplayer(Minecraft.getInstance().player)) {
                SealWorldRenderer.render(e.getPoseStack(), e.getCamera(), bufferSource);
            }
            if (ThaumcraftUtils.playerHasGoggleSight(Minecraft.getInstance().player) && Minecraft.getInstance().hitResult instanceof BlockHitResult hit) {
                dispatchGoggleSight(bufferSource, e.getPoseStack(), e.getCamera(), e.getLevel(), hit, e.getPartialTick());
            }
            bufferSource.endBatch();
        }
    }

    private static boolean isHoldingSealDisplayer(Player player) {
        if (player == null) return false;
        return isSealDisplayer(player.getMainHandItem()) || isSealDisplayer(player.getOffhandItem());
    }

    private static boolean isSealDisplayer(ItemStack stack) {
        return stack.getItem() instanceof GolemBellItem || stack.getItem() instanceof SealPlacerItem;
    }

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post e) {
        OreScanRenderer.tick();
    }

    @SubscribeEvent
    public static void onClientDisconnect(ClientPlayerNetworkEvent.LoggingOut e) {
        SealClientData.clear();
    }

    private static final int HIGHER_FONT = 15;

    private static void dispatchGoggleSight(MultiBufferSource.BufferSource bufferSource, PoseStack stack, Camera camera, Level level, BlockHitResult hit, DeltaTracker delta) {
        IGoggleRendererCapability renderCap = level.getCapability(ConfigCapabilities.GOGGLE_RENDERER, hit.getBlockPos(), hit.getDirection());
        if (renderCap != null) {
            Font font = Minecraft.getInstance().font;
            stack.pushPose();

            stack.translate(Vec3.atLowerCornerOf(hit.getBlockPos()).subtract(camera.getPosition()));
            renderCap.render(stack, bufferSource, delta);

            stack.translate(.5F, .5F, .5F);
            stack.mulPose(camera.rotation());
            stack.scale(.0125F, -.0125F, .0125F);

            RenderSystem.disableDepthTest();
            int lineCount = renderCap.textDisplay().size();
            float yOffset = lineCount * HIGHER_FONT / 2F;
            for (int i = 0; i < lineCount; i++) {
                Component component = renderCap.textDisplay().get(i);
                float centerOffset = (float) (-font.width(component) / 2);
                font.drawInBatch(component, centerOffset, -yOffset + (i * HIGHER_FONT), -1, true, stack.last().pose(), bufferSource, Font.DisplayMode.SEE_THROUGH, 0, LightTexture.FULL_BRIGHT);
            }
            bufferSource.endLastBatch();
            RenderSystem.enableDepthTest();

            stack.popPose();
        }
    }
}
