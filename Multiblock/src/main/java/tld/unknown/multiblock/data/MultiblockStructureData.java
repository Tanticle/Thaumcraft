package tld.unknown.multiblock.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.handler.codec.DecoderException;
import it.unimi.dsi.fastutil.chars.Char2ObjectArrayMap;
import lombok.Getter;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import tld.unknown.multiblock.util.Codecs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public record MultiblockStructureData(
        Vec3i boundingBox,
        BlockPredicate[][][] structure,
        Char2ObjectArrayMap<BlockPredicate> materialTable,
        boolean allowMirror) {

    public static MultiblockStructureData createFromData(Vec3i boundingBox, char[][][] pattern, Map<Character, BlockPredicate> materialTable, boolean allowMirror) {
        BlockPredicate[][][] structure = new BlockPredicate[boundingBox.getY()][boundingBox.getZ()][boundingBox.getX()];
        for(int y = 0; y < boundingBox.getY(); y++) {
            for(int z = 0; z < boundingBox.getZ(); z++) {
                for(int x = 0; x < boundingBox.getX(); x++) {
                    structure[y][z][x] = materialTable.get(pattern[y][z][x]);
                }
            }
        }
        return new MultiblockStructureData(boundingBox, structure, new Char2ObjectArrayMap<>(materialTable), allowMirror);
    }

    private char[][][] getStructurePattern() throws DecoderException {
        char[][][] pattern = new char[boundingBox.getY()][boundingBox.getZ()][boundingBox.getX()];
        for(int y = 0; y < boundingBox.getY(); y++) {
            for(int z = 0; z < boundingBox.getZ(); z++) {
                for(int x = 0; x < boundingBox.getX(); x++) {
                    pattern[y][z][x] = findMaterialKey(structure[y][z][x]);
                }
            }
        }
        return pattern;
    }

    private char findMaterialKey(BlockPredicate predicate) throws DecoderException {
        for (char c : materialTable.keySet()) {
            BlockPredicate material = materialTable.get(c);
            if(predicate.equals(material)) {
                return c;
            }
        }
        throw new DecoderException("No material matching the predicate " + predicate + " has been found!");
    }

    private static final Codec<char[][][]> PATTERN_CODEC =
            Codec.list(Codec.list(Codec.STRING.xmap(String::toCharArray, String::new))).xmap(
                    list -> {
                        char[][][] out = new char[list.size()][][];
                        for (int i = 0; i < list.size(); i++) {
                            List<char[]> rows = list.get(i);
                            out[i] = rows.toArray(char[][]::new);
                        }
                        return out;
                    },
                    array -> {
                        List<List<char[]>> list = new ArrayList<>();
                        for (char[][] layer : array) {
                            list.add(Arrays.asList(layer));
                        }
                        return list;
                    }
            );

    public static final Codec<MultiblockStructureData> CODEC = RecordCodecBuilder.create(i -> i.group(
            Vec3i.CODEC.fieldOf("bounding_box").forGetter(MultiblockStructureData::boundingBox),
            PATTERN_CODEC.fieldOf("pattern").forGetter(MultiblockStructureData::getStructurePattern),
            Codec.unboundedMap(Codecs.CHAR, BlockPredicate.CODEC).fieldOf("materials").forGetter(MultiblockStructureData::materialTable),
            Codec.BOOL.optionalFieldOf("allow_mirror", false).forGetter(MultiblockStructureData::allowMirror)
    ).apply(i, MultiblockStructureData::createFromData));
}
