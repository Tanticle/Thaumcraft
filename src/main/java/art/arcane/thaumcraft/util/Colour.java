package art.arcane.thaumcraft.util;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

@AllArgsConstructor
public class Colour {

    private byte red, green, blue, alpha;

    public static Colour fromInteger(int value, boolean hasAlpha) {
        return new Colour(
                (byte)(value >> 24 & 0xFF),
                (byte)(value >> 16 & 0xFF),
                (byte)(value >> 8 & 0xFF),
                hasAlpha ? (byte)(value & 0xFF) : (byte)255);
    }

    public static Colour fromRGB(int r, int g, int b) {
        return new Colour((byte)r, (byte)g, (byte)b, (byte)255);
    }

    public static Colour fromARGB(int a, int r, int g, int b) {
        return new Colour((byte)r, (byte)g, (byte)b, (byte)a);
    }

    public static Colour fromARGB(float a, float r, float g, float b) {
        return new Colour((byte)(r * 255), (byte)(g * 255), (byte)(b * 255), (byte)(a * 255));
    }

    public static Colour fromHex(String hex) {
        int value = Integer.parseUnsignedInt(hex.substring(1), 16);
        if(hex.length() <= 7) {
            value &= 0x00FFFFFF;
            byte red = (byte)((value >> 16) & 0xFF);
            byte green = (byte)((value >> 8) & 0xFF);
            byte blue = (byte)(value & 0xFF);
            return new Colour(red, green, blue, (byte)0xFF);
        }
        byte red = (byte)((value >> 24) & 0xFF);
        byte green = (byte)((value >> 16) & 0xFF);
        byte blue = (byte)((value >> 8) & 0xFF);
        byte alpha = (byte)(value & 0xFF);
        return new Colour(red, green, blue, alpha);
    }

    public int argb32(boolean includeAlpha) {
        int value = ((includeAlpha ? alpha : 0xFF) & 0xFF) << 24 | (red & 0xFF) << 16 | (green & 0xFF) << 8 | (blue & 0xFF);
        return value;
    }

    public String hex() {
        if(alpha == (byte)0xFF)
            return String.format("#%02X%02X%02X", red, green, blue);
        return String.format("#%02X%02X%02X%02X", red, green, blue, alpha);
    }

    @Override
    public String toString() {
        return hex();
    }

    public static final Codec<Colour> CODEC = Codec.STRING.xmap(Colour::fromHex, Colour::hex);
    public static final StreamCodec<ByteBuf, Colour> STREAM_CODEC = ByteBufCodecs.STRING_UTF8.map(Colour::fromHex, Colour::hex);
}
