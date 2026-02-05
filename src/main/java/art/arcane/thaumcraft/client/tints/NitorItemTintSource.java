package art.arcane.thaumcraft.client.tints;

import art.arcane.thaumcraft.items.blocks.NitorBlockItem;
import com.mojang.serialization.MapCodec;
import net.minecraft.client.color.item.ItemTintSource;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class NitorItemTintSource implements ItemTintSource {

    public static final MapCodec<NitorItemTintSource> CODEC = MapCodec.unit(NitorItemTintSource::new);

    @Override
    public int calculate(ItemStack itemStack, @Nullable ClientLevel clientLevel, @Nullable LivingEntity livingEntity) {
        if (itemStack.getItem() instanceof NitorBlockItem nitor) {
            return 0xFF000000 | nitor.getColor().getMapColor().col;
        }
        return 0xFFFFFFFF;
    }

    @Override
    public MapCodec<? extends ItemTintSource> type() {
        return CODEC;
    }
}
