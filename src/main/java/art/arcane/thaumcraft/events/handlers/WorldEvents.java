package art.arcane.thaumcraft.events.handlers;

import art.arcane.thaumcraft.commands.GiveCommand;
import art.arcane.thaumcraft.commands.VisChargeCommand;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.chunk.LevelChunk;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.level.ChunkEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;
import art.arcane.thaumcraft.Thaumcraft;
import art.arcane.thaumcraft.commands.AuraCommands;
import art.arcane.thaumcraft.data.attachments.AuraAttachment;
import art.arcane.thaumcraft.data.aura.VisFlowProcessor;
import art.arcane.thaumcraft.registries.ConfigDataAttachments;

@EventBusSubscriber(modid = Thaumcraft.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public final class WorldEvents {

    private static final int VIS_FLOW_INTERVAL = 20;
    private static int tickCounter = 0;

    @SubscribeEvent
    public static void onChunkLoad(ChunkEvent.Load e) {
        if(e.isNewChunk() && e.getChunk() instanceof LevelChunk chunk) {
            chunk.setData(ConfigDataAttachments.CHUNK_AURA.get(), new AuraAttachment(chunk, chunk.getLevel().getRandom()));
        }
    }

    @SubscribeEvent
    public static void onLevelTick(LevelTickEvent.Post e) {
        if (e.getLevel() instanceof ServerLevel serverLevel) {
            tickCounter++;
            if (tickCounter >= VIS_FLOW_INTERVAL) {
                tickCounter = 0;
                VisFlowProcessor.processLevel(serverLevel);
            }
        }
    }

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent e) {
		if(Thaumcraft.isDev()) {
			AuraCommands.register(e.getDispatcher());
			VisChargeCommand.register(e.getDispatcher());
			GiveCommand.register(e.getDispatcher());
		}
    }
}
