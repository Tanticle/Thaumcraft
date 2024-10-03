package tld.unknown.mystery.api.capabilities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceKey;
import tld.unknown.mystery.api.aspects.Aspect;

public interface IEssentiaCapability {

    SideStatus getSideStatus(Direction dir);

    int getEssentia(Direction dir);
    ResourceKey<Aspect> getEssentiaType(Direction dir);

    int getMinimumSuction(Direction dir);
    int getSuction(Direction dir);
    ResourceKey<Aspect> getSuctionType(Direction dir);

    int drainAspect(ResourceKey<Aspect> aspect, int amount, Direction dir);
    int fillAspect(ResourceKey<Aspect> aspect, int amount, Direction dir);

    boolean canFit(ResourceKey<Aspect> aspect, int amount, Direction dir);
    boolean contains(ResourceKey<Aspect> aspect, int amount, Direction dir);

    boolean compliesToAspect(ResourceKey<Aspect> aspect, Direction dir);

    @Getter
    @AllArgsConstructor
    enum SideStatus {
        INPUT(true, false),
        OUTPUT(false, true),
        INPUT_OUTPUT(true, true);

        private final boolean input, output;
    }
}
