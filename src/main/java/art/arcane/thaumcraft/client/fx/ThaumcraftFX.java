package art.arcane.thaumcraft.client.fx;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import art.arcane.thaumcraft.client.fx.particles.FXGenericParticle;
import art.arcane.thaumcraft.networking.packets.ClientboundBamfEffectPacket;
import art.arcane.thaumcraft.networking.packets.ClientboundSalisMundusEffectPacket;
import art.arcane.thaumcraft.registries.ConfigSounds;

import java.util.List;

public final class ThaumcraftFX {

    public static void drawSalisMundusSparkles(ClientboundSalisMundusEffectPacket data) {
        Minecraft mc = Minecraft.getInstance();
        ClientLevel level = mc.level;
        if (level == null) {
            return;
        }

        RandomSource random = level.random;
        Vec3 handPos = data.handPos();
        BlockPos target = data.target();
        Vec3 targetCenter = Vec3.atCenterOf(target);

        Vec3 direction = targetCenter.subtract(handPos);

        for (int i = 0; i < 50; i++) {
            boolean floaty = i < 50 / 3;
            float r = 1.0f;
            float g = Mth.randomBetween(random, 189f / 255f, 1.0f);
            float b = Mth.randomBetween(random, 64f / 255f, 1.0f);

            double vx = direction.x / 6.0 + random.nextGaussian() * 0.05;
            double vy = direction.y / 6.0 + random.nextGaussian() * 0.05 + (floaty ? 0.05 : 0.15);
            double vz = direction.z / 6.0 + random.nextGaussian() * 0.05;

            drawSimpleSparkle(
                    random,
                    handPos.x, handPos.y, handPos.z,
                    vx, vy, vz,
                    0.5f, r, g, b,
                    random.nextInt(5),
                    floaty ? (0.3f + random.nextFloat() * 0.5f) : 0.85f,
                    floaty ? 0.2f : 0.5f,
                    16
            );
        }

        level.playLocalSound(
                target.getX() + 0.5,
                target.getY() + 0.5,
                target.getZ() + 0.5,
                ConfigSounds.DUST.value(),
                SoundSource.PLAYERS,
                0.33f,
                1.0f + (float) random.nextGaussian() * 0.05f,
                false
        );

        Vec3 hitPos = data.hitPos();
        List<BlockPos> sparklePositions = data.sparklePositions();
        for (BlockPos sparklePos : sparklePositions) {
            drawBlockSparkles(sparklePos, hitPos);
        }
    }

