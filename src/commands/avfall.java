package commands;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import cDisposal.Main;

public class avfall implements CommandExecutor {
	Main pl = Main.getPlugin(Main.class);
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(pl.cfgm.getLanguage().getString("onlyPlayer"));
			return false;
		}
		Player p = (Player) sender;
		if (!sender.hasPermission("*") &! sender.hasPermission("cDisposal.*") &! sender.hasPermission("cDisposal.soptunna")
				&! sender.isOp()) {
			sender.sendMessage(pl.cfgm.getLanguage().getString("noPermissions"));
			return false;
		} 
		if (args.length == 0) {
			p.openInventory(createAfallsGui());
		} else if (args.length == 1 && args[0].equalsIgnoreCase("true")) {
			Inventory avfall = Bukkit.getServer().createInventory(null, 36, pl.cfgm.getLanguage().getString("disposalInventoryName"));
			p.openInventory(avfall);
		} else if (args.length == 1 && args[0].equalsIgnoreCase("false")) {
			p.sendMessage(pl.cfgm.getLanguage().getString("didNotOpenTrashCan"));
		} else if (args.length == 1 && args[0].equalsIgnoreCase("toggle")) {
			ToggleAvfallWarning.main(sender);
		}

		return false;
	}
	
	private Inventory createAfallsGui() {
		Inventory avfallsGUI = Bukkit.getServer().createInventory(null, 27, pl.cfgm.getLanguage().getString("GUIName"));
		ItemStack empty = new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1);
		for(int i = 0; i < 27; i++) {
			avfallsGUI.setItem(i, empty);
		}
		
		ItemStack soptunna = new ItemStack(Material.CAULDRON, 1);
		ItemMeta soptunnaMeta = soptunna.getItemMeta();
		soptunnaMeta.setDisplayName(pl.cfgm.getLanguage().getString("cauldronName"));
		soptunna.setItemMeta(soptunnaMeta);
		avfallsGUI.setItem(4, soptunna);
		
		
		ItemStack yes = new ItemStack(Material.GREEN_CONCRETE, 1);
		ItemMeta yesMeta = yes.getItemMeta();
		ArrayList<String> yesLore = new ArrayList<String>();
		yesMeta.setDisplayName(pl.cfgm.getLanguage().getString("nameAccept"));
		
		String yesLoreMessage = pl.cfgm.getLanguage().getString("loreAccept");
		String[] yesLoreSpited = yesLoreMessage.split("\n");
		for(String yesLoremsg : yesLoreSpited) {
			yesLore.add(yesLoremsg);
		}
		yesMeta.setLore(yesLore);
		yes.setItemMeta(yesMeta);
		avfallsGUI.setItem(10, yes);
		
		ItemStack no = new ItemStack(Material.RED_CONCRETE, 1);
		ItemMeta noMeta = no.getItemMeta();
		noMeta.setDisplayName(pl.cfgm.getLanguage().getString("nameDeny"));
		no.setItemMeta(noMeta);
		avfallsGUI.setItem(16, no);
		
		return avfallsGUI;
	}

}
