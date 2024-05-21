package tld.unknown.mystery.util.codec;

import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.codecs.PrimitiveCodec;

import java.util.Optional;

public record EnumCodec<E extends Enum<E> & EnumCodec.Values>(Class<E> clazz) implements PrimitiveCodec<E> {

    @Override
    public <T> DataResult<E> read(DynamicOps<T> dynamicOps, T t) {
        DataResult<String> res = dynamicOps.getStringValue(t);
        if(res.error().isPresent() || res.result().isEmpty())
            return DataResult.error(() -> "Unable to parse EnumCodec for \"" + clazz.getSimpleName() + "\": " + res.error().get().message());
        Optional<E> value = Values.getValue(clazz, res.result().get());
        return value.map(DataResult::success).orElseGet(() -> DataResult.error(() -> "Unable to parse EnumCodec for \"" + clazz.getSimpleName() + "\": Unknown enum value \"" + res.result().get() + "\""));
    }

    @Override
    public <T> T write(DynamicOps<T> dynamicOps, E e) {
        return dynamicOps.createString(e.getSerializedName());
    }

    public interface Values {

        String getSerializedName();

        static <E extends Enum<E> & Values> Optional<E> getValue(Class<E> clazz, String serializedName) {
            for(E e : clazz.getEnumConstants())
                if(e.getSerializedName().equalsIgnoreCase(serializedName))
                    return Optional.of(e);
            return Optional.empty();
        }
    }
}
