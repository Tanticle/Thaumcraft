package art.arcane.thaumcraft.client.tints;

import com.mojang.serialization.MapCodec;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.item.ItemTintSource;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.HolderLookup;
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
        if (config == null) {
            return 0xFFFFFFFF;
        }

        HolderLookup.Provider access = null;
        if (clientLevel != null) {
            access = clientLevel.registryAccess();
        } else if (livingEntity != null && livingEntity.level() instanceof ClientLevel entityClientLevel) {
            access = entityClientLevel.registryAccess();
        } else {
            Minecraft mc = Minecraft.getInstance();
            if (mc.level != null) {
                access = mc.level.registryAccess();
            }
        }

        if (access != null) {
            try {
                GolemMaterial material = ConfigDataRegistries.GOLEM_MATERIALS.get(access, config.material());
                return material.itemColor().argb32(true);
            } catch (Exception ignored) {
            }
        }

        return switch (config.material().location().getPath()) {
            case "wood" -> 0xFF8B6914;
            case "iron" -> 0xFFC8C8C8;
            case "clay" -> 0xFFB87333;
            case "brass" -> 0xFFDAA520;
            case "thaumium" -> 0xFF5050A0;
            case "void" -> 0xFF2D0060;
            default -> 0xFFFFFFFF;
        };
    }

    @Override
    public MapCodec<? extends ItemTintSource> type() {
        return CODEC;
    }
}