    public static void drawBlockSparkles(BlockPos pos, Vec3 start) {
        Minecraft mc = Minecraft.getInstance();
        ClientLevel level = mc.level;
        if (level == null) {
            return;
        }

        RandomSource random = level.random;
        BlockState state = level.getBlockState(pos);
        AABB bs = state.getShape(level, pos).bounds();
        int num = (int) (getAverageEdgeLength(bs) * 20.0);

        double centerX = (bs.minX + bs.maxX) / 2.0;
        double centerY = (bs.minY + bs.maxY) / 2.0;
        double centerZ = (bs.minZ + bs.maxZ) / 2.0;
        double spreadX = (bs.maxX - bs.minX) / 2.0;
        double spreadY = (bs.maxY - bs.minY) / 2.0;
        double spreadZ = (bs.maxZ - bs.minZ) / 2.0;

        for (Direction face : Direction.values()) {
            BlockPos offsetPos = pos.relative(face);
            BlockState offsetState = level.getBlockState(offsetPos);
            if (!offsetState.isFaceSturdy(level, offsetPos, face.getOpposite())) {
                double faceX = face.getStepX() != 0 ? (face.getStepX() > 0 ? bs.maxX + 0.01 : bs.minX - 0.01) : centerX;
                double faceY = face.getStepY() != 0 ? (face.getStepY() > 0 ? bs.maxY + 0.01 : bs.minY - 0.01) : centerY;
                double faceZ = face.getStepZ() != 0 ? (face.getStepZ() > 0 ? bs.maxZ + 0.01 : bs.minZ - 0.01) : centerZ;

                for (int a = 0; a < num * 2; a++) {
                    double x = faceX;
                    double y = faceY;
                    double z = faceZ;

                    if (face.getStepX() == 0) x += random.nextGaussian() * spreadX;
                    if (face.getStepY() == 0) y += random.nextGaussian() * spreadY;
                    if (face.getStepZ() == 0) z += random.nextGaussian() * spreadZ;

                    if (face.getStepX() == 0) x = Mth.clamp(x, bs.minX, bs.maxX);
                    if (face.getStepY() == 0) y = Mth.clamp(y, bs.minY, bs.maxY);
                    if (face.getStepZ() == 0) z = Mth.clamp(z, bs.minZ, bs.maxZ);

                    float r = 1.0f;
                    float g = Mth.randomBetween(random, 189f / 255f, 1.0f);
                    float b = Mth.randomBetween(random, 64f / 255f, 1.0f);

                    Vec3 v1 = new Vec3(pos.getX() + x, pos.getY() + y, pos.getZ() + z);
                    int delay = random.nextInt(5) + (int) (v1.distanceTo(start) * 16.0);

                    drawSimpleSparkle(
                            random,
                            pos.getX() + x, pos.getY() + y, pos.getZ() + z,
                            0.0, 0.0025, 0.0,
                            0.4f + (float) random.nextGaussian() * 0.1f, r, g, b,
                            delay,
                            1.0f, 0.01f, 16
                    );
                }
            }
        }
    }

    public static void drawSimpleSparkle(RandomSource random, double x, double y, double z,
                                          double vx, double vy, double vz, float scale,
                                          float r, float g, float b, int delay,
                                          float decay, float grav, int baseAge) {
        Minecraft mc = Minecraft.getInstance();
        ClientLevel level = mc.level;
        if (level == null) {
            return;
        }

        boolean sp = random.nextFloat() < 0.2f;
        int age = baseAge * 4 + random.nextInt(baseAge);

        int alphaCount = 6 + random.nextInt(age / 3);
        float[] alphaKeys = new float[alphaCount];
        alphaKeys[0] = 0.0f;
        for (int a = 1; a < alphaKeys.length - 1; a++) {
            alphaKeys[a] = random.nextFloat();
        }
        alphaKeys[alphaKeys.length - 1] = 0.0f;

        float[] scaleKeys = new float[]{scale, scale * 2.0f};

        Wind wind = getWind(level, 5.0E-4);
        FXGenericParticle particle = new FXGenericParticle(
                level, x, y, z, vx, vy, vz,
                age, r, g, b, r, g, b,
                alphaKeys, scaleKeys,
                64,
                sp ? 320 : 512,
                16, 1, true,
                grav, decay,
                0.0f,
                5.0E-4f, 0.001f, 5.0E-4f,
                wind.x(), wind.z()
        );

        if (delay > 0) {
            addParticleWithDelay(particle, delay);
        } else {
            ThaumcraftParticleRenderer.addParticle(particle);
        }
    }

