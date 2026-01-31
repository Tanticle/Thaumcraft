package art.arcane.thaumcraft.util;

import com.mojang.serialization.Codec;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

public final class BitPacker {

    public static int encodeFlags(boolean[] flags, Length length) {
        int baseValue = length.base;
        for(int i = 0; i < length.length && i < flags.length; i++) {
            baseValue |= (flags[i] ? 1 : 0) << i;
        }
        return baseValue;
    }

    public static boolean[] readFlags(int value, int flagAmount, Length length) {
        boolean[] values = new boolean[flagAmount];
        for(int i = 0; i < length.length && i < flagAmount; i++) {
            values[i] = (value & 1 << i) != 0;
        }
        return values;
    }

    public static <T extends Enum<T>> int encodeFlags(Set<T> enumValues, Length length) {
        int base = length.base;
        for(T value : enumValues)
            if(value.ordinal() < length.length)
                base |= 1 << value.ordinal();
        return base;
    }

    public static <T extends Enum<T>> Set<T> readFlags(int value, Class<T> clazz, Length length) {
        Set<T> values = new HashSet<>();
        for(int i = 0; i < clazz.getEnumConstants().length && i < length.length; i++)
            if((value & (1 << i)) != 0)
                values.add(clazz.getEnumConstants()[i]);
        return values;
    }

    @Getter
    @AllArgsConstructor
    public enum Length {
        BYTE(8, (byte)0, Codec.BYTE),
        SHORT(16, (short)0, Codec.SHORT),
        INTEGER(32, 0, Codec.INT);

        private final int length, base;
        private final Codec<?> codec;
    }
}
