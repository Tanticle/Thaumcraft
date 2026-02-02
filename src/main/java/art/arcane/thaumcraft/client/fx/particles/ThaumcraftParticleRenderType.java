package art.arcane.thaumcraft.client.fx.particles;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.TriState;
import art.arcane.thaumcraft.Thaumcraft;

public class ThaumcraftParticleRenderType {

    public static final ResourceLocation PARTICLES_TEXTURE = Thaumcraft.id("textures/misc/particles.png");

    private static final RenderType THAUMCRAFT_PARTICLES_RENDER_TYPE = RenderType.create(
            "thaumcraft_particles",
            DefaultVertexFormat.PARTICLE,
            VertexFormat.Mode.QUADS,
            1536,
            false,
            false,
            RenderType.CompositeState.builder()
                    .setShaderState(RenderStateShard.PARTICLE_SHADER)
                    .setTextureState(new RenderStateShard.TextureStateShard(PARTICLES_TEXTURE, TriState.FALSE, false))
                    .setTransparencyState(RenderStateShard.TRANSLUCENT_TRANSPARENCY)
                    .setDepthTestState(RenderStateShard.LEQUAL_DEPTH_TEST)
                    .setLightmapState(RenderStateShard.LIGHTMAP)
                    .setCullState(RenderStateShard.NO_CULL)
                    .setWriteMaskState(RenderStateShard.COLOR_DEPTH_WRITE)
                    .createCompositeState(false)
    );

    private static final RenderType THAUMCRAFT_PARTICLES_TRANSLUCENT_RENDER_TYPE = RenderType.create(
            "thaumcraft_particles_translucent",
            DefaultVertexFormat.PARTICLE,
            VertexFormat.Mode.QUADS,
            1536,
            false,
            false,
            RenderType.CompositeState.builder()
                    .setShaderState(RenderStateShard.PARTICLE_SHADER)
                    .setTextureState(new RenderStateShard.TextureStateShard(PARTICLES_TEXTURE, TriState.FALSE, false))
                    .setTransparencyState(RenderStateShard.TRANSLUCENT_TRANSPARENCY)
                    .setDepthTestState(RenderStateShard.LEQUAL_DEPTH_TEST)
                    .setLightmapState(RenderStateShard.LIGHTMAP)
                    .setCullState(RenderStateShard.NO_CULL)
                    .setWriteMaskState(RenderStateShard.COLOR_WRITE)
                    .createCompositeState(false)
    );

    public static final ParticleRenderType THAUMCRAFT_PARTICLES = new ParticleRenderType(
            "THAUMCRAFT_PARTICLES",
            THAUMCRAFT_PARTICLES_RENDER_TYPE
    );

    public static final ParticleRenderType THAUMCRAFT_PARTICLES_TRANSLUCENT = new ParticleRenderType(
            "THAUMCRAFT_PARTICLES_TRANSLUCENT",
            THAUMCRAFT_PARTICLES_TRANSLUCENT_RENDER_TYPE,
            false
    );
}
