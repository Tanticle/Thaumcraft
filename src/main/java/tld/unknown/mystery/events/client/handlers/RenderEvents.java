package tld.unknown.mystery.events.client.handlers;

import com.mojang.datafixers.util.Either;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderTooltipEvent;
import tld.unknown.mystery.Thaumcraft;
import tld.unknown.mystery.api.WarpingGear;
import tld.unknown.mystery.client.rendering.ui.AspectTooltip;
import tld.unknown.mystery.registries.ConfigItemComponents;

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

        if(DataRegistries.ASPECT_REGISTRY.hasAspects(e.getItemStack()))
            e.getTooltipElements().add(Either.right(new AspectTooltip.Data(DataRegistries.ASPECT_REGISTRY.getAspects(e.getItemStack()))));
        if(e.getItemStack().getItem() instanceof WarpingGear w)
            e.getTooltipElements().add(Either.left(Component.translatable("misc.thaumcraft.warping_tooltip", w.getWarp(e.getItemStack(), Minecraft.getInstance().player)).withStyle(ChatFormatting.DARK_PURPLE)));
    }
}
