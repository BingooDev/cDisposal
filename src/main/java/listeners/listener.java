package listeners;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
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

	// Using highest to overwrite CMI check. CMI will otherwise stop coloring of sign if player doesn't have permission.
	@EventHandler(priority = EventPriority.HIGHEST)
	public void signPlace(SignChangeEvent e) {
		if (e.getBlock().getType().toString().contains("WALL_SIGN")) {
			if (!ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', e.getLine(0))).equalsIgnoreCase("[cdisposal]")
					&! ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', e.getLine(0))).equalsIgnoreCase("[soptunna]")) {
				return;
			}
			BlockData data = e.getBlock().getBlockData();
			if (!(data instanceof Directional)) {
				return;
			}
			Directional directional = (Directional) data;
			Block attachedBlock = e.getBlock().getRelative(directional.getFacing().getOppositeFace());
			if (attachedBlock.getType() != Material.CHEST
					& attachedBlock.getType() != Material.TRAPPED_CHEST
					& attachedBlock.getType() != Material.BARREL) {
				return;
			}
			if(e.getLine(0).equalsIgnoreCase("[cdisposal]")) {
				e.setLine(0, "§8[§2cDisposal§8]");
				e.getPlayer().sendMessage("§8[§2cDisposal§8] §aDu har skapat en §2cDisposal§a-skylt med framgång!");
			} else if(e.getLine(0).equalsIgnoreCase("[soptunna]")) {
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
			if (b.getType().toString().contains("WALL_SIGN")) {
				BlockData data = b.getBlockData();
				if (!(data instanceof Directional)) {
					return;
				}
				Directional directional = (Directional) data;
				Block attachedBlock = b.getRelative(directional.getFacing().getOppositeFace());
				// Check if sign is facing against same block as e.getblock.
				if (attachedBlock.getLocation().equals(e.getBlock().getLocation())) {
					org.bukkit.block.Sign signState = (org.bukkit.block.Sign) b.getState();
					if (signState.getLine(0).equals("[cDisposal]") || signState.getLine(0).equals("[Soptunna]")) {
						if (e.isCancelled()) {
							return;
						}
						pl.disposalSigns.remove(signState.getBlock().getLocation());
						pl.disposalSignsOwner.remove(signState.getBlock().getLocation());

					}
				}
			}
		}

		//Check if the block that has been break is not a sign. If it's not a sign check if a sign with  [soptunna] or [cdisposal] is placed on the block.
		if (!e.getBlock().getType().toString().contains("WALL_SIGN")) {
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