package pt.josegamerpt.realskywars.player;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.block.Chest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import pt.josegamerpt.realskywars.classes.Enum;
import pt.josegamerpt.realskywars.classes.Enum.GameState;
import pt.josegamerpt.realskywars.classes.Enum.Selection;
import pt.josegamerpt.realskywars.configuration.Chests;
import pt.josegamerpt.realskywars.configuration.Items;
import pt.josegamerpt.realskywars.gui.ChestTierVote;
import pt.josegamerpt.realskywars.gui.GUIManager;
import pt.josegamerpt.realskywars.gui.MapsViewer;
import pt.josegamerpt.realskywars.gui.ProfileContent;
import pt.josegamerpt.realskywars.managers.ChestManager;
import pt.josegamerpt.realskywars.managers.PlayerManager;
import pt.josegamerpt.realskywars.utils.Itens;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

public class PlayerInteractions implements Listener {

    @EventHandler
    public void onPlayerInteractEvent(PlayerInteractEvent e) {
        items(e);
    }

    public void items(PlayerInteractEvent e) {
        if (e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.RIGHT_CLICK_AIR) {
            if (e.getPlayer().getInventory().getItemInMainHand().hasItemMeta()) {
                GamePlayer gp = PlayerManager.getPlayer(e.getPlayer());
                assert gp != null;
                switch (e.getPlayer().getInventory().getItemInMainHand()
                        .getType()) {
                    case AIR:
                        break;
                    case PLAYER_HEAD:
                        GUIManager.openPlayerMenu(gp);
                        e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.ITEM_BOOK_PAGE_TURN, 50, 50);
                        e.setCancelled(true);
                        break;
                }
                if (e.getPlayer().getInventory().getItemInMainHand()
                        .equals(Items.SPECTATE)) {
                    GUIManager.openSpectate(gp);
                    e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.ITEM_BOOK_PAGE_TURN, 50, 50);
                    e.setCancelled(true);
                }
                if (e.getPlayer().getInventory().getItemInMainHand()
                        .equals(Items.KITS)) {
                    ProfileContent ds = new ProfileContent(e.getPlayer(), Enum.Categories.KITS, "&9Kits");
                    ds.openInventory(gp);
                    e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.ITEM_BOOK_PAGE_TURN, 50, 50);
                    e.setCancelled(true);
                }
                if (e.getPlayer().getInventory().getItemInMainHand()
                        .equals(Items.MAPS)) {
                    MapsViewer v = new MapsViewer(gp, PlayerManager.getSelection(gp, Selection.MAPVIEWER), "Maps");
                    v.openInventory(gp);
                    e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.ITEM_BOOK_PAGE_TURN, 50, 50);
                    e.setCancelled(true);
                }
                if (e.getPlayer().getInventory().getItemInMainHand()
                        .equals(Items.LEAVE)) {
                    gp.room.removePlayer(gp);
                    e.setCancelled(true);
                }
                if (e.getPlayer().getInventory().getItemInMainHand()
                        .equals(Items.CHESTS)) {
                    ChestTierVote v = new ChestTierVote(gp.p.getUniqueId());
                    v.openInventory(gp);
                    e.setCancelled(true);
                }
                if (e.getPlayer().getInventory().getItemInMainHand()
                        .equals(Items.SHOP)) {
                    GUIManager.openShopMenu(gp);
                    e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.ITEM_BOOK_PAGE_TURN, 50, 50);
                    e.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void fillChest(PlayerInteractEvent event) {
        if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            if (Objects.requireNonNull(event.getClickedBlock()).getState() instanceof Chest) {
                GamePlayer gp = PlayerManager.getPlayer(event.getPlayer());
                Location chestloc = event.getClickedBlock().getLocation();
                Chest bau = (Chest) event.getClickedBlock().getState();

                assert gp != null;
                if (gp.room != null) {
                    if (gp.room.getState().equals(GameState.PLAYING)) {

                        if (!gp.room.getOpenedChests().contains(chestloc)) {

                            dochest(bau, gp);

                            gp.room.getOpenedChests().add(chestloc);
                        }
                    }
                }
            }
        }
    }

    private void dochest(Chest bau, GamePlayer gp) {
        bau.getInventory().clear();

        Random random = new Random();

        ArrayList<ItemStack> l = ChestManager.putInChest(gp.room.getTierType());

        if (l.size() > Chests.file().getInt("Max-Items-On-Chest")) {
            Itens.shrink(l, Chests.file().getInt("Max-Items-On-Chest"));
        }

        boolean[] chosen = new boolean[l.size()];

        for (int i = 0; i < l.size(); i++) {
            int slot;

            do {
                slot = random.nextInt(l.size());
            } while (chosen[slot]);

            chosen[slot] = true;
            bau.getInventory().setItem(random.nextInt(l.size()), l.get(i));
        }
    }
}