    public static void drawBamf(ClientboundBamfEffectPacket data) {
        Minecraft mc = Minecraft.getInstance();
        ClientLevel level = mc.level;
        if (level == null) {
            return;
        }

        RandomSource random = level.random;
        BlockPos pos = data.pos();
        double x = pos.getX() + 0.5;
        double y = pos.getY() + 0.5;
        double z = pos.getZ() + 0.5;

        int color = data.color();
        float r = ((color >> 16) & 0xFF) / 255.0f;
        float g = ((color >> 8) & 0xFF) / 255.0f;
        float b = (color & 0xFF) / 255.0f;

        if (data.playSound()) {
            level.playLocalSound(
                    x, y, z,
                    ConfigSounds.POOF.value(),
                    SoundSource.BLOCKS,
                    0.4f,
                    1.0f + (float) random.nextGaussian() * 0.05f,
                    false
            );
        }

        for (int a = 0; a < 6 + random.nextInt(3) + 2; a++) {
            double vx = (0.05f + random.nextFloat() * 0.05f) * (random.nextBoolean() ? -1 : 1);
            double vy = (0.05f + random.nextFloat() * 0.05f) * (random.nextBoolean() ? -1 : 1);
            double vz = (0.05f + random.nextFloat() * 0.05f) * (random.nextBoolean() ? -1 : 1);

            float pr = Mth.clamp(r * (1.0f + (float) random.nextGaussian() * 0.1f), 0.0f, 1.0f);
            float pg = Mth.clamp(g * (1.0f + (float) random.nextGaussian() * 0.1f), 0.0f, 1.0f);
            float pb = Mth.clamp(b * (1.0f + (float) random.nextGaussian() * 0.1f), 0.0f, 1.0f);

            int age = 20 + random.nextInt(15);
            float[] alphaKeys = new float[]{1.0f, 0.1f};
            float[] scaleKeys = new float[]{3.0f, 4.0f + random.nextFloat() * 3.0f};

            FXGenericParticle particle = new FXGenericParticle(
                    level,
                    x + vx * 2.0, y + vy * 2.0, z + vz * 2.0,
                    vx / 2.0, vy / 2.0, vz / 2.0,
                    age, pr, pg, pb, pr, pg, pb,
                    alphaKeys, scaleKeys,
                    16,
                    123, 5, 1, false,
                    0.0f, 0.7f,
                    random.nextFloat() * (random.nextBoolean() ? -1.0f : 1.0f),
                    0.0f, 0.0f, 0.0f,
                    0.0f, 0.0f
            );

            ThaumcraftParticleRenderer.addParticle(particle);
        }

        if (data.flair()) {
            for (int a = 0; a < 2 + random.nextInt(3); a++) {
                double vx = (0.025f + random.nextFloat() * 0.025f) * (random.nextBoolean() ? -1 : 1);
                double vy = (0.025f + random.nextFloat() * 0.025f) * (random.nextBoolean() ? -1 : 1);
                double vz = (0.025f + random.nextFloat() * 0.025f) * (random.nextBoolean() ? -1 : 1);
                drawWispyMotes(x + vx * 2.0, y + vy * 2.0, z + vz * 2.0, vx, vy, vz, 15 + random.nextInt(10), -0.01f);
            }

            int flashAge = 10 + random.nextInt(5);
            float[] flashAlpha = new float[]{1.0f, 0.0f};
            float[] flashScale = new float[]{10.0f + random.nextFloat() * 2.0f, 0.0f};

            FXGenericParticle flash = new FXGenericParticle(
                    level, x, y, z, 0, 0, 0,
                    flashAge, 1.0f, 0.9f, 1.0f, 1.0f, 0.9f, 1.0f,
                    flashAlpha, flashScale,
                    16,
                    77, 1, 1, false,
                    0.0f, 1.0f,
                    (float) random.nextGaussian(),
                    0.0f, 0.0f, 0.0f,
                    0.0f, 0.0f
            );

            mc.particleEngine.add(flash);
        }

        for (int a = 0; a < (data.flair() ? 2 : 0) + random.nextInt(3); a++) {
            drawCurlyWisp(x, y, z, 0, 0, 0, 1.0f,
                    (0.9f + random.nextFloat() * 0.1f + r) / 2.0f,
                    (0.1f + g) / 2.0f,
                    (0.5f + random.nextFloat() * 0.1f + b) / 2.0f,
                    0.75f, null, a, 0, 0);
        }
    }

