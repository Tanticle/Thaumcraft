package tld.unknown.mystery.data.research;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lombok.Builder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import tld.unknown.mystery.data.DataRegistries;
import tld.unknown.mystery.util.DataResource;
import tld.unknown.mystery.util.codec.Codecs;
import tld.unknown.mystery.util.IconTexture;

import java.util.Collections;
import java.util.List;

@Builder
public record ResearchCategory(IconTexture icon, List<DataResource<ResearchEntry>> requirements) {

    public static Component getName(ResourceLocation loc) {
        return Component.translatable("research." + loc.getNamespace() + ".category." + loc.getPath() + ".name");
    }

    public static final Codec<ResearchCategory> CODEC = RecordCodecBuilder.create(i -> i.group(
            Codecs.ICON_TEXTURE.fieldOf("icon").forGetter(ResearchCategory::icon),
            Codecs.dataResourceCodec(DataRegistries.RESEARCH_ENTRIES).listOf().optionalFieldOf("requirements", Collections.emptyList()).forGetter(ResearchCategory::requirements)
    ).apply(i, ResearchCategory::new));
}
