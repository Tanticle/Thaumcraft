package art.arcane.thaumcraft.data.golemancy.seals;

import art.arcane.thaumcraft.api.ThaumcraftData;
import art.arcane.thaumcraft.data.golemancy.SealBehaviors;

public final class SealBehaviorInit {

    public static void init() {
        SealBehaviors.register(ThaumcraftData.SealTypes.GUARD, new SealGuardBehavior());
        SealBehaviors.register(ThaumcraftData.SealTypes.GUARD_ADVANCED, new SealGuardBehavior());
        SealBehaviors.register(ThaumcraftData.SealTypes.PICKUP, new SealPickupBehavior());
        SealBehaviors.register(ThaumcraftData.SealTypes.PICKUP_ADVANCED, new SealPickupBehavior());
        SealBehaviors.register(ThaumcraftData.SealTypes.PROVIDE, new SealProvideBehavior());
        SealBehaviors.register(ThaumcraftData.SealTypes.STOCK, new SealStockBehavior());
        SealBehaviors.register(ThaumcraftData.SealTypes.BREAKER, new SealBreakerBehavior());
        SealBehaviors.register(ThaumcraftData.SealTypes.BREAKER_ADVANCED, new SealBreakerBehavior());
        SealBehaviors.register(ThaumcraftData.SealTypes.HARVEST, new SealHarvestBehavior());
        SealBehaviors.register(ThaumcraftData.SealTypes.LUMBER, new SealLumberBehavior());
        SealBehaviors.register(ThaumcraftData.SealTypes.BUTCHER, new SealButcherBehavior());
        SealBehaviors.register(ThaumcraftData.SealTypes.FILL, new SealFillBehavior());
        SealBehaviors.register(ThaumcraftData.SealTypes.FILL_ADVANCED, new SealFillBehavior());
        SealBehaviors.register(ThaumcraftData.SealTypes.EMPTY, new SealEmptyBehavior());
        SealBehaviors.register(ThaumcraftData.SealTypes.EMPTY_ADVANCED, new SealEmptyBehavior());
        SealBehaviors.register(ThaumcraftData.SealTypes.USE, new SealUseBehavior());
    }
}
