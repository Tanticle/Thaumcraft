package art.arcane.thaumcraft.api.crafting;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public interface DustTrigger {

    List<DustTrigger> TRIGGERS = new ArrayList<>();

    static void register(DustTrigger trigger) {
        TRIGGERS.add(trigger);
    }

    Placement getValidTarget(Level level, Player player, BlockPos pos, Direction face);

    void execute(Level level, Player player, BlockPos pos, Placement placement, Direction face);

    default List<BlockPos> sparkle(Level level, Player player, BlockPos pos, Placement placement) {
        return Collections.singletonList(pos);
    }

    record Placement(int xOffset, int yOffset, int zOffset, Direction facing) {
        public static final Placement ZERO = new Placement(0, 0, 0, null);
    }
}
