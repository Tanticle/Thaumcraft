package tld.unknown.mystery.data;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import net.neoforged.neoforge.network.event.RegisterConfigurationTasksEvent;
import tld.unknown.mystery.Thaumcraft;
import tld.unknown.mystery.api.aspects.Aspect;
import tld.unknown.mystery.data.aspects.AspectRegistryManager;
import tld.unknown.mystery.data.aspects.PrimalAspects;
import tld.unknown.mystery.data.aura.AuraBiomeInfo;
import tld.unknown.mystery.data.research.ResearchCategory;
import tld.unknown.mystery.data.research.ResearchEntry;
import tld.unknown.mystery.util.ReflectionUtils;
import tld.unknown.mystery.util.codec.data.CodecDataManager;

import java.util.Map;

@EventBusSubscriber(modid = Thaumcraft.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public final class DataRegistries {

    public static final AspectRegistryManager ASPECT_REGISTRY = new AspectRegistryManager();
    public static final CodecDataManager<Aspect> ASPECTS = new CodecDataManager<>(Aspect.CODEC,
            "Aspects", "aspects", PrimalAspects.DEFAULTS, id -> !id.getPath().contains("/"));
    public static final CodecDataManager<AuraBiomeInfo> AURA_BIOMES = new CodecDataManager<>(AuraBiomeInfo.CODEC,
            "AuraBiomes", "aura/biomes", null, id -> !id.getPath().contains("/"));
    public static final CodecDataManager<ResearchCategory> RESEARCH_CATEGORY = new CodecDataManager<>(ResearchCategory.CODEC,
            "ResearchCategory", "research", null, id -> !id.getPath().contains("/"));
    public static final CodecDataManager<ResearchEntry> RESEARCH_ENTRIES = new CodecDataManager<>(ResearchEntry.CODEC,
            "ResearchEntry", "research", null, id -> {
                if(!id.getPath().contains("/"))
                    return false;
                ResourceLocation category = new ResourceLocation(id.getNamespace(), id.getPath().split("/")[0]);
                return RESEARCH_CATEGORY.entryPresent(category);
            });

    public static final Map<String, CodecDataManager<?>> REGISTRY = Map.of(
            ASPECT_REGISTRY.getName(), ASPECT_REGISTRY,
            ASPECTS.getName(), ASPECTS,
            AURA_BIOMES.getName(), AURA_BIOMES,
            RESEARCH_CATEGORY.getName(), RESEARCH_CATEGORY,
            RESEARCH_ENTRIES.getName(), RESEARCH_ENTRIES
    );

    @SubscribeEvent
    public static void registerListeners(RegisterConfigurationTasksEvent e) {
        ReflectionUtils.getAllStaticsOfType(DataRegistries.class, CodecDataManager.class).forEach(c -> {
            e.register(c);
            c.setSyncListener(e.getListener());
        });
    }

    @EventBusSubscriber(modid = Thaumcraft.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
    public static class ForgeEvents {

        @SubscribeEvent
        public static void onDatapackLoad(AddReloadListenerEvent e) {
            e.addListener(ASPECT_REGISTRY);
            e.addListener(ASPECTS);
            e.addListener(AURA_BIOMES);
            e.addListener(RESEARCH_CATEGORY);
            e.addListener(RESEARCH_ENTRIES);
        }
    }
}
