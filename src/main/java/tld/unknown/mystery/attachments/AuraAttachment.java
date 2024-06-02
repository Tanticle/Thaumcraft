package tld.unknown.mystery.attachments;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.Vec3i;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.chunk.ChunkAccess;
import org.joml.Math;
import tld.unknown.mystery.registries.ConfigDataRegistries;

import java.util.Optional;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AuraAttachment {

    private static final String NBT_BASE = "Base";
    private static final String NBT_VIS = "Vis";
    private static final String NBT_FLUX = "Flux";

    public static final short MAX_AURA = 500;

    private short baseVis;
    @Setter
    private float vis, flux;

    public AuraAttachment(short baseVis) {
        this.baseVis = baseVis;
        this.vis = baseVis;
        this.flux = 0;
    }

    public AuraAttachment(ChunkAccess access, RandomSource random) {
        Vec3i chunkPos = new Vec3i(access.getPos().x, access.getHeight(), access.getPos().z);
        float value = getBiomeAuraBase(access.getLevel().registryAccess(), access.getNoiseBiome(chunkPos.getX(), chunkPos.getY(), chunkPos.getZ()));
        for(Direction dir : Direction.values()) {
            Vec3i offset = chunkPos.offset(dir.getNormal());
            value += getBiomeAuraBase(access.getLevel().registryAccess(), access.getNoiseBiome(offset.getX(), offset.getY(), offset.getZ()));
        }
        value /= 5.0F;
        this.baseVis = (short)Math.clamp( value * MAX_AURA * (float)((1.0F + random.nextGaussian()) * 0.1F), 0, MAX_AURA);
        this.vis = this.baseVis;
        this.flux = 0;
    }

    public static float getBiomeAuraBase(RegistryAccess access, Holder<Biome> biome) {
        Optional<ResourceKey<Biome>> key = biome.unwrapKey();
        return key.map(biomeResourceKey -> ConfigDataRegistries.AURA_BIOME_INFO.get(access, biomeResourceKey.location()).auraLevel()).orElse(0F);
    }

    public static final Codec<AuraAttachment> CODEC = RecordCodecBuilder.create(i -> i.group(
            Codec.SHORT.fieldOf(NBT_BASE).forGetter(AuraAttachment::getBaseVis),
            Codec.FLOAT.fieldOf(NBT_VIS).forGetter(AuraAttachment::getVis),
            Codec.FLOAT.fieldOf(NBT_FLUX).forGetter(AuraAttachment::getFlux)
    ).apply(i, AuraAttachment::new));
}
