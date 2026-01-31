package art.arcane.thaumcraft.events.client.handlers;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.datafixers.util.Either;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
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
import org.joml.Matrix4fStack;
import art.arcane.thaumcraft.Thaumcraft;
import art.arcane.thaumcraft.api.WarpingGear;
import art.arcane.thaumcraft.api.aspects.Aspect;
import art.arcane.thaumcraft.api.aspects.AspectContainerItem;
import art.arcane.thaumcraft.client.ThaumcraftClient;
import art.arcane.thaumcraft.client.rendering.ui.AspectTooltip;
import art.arcane.thaumcraft.data.aspects.AspectList;
import art.arcane.thaumcraft.items.AbstractAspectItem;
import art.arcane.thaumcraft.registries.ConfigDataRegistries;
import art.arcane.thaumcraft.registries.ConfigItemComponents;
import art.arcane.thaumcraft.util.RegistryUtils;

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
