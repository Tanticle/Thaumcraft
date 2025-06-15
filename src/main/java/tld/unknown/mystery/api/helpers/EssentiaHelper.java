package tld.unknown.mystery.api.helpers;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import tld.unknown.mystery.api.aspects.Aspect;
import tld.unknown.mystery.api.capabilities.IEssentiaCapability;
import tld.unknown.mystery.registries.ConfigCapabilities;

import java.util.ArrayList;
import java.util.List;

public final class EssentiaHelper {

    public static boolean drainEssentia(Level level, BlockPos center, int range, ResourceKey<Aspect> aspect, int amount) {
        List<BlockEntity> sources = new ArrayList<>();
        for(int x = -range; x <= range; x++) {
            for(int z = -range; z <= range; z++) {
                for(int y = -range; y <= range; y++) {
                    BlockPos pos = center.offset(x,y,z);
                    for(Direction direction : Direction.values()) {

                    }
                }
            }
        }
        return false;
    }
}
