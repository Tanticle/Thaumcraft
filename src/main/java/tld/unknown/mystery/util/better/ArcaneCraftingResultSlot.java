package tld.unknown.mystery.util.better;

import net.minecraft.core.NonNullList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import tld.unknown.mystery.Thaumcraft;
import tld.unknown.mystery.api.aspects.Aspect;
import tld.unknown.mystery.data.recipes.ArcaneCraftingRecipe;
import tld.unknown.mystery.registries.ConfigRecipeTypes;

public class ArcaneCraftingResultSlot extends Slot {

    private final SimpleContainer craftSlots;
    private final Player player;
    private int removeCount;

    public ArcaneCraftingResultSlot(Player pPlayer, SimpleContainer pCraftSlots, ResultContainer pContainer, int pSlot, int pXPosition, int pYPosition) {
        super(pContainer, pSlot, pXPosition, pYPosition);
        this.player = pPlayer;
        this.craftSlots = pCraftSlots;
    }

    public boolean mayPlace(ItemStack pStack) {
        return false;
    }

    public ItemStack remove(int pAmount) {
        if(this.hasItem()) {
            this.removeCount += Math.min(pAmount, this.getItem().getCount());
        }

        return super.remove(pAmount);
    }

    protected void onQuickCraft(ItemStack pStack, int pAmount) {
        this.removeCount += pAmount;
        this.checkTakeAchievements(pStack);
    }

    protected void onSwapCraft(int pNumItemsCrafted) {
        this.removeCount += pNumItemsCrafted;
    }

    //TODO Better crafting event
    protected void checkTakeAchievements(ItemStack pStack) {
        if(this.removeCount > 0) {
            pStack.onCraftedBy(this.player.level(), this.player, this.removeCount);
            //CRaftingEv.firePlayerCraftingEvent(this.player, pStack, this.craftSlots);
        }

        this.removeCount = 0;
    }

    public void onTake(Player pPlayer, ItemStack pStack) {
        this.checkTakeAchievements(pStack);
        ArcaneCraftingRecipe recipe = (ArcaneCraftingRecipe)((ResultContainer)container).getRecipeUsed().value();
        ServerLevel world = (ServerLevel)this.player.level();

        /*NonNullList<ItemStack> remains = world.recipeAccess().getRecipeFor(ConfigRecipeTypes.ARCANE_CRAFTING.type(), CraftingInput.of(3, 3, this.craftSlots.getItems()), pPlayer.level());

        for(int i = 0; i < remains.size(); ++i) {
            ItemStack currentSlot = this.craftSlots.getItem(i);
            ItemStack slotRemains = remains.get(i);
            if(!currentSlot.isEmpty()) {
                if(i < 6) {
                    Aspect.Primal primal = Aspect.Primal.values()[i];
                    if(recipe.getCrystals().containsKey(primal)) {
                        Thaumcraft.info("Removing %s crystals. [%d]", primal.getSerializedName(), recipe.getCrystals().get(primal));
                        this.craftSlots.removeItem(i, recipe.getCrystals().get(primal));
                    }
                } else {
                    this.craftSlots.removeItem(i, 1);
                }
                currentSlot = this.craftSlots.getItem(i);
            }

            if(!slotRemains.isEmpty()) {
                if(currentSlot.isEmpty()) {
                    this.craftSlots.setItem(i, slotRemains);
                } else if(ItemStack.isSameItemSameComponents(currentSlot, slotRemains)) {
                    slotRemains.grow(currentSlot.getCount());
                    this.craftSlots.setItem(i, slotRemains);
                } else if(!this.player.getInventory().add(slotRemains)) {
                    this.player.drop(slotRemains, false);
                }
            }
        }*/
    }
}
