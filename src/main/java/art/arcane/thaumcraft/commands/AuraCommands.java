package art.arcane.thaumcraft.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.LevelChunk;
import art.arcane.thaumcraft.data.attachments.AuraAttachment;
import art.arcane.thaumcraft.data.aura.AuraHelper;
import art.arcane.thaumcraft.registries.ConfigDataAttachments;

public final class AuraCommands {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        float max = AuraAttachment.MAX_AURA;
        dispatcher.register(Commands.literal("thaumcraft")
                .requires(source -> source.hasPermission(2))
                .then(Commands.literal("aura")
                        .then(Commands.literal("get")
                                .executes(AuraCommands::getAura))
                        .then(Commands.literal("setVis")
                                .then(Commands.argument("amount", FloatArgumentType.floatArg(0, max))
                                        .executes(AuraCommands::setVis)))
                        .then(Commands.literal("setFlux")
                                .then(Commands.argument("amount", FloatArgumentType.floatArg(0, max))
                                        .executes(AuraCommands::setFlux)))
                        .then(Commands.literal("setBase")
                                .then(Commands.argument("amount", FloatArgumentType.floatArg(0, max))
                                        .executes(AuraCommands::setBase)))
                        .then(Commands.literal("addVis")
                                .then(Commands.argument("amount", FloatArgumentType.floatArg(-max, max))
                                        .executes(AuraCommands::addVis)))
                        .then(Commands.literal("addFlux")
                                .then(Commands.argument("amount", FloatArgumentType.floatArg(-max, max))
                                        .executes(AuraCommands::addFlux)))
                        .then(Commands.literal("reset")
                                .executes(AuraCommands::resetAura))
                )
        );
    }

    private static int getAura(CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();
        BlockPos pos = BlockPos.containing(source.getPosition());
        ServerLevel level = source.getLevel();
        ChunkPos chunkPos = new ChunkPos(pos);

        var auraOpt = AuraHelper.getAura(level, chunkPos);
        if (auraOpt.isEmpty()) {
            source.sendFailure(Component.literal("No aura data for chunk at " + chunkPos));
            return 0;
        }

        AuraAttachment aura = auraOpt.get();
        source.sendSuccess(() -> Component.literal(String.format(
                "Chunk [%d, %d] Aura: Base=%d, Vis=%.1f, Flux=%.1f",
                chunkPos.x, chunkPos.z, aura.getBaseVis(), aura.getVis(), aura.getFlux()
        )), false);
        return 1;
    }

    private static int setVis(CommandContext<CommandSourceStack> context) {
        float amount = FloatArgumentType.getFloat(context, "amount");
        return modifyAura(context, aura -> aura.setVis(Math.min(AuraAttachment.MAX_AURA, amount)), "Vis set to " + amount);
    }

    private static int setFlux(CommandContext<CommandSourceStack> context) {
        float amount = FloatArgumentType.getFloat(context, "amount");
        return modifyAura(context, aura -> aura.setFlux(Math.min(AuraAttachment.MAX_AURA, amount)), "Flux set to " + amount);
    }

    private static int setBase(CommandContext<CommandSourceStack> context) {
        float amount = FloatArgumentType.getFloat(context, "amount");
        CommandSourceStack source = context.getSource();
        BlockPos pos = BlockPos.containing(source.getPosition());
        ServerLevel level = source.getLevel();
        ChunkPos chunkPos = new ChunkPos(pos);

        if (!level.hasChunk(chunkPos.x, chunkPos.z)) {
            source.sendFailure(Component.literal("Chunk not loaded"));
            return 0;
        }

        LevelChunk chunk = level.getChunk(chunkPos.x, chunkPos.z);
        AuraAttachment newAura = new AuraAttachment((short) amount);
        chunk.setData(ConfigDataAttachments.CHUNK_AURA.get(), newAura);
        chunk.markUnsaved();

        source.sendSuccess(() -> Component.literal("Base aura set to " + (int) amount + " (vis reset to base)"), true);
        return 1;
    }

    private static int addVis(CommandContext<CommandSourceStack> context) {
        float amount = FloatArgumentType.getFloat(context, "amount");
        return modifyAura(context, aura -> aura.setVis(Math.max(0, Math.min(AuraAttachment.MAX_AURA, aura.getVis() + amount))),
                "Added " + amount + " vis");
    }

    private static int addFlux(CommandContext<CommandSourceStack> context) {
        float amount = FloatArgumentType.getFloat(context, "amount");
        return modifyAura(context, aura -> aura.setFlux(Math.max(0, Math.min(AuraAttachment.MAX_AURA, aura.getFlux() + amount))),
                "Added " + amount + " flux");
    }

    private static int resetAura(CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();
        BlockPos pos = BlockPos.containing(source.getPosition());
        ServerLevel level = source.getLevel();
        ChunkPos centerChunk = new ChunkPos(pos);

        int regionX = centerChunk.x >> 5;
        int regionZ = centerChunk.z >> 5;

        int resetCount = 0;
        int totalVis = 0;

        for (int rx = regionX - 1; rx <= regionX + 1; rx++) {
            for (int rz = regionZ - 1; rz <= regionZ + 1; rz++) {
                int minChunkX = rx << 5;
                int minChunkZ = rz << 5;
                int maxChunkX = minChunkX + 32;
                int maxChunkZ = minChunkZ + 32;

                for (int cx = minChunkX; cx < maxChunkX; cx++) {
                    for (int cz = minChunkZ; cz < maxChunkZ; cz++) {
                        if (!level.hasChunk(cx, cz)) continue;

                        LevelChunk chunk = level.getChunk(cx, cz);
                        AuraAttachment newAura = new AuraAttachment(chunk, level.getRandom());
                        chunk.setData(ConfigDataAttachments.CHUNK_AURA.get(), newAura);
                        chunk.markUnsaved();
                        resetCount++;
                        totalVis += newAura.getBaseVis();
                    }
                }
            }
        }

        int avgVis = resetCount > 0 ? totalVis / resetCount : 0;
        int finalResetCount = resetCount;
        source.sendSuccess(() -> Component.literal(String.format(
                "Reset aura for %d chunks in 3x3 region area. Average base vis: %d",
                finalResetCount, avgVis
        )), true);
        return resetCount;
    }

    private static int modifyAura(CommandContext<CommandSourceStack> context, java.util.function.Consumer<AuraAttachment> modifier, String successMsg) {
        CommandSourceStack source = context.getSource();
        BlockPos pos = BlockPos.containing(source.getPosition());
        ServerLevel level = source.getLevel();
        ChunkPos chunkPos = new ChunkPos(pos);

        var auraOpt = AuraHelper.getAura(level, chunkPos);
        if (auraOpt.isEmpty()) {
            source.sendFailure(Component.literal("No aura data for chunk"));
            return 0;
        }

        modifier.accept(auraOpt.get());
        level.getChunk(chunkPos.x, chunkPos.z).markUnsaved();

        source.sendSuccess(() -> Component.literal(successMsg), true);
        return 1;
    }
}
