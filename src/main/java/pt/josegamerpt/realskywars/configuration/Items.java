package pt.josegamerpt.realskywars.configuration;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import pt.josegamerpt.realskywars.utils.Itens;

public class Items {
	
	public static ItemStack CAGESET = Itens.createItem(Material.BEACON, 1, "&bLocation Setter");
	public static ItemStack MAPS = Itens.createItem(Material.NETHER_STAR, 1, "&bMaps &7(Right Click)");
	public static ItemStack SHOP = Itens.createItem(Material.EMERALD, 1, "&2Shop &7(Right Click)");
	public static ItemStack KITS = Itens.createItem(Material.CHEST, 1, "&aKits &7(Right Click)");
	public static ItemStack LEAVE = Itens.createItem(Material.MINECART, 1, "&cLeave &7(Right Click)");
	public static ItemStack CHESTS = Itens.createItem(Material.ENDER_CHEST, 1, "&9Chests &7(Right Click)");
	public static ItemStack SPECTATE = Itens.createItem(Material.COMPASS, 1, "&bSpectate &7(Right Click)");

}
