package tld.unknown.mystery.util;

import com.mojang.datafixers.util.Pair;
import net.minecraft.resources.ResourceLocation;

import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;

public final class ReflectionUtils {

    public static <T, U> List<U> getAllStaticsOfType(Class<T> clazz, Class<U> type) {
        return Arrays.stream(clazz.getDeclaredFields()).filter(f -> {
            try {
                boolean stat = Modifier.isStatic(f.getModifiers());
                boolean assignable = f.get(null).getClass().equals(type);
                return stat && assignable;
            } catch(IllegalAccessException e) {
                return false;
            }
        }).map(f -> {
            try {
                f.setAccessible(true);
                return (U)f.get(null);
            } catch(IllegalAccessException ex) {
                throw new RuntimeException(ex);
            }
        }).toList();
    }
}
