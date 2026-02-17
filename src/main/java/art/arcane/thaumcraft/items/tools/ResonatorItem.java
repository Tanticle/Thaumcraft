package art.arcane.thaumcraft.items.tools;

import art.arcane.thaumcraft.api.aspects.Aspect;
import art.arcane.thaumcraft.api.capabilities.IEssentiaCapability;
import art.arcane.thaumcraft.blocks.entities.TubeBlockEntity;
import art.arcane.thaumcraft.blocks.entities.TubeBufferBlockEntity;
import art.arcane.thaumcraft.blocks.entities.TubeFilterBlockEntity;
import art.arcane.thaumcraft.blocks.entities.TubeOnewayBlockEntity;
import art.arcane.thaumcraft.blocks.entities.TubeRestrictBlockEntity;
import art.arcane.thaumcraft.blocks.entities.TubeValveBlockEntity;
import art.arcane.thaumcraft.registries.ConfigCapabilities;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Direction;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.context.UseOnContext;

import java.util.Comparator;

public class ResonatorItem extends Item {

    private static final String COMPONENT_SUCTION = "msg.thaumcraft.resonator.suction";
    private static final String COMPONENT_SUCTION_VALUE = "msg.thaumcraft.resonator.suction.value";
    private static final String COMPONENT_CONTENT = "msg.thaumcraft.resonator.content";
    private static final String COMPONENT_CONTENT_VALUE = "msg.thaumcraft.resonator.content.value";
    private static final String COMPONENT_VALVE = "msg.thaumcraft.resonator.valve";
    private static final String COMPONENT_VALVE_OPEN = "msg.thaumcraft.resonator.valve.open";
    private static final String COMPONENT_VALVE_CLOSED = "msg.thaumcraft.resonator.valve.closed";
    private static final String COMPONENT_CONNECTIONS = "msg.thaumcraft.resonator.connections";
    private static final String COMPONENT_FACING = "msg.thaumcraft.resonator.facing";
    private static final String COMPONENT_FILTER = "msg.thaumcraft.resonator.filter";
    private static final String COMPONENT_FILTER_NONE = "msg.thaumcraft.resonator.filter.none";
    private static final String COMPONENT_RESTRICT = "msg.thaumcraft.resonator.restrict";
    private static final String COMPONENT_ONEWAY = "msg.thaumcraft.resonator.oneway";
    private static final String COMPONENT_CHOKE = "msg.thaumcraft.resonator.choke";

    public ResonatorItem(Properties props) {
        super(props.rarity(Rarity.UNCOMMON).stacksTo(1));
    }

    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        if (pContext.getLevel().isClientSide() || pContext.getPlayer() == null) {
            return InteractionResult.SUCCESS;
        }

        RegistryAccess access = pContext.getLevel().registryAccess();
        Direction side = pContext.getClickedFace();
        IEssentiaCapability cap = pContext.getLevel().getCapability(ConfigCapabilities.ESSENTIA, pContext.getClickedPos(), side);
        if (cap == null && pContext.getLevel().getBlockEntity(pContext.getClickedPos()) instanceof IEssentiaCapability fallback) {
            cap = fallback;
        }
        if (cap == null) {
            return InteractionResult.PASS;
        }

        MutableComponent suction = Component.translatable(COMPONENT_SUCTION).withStyle(ChatFormatting.DARK_PURPLE)
                .append(Component.literal(" - ").withStyle(ChatFormatting.DARK_GRAY))
                .append(Component.translatable(
                        COMPONENT_SUCTION_VALUE,
                        cap.getSuction(side),
                        Aspect.getName(access, cap.getSuctionType(side), false, false)).withStyle(ChatFormatting.RESET));
        pContext.getPlayer().displayClientMessage(suction, false);

        if (cap instanceof TubeBlockEntity tube) {
            pContext.getPlayer().displayClientMessage(
                    Component.translatable(COMPONENT_CONNECTIONS).withStyle(ChatFormatting.GRAY)
                            .append(Component.literal(" - ").withStyle(ChatFormatting.DARK_GRAY))
                            .append(buildConnectionSummary(tube)),
                    false
            );
            if (tube.supportsFacingControl()) {
                pContext.getPlayer().displayClientMessage(
                        Component.translatable(COMPONENT_FACING).withStyle(ChatFormatting.GRAY)
                                .append(Component.literal(" - ").withStyle(ChatFormatting.DARK_GRAY))
                                .append(directionComponent(tube.getFacing()).withStyle(ChatFormatting.RESET)),
                        false
                );
            }
        }

        if (cap instanceof TubeValveBlockEntity valve) {
            pContext.getPlayer().displayClientMessage(Component.translatable(COMPONENT_VALVE).withStyle(ChatFormatting.GOLD)
                    .append(Component.literal(" - ").withStyle(ChatFormatting.DARK_GRAY))
                    .append(Component.translatable(valve.isFlowAllowed() ? COMPONENT_VALVE_OPEN : COMPONENT_VALVE_CLOSED)
                            .withStyle(valve.isFlowAllowed() ? ChatFormatting.GREEN : ChatFormatting.RED)), false);
        }

