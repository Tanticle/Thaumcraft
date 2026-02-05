package art.arcane.thaumcraft.client.tints;

import art.arcane.thaumcraft.registries.ConfigItemComponents;
import com.mojang.serialization.MapCodec;
import net.minecraft.client.color.item.ItemTintSource;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class NitorItemTintSource implements ItemTintSource {

    public static final MapCodec<NitorItemTintSource> CODEC = MapCodec.unit(NitorItemTintSource::new);

    @Override
    public int calculate(ItemStack itemStack, @Nullable ClientLevel clientLevel, @Nullable LivingEntity livingEntity) {
        DyeColor color = itemStack.getOrDefault(ConfigItemComponents.DYE_COLOR.value(), DyeColor.YELLOW);
        return 0xFF000000 | color.getMapColor().col;
    }

    @Override
    public MapCodec<? extends ItemTintSource> type() {
        return CODEC;
    }
}
