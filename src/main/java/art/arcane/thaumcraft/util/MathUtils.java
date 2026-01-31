package art.arcane.thaumcraft.util;

import org.joml.Vector2f;
import org.joml.Vector3f;

public final class MathUtils {

    public static float px(float amount) {
        return 1F / 16 * amount;
    }

    public static Vector3f pxVector3f(float x, float y, float z) {
        return new Vector3f(px(x), px(y), px(z));
    }

    public static Vector2f pxVector2f(float x, float y) {
        return new Vector2f(px(x), px(y));
    }
}
