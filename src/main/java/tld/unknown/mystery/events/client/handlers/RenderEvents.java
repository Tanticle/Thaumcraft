package tld.unknown.mystery.events.client.handlers;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.ByteBufferBuilder;
import com.mojang.datafixers.util.Either;
import com.mojang.math.Axis;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.neoforged.neoforge.client.event.RenderTooltipEvent;
import org.joml.Matrix4f;
import org.joml.Matrix4fStack;
import tld.unknown.mystery.Thaumcraft;
import tld.unknown.mystery.api.WarpingGear;
import tld.unknown.mystery.api.aspects.Aspect;
import tld.unknown.mystery.api.aspects.AspectContainerItem;
import tld.unknown.mystery.client.ThaumcraftClient;
import tld.unknown.mystery.client.rendering.ui.AspectTooltip;
import tld.unknown.mystery.data.aspects.AspectList;
import tld.unknown.mystery.items.AbstractAspectItem;
import tld.unknown.mystery.registries.ConfigDataRegistries;
import tld.unknown.mystery.registries.ConfigItemComponents;
import tld.unknown.mystery.util.RegistryUtils;

import java.util.ArrayList;
import java.util.List;

@EventBusSubscriber(modid = Thaumcraft.MOD_ID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.GAME)
public class RenderEvents {

    @SubscribeEvent
    public static void onTooltipGather(RenderTooltipEvent.GatherComponents e) {
        //TODO Actual restrictions, equipment, thaumonometer etc
        if(e.getItemStack().has(ConfigItemComponents.INFUSION_ENCHANTMENT.value())) {
            List<Either<FormattedText, TooltipComponent>> components = new ArrayList<>();
            e.getItemStack().get(ConfigItemComponents.INFUSION_ENCHANTMENT.value()).enchantments().forEach((ench, lvl) -> {
                MutableComponent comp = ench.getTranslationKey().withStyle(ChatFormatting.GOLD);
                if(ench.getMaxLevel()> 1)
                    comp.append(Component.literal(" ")).append(Component.translatable("enchantment.level." + lvl));
                components.add(Either.left(comp));
            });
            e.getTooltipElements().addAll(1, components);
        }

        AspectList aspects = ConfigDataRegistries.ASPECT_REGISTRY.getAspects(e.getItemStack());
        if(!aspects.isEmpty())
            e.getTooltipElements().add(Either.right(new AspectTooltip.Data(aspects)));

        if(e.getItemStack().getItem() instanceof WarpingGear w)
            e.getTooltipElements().add(Either.left(Component.translatable("misc.thaumcraft.tooltip.warping", w.getWarp(e.getItemStack(), Minecraft.getInstance().player)).withStyle(ChatFormatting.DARK_PURPLE)));
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
            //RenderSystem.applyModelViewMatrix();
            e.getPoseStack().pushPose();
            ThaumcraftClient.TUBE_DEBUG_RENDERER.render(e.getPoseStack(), Minecraft.getInstance().renderBuffers().bufferSource(), e.getCamera().getPosition().x, e.getCamera().getPosition().y, e.getCamera().getPosition().z);
            e.getPoseStack().popPose();
            matrix4fstack.popMatrix();
            //RenderSystem.applyModelViewMatrix();
        }
    }
}
