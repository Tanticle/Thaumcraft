package art.arcane.thaumcraft.client.fx.particles;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class FXGenericParticle extends Particle {

    private final int gridSize;
    private final int startParticle;
    private final int numParticles;
    private final int particleInc;
    private final boolean loop;
    private final float[] alphaKeys;
    private final float[] scaleKeys;
    private final float slowDown;
    private final float rotationSpeed;
    private final float randomX;
    private final float randomY;
    private final float randomZ;
    private final float windX;
    private final float windZ;
    private final float destRed;
    private final float destGreen;
    private final float destBlue;
    private float[] alphaFrames;
    private float[] scaleFrames;
    private float currentAngle;
    private float prevAngle;
    private int particleTextureIndexX;
    private int particleTextureIndexY;

    public FXGenericParticle(ClientLevel level, double x, double y, double z, double vx, double vy, double vz,
                              int maxAge, float r, float g, float b, float destR, float destG, float destB,
                              float[] alphaKeys, float[] scaleKeys, int gridSize, int startParticle,
                              int numParticles, int particleInc, boolean loop, float gravity, float slowDown,
                              float rotationSpeed, float randomX, float randomY, float randomZ,
                              float windX, float windZ) {
        super(level, x, y, z);
        this.xd = vx;
        this.yd = vy;
        this.zd = vz;
        this.lifetime = maxAge;
        this.rCol = r;
        this.gCol = g;
        this.bCol = b;
        this.destRed = destR;
        this.destGreen = destG;
        this.destBlue = destB;
        this.alphaKeys = alphaKeys;
        this.scaleKeys = scaleKeys;
        this.gridSize = gridSize;
        this.startParticle = startParticle;
        this.numParticles = numParticles;
        this.particleInc = particleInc;
        this.loop = loop;
        this.gravity = gravity;
        this.slowDown = slowDown;
        this.rotationSpeed = rotationSpeed;
        this.randomX = randomX;
        this.randomY = randomY;
        this.randomZ = randomZ;
        this.windX = windX;
        this.windZ = windZ;
        this.hasPhysics = false;
        this.currentAngle = level.random.nextFloat() * (float) Math.PI * 2.0f;
        this.prevAngle = currentAngle;
        calculateFrames();
        setParticleTextureIndex(startParticle);
    }

    private void calculateFrames() {
        alphaFrames = new float[lifetime + 1];
        float inc = (alphaKeys.length - 1) / (float) lifetime;
        float is = 0.0f;
        for (int a = 0; a <= lifetime; a++) {
            int isF = Mth.floor(is);
            float diff = (isF < alphaKeys.length - 1) ? alphaKeys[isF + 1] - alphaKeys[isF] : 0.0f;
            float pa = is - isF;
            alphaFrames[a] = alphaKeys[isF] + diff * pa;
            is += inc;
        }

        scaleFrames = new float[lifetime + 1];
        inc = (scaleKeys.length - 1) / (float) lifetime;
        is = 0.0f;
        for (int a = 0; a <= lifetime; a++) {
            int isF = Mth.floor(is);
            float diff = (isF < scaleKeys.length - 1) ? scaleKeys[isF + 1] - scaleKeys[isF] : 0.0f;
            float pa = is - isF;
            scaleFrames[a] = scaleKeys[isF] + diff * pa;
            is += inc;
        }
    }

    private void setParticleTextureIndex(int index) {
        if (index < 0) index = 0;
        particleTextureIndexX = index % gridSize;
        particleTextureIndexY = index / gridSize;
    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        this.prevAngle = this.currentAngle;

        if (this.age++ >= this.lifetime) {
            this.remove();
            return;
        }

        this.currentAngle += (float) Math.PI * rotationSpeed * 2.0f;
        this.yd -= 0.04 * gravity;
        this.move(this.xd, this.yd, this.zd);
        this.xd *= slowDown;
        this.yd *= slowDown;
        this.zd *= slowDown;
        this.xd += level.random.nextGaussian() * randomX;
        this.yd += level.random.nextGaussian() * randomY;
        this.zd += level.random.nextGaussian() * randomZ;
        this.xd += windX;
        this.zd += windZ;

        if (loop) {
            setParticleTextureIndex(startParticle + (age / particleInc) % numParticles);
        } else {
            float fs = age / (float) lifetime;
            setParticleTextureIndex((int) (startParticle + Math.min(numParticles * fs, numParticles - 1)));
        }
    }

    @Override
    public void render(VertexConsumer buffer, Camera camera, float partialTick) {
        float fs = Mth.clamp((age + partialTick) / lifetime, 0.0f, 1.0f);
        float pr = rCol + (destRed - rCol) * fs;
        float pg = gCol + (destGreen - gCol) * fs;
        float pb = bCol + (destBlue - bCol) * fs;

        float currentAlpha = (alphaFrames.length > 0) ? alphaFrames[Math.min(age, alphaFrames.length - 1)] : 1.0f;
        float currentScale = (scaleFrames.length > 0) ? scaleFrames[Math.min(age, scaleFrames.length - 1)] : 1.0f;

        Vec3 camPos = camera.getPosition();
        float fx = (float) (Mth.lerp(partialTick, this.xo, this.x) - camPos.x);
        float fy = (float) (Mth.lerp(partialTick, this.yo, this.y) - camPos.y);
        float fz = (float) (Mth.lerp(partialTick, this.zo, this.z) - camPos.z);

        float ts = 0.1f * currentScale;
        float tx1 = particleTextureIndexX / (float) gridSize;
        float tx2 = tx1 + 1.0f / gridSize;
        float ty1 = particleTextureIndexY / (float) gridSize;
        float ty2 = ty1 + 1.0f / gridSize;

        Quaternionf quaternion = camera.rotation();
        float angle = Mth.lerp(partialTick, prevAngle, currentAngle);
        if (angle != 0.0f) {
            quaternion = new Quaternionf(quaternion);
            quaternion.rotateZ(angle);
        }

        Vector3f[] vertices = new Vector3f[]{
                new Vector3f(-1.0f, -1.0f, 0.0f),
                new Vector3f(-1.0f, 1.0f, 0.0f),
                new Vector3f(1.0f, 1.0f, 0.0f),
                new Vector3f(1.0f, -1.0f, 0.0f)
        };

        for (int i = 0; i < 4; i++) {
            Vector3f vertex = vertices[i];
            vertex.rotate(quaternion);
            vertex.mul(ts);
            vertex.add(fx, fy, fz);
        }

        int light = getLightColor(partialTick);
        buffer.addVertex(vertices[0].x(), vertices[0].y(), vertices[0].z())
                .setUv(tx1, ty2).setColor(pr, pg, pb, currentAlpha).setLight(light);
        buffer.addVertex(vertices[1].x(), vertices[1].y(), vertices[1].z())
                .setUv(tx1, ty1).setColor(pr, pg, pb, currentAlpha).setLight(light);
        buffer.addVertex(vertices[2].x(), vertices[2].y(), vertices[2].z())
                .setUv(tx2, ty1).setColor(pr, pg, pb, currentAlpha).setLight(light);
        buffer.addVertex(vertices[3].x(), vertices[3].y(), vertices[3].z())
                .setUv(tx2, ty2).setColor(pr, pg, pb, currentAlpha).setLight(light);
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ThaumcraftParticleRenderType.THAUMCRAFT_PARTICLES;
    }

    @Override
    public int getLightColor(float partialTick) {
        return 0xF000F0;
    }
}
