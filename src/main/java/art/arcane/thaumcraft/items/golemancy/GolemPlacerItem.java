package art.arcane.thaumcraft.items.golemancy;

import net.minecraft.ChatFormatting;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import art.arcane.thaumcraft.api.ThaumcraftData;
import art.arcane.thaumcraft.api.components.GolemConfiguration;
import art.arcane.thaumcraft.data.golemancy.GolemMaterial;
import art.arcane.thaumcraft.entities.golem.GolemEntity;
import art.arcane.thaumcraft.registries.ConfigEntities;
import art.arcane.thaumcraft.registries.ConfigDataRegistries;
import art.arcane.thaumcraft.registries.ConfigItemComponents;
import art.arcane.thaumcraft.util.simple.SimpleCreativeTab;

import java.util.List;

public class GolemPlacerItem extends Item implements SimpleCreativeTab.MultipleRegistrar {

    public GolemPlacerItem(Properties props) {
        super(props.stacksTo(1));
    }

    @Override
    public Component getName(ItemStack stack) {
        GolemConfiguration config = stack.get(ConfigItemComponents.GOLEM_CONFIG.value());
        if (config != null) {
            String materialKey = config.material().location().getPath();
            Component materialName = Component.translatable("golem_material.thaumcraft." + materialKey);
            return Component.translatable("item.thaumcraft.golem_placer.has_material", materialName);
        }
        return super.getName(stack);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);

        GolemConfiguration config = stack.get(ConfigItemComponents.GOLEM_CONFIG.value());
        if (config == null) {
            return;
        }

        tooltipComponents.add(Component.translatable("tooltip.thaumcraft.golem_placer.flavor")
                .withStyle(ChatFormatting.DARK_GRAY, ChatFormatting.ITALIC));
        tooltipComponents.add(Component.translatable("tooltip.thaumcraft.golem_placer.material",
                        Component.translatable("golem_material.thaumcraft." + config.material().location().getPath()))
                .withStyle(ChatFormatting.GRAY));
        tooltipComponents.add(Component.translatable("tooltip.thaumcraft.golem_placer.head",
                        Component.translatable("golem_part.thaumcraft." + config.head().location().getPath()))
                .withStyle(ChatFormatting.GRAY));
        tooltipComponents.add(Component.translatable("tooltip.thaumcraft.golem_placer.arms",
                        Component.translatable("golem_part.thaumcraft." + config.arms().location().getPath()))
                .withStyle(ChatFormatting.GRAY));
        tooltipComponents.add(Component.translatable("tooltip.thaumcraft.golem_placer.legs",
                        Component.translatable("golem_part.thaumcraft." + config.legs().location().getPath()))
                .withStyle(ChatFormatting.GRAY));
        tooltipComponents.add(Component.translatable("tooltip.thaumcraft.golem_placer.addon",
                        Component.translatable("golem_part.thaumcraft." + config.addon().location().getPath()))
                .withStyle(ChatFormatting.GRAY));
        if (config.rank() > 0) {
            tooltipComponents.add(Component.translatable("tooltip.thaumcraft.golem_placer.rank", config.rank())
                    .withStyle(ChatFormatting.DARK_GRAY));
        }
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        if (!(level instanceof ServerLevel serverLevel)) return InteractionResult.SUCCESS;

        ItemStack stack = context.getItemInHand();
        GolemConfiguration config = stack.get(ConfigItemComponents.GOLEM_CONFIG.value());
        if (config == null) config = GolemConfiguration.defaultConfig();

        GolemEntity golem = new GolemEntity(ConfigEntities.GOLEM.entityType(), serverLevel);

        double x = context.getClickedPos().getX() + context.getClickedFace().getStepX() + 0.5;
        double y = context.getClickedPos().getY() + context.getClickedFace().getStepY();
        double z = context.getClickedPos().getZ() + context.getClickedFace().getStepZ() + 0.5;

        golem.moveTo(x, y, z, context.getRotation(), 0);
        golem.setMaterialKey(config.material());
        golem.setHeadKey(config.head());
        golem.setArmsKey(config.arms());
        golem.setLegsKey(config.legs());
        golem.setAddonKey(config.addon());
        golem.setRank(config.rank());
        golem.setHomePos(context.getClickedPos().relative(context.getClickedFace()));

        if (context.getPlayer() != null) {
            golem.setOwnerUUID(context.getPlayer().getUUID());
        }

        golem.applyMaterialModifiers();
        serverLevel.addFreshEntity(golem);

        if (context.getPlayer() != null && !context.getPlayer().getAbilities().instabuild) {
            stack.shrink(1);
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    public void getCreativeTabEntries(HolderLookup.Provider access, NonNullList<ItemStack> items) {
        ConfigDataRegistries.GOLEM_MATERIALS.keys(access).forEach(materialKey -> {
            ItemStack stack = new ItemStack(this);
            stack.set(ConfigItemComponents.GOLEM_CONFIG.value(), new GolemConfiguration(
                    materialKey,
                    ThaumcraftData.GolemParts.HEAD_BASIC,
                    ThaumcraftData.GolemParts.ARM_BASIC,
                    ThaumcraftData.GolemParts.LEG_WALKER,
                    ThaumcraftData.GolemParts.ADDON_NONE,
                    0, 0
            ));
            items.add(stack);
        });
    }
}
