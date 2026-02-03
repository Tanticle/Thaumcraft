package art.arcane.thaumcraft.commands;

import art.arcane.thaumcraft.items.VisChargeItem;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public final class VisChargeCommand {

	public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
		dispatcher.register(Commands.literal("thaumcraft")
				.requires(source -> source.hasPermission(2) && source.isPlayer())
				.then(Commands.literal("charge")
						.executes(ctx -> chargeAll(ctx.getSource().getPlayer(), -1))
						.then(Commands.argument("amount", IntegerArgumentType.integer(1))
								.executes(ctx -> chargeAll(ctx.getSource().getPlayer(), IntegerArgumentType.getInteger(ctx, "amount"))))
				)
		);
	}

	private static int chargeAll(Player player, int amount) {
		player.getInventory().items.stream().filter(itemStack -> itemStack.getItem() instanceof VisChargeItem).forEach(i -> chargeItem(i, amount));
		player.getInventory().armor.stream().filter(itemStack -> itemStack.getItem() instanceof VisChargeItem).forEach(i -> chargeItem(i, amount));
		player.getInventory().offhand.stream().filter(itemStack -> itemStack.getItem() instanceof VisChargeItem).forEach(i -> chargeItem(i, amount));
		return 0;
	}

	private static void chargeItem(ItemStack itemStack, int amount) {
		if(itemStack.getItem() instanceof VisChargeItem vis) {
			vis.setCharge(itemStack, amount == -1 ? 9999 : amount);
		}
	}
}