        if (cap instanceof TubeFilterBlockEntity filter) {
            pContext.getPlayer().displayClientMessage(
                    Component.translatable(COMPONENT_FILTER).withStyle(ChatFormatting.GOLD)
                            .append(Component.literal(" - ").withStyle(ChatFormatting.DARK_GRAY))
                            .append(filter.getFilterAspect() == null
                                    ? Component.translatable(COMPONENT_FILTER_NONE).withStyle(ChatFormatting.GRAY)
                                    : Aspect.getName(access, filter.getFilterAspect(), false, false)),
                    false
            );
        }

        if (cap instanceof TubeOnewayBlockEntity) {
            pContext.getPlayer().displayClientMessage(Component.translatable(COMPONENT_ONEWAY).withStyle(ChatFormatting.GOLD), false);
        }

        if (cap instanceof TubeRestrictBlockEntity) {
            pContext.getPlayer().displayClientMessage(Component.translatable(COMPONENT_RESTRICT).withStyle(ChatFormatting.GOLD), false);
        }

        boolean printedDetailedContent = false;
        if (cap instanceof TubeBufferBlockEntity buffer) {
            pContext.getPlayer().displayClientMessage(
                    Component.translatable(COMPONENT_CHOKE).withStyle(ChatFormatting.GOLD)
                            .append(Component.literal(" - ").withStyle(ChatFormatting.DARK_GRAY))
                            .append(buildChokeSummary(buffer)),
                    false
            );
            if (!buffer.getContents().isEmpty()) {
                pContext.getPlayer().displayClientMessage(Component.translatable(COMPONENT_CONTENT).withStyle(ChatFormatting.BLUE), false);
                buffer.getContents().entrySet().stream()
                        .sorted(Comparator.comparing(e -> e.getKey().location().toString()))
                        .forEach(entry -> pContext.getPlayer().displayClientMessage(
                                Component.translatable(COMPONENT_CONTENT_VALUE, entry.getValue(), Aspect.getName(access, entry.getKey(), false, false)).withStyle(ChatFormatting.RESET),
                                false));
                printedDetailedContent = true;
            }
        }

        if (!printedDetailedContent && cap.getEssentia(side) > 0) {
            pContext.getPlayer().displayClientMessage(Component.translatable(COMPONENT_CONTENT).withStyle(ChatFormatting.BLUE)
                    .append(Component.literal(" - ").withStyle(ChatFormatting.DARK_GRAY))
                    .append(Component.translatable(COMPONENT_CONTENT_VALUE, cap.getEssentia(side), Aspect.getName(access, cap.getEssentiaType(side), false, false)).withStyle(ChatFormatting.RESET)), false);
        }

        return InteractionResult.SUCCESS;
    }

    private static MutableComponent directionComponent(Direction direction) {
        return Component.translatable("direction.minecraft." + direction.getSerializedName());
    }

    private static MutableComponent buildConnectionSummary(TubeBlockEntity tube) {
        MutableComponent summary = Component.empty();
        boolean first = true;
        for (Direction direction : Direction.values()) {
            if (!first) {
                summary.append(Component.literal(" "));
            }
            first = false;
            boolean open = tube.isConnectable(direction);
            summary.append(Component.literal(shortDirection(direction) + ":").withStyle(ChatFormatting.DARK_GRAY));
            summary.append(Component.literal(open ? "open" : "closed").withStyle(open ? ChatFormatting.GREEN : ChatFormatting.RED));
        }
        return summary;
    }

    private static MutableComponent buildChokeSummary(TubeBufferBlockEntity buffer) {
        MutableComponent summary = Component.empty();
        boolean first = true;
        for (Direction direction : Direction.values()) {
            if (!first) {
                summary.append(Component.literal(" "));
            }
            first = false;
            byte choke = buffer.getChoke(direction);
            summary.append(Component.literal(shortDirection(direction) + ":").withStyle(ChatFormatting.DARK_GRAY));
            summary.append(Component.literal(chokeName(choke)).withStyle(switch (choke) {
                case 1 -> ChatFormatting.YELLOW;
                case 2 -> ChatFormatting.RED;
                default -> ChatFormatting.GREEN;
            }));
        }
        return summary;
    }

    private static String shortDirection(Direction direction) {
        return switch (direction) {
            case NORTH -> "N";
            case SOUTH -> "S";
            case EAST -> "E";
            case WEST -> "W";
            case UP -> "U";
            case DOWN -> "D";
        };
    }

    private static String chokeName(byte choke) {
        return switch (choke) {
            case 1 -> "half";
            case 2 -> "closed";
            default -> "open";
        };
    }
}
