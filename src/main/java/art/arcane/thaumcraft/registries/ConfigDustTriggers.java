package art.arcane.thaumcraft.registries;

import net.minecraft.world.level.block.Blocks;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import art.arcane.thaumcraft.Thaumcraft;
import art.arcane.thaumcraft.api.crafting.DustTrigger;
import art.arcane.thaumcraft.api.crafting.SimpleDustTrigger;

@EventBusSubscriber(modid = Thaumcraft.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public final class ConfigDustTriggers {

    @SubscribeEvent
    public static void onCommonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(ConfigDustTriggers::registerTriggers);
    }

    private static void registerTriggers() {
        DustTrigger.register(new SimpleDustTrigger(
                Blocks.CRAFTING_TABLE,
                ConfigBlocks.ARCANE_WORKBENCH.block()
        ));
    }
}
