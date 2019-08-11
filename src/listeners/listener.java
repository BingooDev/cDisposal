package listeners;

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

	@EventHandler
	public void signPlace(SignChangeEvent e) {
		if (e.getBlock().getType() == Material.SPRUCE_WALL_SIGN || e.getBlock().getType() == Material.ACACIA_WALL_SIGN
				|| e.getBlock().getType() == Material.BIRCH_WALL_SIGN
				|| e.getBlock().getType() == Material.DARK_OAK_WALL_SIGN
				|| e.getBlock().getType() == Material.JUNGLE_WALL_SIGN
				|| e.getBlock().getType() == Material.OAK_WALL_SIGN) {
			if (!e.getLine(0).toLowerCase().equals("[cdisposal]") &!e.getLine(0).toLowerCase().equals("[soptunna]")) {
				return;
			}
			BlockData data = e.getBlock().getBlockData();
			if (!(data instanceof Directional)) {
				return;
			}
			Directional directional = (Directional) data;
			Block attachedBlock = e.getBlock().getRelative(directional.getFacing().getOppositeFace());
			if (!attachedBlock.getLocation().getBlock().getType().equals(Material.CHEST)
					& !attachedBlock.getLocation().getBlock().getType().equals(Material.TRAPPED_CHEST)) {
				return;
			}
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
			if (b.getType() == Material.ACACIA_WALL_SIGN || b.getType() == Material.BIRCH_WALL_SIGN
					|| b.getType() == Material.DARK_OAK_WALL_SIGN || b.getType() == Material.JUNGLE_WALL_SIGN
					|| b.getType() == Material.OAK_WALL_SIGN || b.getType() == Material.SPRUCE_WALL_SIGN) {
				BlockData data = b.getBlockData();
				if (!(data instanceof Directional)) {
					return;
				}
				Directional directional = (Directional) data;
				Block attachedBlock = b.getRelative(directional.getFacing().getOppositeFace());
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

		//Check if the block that has been break is not a sign. If it's not a sign check if a sign with  [soptunna] or [cdisposal] is placed on the block.
		if (!e.getBlock().getType().equals(Material.ACACIA_WALL_SIGN) & !e.getBlock().getType().equals(Material.BIRCH_WALL_SIGN)
				& !e.getBlock().getType().equals(Material.DARK_OAK_WALL_SIGN)
				& !e.getBlock().getType().equals(Material.JUNGLE_WALL_SIGN)
				& !e.getBlock().getType().equals(Material.OAK_WALL_SIGN)
				& !e.getBlock().getType().equals(Material.SPRUCE_WALL_SIGN)) {
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