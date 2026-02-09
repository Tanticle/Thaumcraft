package art.arcane.thaumcraft.client.tints;

import com.mojang.serialization.MapCodec;
import net.minecraft.client.color.item.ItemTintSource;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import art.arcane.thaumcraft.api.components.GolemConfiguration;
import art.arcane.thaumcraft.data.golemancy.GolemMaterial;
import art.arcane.thaumcraft.registries.ConfigDataRegistries;
import art.arcane.thaumcraft.registries.ConfigItemComponents;

public class GolemMaterialItemTintSource implements ItemTintSource {

    public static final MapCodec<GolemMaterialItemTintSource> CODEC = MapCodec.unit(GolemMaterialItemTintSource::new);

    @Override
    public int calculate(ItemStack itemStack, @Nullable ClientLevel clientLevel, @Nullable LivingEntity livingEntity) {
        GolemConfiguration config = itemStack.get(ConfigItemComponents.GOLEM_CONFIG.value());
        if (config != null && clientLevel != null) {
            GolemMaterial material = ConfigDataRegistries.GOLEM_MATERIALS.get(clientLevel.registryAccess(), config.material());
            return material.itemColor().argb32(true);
        }
        return 0xFFFFFFFF;
    }

    @Override
    public MapCodec<? extends ItemTintSource> type() {
        return CODEC;
    }
}
