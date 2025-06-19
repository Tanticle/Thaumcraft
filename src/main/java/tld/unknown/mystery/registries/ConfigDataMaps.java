package tld.unknown.mystery.registries;

import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.datamaps.DataMapType;
import net.neoforged.neoforge.registries.datamaps.RegisterDataMapTypesEvent;
import tld.unknown.mystery.Thaumcraft;
import tld.unknown.mystery.api.ThaumcraftData;
import tld.unknown.mystery.data.DataMapEntries;
import tld.unknown.mystery.util.ReflectionUtils;

@EventBusSubscriber(modid = Thaumcraft.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public final class ConfigDataMaps {

    /* -------------------------------------------------------------------------------------------------------------- */

    public static final DataMapType<Block, DataMapEntries.InfusionStabilizerData> INFUSION_STABILIZER = register(ThaumcraftData.DataMaps.INFUSION_STABILIZER, Registries.BLOCK, DataMapEntries.InfusionStabilizerData.CODEC);
    public static final DataMapType<Block, DataMapEntries.InfusionModifierData> INFUSION_MODIFIER = register(ThaumcraftData.DataMaps.INFUSION_MODIFIER, Registries.BLOCK, DataMapEntries.InfusionModifierData.CODEC);

    /* -------------------------------------------------------------------------------------------------------------- */

    @SubscribeEvent
    private static void registerDataMapTypes(RegisterDataMapTypesEvent event) {
        ReflectionUtils.getAllStaticsOfType(ConfigDataMaps.class, DataMapType.class).forEach(event::register);
    }

    private static <T, D> DataMapType<T, D> register(ResourceLocation key, ResourceKey<Registry<T>> registry, Codec<D> dataCodec) {
        return DataMapType.builder(key, registry, dataCodec).build();
    }
}
