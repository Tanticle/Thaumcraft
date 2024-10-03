package tld.unknown.mystery.items.tools;

import net.minecraft.ChatFormatting;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.context.UseOnContext;
import tld.unknown.mystery.api.aspects.Aspect;
import tld.unknown.mystery.api.capabilities.IEssentiaCapability;
import tld.unknown.mystery.registries.ConfigCapabilities;

public class ResonatorItem extends Item {

    private static final String COMPONENT_SUCTION = "msg.chaumtraft.resonator.suction";
    private static final String COMPONENT_SUCTION_VALUE = "msg.chaumtraft.resonator.suction.value";
    private static final String COMPONENT_CONTENT = "msg.chaumtraft.resonator.content";
    private static final String COMPONENT_CONTENT_VALUE = "msg.chaumtraft.resonator.content.value";

    public ResonatorItem() {
        super(new Properties().rarity(Rarity.UNCOMMON).stacksTo(1));
    }

    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        if(!pContext.getLevel().isClientSide()) {
            RegistryAccess access = pContext.getLevel().registryAccess();
            IEssentiaCapability cap = pContext.getLevel().getCapability(ConfigCapabilities.ESSENTIA, pContext.getClickedPos(), pContext.getClickedFace());
            if(cap != null) {
                Component suction = Component.translatable(COMPONENT_SUCTION).withStyle(ChatFormatting.DARK_PURPLE)
                        .append(Component.literal(" - ").withStyle(ChatFormatting.DARK_GRAY))
                        .append(Component.translatable(COMPONENT_SUCTION_VALUE, cap.getSuction(pContext.getClickedFace()), Aspect.getName(pContext.getLevel().registryAccess(), cap.getSuctionType(pContext.getClickedFace()), false, false)).withStyle(ChatFormatting.RESET));
                pContext.getPlayer().sendSystemMessage(suction);
                if(cap.getEssentia(pContext.getClickedFace()) > 0) {
                    Component content = Component.translatable(COMPONENT_CONTENT).withStyle(ChatFormatting.DARK_BLUE)
                            .append(Component.literal(" - ").withStyle(ChatFormatting.DARK_GRAY))
                            .append(Component.translatable(COMPONENT_CONTENT_VALUE, cap.getEssentia(pContext.getClickedFace()), Aspect.getName(pContext.getLevel().registryAccess(), cap.getEssentiaType(pContext.getClickedFace()), false, false)));
                    pContext.getPlayer().sendSystemMessage(content);
                }
                return InteractionResult.SUCCESS;
            }
        }
        return super.useOn(pContext);
    }
}
