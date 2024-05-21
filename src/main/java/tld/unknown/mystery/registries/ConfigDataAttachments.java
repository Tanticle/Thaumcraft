package tld.unknown.mystery.registries;

import com.mojang.serialization.Codec;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import tld.unknown.mystery.Thaumcraft;
import tld.unknown.mystery.attachments.AuraAttachment;
import tld.unknown.mystery.attachments.InfusionAttachment;
import tld.unknown.mystery.attachments.ResearchAttachment;
import static tld.unknown.mystery.api.ThaumcraftData.Capabilities;

import java.util.HashMap;
import java.util.function.Supplier;

public final class ConfigDataAttachments {

    private static final DeferredRegister<AttachmentType<?>> REGISTRY = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, Thaumcraft.MOD_ID);

    /* -------------------------------------------------------------------------------------------------------------- */

    public static final Supplier<AttachmentType<AuraAttachment>> CHUNK_AURA = register(Capabilities.AURA, () -> new AuraAttachment((short)0), AuraAttachment.CODEC);
    public static final Supplier<AttachmentType<ResearchAttachment>> PLAYER_RESEARCH = register(Capabilities.RESEARCH, ResearchAttachment::new, ResearchAttachment.CODEC);
    public static final Supplier<AttachmentType<InfusionAttachment>> ITEM_ENCHANTMENT = register(Capabilities.INFUSION_ENCHANTMENT, () -> new InfusionAttachment(new HashMap<>()), InfusionAttachment.CODEC);

    /* -------------------------------------------------------------------------------------------------------------- */

    public static void init(IEventBus bus) {
        REGISTRY.register(bus);
    }

    private static <T> Supplier<AttachmentType<T>> register(ResourceLocation id, Supplier<T> defaultValue, Codec<T> codec) {
        return REGISTRY.register(id.getPath(), () -> AttachmentType.builder(defaultValue).serialize(codec).build());
    }
}
