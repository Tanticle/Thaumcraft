package tld.unknown.mystery.util.codec;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.codecs.EitherCodec;
import com.mojang.serialization.codecs.PrimitiveCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec2;
import tld.unknown.mystery.util.BitPacker;
import tld.unknown.mystery.util.IconTexture;
import tld.unknown.mystery.util.ItemUtils;
import tld.unknown.mystery.util.codec.data.CodecDataManager;
import tld.unknown.mystery.util.DataResource;

import java.util.List;

public final class Codecs {

    public static final Codec<TextColor> HEX_COLOR = Codec.INT.xmap(TextColor::fromRgb, TextColor::getValue);

    public static final Codec<Vec2> VECTOR_2 = Codec.INT.listOf().xmap(l -> new Vec2(l.get(0), l.get(1)), v -> List.of((int)v.x, (int)v.y));

    public static final Codec<Item> ITEM = ResourceLocation.CODEC.comapFlatMap(id -> {
        if(BuiltInRegistries.ITEM.containsKey(id))
            return DataResult.success(BuiltInRegistries.ITEM.get(id));
        return DataResult.error(() -> "Unknown Item Type \"" + id + "\"");
    }, BuiltInRegistries.ITEM::getKey);

    public static final Codec<ItemStack> ITEM_STACK_FULL = RecordCodecBuilder.create(i -> i.group(
            ITEM.fieldOf("type").forGetter(ItemStack::getItem),
            Codec.INT.optionalFieldOf("amount", 1).forGetter(ItemStack::getCount)
    ).apply(i, ItemStack::new));

    public static final Codec<ItemStack> ITEM_STACK = new EitherCodec<>(ITEM, ITEM_STACK_FULL).xmap(
            e -> e.map(ItemStack::new, is -> is),
            is -> ItemUtils.isSimple(is) ? Either.left(is.getItem()) : Either.right(is));

    public static final Codec<IconTexture> ICON_TEXTURE = ResourceLocation.CODEC.xmap(IconTexture::new, IconTexture::getLocation);

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

    public static <T> Codec<DataResource<T>> dataResourceCodec(CodecDataManager<T> registry) {
        return ResourceLocation.CODEC.xmap(id -> DataResource.of(registry, id), DataResource::getId);
    }

    public static <T> Codec<DataResource<T>> dataResourceCodec(Registry<T> registry) {
        return ResourceLocation.CODEC.xmap(id -> DataResource.of(registry, id), DataResource::getId);
    }

    public static Codec<boolean[]> bitFieldCodec(int length, BitPacker.Length size) {
        return switch (size) {
            case BYTE -> Codec.BYTE.xmap(b -> BitPacker.readFlags(b, length, size), array -> (byte)BitPacker.encodeFlags(array, size));
            case SHORT -> Codec.SHORT.xmap(b -> BitPacker.readFlags(b, length, size), array -> (short)BitPacker.encodeFlags(array, size));
            case INTEGER -> Codec.INT.xmap(b -> BitPacker.readFlags(b, length, size), array -> BitPacker.encodeFlags(array, size));
        };
    }
}
