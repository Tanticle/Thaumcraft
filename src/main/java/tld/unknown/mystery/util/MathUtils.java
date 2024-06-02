package tld.unknown.mystery.util;

import org.joml.Vector3f;

public final class MathUtils {

    public static float px(float amount) {
        return 1F / 16 * amount;
    }

    public static Vector3f pxVector3f(float x, float y, float z) {
        return new Vector3f(px(x), px(y), px(z));
    }


}
