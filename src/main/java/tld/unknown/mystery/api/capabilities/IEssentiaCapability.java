package tld.unknown.mystery.api.capabilities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;

public interface IEssentiaCapability {

    SideStatus getSideStatus(Direction dir);

    int getEssentia(Direction dir);
    ResourceLocation getEssentiaType(Direction dir);

    int getMinimumSuction(Direction dir);
    int getSuction(Direction dir);
    ResourceLocation getSuctionType(Direction dir);

    int drainAspect(ResourceLocation aspect, int amount, Direction dir);
    int fillAspect(ResourceLocation aspect, int amount, Direction dir);

    boolean canFit(ResourceLocation aspect, int amount, Direction dir);
    boolean contains(ResourceLocation aspect, int amount, Direction dir);

    boolean compliesToAspect(ResourceLocation aspect, Direction dir);

    @Getter
    @AllArgsConstructor
    enum SideStatus {
        INPUT(true, false),
        OUTPUT(false, true),
        INPUT_OUTPUT(true, true);

        private final boolean input, output;
    }
}
