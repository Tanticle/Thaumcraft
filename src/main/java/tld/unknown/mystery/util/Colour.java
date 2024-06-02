package tld.unknown.mystery.util;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import net.minecraft.network.chat.TextColor;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.FastColor;

@AllArgsConstructor
public class Colour {

    private byte red, green, blue, alpha;

    public static Colour fromInteger(int value, boolean hasAlpha) {
        return new Colour(
                (byte)FastColor.ARGB32.red(value),
                (byte)FastColor.ARGB32.green(value),
                (byte)FastColor.ARGB32.blue(value),
                hasAlpha ? (byte)FastColor.ARGB32.alpha(value) : (byte)255);
    }

    public static Colour fromRGB(int r, int g, int b) {
        return new Colour((byte)r, (byte)g, (byte)b, (byte)255);
    }

    public static Colour fromARGB( int a, int r, int g, int b) {
        return new Colour((byte)r, (byte)g, (byte)b, (byte)a);
    }

    public static Colour fromHex(String hex) {
        return fromInteger(TextColor.parseColor(hex).result().orElse(TextColor.fromRgb(0x000000)).getValue(), true);
    }

    public int rgba32(boolean includeAlpha) {
        return includeAlpha ? FastColor.ARGB32.color(alpha, red, green, blue) : FastColor.ARGB32.color(red, green, blue);
    }

    public String hex() {
        return String.format("#%02x%02x%02x%02x", red, green, blue, alpha);
    }

    public static final Codec<Colour> CODEC = Codec.STRING.xmap(Colour::fromHex, Colour::hex);
    public static final StreamCodec<ByteBuf, Colour> STREAM_CODEC = ByteBufCodecs.STRING_UTF8.map(Colour::fromHex, Colour::hex);
}
