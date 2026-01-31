package tld.unknown.multiblock;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import tld.unknown.multiblock.data.MultiblockData;

public final class Constants {

    public static final String MOD_ID = "multiblock";

    public static final ResourceKey<Registry<MultiblockData>> DATA_REGISTRY_KEY = ResourceKey.createRegistryKey(Multiblock.id("multiblocks"));
}
