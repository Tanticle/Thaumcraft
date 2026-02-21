package art.arcane.thaumcraft.registries;

import art.arcane.thaumcraft.api.capabilities.*;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.EntityType;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.EntityCapability;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import art.arcane.thaumcraft.Thaumcraft;

import static art.arcane.thaumcraft.api.ThaumcraftData.Capabilities;

@EventBusSubscriber(modid = Thaumcraft.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class ConfigCapabilities {

    public static final BlockCapability<IEssentiaCapability, Direction> ESSENTIA = BlockCapability.createSided(Capabilities.ESSENTIA, IEssentiaCapability.class);
    public static final BlockCapability<IInfusionStabilizerCapability, Void> INFUSION_STABILIZER = BlockCapability.createVoid(Capabilities.INFUSION_STABILIZER, IInfusionStabilizerCapability.class);
    public static final BlockCapability<IInfusionPedestalCapability, Void> INFUSION_PEDESTAL = BlockCapability.createVoid(Capabilities.INFUSION_PEDESTAL, IInfusionPedestalCapability.class);
    public static final BlockCapability<IInfusionModifierCapability, Void> INFUSION_MODIFIER = BlockCapability.createVoid(Capabilities.INFUSION_MODIFIER, IInfusionModifierCapability.class);
    public static final BlockCapability<IGoggleRendererCapability, Direction> GOGGLE_RENDERER = BlockCapability.createSided(Capabilities.GOGGLE_RENDERER, IGoggleRendererCapability.class);

    public static final EntityCapability<IResearchCapability, Void> RESEARCH = EntityCapability.createVoid(Capabilities.RESEARCH, IResearchCapability.class);

    @SubscribeEvent
    private static void registerCapabilities(RegisterCapabilitiesEvent e) {
        e.registerBlockEntity(ESSENTIA, ConfigBlockEntities.JAR.entityType(), (be, side) -> side == Direction.UP ? be : null);
        e.registerBlockEntity(ESSENTIA, ConfigBlockEntities.TUBE.entityType(), (be, side) -> be.isDirectionDisabled(side) ? null : be);
        e.registerBlockEntity(ESSENTIA, ConfigBlockEntities.TUBE_VALVE.entityType(), (be, side) -> be.isDirectionDisabled(side) ? null : be);
        e.registerBlockEntity(ESSENTIA, ConfigBlockEntities.TUBE_FILTER.entityType(), (be, side) -> be.isDirectionDisabled(side) ? null : be);
        e.registerBlockEntity(ESSENTIA, ConfigBlockEntities.TUBE_RESTRICT.entityType(), (be, side) -> be.isDirectionDisabled(side) ? null : be);
        e.registerBlockEntity(ESSENTIA, ConfigBlockEntities.TUBE_ONEWAY.entityType(), (be, side) -> be.isDirectionDisabled(side) ? null : be);
        e.registerBlockEntity(ESSENTIA, ConfigBlockEntities.TUBE_BUFFER.entityType(), (be, side) -> be.isDirectionDisabled(side) ? null : be);
        e.registerBlockEntity(ESSENTIA, ConfigBlockEntities.ESSENTIA_INPUT.entityType(), (be, side) -> side == be.getConnectionSide() ? be : null);
        e.registerBlockEntity(ESSENTIA, ConfigBlockEntities.ESSENTIA_OUTPUT.entityType(), (be, side) -> side == be.getConnectionSide() ? be : null);
        e.registerBlockEntity(ESSENTIA, ConfigBlockEntities.CREATIVE_ASPECT_SOURCE.entityType(), (be, side) -> be);

		e.registerBlockEntity(INFUSION_PEDESTAL, ConfigBlockEntities.PEDESTAL.entityType(), (be, side) -> be);
		e.registerBlockEntity(INFUSION_MODIFIER, ConfigBlockEntities.PEDESTAL.entityType(), (be, side) -> be);
		e.registerBlockEntity(INFUSION_STABILIZER, ConfigBlockEntities.PEDESTAL.entityType(), (be, side) -> be);

        e.registerBlock(INFUSION_MODIFIER, (level, pos, state, be, $) -> (IInfusionModifierCapability)state.getBlock(), ConfigBlocks.ANCIENT_PEDESTAL.block(), ConfigBlocks.ELDRITCH_PEDESTAL.block());
        e.registerBlock(INFUSION_MODIFIER, (level, pos, state, be, $) -> (IInfusionModifierCapability)state.getBlock(), ConfigBlocks.INFUSION_STONE_SPEED.block(), ConfigBlocks.INFUSION_STONE_COST.block());

        e.registerEntity(RESEARCH, EntityType.PLAYER, (p, x) -> p.getData(ConfigDataAttachments.PLAYER_RESEARCH));

		e.registerBlockEntity(GOGGLE_RENDERER, ConfigBlockEntities.JAR.entityType(), (be, side) -> be);
		e.registerBlockEntity(GOGGLE_RENDERER, ConfigBlockEntities.RUNIC_MATRIX.entityType(), (be, side) -> be);

        e.registerBlockEntity(net.neoforged.neoforge.capabilities.Capabilities.FluidHandler.BLOCK, ConfigBlockEntities.CRUCIBLE.entityType(), (be, side) -> side == Direction.UP ? be : null);
    }
}
