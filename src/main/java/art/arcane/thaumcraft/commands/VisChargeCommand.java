package art.arcane.thaumcraft.commands;

import art.arcane.thaumcraft.items.VisChargeItem;
import art.arcane.thaumcraft.registries.ConfigItemComponents;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.world.item.ItemStack;

public final class VisChargeCommand {

	public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
		dispatcher.register(Commands.literal("thaumcraft")
				.requires(source -> source.hasPermission(2) && source.isPlayer())
				.then(Commands.literal("charge")
						.executes(commandContext -> {
							commandContext.getSource().getPlayer().getInventory().items.stream().filter(itemStack -> itemStack.getItem() instanceof VisChargeItem).forEach(VisChargeCommand::chargeItem);
							return 0;
						})
				)
		);
	}

	private static void chargeItem(ItemStack itemStack) {
		if(itemStack.getItem() instanceof VisChargeItem vis) {
			int maxChange = itemStack.get(ConfigItemComponents.VIS_CHARGE_MAX.value());
			vis.addCharge(itemStack, 9999);
		}
	}
}
