package art.arcane.thaumcraft.networking;

import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import art.arcane.thaumcraft.Thaumcraft;
import art.arcane.thaumcraft.client.fx.OreScanHandler;
import art.arcane.thaumcraft.client.fx.ThaumcraftFX;
import art.arcane.thaumcraft.items.tools.ElementalShovelItem;
import art.arcane.thaumcraft.networking.packets.ClientboundAspectRegistrySyncPacket;
import art.arcane.thaumcraft.networking.packets.ClientboundBamfEffectPacket;
import art.arcane.thaumcraft.networking.packets.ClientboundEssentiaTrailPacket;
import art.arcane.thaumcraft.networking.packets.ClientboundSalisMundusEffectPacket;
import art.arcane.thaumcraft.networking.packets.ClientboundSoundingPacket;
import art.arcane.thaumcraft.networking.packets.ServerboundCycleToolModePacket;
import art.arcane.thaumcraft.registries.ConfigDataRegistries;

@EventBusSubscriber(modid = Thaumcraft.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class ThaumcraftNetworking {

    private static final String PROTOCOL = "0.1";

    @SubscribeEvent
    public static void onRegisterPayloadHander(RegisterPayloadHandlersEvent e) {
        final PayloadRegistrar registrar = e.registrar(PROTOCOL);

        registrar.playToClient(ClientboundAspectRegistrySyncPacket.TYPE, ClientboundAspectRegistrySyncPacket.STREAM_CODEC, (data, ctx) -> {
            ctx.enqueueWork(() -> ConfigDataRegistries.ASPECT_REGISTRY.deserialize(data)).exceptionally(error -> {
                ctx.disconnect(Component.translatable("network.thaumcraft.aspect_registry_disconnect"));
                return null;
            });
        });

        registrar.playToClient(ClientboundSoundingPacket.TYPE, ClientboundSoundingPacket.STREAM_CODEC, (data, ctx) -> {
            ctx.enqueueWork(() -> OreScanHandler.handleSoundingScan(data.origin(), data.level()));
        });

        registrar.playToClient(ClientboundSalisMundusEffectPacket.TYPE, ClientboundSalisMundusEffectPacket.STREAM_CODEC, (data, ctx) -> {
            ctx.enqueueWork(() -> ThaumcraftFX.drawSalisMundusSparkles(data));
        });

        registrar.playToClient(ClientboundBamfEffectPacket.TYPE, ClientboundBamfEffectPacket.STREAM_CODEC, (data, ctx) -> {
            ctx.enqueueWork(() -> ThaumcraftFX.drawBamf(data));
        });

        registrar.playToClient(ClientboundEssentiaTrailPacket.TYPE, ClientboundEssentiaTrailPacket.STREAM_CODEC, (data, ctx) -> {
            ctx.enqueueWork(() -> ThaumcraftFX.drawEssentiaTrail(data));
        });

        registrar.playToServer(ServerboundCycleToolModePacket.TYPE, ServerboundCycleToolModePacket.STREAM_CODEC, (data, ctx) -> {
            ctx.enqueueWork(() -> {
                Player player = ctx.player();
                ItemStack held = player.getMainHandItem();
                if (held.getItem() instanceof ElementalShovelItem) {
                    Direction.Axis current = ElementalShovelItem.getOrientation(held);
                    Direction.Axis next = switch (current) {
                        case X -> Direction.Axis.Y;
                        case Y -> Direction.Axis.Z;
                        case Z -> Direction.Axis.X;
                    };
                    ElementalShovelItem.setOrientation(held, next);
                }
            });
        });
    }
}
