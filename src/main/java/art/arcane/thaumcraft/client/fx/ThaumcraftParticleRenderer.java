package art.arcane.thaumcraft.client.fx;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.MeshData;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.CoreShaders;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import art.arcane.thaumcraft.Thaumcraft;
import art.arcane.thaumcraft.client.fx.particles.FXGenericParticle;
import art.arcane.thaumcraft.client.fx.particles.ThaumcraftParticleRenderType;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@EventBusSubscriber(modid = Thaumcraft.MOD_ID, bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
public class ThaumcraftParticleRenderer {

    private static final List<FXGenericParticle> particles = new ArrayList<>();

    public static void addParticle(FXGenericParticle particle) {
        synchronized (particles) {
            particles.add(particle);
        }
    }

    public static void tickParticles() {
        synchronized (particles) {
            Iterator<FXGenericParticle> iterator = particles.iterator();
            while (iterator.hasNext()) {
                FXGenericParticle particle = iterator.next();
                particle.tick();
                if (!particle.isAlive()) {
                    iterator.remove();
                }
            }
        }
    }

    public static void clear() {
        synchronized (particles) {
            particles.clear();
        }
    }

    @SubscribeEvent
    public static void onRenderLevel(RenderLevelStageEvent event) {
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_TRANSLUCENT_BLOCKS) {
            return;
        }

        Minecraft mc = Minecraft.getInstance();
        ClientLevel level = mc.level;
        if (level == null) {
            return;
        }

        List<FXGenericParticle> toRender;
        synchronized (particles) {
            if (particles.isEmpty()) {
                return;
            }
            toRender = new ArrayList<>(particles);
        }

        Camera camera = event.getCamera();
        float partialTick = event.getPartialTick().getGameTimeDeltaPartialTick(true);

        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
        RenderSystem.disableCull();
        RenderSystem.setShader(CoreShaders.PARTICLE);
        RenderSystem.setShaderTexture(0, ThaumcraftParticleRenderType.PARTICLES_TEXTURE);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder buffer = tesselator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.PARTICLE);

        for (FXGenericParticle particle : toRender) {
            try {
                particle.render(buffer, camera, partialTick);
            } catch (Exception e) {
                Thaumcraft.LOGGER.error("Failed to render particle", e);
            }
        }

        MeshData meshData = buffer.build();
        if (meshData != null) {
            BufferUploader.drawWithShader(meshData);
        }

        RenderSystem.enableCull();
        RenderSystem.disableBlend();
    }
}