    public static void drawWispyMotes(double x, double y, double z, double vx, double vy, double vz, int age, float grav) {
        Minecraft mc = Minecraft.getInstance();
        ClientLevel level = mc.level;
        if (level == null) {
            return;
        }

        RandomSource random = level.random;
        float r = 0.25f + random.nextFloat() * 0.75f;
        float g = 0.25f + random.nextFloat() * 0.75f;
        float b = 0.25f + random.nextFloat() * 0.75f;

        int maxAge = (int) (age + age / 2 * random.nextFloat());
        float[] alphaKeys = new float[]{0.0f, 0.6f, 0.6f, 0.0f};
        float[] scaleKeys = new float[]{1.0f, 0.5f};

        Wind wind = getWind(level, 0.001);
        FXGenericParticle particle = new FXGenericParticle(
                level, x, y, z, vx, vy, vz,
                maxAge, r, g, b, r, g, b,
                alphaKeys, scaleKeys,
                64,
                512, 16, 1, true,
                grav, 1.0f,
                0.0f,
                0.0025f, 0.0f, 0.0025f,
                wind.x(), wind.z()
        );

        ThaumcraftParticleRenderer.addParticle(particle);
    }

    public static void drawCurlyWisp(double x, double y, double z, double vx, double vy, double vz,
                                      float scale, float r, float g, float b, float a,
                                      Direction side, int seed, int layer, int delay) {
        Minecraft mc = Minecraft.getInstance();
        ClientLevel level = mc.level;
        if (level == null) {
            return;
        }

        RandomSource random = level.random;

        vx += (0.0025f + random.nextFloat() * 0.005f) * (random.nextBoolean() ? -1 : 1);
        vy += (0.0025f + random.nextFloat() * 0.005f) * (random.nextBoolean() ? -1 : 1);
        vz += (0.0025f + random.nextFloat() * 0.005f) * (random.nextBoolean() ? -1 : 1);

        if (side != null) {
            vx += side.getStepX() * 0.025f;
            vy += side.getStepY() * 0.025f;
            vz += side.getStepZ() * 0.025f;
        }

        int age = 25 + random.nextInt(20 + 20 * seed);
        float[] alphaKeys = new float[]{a, 0.0f};
        float[] scaleKeys = new float[]{5.0f * scale, (10.0f + random.nextFloat() * 4.0f) * scale};

        FXGenericParticle particle = new FXGenericParticle(
                level,
                x + vx * 5.0, y + vy * 5.0, z + vz * 5.0,
                vx, vy, vz,
                age, r, g, b, 0.1f, 0.0f, 0.1f,
                alphaKeys, scaleKeys,
                16,
                60 + random.nextInt(4), 1, 1, false,
                0.0f, 1.0f,
                (random.nextBoolean() ? (-2.0f - random.nextFloat() * 2.0f) : (2.0f + random.nextFloat() * 2.0f)),
                0.0f, 0.0f, 0.0f,
                0.0f, 0.0f
        );

        if (delay > 0) {
            addParticleWithDelay(particle, delay);
        } else {
            ThaumcraftParticleRenderer.addParticle(particle);
        }
    }

    private static void addParticleWithDelay(FXGenericParticle particle, int delayTicks) {
        DelayedParticleQueue.add(particle, delayTicks);
    }

    private static double getAverageEdgeLength(AABB box) {
        double dx = box.maxX - box.minX;
        double dy = box.maxY - box.minY;
        double dz = box.maxZ - box.minZ;
        return (dx + dy + dz) / 3.0;
    }

    public record Wind(float x, float z) {}

    private static Wind getWind(ClientLevel level, double windFactor) {
        int m = level.getMoonPhase();
        float angle = m * (40 + level.random.nextInt(10)) / 180.0f * (float) Math.PI;
        float cos = Mth.cos(angle);
        float sin = Mth.sin(angle);
        float windX = (float) (0.1 * cos * windFactor);
        float windZ = (float) (-0.1 * sin * windFactor);
        return new Wind(windX, windZ);
    }
}
