package art.arcane.thaumcraft.data.attachments;

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
import art.arcane.thaumcraft.api.ThaumcraftData;
import art.arcane.thaumcraft.data.aura.AuraBiomeInfo;
import art.arcane.thaumcraft.registries.ConfigDataRegistries;

import java.util.Optional;

@Getter
public class AuraAttachment {

    private static final String NBT_BASE = "Base";
    private static final String NBT_VIS = "Vis";
    private static final String NBT_FLUX = "Flux";

    public static final short MAX_AURA = 512;

    private short baseVis;
    @Setter
    private float vis, flux;

    public AuraAttachment(short baseVis, float vis, float flux) {
        this.baseVis = baseVis;
        this.vis = vis;
        this.flux = flux;
    }

    public AuraAttachment(short baseVis) {
        this(baseVis, baseVis, 0);
    }

    private static final float DEFAULT_AURA_LEVEL = 0.5F;
    private static final Direction[] HORIZONTAL_DIRECTIONS = {Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST};

    public AuraAttachment(ChunkAccess access, RandomSource random) {
        Vec3i chunkPos = new Vec3i(access.getPos().x, access.getHeight(), access.getPos().z);
        RegistryAccess registryAccess = access.getLevel().registryAccess();
        float value = getBiomeAuraBase(registryAccess, access.getNoiseBiome(chunkPos.getX(), chunkPos.getY(), chunkPos.getZ()));
        for (Direction dir : HORIZONTAL_DIRECTIONS) {
            Vec3i offset = chunkPos.offset(dir.getUnitVec3i());
            value += getBiomeAuraBase(registryAccess, access.getNoiseBiome(offset.getX(), offset.getY(), offset.getZ()));
        }
        value /= 5.0F;
        float noise = 1.0F + (float)(random.nextGaussian() * 0.1);
        this.baseVis = (short)Math.clamp(value * MAX_AURA * noise, 0, MAX_AURA);
        this.vis = this.baseVis;
        this.flux = 0;
    }

    public static float getBiomeAuraBase(RegistryAccess access, Holder<Biome> biome) {
        try {
            Optional<ResourceKey<Biome>> key = biome.unwrapKey();
            if (key.isEmpty()) {
                return DEFAULT_AURA_LEVEL;
            }
            ResourceKey<AuraBiomeInfo> auraBiome = ResourceKey.create(ThaumcraftData.Registries.AURA_BIOME_INFO, key.get().location());
            AuraBiomeInfo info = ConfigDataRegistries.AURA_BIOME_INFO.get(access, auraBiome);
            float level = info != null ? info.auraLevel() : DEFAULT_AURA_LEVEL;
            return level > 0.01f ? level : DEFAULT_AURA_LEVEL;
        } catch (Exception e) {
            return DEFAULT_AURA_LEVEL;
        }
    }

    public static final Codec<AuraAttachment> CODEC = RecordCodecBuilder.create(i -> i.group(
            Codec.SHORT.fieldOf(NBT_BASE).forGetter(AuraAttachment::getBaseVis),
            Codec.FLOAT.fieldOf(NBT_VIS).forGetter(AuraAttachment::getVis),
            Codec.FLOAT.fieldOf(NBT_FLUX).forGetter(AuraAttachment::getFlux)
    ).apply(i, AuraAttachment::new));
}
