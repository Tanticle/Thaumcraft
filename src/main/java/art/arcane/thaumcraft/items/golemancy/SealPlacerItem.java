package art.arcane.thaumcraft.items.golemancy;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import art.arcane.thaumcraft.data.golemancy.SealBehavior;
import art.arcane.thaumcraft.data.golemancy.SealBehaviors;
import art.arcane.thaumcraft.data.golemancy.SealInstance;
import art.arcane.thaumcraft.data.golemancy.SealPos;
import art.arcane.thaumcraft.data.golemancy.SealSavedData;
import art.arcane.thaumcraft.data.golemancy.SealType;
import art.arcane.thaumcraft.registries.ConfigDataRegistries;
import art.arcane.thaumcraft.registries.ConfigItemComponents;
import art.arcane.thaumcraft.util.simple.SimpleCreativeTab;

public class SealPlacerItem extends Item implements SimpleCreativeTab.MultipleRegistrar {

    public SealPlacerItem(Properties props) {
        super(props.stacksTo(1));
    }

    @Override
    public Component getName(ItemStack stack) {
        ResourceKey<SealType> sealTypeKey = stack.get(ConfigItemComponents.SEAL_TYPE.value());
        if (sealTypeKey != null) {
            String sealKey = sealTypeKey.location().getPath();
            Component sealName = Component.translatable("seal.thaumcraft." + sealKey);
            return Component.translatable("item.thaumcraft.seal_placer.has_seal", sealName);
        }
        return super.getName(stack);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        if (!(level instanceof ServerLevel serverLevel)) return InteractionResult.SUCCESS;

        ItemStack stack = context.getItemInHand();
        ResourceKey<SealType> sealTypeKey = stack.get(ConfigItemComponents.SEAL_TYPE.value());
        if (sealTypeKey == null) return InteractionResult.FAIL;

        SealPos sealPos = new SealPos(context.getClickedPos(), context.getClickedFace());

        SealBehavior behavior = SealBehaviors.get(sealTypeKey);
        if (behavior != null && !behavior.canPlaceAt(serverLevel, context.getClickedPos(), context.getClickedFace())) {
            return InteractionResult.FAIL;
        }

        SealSavedData data = SealSavedData.get(serverLevel);

        if (data.getSeal(sealPos) != null) {
            return InteractionResult.FAIL;
        }

        SealInstance seal = new SealInstance(sealPos, sealTypeKey,
                context.getPlayer() != null ? context.getPlayer().getUUID() : java.util.UUID.randomUUID());
        data.addSeal(seal, serverLevel);

        if (context.getPlayer() != null && !context.getPlayer().getAbilities().instabuild) {
            stack.shrink(1);
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    public void getCreativeTabEntries(HolderLookup.Provider access, NonNullList<ItemStack> items) {
        ConfigDataRegistries.SEAL_TYPES.keys(access).forEach(sealTypeKey -> {
            ItemStack stack = new ItemStack(this);
            stack.set(ConfigItemComponents.SEAL_TYPE.value(), sealTypeKey);
            items.add(stack);
        });
    }
}
