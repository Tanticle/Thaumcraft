package tld.unknown.mystery.events.handlers;

import net.minecraft.world.level.chunk.LevelChunk;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.level.ChunkEvent;
import tld.unknown.mystery.Thaumcraft;
import tld.unknown.mystery.attachments.AuraAttachment;
import tld.unknown.mystery.registries.ConfigDataAttachments;

@EventBusSubscriber(modid = Thaumcraft.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public final class WorldEvents {

    @SubscribeEvent
    public static void onChunkLoad(ChunkEvent.Load e) {
        if(e.isNewChunk() && e.getChunk() instanceof LevelChunk chunk) {
            chunk.setData(ConfigDataAttachments.CHUNK_AURA.get(), new AuraAttachment(chunk, chunk.getLevel().getRandom()));
        }
    }
}
