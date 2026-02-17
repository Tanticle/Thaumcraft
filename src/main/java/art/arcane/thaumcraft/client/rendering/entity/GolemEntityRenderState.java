package art.arcane.thaumcraft.client.rendering.entity;

import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.resources.ResourceLocation;

public class GolemEntityRenderState extends LivingEntityRenderState {

    public int entityId;
    public float attackAnim;
    public float wheelRotation;
    public float breakerRotation;
    public double moveX;
    public double moveZ;
    public ResourceLocation materialTexture;
    public int materialColor;
    public ResourceLocation headModel;
    public ResourceLocation headTexture;
    public ResourceLocation armsModel;
    public ResourceLocation armsTexture;
    public ResourceLocation legsModel;
    public ResourceLocation legsTexture;
    public ResourceLocation addonModel;
    public ResourceLocation addonTexture;
    public byte golemColor = -1;
    public boolean following;
}
