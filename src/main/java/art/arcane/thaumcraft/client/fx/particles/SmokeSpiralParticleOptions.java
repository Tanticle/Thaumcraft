package art.arcane.thaumcraft.client.fx.particles;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import art.arcane.thaumcraft.registries.ConfigParticles;

public record SmokeSpiralParticleOptions(float radius, int startAngle, int minY, int color) implements ParticleOptions {

    public static final MapCodec<SmokeSpiralParticleOptions> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Codec.FLOAT.fieldOf("radius").forGetter(SmokeSpiralParticleOptions::radius),
                    Codec.INT.fieldOf("startAngle").forGetter(SmokeSpiralParticleOptions::startAngle),
                    Codec.INT.fieldOf("minY").forGetter(SmokeSpiralParticleOptions::minY),
                    Codec.INT.fieldOf("color").forGetter(SmokeSpiralParticleOptions::color)
            ).apply(instance, SmokeSpiralParticleOptions::new)
    );

    public static final StreamCodec<ByteBuf, SmokeSpiralParticleOptions> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.FLOAT, SmokeSpiralParticleOptions::radius,
            ByteBufCodecs.INT, SmokeSpiralParticleOptions::startAngle,
            ByteBufCodecs.INT, SmokeSpiralParticleOptions::minY,
            ByteBufCodecs.INT, SmokeSpiralParticleOptions::color,
            SmokeSpiralParticleOptions::new
    );

    public static final ParticleType<SmokeSpiralParticleOptions> TYPE = new ParticleType<>(false) {
        @Override
        public MapCodec<SmokeSpiralParticleOptions> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<? super ByteBuf, SmokeSpiralParticleOptions> streamCodec() {
            return STREAM_CODEC;
        }
    };

    @Override
    public ParticleType<?> getType() {
        return ConfigParticles.SMOKE_SPIRAL.get();
    }
}
