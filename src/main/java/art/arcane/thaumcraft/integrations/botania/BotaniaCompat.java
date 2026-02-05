package art.arcane.thaumcraft.integrations.botania;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.fml.ModList;

import java.lang.reflect.Method;

public final class BotaniaCompat {

    private static final String BOTANIA_MOD_ID = "botania";
    private static Boolean loaded;
    private static Class<?> petalApothecaryClass;
    private static Class<?> stateEnumClass;
    private static Method getFluidMethod;
    private static Method setFluidMethod;
    private static Object waterState;
    private static Object emptyState;

    public static boolean isLoaded() {
        if (loaded == null) {
            loaded = ModList.get().isLoaded(BOTANIA_MOD_ID);
            if (loaded) {
                initReflection();
            }
        }
        return loaded;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private static void initReflection() {
        try {
            petalApothecaryClass = Class.forName("vazkii.botania.api.block.PetalApothecary");
            stateEnumClass = Class.forName("vazkii.botania.api.block.PetalApothecary$State");
            getFluidMethod = petalApothecaryClass.getMethod("getFluid");
            setFluidMethod = petalApothecaryClass.getMethod("setFluid", stateEnumClass);
            waterState = Enum.valueOf((Class<Enum>) stateEnumClass, "WATER");
            emptyState = Enum.valueOf((Class<Enum>) stateEnumClass, "EMPTY");
        } catch (Exception e) {
            loaded = false;
        }
    }

    public static boolean isPetalApothecary(BlockEntity blockEntity) {
        if (!isLoaded() || blockEntity == null || petalApothecaryClass == null) {
            return false;
        }
        return petalApothecaryClass.isInstance(blockEntity);
    }

    public static boolean needsWater(BlockEntity blockEntity) {
        if (!isPetalApothecary(blockEntity)) {
            return false;
        }
        try {
            Object currentState = getFluidMethod.invoke(blockEntity);
            return currentState == emptyState;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean fillWithWater(BlockEntity blockEntity) {
        if (!isPetalApothecary(blockEntity)) {
            return false;
        }
        try {
            setFluidMethod.invoke(blockEntity, waterState);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean tryFillApothecary(Level level, BlockPos pos) {
        if (!isLoaded()) {
            return false;
        }
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (needsWater(blockEntity)) {
            return fillWithWater(blockEntity);
        }
        return false;
    }
}
