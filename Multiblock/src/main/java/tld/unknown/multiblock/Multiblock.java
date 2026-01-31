package tld.unknown.multiblock;

import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;
import org.openjdk.nashorn.internal.objects.annotations.Getter;
import org.slf4j.Logger;
import tld.unknown.multiblock.data.MultiblockData;
import tld.unknown.multiblock.util.DataRegistry;

@Mod(Constants.MOD_ID)
public class Multiblock {

    private static final Logger LOGGER = LogUtils.getLogger();

    public static final DataRegistry<MultiblockData> DATA_REGISTRY = new DataRegistry<>(Constants.DATA_REGISTRY_KEY, MultiblockData.CODEC, MultiblockData.CODEC, id("unknown"));

    public static final MultiblockManager MANAGER = new MultiblockManager();

    public static ResourceLocation id(String value) {
        return ResourceLocation.tryBuild(Constants.MOD_ID, value);
    }

    public Multiblock(IEventBus bus) {

    }

    @SubscribeEvent
    public void onDatapackRegistry(DataPackRegistryEvent.NewRegistry event) {
        DATA_REGISTRY.register(event);
    }
}
