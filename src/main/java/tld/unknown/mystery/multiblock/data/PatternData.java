package tld.unknown.mystery.multiblock.data;

import com.google.gson.JsonParseException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.PrimitiveCodec;
import net.minecraft.world.item.crafting.Ingredient;

public class PatternData {

    private final StructureMaterial[] data;
    private final int width, height, depth;

    public PatternData(int width, int height, int depth) {
        this.width = width;
        this.height = height;
        this.depth = depth;

        this.data = new StructureMaterial[width * height * depth];
    }

    public StructureMaterial get(int x, int y, int z) throws IndexOutOfBoundsException {
        return data[determineIndex(x, y, z)];
    }

    public StructureMaterial set(int x, int y, int z, StructureMaterial value) throws IndexOutOfBoundsException {
        int index = determineIndex(x, y, z);
        StructureMaterial previous = data[index];
        data[index] = value;
        return previous;
    }

    private int determineIndex(int x, int y, int z) throws IndexOutOfBoundsException {
        if(x >= width || y >= height || z >= depth) {
            throw new IndexOutOfBoundsException(String.format("Position [%d, %d, %d] is outside the bounds of this 3D Grid! Max: [%d, %d, %d]", x, y, z, width - 1, height - 1, depth - 1));
        }

        int depth = (width - 1) * (height - 1) * z;
        int height = (width - 1) * y;
        return depth + height + x;
    }

    /*public static final Codec<PatternData> INGREDIENT = new PrimitiveCodec<>() {
        @Override
        public <T> DataResult<PatternData> read(DynamicOps<T> ops, T input) {
            try {
                return DataResult.success(Ingredient.fromJson(ops.convertTo(JsonOps.INSTANCE, input)));
            } catch(JsonParseException e) {
                return DataResult.error(() -> "Failed to parse Ingredient: " + e.getMessage());
            }
        }

        @Override
        public <T> T write(DynamicOps<T> ops, PatternData value) {
            return JsonOps.INSTANCE.convertTo(ops, value.toJson());
        }
    };*/
}
