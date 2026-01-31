package art.arcane.thaumcraft.events.handlers;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import art.arcane.thaumcraft.Thaumcraft;
import art.arcane.thaumcraft.registries.ConfigCreativeTabs;

@EventBusSubscriber(modid = Thaumcraft.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class ItemEvents {

    @SubscribeEvent
    public static void onCreativeTabCollection(BuildCreativeModeTabContentsEvent event) {
        ResourceLocation id = BuiltInRegistries.CREATIVE_MODE_TAB.getKey(event.getTab());
        if(id.equals(ConfigCreativeTabs.MAIN.id())) {
            ConfigCreativeTabs.MAIN.collect(event.getParameters().holders()).forEach(is -> event.accept(is, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS));
        }
    }
}
