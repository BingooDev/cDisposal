package commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import cDisposal.Main;

public class ToggleAvfallWarning {
	public static Main pl = Main.getPlugin(Main.class);
	
	public static void main(CommandSender sender) {
		if(!(sender instanceof Player)) {
			sender.sendMessage(pl.cfgm.getLanguage().getString("onlyPlayer"));
			return;
		}
		Player p = (Player) sender;
		
		if(pl.openAfAuto.contains(p.getUniqueId().toString())) {
			p.sendMessage(pl.cfgm.getLanguage().getString("warningMessageOn"));
			pl.openAfAuto.remove(p.getUniqueId().toString());
		} else {
			p.sendMessage(pl.cfgm.getLanguage().getString("warningMessageOff"));
			if (!sender.hasPermission("*") &! sender.hasPermission("cDisposal.*") &! sender.hasPermission("cDisposal.soptunna.toggle")
					&! sender.isOp()) {
				sender.sendMessage(pl.cfgm.getLanguage().getString("noPermissions"));
				return;
			} 
			pl.openAfAuto.add(p.getUniqueId().toString());
		}
		return;
	}

}
