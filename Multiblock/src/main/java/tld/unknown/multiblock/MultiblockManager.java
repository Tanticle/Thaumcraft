package tld.unknown.multiblock;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.OnDatapackSyncEvent;

import java.util.HashMap;
import java.util.Map;

@EventBusSubscriber(modid = Constants.MOD_ID)
public class MultiblockManager {

    private Map<Ingredient, ResourceLocation> multiblocks = new HashMap<>();

    @SubscribeEvent
    public static void onDataSync(OnDatapackSyncEvent event) {

    }
}
