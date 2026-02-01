package art.arcane.thaumcraft.client.fx.particles;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.util.Mth;

public class SmokeSpiralParticle extends TextureSheetParticle {

    private final double originX;
    private final double originY;
    private final double originZ;
    private final float radius;
    private final float startAngle;
    private final int minY;

    protected SmokeSpiralParticle(ClientLevel level, double x, double y, double z, SmokeSpiralParticleOptions options, SpriteSet sprites) {
        super(level, x, y, z);
        this.originX = x;
        this.originY = y;
        this.originZ = z;
        this.radius = options.radius();
        this.startAngle = options.startAngle();
        this.minY = options.minY();

        int color = options.color();
        this.rCol = ((color >> 16) & 0xFF) / 255.0f;
        this.gCol = ((color >> 8) & 0xFF) / 255.0f;
        this.bCol = (color & 0xFF) / 255.0f;

        this.lifetime = 20 + level.random.nextInt(10);
        this.quadSize = 0.15f;
        this.gravity = 0;
        this.xd = 0;
        this.yd = 0;
        this.zd = 0;
        this.setSpriteFromAge(sprites);
    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;

        if (this.age++ >= this.lifetime) {
            this.remove();
            return;
        }

        float progress = (float) this.age / this.lifetime;

        float r1 = startAngle + 720.0f * progress;
        float r2 = 90.0f - 180.0f * progress;

        float mX = -Mth.sin(r1 * Mth.DEG_TO_RAD) * Mth.cos(r2 * Mth.DEG_TO_RAD) * radius;
        float mZ = Mth.cos(r1 * Mth.DEG_TO_RAD) * Mth.cos(r2 * Mth.DEG_TO_RAD) * radius;
        float mY = -Mth.sin(r2 * Mth.DEG_TO_RAD) * radius;

        this.x = originX + mX;
        this.y = Math.max(originY + mY, minY + 0.1);
        this.z = originZ + mZ;

        this.alpha = 0.66f * (1.0f - progress);
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    public static class Provider implements ParticleProvider<SmokeSpiralParticleOptions> {

        private final SpriteSet sprites;

        public Provider(SpriteSet sprites) {
            this.sprites = sprites;
        }

        @Override
        public Particle createParticle(SmokeSpiralParticleOptions options, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            SmokeSpiralParticle particle = new SmokeSpiralParticle(level, x, y, z, options, sprites);
            particle.pickSprite(sprites);
            return particle;
        }
    }
}
