package pt.josegamerpt.realskywars.classes;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.inventory.ItemStack;
import pt.josegamerpt.realskywars.managers.KitManager;
import pt.josegamerpt.realskywars.utils.Itens;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

public class DisplayItem {

	public int id;
	public ItemStack i;
	public Double price;
	public Boolean bought = false;
	public String name;
	public String permission;
    public Boolean interactable = true;
	public Enum.Categories it;
	public HashMap<String, Object> info = new HashMap<String, Object>();

	public DisplayItem(int id, Material m, String n, Double per, Boolean b, String perm, Enum.Categories t) {
		this.id = id;
		this.price = per;
		this.bought = b;
		this.name = n;
		this.permission = perm;
		this.interactable = true;
		this.it = t;
		makeItemStack(m);
	}

	public void addInfo(String a, Object b) {
		info.put(a, b);
	}

	private void makeItemStack(Material m) {
		switch (it) {
			case KITS:
				if (!bought) {
					assert KitManager.getKit(name) != null;
					i = Itens.createItemLore(m, 1, name, KitManager.getKit(name).getDescription(true));
				} else {
					i = Itens.createItemLoreEnchanted(m, 1, name, KitManager.getKit(name).getDescription(false));
				}
                break;
			case CAGEBLOCK:
				if (bought) {
					i = Itens.createItemLoreEnchanted(m, 1, name, Collections.singletonList("&aYou already bought this block."));
				} else {
					i = Itens.createItemLore(m, 1, name, Arrays.asList("&fPrice: &b" + price, "&fClick to Buy."));
				}
				break;
            default:
				if (bought) {
					i = Itens.createItemLoreEnchanted(m, 1, name, Collections.singletonList("&aYou already bought this."));
				} else {
					i = Itens.createItemLore(m, 1, name, Arrays.asList("&fPrice: &b" + price, "&fClick to Buy."));
				}
				break;
		}
	}

	public DisplayItem() {
		this.interactable = false;
		this.i = Itens.createItemLore(Material.BUCKET, 1, "&aEmpty", Collections.singletonList("&fNothing found in this category."));
	}

	public Object getInfo(String particle) {
		if (info.containsKey(particle)) {
			return info.get(particle);
		}
		return null;
	}
}
