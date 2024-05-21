package tld.unknown.mystery.api;

import net.minecraft.client.Minecraft;

public final class ChaumtraftUtils {

    public static boolean shouldShowAspects() {
        Minecraft mc = Minecraft.getInstance();
        return mc.screen != null && mc.level != null && mc.options.keyShift.isDown();
    }
}
