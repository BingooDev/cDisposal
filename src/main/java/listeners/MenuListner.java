package listeners;

import cDisposal.Main;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class MenuListner implements Listener {
	Main pl = Main.getPlugin(Main.class);
	
	@EventHandler
	public void onClick(InventoryClickEvent e) {
		if(e.getView().getTitle().equals(pl.cfgm.getLanguage().getString("GUIName"))) {
			Player p = (Player) e.getWhoClicked();
			e.setCancelled(true);
			
			ItemStack clicked = e.getCurrentItem();
			if (e.getSlot() != -999) {
				if (clicked.getType().equals(Material.RED_CONCRETE)) {
					if (clicked.getItemMeta().getDisplayName().equals(pl.cfgm.getLanguage().getString("nameDeny"))) {
						p.closeInventory();
						p.sendMessage(pl.cfgm.getLanguage().getString("didNotOpenTrashCan"));
					}
					return;
				}
				if (clicked.getType().equals(Material.GREEN_CONCRETE)) {
					if (clicked.getItemMeta().getDisplayName().equals(pl.cfgm.getLanguage().getString("nameAccept"))) {
						//Check if extra warning is turned off, if it is that the disposal inventory will show up.
						if(pl.openAfAuto.contains(p.getUniqueId().toString())) {
							Inventory avfall = Bukkit.getServer().createInventory(null, 36, pl.cfgm.getLanguage().getString("disposalInventoryName"));
							p.openInventory(avfall);
							return;
						}
						//If the extra warning is turned on, the warning will be sent in chat.
						if(pl.cfgm.languagecfg.getBoolean("lineBreakes")) {
							p.sendMessage(" ");
						}
						p.sendMessage(pl.cfgm.getLanguage().getString("trashCanWarningMessage"));
						String yesMessage = pl.cfgm.getLanguage().getString("warningYesMessage");
						String noMessage = pl.cfgm.getLanguage().getString("warningNoMessage");
						String yesMessageHover = pl.cfgm.getLanguage().getString("yesButtonHover");
						String noMessageHover = pl.cfgm.getLanguage().getString("noButtonHover");

						TextComponent yesMessageComponent = new TextComponent(yesMessage);
						yesMessageComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/avfall true"));
						yesMessageComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(yesMessageHover)));

						TextComponent noMessageComponent = new TextComponent(noMessage);
						noMessageComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/avfall false"));
						noMessageComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(noMessageHover)));

						BaseComponent[] component = new ComponentBuilder()
								.append(yesMessageComponent)
								.append("\n")
								.append(noMessageComponent).create();

						p.spigot().sendMessage(component);
						if(pl.cfgm.languagecfg.getBoolean("lineBreakes")) {
							p.sendMessage(" ");
						}
						p.closeInventory();
					}
					return;
				}
			}
		}
	}
}
