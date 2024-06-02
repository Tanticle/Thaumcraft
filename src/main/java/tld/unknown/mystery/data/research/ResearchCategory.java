package tld.unknown.mystery.data.research;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import lombok.Builder;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.RegistryFileCodec;
import net.minecraft.resources.ResourceLocation;
import tld.unknown.mystery.api.ThaumcraftData;
import tld.unknown.mystery.util.IconTexture;

import java.util.Collections;
import java.util.List;

@Builder
public record ResearchCategory(IconTexture icon, List<ResourceLocation> requirements) {

    public static final ResearchCategory UNKNOWN = new ResearchCategory(new IconTexture(ThaumcraftData.Textures.UNKNOWN), Collections.emptyList());

    public static Component getName(ResourceLocation loc) {
        return Component.translatable("research." + loc.getNamespace() + ".category." + loc.getPath() + ".name");
    }

    public static final MapCodec<ResearchCategory> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
            IconTexture.CODEC.fieldOf("icon").forGetter(ResearchCategory::icon),
            ResourceLocation.CODEC.listOf().optionalFieldOf("requirements", Collections.emptyList()).forGetter(ResearchCategory::requirements)
    ).apply(i, ResearchCategory::new));
    public static final Codec<Holder<ResearchCategory>> REGISTRY_CODEC = RegistryFileCodec.create(ThaumcraftData.Registries.RESEARCH_CATEGORY, CODEC.codec());

    public static final StreamCodec<ByteBuf, ResearchCategory> STREAM_CODEC = StreamCodec.composite(
            IconTexture.STREAM_CODEC, ResearchCategory::icon,
            ResourceLocation.STREAM_CODEC.apply(ByteBufCodecs.list()), ResearchCategory::requirements,
            ResearchCategory::new);
    public static final StreamCodec<RegistryFriendlyByteBuf, Holder<ResearchCategory>> REGISTRY_STREAM_CODEC = ByteBufCodecs.holder(ThaumcraftData.Registries.RESEARCH_CATEGORY, STREAM_CODEC);
}
