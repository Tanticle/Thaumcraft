package tld.unknown.mystery.registries;

import net.minecraft.core.Direction;
import net.minecraft.world.entity.EntityType;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.EntityCapability;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import tld.unknown.mystery.Thaumcraft;
import tld.unknown.mystery.api.capabilities.*;

import static tld.unknown.mystery.api.ThaumcraftData.Capabilities;

@EventBusSubscriber(modid = Thaumcraft.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class ConfigCapabilities {

    public static final BlockCapability<IEssentiaCapability, Direction> ESSENTIA = BlockCapability.createSided(Capabilities.ESSENTIA, IEssentiaCapability.class);
    public static final BlockCapability<IInfusionStabilizerCapability, Void> INFUSION_STABILIZER = BlockCapability.createVoid(Capabilities.INFUSION_STABILIZER, IInfusionStabilizerCapability.class);
    public static final BlockCapability<IInfusionPedestalCapability, Void> INFUSION_PEDESTAL = BlockCapability.createVoid(Capabilities.INFUSION_PEDESTAL, IInfusionPedestalCapability.class);
    public static final BlockCapability<InfusionModifierCapability, Void> INFUSION_MODIFIER = BlockCapability.createVoid(Capabilities.INFUSION_MODIFIER, InfusionModifierCapability.class);

    public static final EntityCapability<IResearchCapability, Void> RESEARCH = EntityCapability.createVoid(Capabilities.RESEARCH, IResearchCapability.class);

    @SubscribeEvent
    private static void registerCapabilities(RegisterCapabilitiesEvent e) {
        e.registerBlockEntity(ESSENTIA, ConfigBlockEntities.JAR.entityType(), (be, side) -> side == Direction.UP ? be : null);
        e.registerBlockEntity(ESSENTIA, ConfigBlockEntities.TUBE.entityType(), (be, side) -> be.getDisabledDirections().contains(side) ? null : be);
        e.registerBlockEntity(ESSENTIA, ConfigBlockEntities.CREATIVE_ASPECT_SOURCE.entityType(), (be, side) -> be);
        e.registerBlockEntity(INFUSION_PEDESTAL, ConfigBlockEntities.PEDESTAL.entityType(), (be, side) -> be);
        e.registerBlock(INFUSION_MODIFIER, (level, pos, state, be, $) -> (InfusionModifierCapability)state.getBlock(), ConfigBlocks.INFUSION_STONE_SPEED.block(), ConfigBlocks.INFUSION_STONE_COST.block());

        e.registerEntity(RESEARCH, EntityType.PLAYER, (p, x) -> p.getData(ConfigDataAttachments.PLAYER_RESEARCH));
    }
}
