package tld.unknown.multiblock.util;

import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.codecs.PrimitiveCodec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;

public final class Codecs {

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
}
