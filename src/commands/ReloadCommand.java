package commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import cDisposal.Main;

public class ReloadCommand implements CommandExecutor {
	Main pl = Main.getPlugin(Main.class);
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender.hasPermission("*") || sender.hasPermission("cDisposal.*") || sender.hasPermission("cDisposal.reload") || sender.isOp()) {
			pl.cfgm.reloadLanguage();
			sender.sendMessage(pl.cfgm.getLanguage().getString("reload"));
		} else {
			sender.sendMessage(pl.cfgm.getLanguage().getString("noPermissions"));
			return false;
		}
		return false;
	}

}
