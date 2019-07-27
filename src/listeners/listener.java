package listeners;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;

import cDisposal.Main;

public class listener implements Listener {

	Main pl = Main.getPlugin(Main.class);

	// @EventHandler
	// public void diamonPlace(BlockPlaceEvent e) {
	// if (e.getBlock().getType().equals(Material.DIAMOND_BLOCK)) {
	// Bukkit.getServer().broadcastMessage(pl.disposalSignsOwner.toString());
	// }
	// }

	@EventHandler
	public void signPlace(SignChangeEvent e) {
		if (e.getBlock().getType() == Material.WALL_SIGN) {
			org.bukkit.material.Sign s = (org.bukkit.material.Sign) e.getBlock().getState().getData();
			if (!e.getLine(0).toLowerCase().equals("[cdisposal]") & !e.getLine(0).toLowerCase().equals("[soptunna]")) {
				return;
			}
			Block attachedBlock = e.getBlock().getRelative(s.getAttachedFace());
			if (!attachedBlock.getLocation().getBlock().getType().equals(Material.CHEST)
					& !attachedBlock.getLocation().getBlock().getType().equals(Material.TRAPPED_CHEST)) {
				return;
			}
			// Check if chest is empty
			// Chest c = (Chest) attachedBlock.getState();
			// if (c.getInventory() instanceof DoubleChestInventory) {
			// DoubleChest doubleChest = (DoubleChest) c.getInventory().getHolder();
			// for (ItemStack item : doubleChest.getInventory().getContents()) {
			// if (item != null) {
			// e.getPlayer().sendMessage("§4The chest needs to be empty!");
			// return;
			// }
			// }
			// } else {
			// for (ItemStack item : c.getInventory().getContents()) {
			// if (item != null) {
			// e.getPlayer().sendMessage("§4The chest needs to be empty!");
			// return;
			// }
			// }
			// }
			if(e.getLine(0).toLowerCase().equals("[cdisposal]")) {
				e.setLine(0, "§8[§2cDisposal§8]");
				e.getPlayer().sendMessage("§8[§2cDisposal§8] §aDu har skapat en §2cDisposal§a-skylt med framgång!");
			} else if(e.getLine(0).toLowerCase().equals("[soptunna]")) {
				e.setLine(0, "§8[§2Soptunna§8]");
				e.getPlayer().sendMessage("§8[§2Soptunna§8] §aDu har skapat en §2Soptunna§a-skylt med framgång!");
			}
			pl.disposalSigns.add(e.getBlock().getLocation());
			pl.disposalSignsOwner.put(e.getBlock().getLocation(), e.getPlayer().getUniqueId().toString());
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onSignBreak(BlockBreakEvent e) {
		BlockFace[] bf = new BlockFace[] { BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST, BlockFace.NORTH };
		Block b;
		for (int i = 3; i >= 0; i--) {
			b = e.getBlock().getRelative(bf[(i)]);
			if (b.getType() == Material.WALL_SIGN) {
				org.bukkit.material.Sign signStateData = (org.bukkit.material.Sign) b.getState().getData();
				Block attachedBlock = b.getRelative(signStateData.getAttachedFace());
				// Check if sign is facing against same block as e.getblock.
				if (attachedBlock.getLocation().equals(e.getBlock().getLocation())) {
					org.bukkit.block.Sign signState = (org.bukkit.block.Sign) b.getState();
					if (signState.getLine(0).equals("§8[§2cDisposal§8]") || signState.getLine(0).equals("§8[§2Soptunna§8]")) {
						if (e.isCancelled()) {
							return;
						}
						pl.disposalSigns.remove(signState.getBlock().getLocation());
						pl.disposalSignsOwner.remove(signState.getBlock().getLocation());

					}
				}
			}
		}

		if (!e.getBlock().getType().equals(Material.WALL_SIGN)) {
			return;
		}
		org.bukkit.block.Sign s = (org.bukkit.block.Sign) e.getBlock().getState();
		if (!s.getLine(0).equals("§8[§2cDisposal§8]") &!s.getLine(0).equals("§8[§2Soptunna§8]")) {
			return;
		}
		if (e.isCancelled()) {
			return;
		}
		pl.disposalSigns.remove(e.getBlock().getLocation());
		pl.disposalSignsOwner.remove(e.getBlock().getLocation());
	}
}