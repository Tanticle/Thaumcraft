package art.arcane.thaumcraft.api;

import net.minecraft.client.Minecraft;

public final class ThaumcraftUtils {

    public static boolean shouldShowAspects() {
        Minecraft mc = Minecraft.getInstance();
        return mc.screen != null && mc.level != null && mc.options.keyShift.isDown();
    }
}
