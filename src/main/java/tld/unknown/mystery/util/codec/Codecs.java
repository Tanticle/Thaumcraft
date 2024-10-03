package tld.unknown.mystery.util.codec;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.codecs.PrimitiveCodec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.phys.Vec2;
import tld.unknown.mystery.util.BitPacker;

import java.util.List;

public final class Codecs {

    public static final Codec<Vec2> VECTOR_2 = Codec.INT.listOf().xmap(l -> new Vec2(l.get(0), l.get(1)), v -> List.of((int)v.x, (int)v.y));
    public static final StreamCodec<ByteBuf, Vec2> VECTOR_2_STREAM = ByteBufCodecs.INT.apply(ByteBufCodecs.list()).map(l -> new Vec2(l.get(0), l.get(1)), v -> List.of((int)v.x, (int)v.y));

    public static final PrimitiveCodec<Character> CHAR = new PrimitiveCodec<>() {
        @Override
        public <T> DataResult<Character> read(final DynamicOps<T> ops, final T input) {
            DataResult<String> val = ops.getStringValue(input);
            if(val.error().isPresent()) {
                return DataResult.error(() -> "Unable to parse initial string!");
            } else if(val.result().isPresent() && val.result().get().length() > 1) {
                return DataResult.error(() -> "Value is not a singular char!");
            }
            return DataResult.success(val.result().get().charAt(0));
        }

        @Override
        public <T> T write(final DynamicOps<T> ops, final Character value) {
            return ops.createString(String.valueOf(value));
        }

        @Override
        public String toString() {
            return "Char";
        }
    };

    public static StreamCodec<ByteBuf, Character> CHAR_STREAM = new StreamCodec<>() {
        public Character decode(ByteBuf buffer) {
            return buffer.readChar();
        }

        public void encode(ByteBuf buffer, Character value) {
            buffer.writeChar(value);
        }
    };

    public static Codec<boolean[]> bitFieldCodec(int length, BitPacker.Length size) {
        return switch (size) {
            case BYTE -> Codec.BYTE.xmap(b -> BitPacker.readFlags(b, length, size), array -> (byte)BitPacker.encodeFlags(array, size));
            case SHORT -> Codec.SHORT.xmap(b -> BitPacker.readFlags(b, length, size), array -> (short)BitPacker.encodeFlags(array, size));
            case INTEGER -> Codec.INT.xmap(b -> BitPacker.readFlags(b, length, size), array -> BitPacker.encodeFlags(array, size));
        };
    }
}
