package art.arcane.thaumcraft.client.rendering.entity;

import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.resources.ResourceLocation;

public class GolemEntityRenderState extends LivingEntityRenderState {

    public ResourceLocation materialTexture;
    public int materialColor;
    public ResourceLocation headModel;
    public ResourceLocation armsModel;
    public ResourceLocation legsModel;
    public ResourceLocation addonModel;
    public byte golemColor = -1;
    public boolean following;
}
