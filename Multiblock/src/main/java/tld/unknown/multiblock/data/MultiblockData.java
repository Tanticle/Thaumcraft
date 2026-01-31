package tld.unknown.multiblock.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record MultiblockData(
        MultiblockStructureData structure,
        MultiblockLogicData logic) {

    public static final Codec<MultiblockData> CODEC = RecordCodecBuilder.create(i -> i.group(
            MultiblockStructureData.CODEC.fieldOf("structure").forGetter(MultiblockData::structure),
            MultiblockLogicData.CODEC.fieldOf("logic").forGetter(MultiblockData::logic)
    ).apply(i, MultiblockData::new));
}
