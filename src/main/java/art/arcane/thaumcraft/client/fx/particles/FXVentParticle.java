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

public class FXVentParticle extends Particle {

    private static final int GRID_SIZE = 64;
    private float currentScale;
    private float maxScale = 1.0f;

    public FXVentParticle(ClientLevel level, double x, double y, double z, double vx, double vy, double vz, int color) {
        super(level, x, y, z);
        this.setSize(0.02f, 0.02f);
        this.currentScale = this.random.nextFloat() * 0.1f + 0.05f;
        this.xd = vx;
        this.yd = vy;
        this.zd = vz;
        this.rCol = ((color >> 16) & 0xFF) / 255.0f;
        this.gCol = ((color >> 8) & 0xFF) / 255.0f;
        this.bCol = (color & 0xFF) / 255.0f;
        this.alpha = 0.4f;
        setHeading(this.xd, this.yd, this.zd, 0.125f, 5.0f);
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
    }

    public void setScale(float scale) {
        if (scale <= 0.0f) {
            return;
        }
        this.currentScale *= scale;
        this.maxScale *= scale;
    }

    private void setHeading(double x, double y, double z, float speed, float randomness) {
        double length = Math.sqrt(x * x + y * y + z * z);
        if (length < 1.0E-5) {
            return;
        }
        x /= length;
        y /= length;
        z /= length;
        x += this.random.nextGaussian() * (this.random.nextBoolean() ? -1.0 : 1.0) * 0.007499999832361937 * randomness;
        y += this.random.nextGaussian() * (this.random.nextBoolean() ? -1.0 : 1.0) * 0.007499999832361937 * randomness;
        z += this.random.nextGaussian() * (this.random.nextBoolean() ? -1.0 : 1.0) * 0.007499999832361937 * randomness;
        this.xd = x * speed;
        this.yd = y * speed;
        this.zd = z * speed;
    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;

        ++this.age;
        if (this.currentScale >= this.maxScale) {
            this.remove();
            return;
        }

        this.yd += 0.0025;
        this.move(this.xd, this.yd, this.zd);
        this.xd *= 0.85;
        this.yd *= 0.85;
        this.zd *= 0.85;

        if (this.currentScale < this.maxScale) {
            this.currentScale *= 1.15f;
        }
        if (this.currentScale > this.maxScale) {
            this.currentScale = this.maxScale;
        }

        if (this.onGround) {
            this.xd *= 0.7;
            this.zd *= 0.7;
        }
    }

    @Override
    public void render(VertexConsumer buffer, Camera camera, float partialTick) {
        float scale = this.currentScale;
        int part = Mth.clamp((int) (1.0f + scale / this.maxScale * 4.0f), 0, GRID_SIZE * GRID_SIZE - 1);

        // TC6 vent particles map to the same 64x64 atlas tiles used by particles.png.
        float u0 = (part % 16) / (float) GRID_SIZE;
        float u1 = u0 + 1.0f / GRID_SIZE;
        float v0 = (part / GRID_SIZE) / (float) GRID_SIZE;
        float v1 = v0 + 1.0f / GRID_SIZE;
        float quad = 0.3f * scale;

        Vec3 camPos = camera.getPosition();
        float px = (float) (Mth.lerp(partialTick, this.xo, this.x) - camPos.x);
        float py = (float) (Mth.lerp(partialTick, this.yo, this.y) - camPos.y);
        float pz = (float) (Mth.lerp(partialTick, this.zo, this.z) - camPos.z);

        Quaternionf quaternion = camera.rotation();
        Vector3f[] vertices = new Vector3f[]{
                new Vector3f(-1.0f, -1.0f, 0.0f),
                new Vector3f(-1.0f, 1.0f, 0.0f),
                new Vector3f(1.0f, 1.0f, 0.0f),
                new Vector3f(1.0f, -1.0f, 0.0f)
        };

        for (Vector3f vertex : vertices) {
            vertex.rotate(quaternion);
            vertex.mul(quad);
            vertex.add(px, py, pz);
        }

        float fade = this.maxScale <= 0.0f ? 0.0f : Mth.clamp((this.maxScale - scale) / this.maxScale, 0.0f, 1.0f);
        float alpha = this.alpha * fade;
        int light = getLightColor(partialTick);

        buffer.addVertex(vertices[0].x(), vertices[0].y(), vertices[0].z())
                .setUv(u1, v1).setColor(this.rCol, this.gCol, this.bCol, alpha).setLight(light);
        buffer.addVertex(vertices[1].x(), vertices[1].y(), vertices[1].z())
                .setUv(u1, v0).setColor(this.rCol, this.gCol, this.bCol, alpha).setLight(light);
        buffer.addVertex(vertices[2].x(), vertices[2].y(), vertices[2].z())
                .setUv(u0, v0).setColor(this.rCol, this.gCol, this.bCol, alpha).setLight(light);
        buffer.addVertex(vertices[3].x(), vertices[3].y(), vertices[3].z())
                .setUv(u0, v1).setColor(this.rCol, this.gCol, this.bCol, alpha).setLight(light);
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
