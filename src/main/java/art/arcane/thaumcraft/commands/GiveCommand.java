package art.arcane.thaumcraft.commands;

import art.arcane.thaumcraft.api.components.FortressFaceplateComponent;
import art.arcane.thaumcraft.registries.ConfigItemComponents;
import art.arcane.thaumcraft.registries.ConfigItems;
import art.arcane.thaumcraft.registries.ConfigSounds;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class GiveCommand {

	public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
		dispatcher.register(Commands.literal("thaumcraft")
				.requires(source -> source.hasPermission(2) && source.isPlayer())
				.then(Commands.literal("give")
						.then(Commands.argument("fortress", IntegerArgumentType.integer(1))
								.executes(ctx -> giveFortressIterations(ctx.getSource().getPlayer())))
				)
		);
	}

	private static int giveFortressIterations(Player player) {
		for (int i = 1; i < FortressFaceplateComponent.Type.values().length; i++) {
			ItemStack goggles = new ItemStack(ConfigItems.ARMOR_FORTRESS.head().asItem());
			goggles.set(ConfigItemComponents.ARMOR_FORTRESS_FACEPLATE.value(), new FortressFaceplateComponent(FortressFaceplateComponent.Type.values()[i], false));
			player.addItem(goggles.copy());
			goggles.set(ConfigItemComponents.ARMOR_FORTRESS_FACEPLATE.value(), new FortressFaceplateComponent(FortressFaceplateComponent.Type.values()[i], true));
			player.addItem(goggles.copy());
		}
		player.playSound(ConfigSounds.POOF.value());
		return 0;
	}
}
