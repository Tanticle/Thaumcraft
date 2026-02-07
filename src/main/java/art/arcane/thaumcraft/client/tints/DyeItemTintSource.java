package art.arcane.thaumcraft.client.tints;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.color.item.ItemTintSource;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public record DyeItemTintSource(DyeColor defaultColour) implements ItemTintSource {

    public static final MapCodec<DyeItemTintSource> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(DyeColor.CODEC.fieldOf("default").forGetter(DyeItemTintSource::defaultColour)).apply(i, DyeItemTintSource::new));

    @Override
    public int calculate(ItemStack itemStack, @Nullable ClientLevel clientLevel, @Nullable LivingEntity livingEntity) {
        DyeColor color = itemStack.getOrDefault(DataComponents.BASE_COLOR, defaultColour());
        return 0xFF000000 | color.getTextureDiffuseColor();
    }

    @Override
    public MapCodec<? extends ItemTintSource> type() {
        return CODEC;
    }
}
