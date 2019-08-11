package listeners;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import cDisposal.Main;
import net.minecraft.server.v1_14_R1.IChatBaseComponent;
import net.minecraft.server.v1_14_R1.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_14_R1.PacketPlayOutChat;

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
						String message = String.format("[\"\",{\"text\":\"» \",\"color\":\"dark_gray\",\"bold\":true},{\"text\":\"%s\",\"color\":\"dark_green\",\"bold\":true,\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/avfall true\"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":{\"text\":\"\",\"extra\":[{\"text\":\"Klicka här för att godkänna\",\"color\":\"dark_green\"}]}}},{\"text\":\" \\n\",\"color\":\"none\",\"bold\":false},{\"text\":\"» \",\"color\":\"dark_gray\",\"bold\":true},{\"text\":\"%s\",\"color\":\"red\",\"bold\":true,\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/avfall false\"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":{\"text\":\"\",\"extra\":[{\"text\":\"Klicka här för att avbryta\",\"color\":\"red\"}]}}}]", yesMessage, noMessage);
						IChatBaseComponent comp = ChatSerializer.a(message);

						PacketPlayOutChat packet = new PacketPlayOutChat(comp);
						((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
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
