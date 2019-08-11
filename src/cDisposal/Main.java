package cDisposal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.inventory.DoubleChestInventory;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class Main extends JavaPlugin {
	public Map<Location, String> disposalSignsOwner = new HashMap<Location, String>();
	public List<Location> disposalSigns = new ArrayList<>();

	public void onEnable() {
		Bukkit.getServer().getPluginManager().registerEvents(new listeners.listener(), (Plugin) this);
		getSavedDisposalSigns();
		emptyChests();
	}

	public void onDisable() {
		List<String> s = new ArrayList<String>();

		for (int i = 0; i < disposalSigns.size(); i++) {
			if(!s.contains(disposalSigns.get(i).toString())) {
				s.add(disposalSigns.get(i).toString());
			}
		}
		getConfig().set("disposalSigns", s);

		List<String> s2 = new ArrayList<String>();
		for (Location loc : disposalSignsOwner.keySet()) {
			s2.add(loc.toString() + ":" + disposalSignsOwner.get(loc));
		}
		getConfig().set("disposalSignsOwner", s2);
		saveConfig();
	}

	public void getSavedDisposalSigns() {
		List<String> s = this.getConfig().getStringList("disposalSigns");

		for (String str : s) {
			String[] arg = str.split(",");
			arg[0] = arg[0].replace("Location", "");
			arg[0] = arg[0].replace("{world=CraftWorld{name=", "");
			arg[0] = arg[0].replace("}", "");
			arg[1] = arg[1].replace("x=", "");
			arg[2] = arg[2].replace("y=", "");
			arg[3] = arg[3].replace("z=", "");
			arg[4] = arg[4].replace("pitch=", "");
			arg[5] = arg[5].replace("yaw=", "");
			arg[5] = arg[5].replace("}", "");

			Location loc = new Location(Bukkit.getWorld(arg[0]), Double.parseDouble(arg[1]), Double.parseDouble(arg[2]),
					Double.parseDouble(arg[3]), Float.parseFloat(arg[4]), Float.parseFloat(arg[5]));
			disposalSigns.add(loc);

		}

		List<String> s2 = this.getConfig().getStringList("disposalSignsOwner");

		for (String str : s2) {
			String[] words = str.split(":");

			String[] arg = words[0].split(",");
			arg[0] = arg[0].replace("Location", "");
			arg[0] = arg[0].replace("{world=CraftWorld{name=", "");
			arg[0] = arg[0].replace("}", "");
			arg[1] = arg[1].replace("x=", "");
			arg[2] = arg[2].replace("y=", "");
			arg[3] = arg[3].replace("z=", "");
			arg[4] = arg[4].replace("pitch=", "");
			arg[5] = arg[5].replace("yaw=", "");
			arg[5] = arg[5].replace("}", "");

			Location loc = new Location(Bukkit.getWorld(arg[0]), Double.parseDouble(arg[1]), Double.parseDouble(arg[2]),
					Double.parseDouble(arg[3]), Float.parseFloat(arg[4]), Float.parseFloat(arg[5]));
			disposalSignsOwner.put(loc, words[1]);

		}
	}

	public void emptyChests() {
		new BukkitRunnable() {

			@Override
			public void run() {
				for (int i = 0; i < disposalSigns.size(); i++) {
					if (disposalSigns.get(i).getBlock().getType().equals(Material.WALL_SIGN)) {
						org.bukkit.material.Sign s = (org.bukkit.material.Sign) disposalSigns.get(i).getBlock()
								.getState().getData();
						Block attachedBlock = disposalSigns.get(i).getBlock().getRelative(s.getAttachedFace());
						if (attachedBlock.getType().equals(Material.CHEST)
								|| attachedBlock.getType().equals(Material.TRAPPED_CHEST)) {
							Chest c = (Chest) attachedBlock.getState();
							if (c.getInventory() instanceof DoubleChestInventory) {
								DoubleChest doubleChest = (DoubleChest) c.getInventory().getHolder();
								doubleChest.getInventory().clear();
							} else {
								c.getInventory().clear();
							}
						}
					}
				}
			}

		}.runTaskTimerAsynchronously(this, 0, 1200);
	}
}
