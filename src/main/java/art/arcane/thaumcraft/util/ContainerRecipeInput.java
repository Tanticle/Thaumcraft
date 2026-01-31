package art.arcane.thaumcraft.util;

import lombok.AllArgsConstructor;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;

@AllArgsConstructor
public class ContainerRecipeInput implements RecipeInput {

    private final Container container;

    @Override
    public ItemStack getItem(int index) {
        return container.getItem(index);
    }

    @Override
    public int size() {
        return container.getContainerSize();
    }

    @Override
    public boolean isEmpty() {
        return container.isEmpty();
    }
}
