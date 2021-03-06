package pt.josegamerpt.realskywars.gui;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import pt.josegamerpt.realskywars.classes.Enum;
import pt.josegamerpt.realskywars.classes.Enum.Selection;
import pt.josegamerpt.realskywars.classes.Enum.Selections;
import pt.josegamerpt.realskywars.classes.GameRoom;
import pt.josegamerpt.realskywars.classes.MapItem;
import pt.josegamerpt.realskywars.managers.GameManager;
import pt.josegamerpt.realskywars.managers.LanguageManager;
import pt.josegamerpt.realskywars.managers.PlayerManager;
import pt.josegamerpt.realskywars.player.GamePlayer;
import pt.josegamerpt.realskywars.utils.Itens;
import pt.josegamerpt.realskywars.utils.Pagination;
import pt.josegamerpt.realskywars.utils.Text;

import java.util.*;

public class MapsViewer {

    private static final Map<UUID, MapsViewer> inventories = new HashMap<>();
    static ItemStack placeholder = Itens.createItem(Material.BLACK_STAINED_GLASS_PANE, 1, "");
    static ItemStack next = Itens.createItemLore(Material.GREEN_STAINED_GLASS, 1, "&aNext",
            Collections.singletonList("&fClick here to go to the next page."));
    static ItemStack back = Itens.createItemLore(Material.YELLOW_STAINED_GLASS, 1, "&6Back",
            Collections.singletonList("&fClick here to go back to the next page."));
    private final Inventory inv;
    private final UUID uuid;
    private final GamePlayer gp;
    private final HashMap<Integer, GameRoom> display = new HashMap<>();
    private final Selections selMap;
    int pageNumber = 0;
    Pagination<GameRoom> p;

    public MapsViewer(GamePlayer as, Selections t, String invName) {
        this.uuid = as.p.getUniqueId();
        inv = Bukkit.getServer().createInventory(null, 54, Text.addColor(invName) + ": " + LanguageManager.getString(as, select(t), false));

        gp = as;
        this.selMap = t;
        List<GameRoom> items = GameManager.getRooms(t);

        p = new Pagination<>(28, items);
        fillChest(p.getPage(pageNumber), as);

        this.register();
    }

    public static Listener getListener() {
        return new Listener() {
            @EventHandler
            public void onClick(InventoryClickEvent e) {
                HumanEntity clicker = e.getWhoClicked();
                if (clicker instanceof Player) {
                    if (e.getCurrentItem() == null) {
                        return;
                    }
                    UUID uuid = clicker.getUniqueId();
                    if (inventories.containsKey(uuid)) {
                        MapsViewer current = inventories.get(uuid);
                        if (e.getInventory().getHolder() != current.getInventory().getHolder()) {
                            return;
                        }

                        e.setCancelled(true);
                        GamePlayer gp = PlayerManager.getPlayer((Player) clicker);

                        if (e.getRawSlot() == 49) {
                            selectNext(current, gp);
                        }

                        if (e.getRawSlot() == 26 || e.getRawSlot() == 35) {
                            nextPage(current);
                            gp.p.playSound(gp.p.getLocation(), Sound.ITEM_BOOK_PAGE_TURN, 50, 50);
                        }
                        if (e.getRawSlot() == 18 || e.getRawSlot() == 27) {
                            backPage(current);
                            gp.p.playSound(gp.p.getLocation(), Sound.ITEM_BOOK_PAGE_TURN, 50, 50);
                        }

                        if (current.display.containsKey(e.getRawSlot())) {

                            GameRoom a = current.display.get(e.getRawSlot());

                            if (a.isPlaceHolder()) {
                                return;
                            }

                            a.addPlayer(gp);
                        }
                    }
                }
            }

            private void selectNext(MapsViewer cur, GamePlayer gp) {
                Selections s = PlayerManager.getSelection(gp, Selection.MAPVIEWER);
                switch (s) {
                    case MAPV_ALL:
                        PlayerManager.setSelection(gp, Selection.MAPVIEWER, Selections.MAPV_AVAILABLE);
                        break;
                    case MAPV_AVAILABLE:
                        PlayerManager.setSelection(gp, Selection.MAPVIEWER, Selections.MAPV_WAITING);
                        break;
                    case MAPV_STARTING:
                        PlayerManager.setSelection(gp, Selection.MAPVIEWER, Selections.MAPV_SPECTATE);
                        break;
                    case MAPV_WAITING:
                        PlayerManager.setSelection(gp, Selection.MAPVIEWER, Selections.MAPV_STARTING);
                        break;
                    default:
                        PlayerManager.setSelection(gp, Selection.MAPVIEWER, Selections.MAPV_ALL);
                        break;
                }

                gp.p.closeInventory();
                MapsViewer v = new MapsViewer(gp, PlayerManager.getSelection(gp, Selection.MAPVIEWER), "Maps");
                v.openInventory(gp);
                gp.p.playSound(gp.p.getLocation(), Sound.ITEM_BOOK_PAGE_TURN, 50, 50);
            }

            private void backPage(MapsViewer asd) {
                if (asd.p.exists(asd.pageNumber - 1)) {
                    asd.pageNumber--;
                }

                asd.fillChest(asd.p.getPage(asd.pageNumber), asd.gp);
            }

            private void nextPage(MapsViewer asd) {
                if (asd.p.exists(asd.pageNumber + 1)) {
                    asd.pageNumber++;
                }

                asd.fillChest(asd.p.getPage(asd.pageNumber), asd.gp);
            }

            @EventHandler
            public void onClose(InventoryCloseEvent e) {
                if (e.getPlayer() instanceof Player) {
                    if (e.getInventory() == null) {
                        return;
                    }
                    Player p = (Player) e.getPlayer();
                    UUID uuid = p.getUniqueId();
                    if (inventories.containsKey(uuid)) {
                        inventories.get(uuid).unregister();
                    }
                }
            }
        };
    }

    private Enum.TS select(Selections t) {
        switch (t) {
            case MAPV_ALL:
                return Enum.TS.MAP_ALL;
            case MAPV_WAITING:
                return Enum.TS.MAP_WAITING;
            case MAPV_SPECTATE:
                return Enum.TS.MAP_SPECTATE;
            case MAPV_STARTING:
                return Enum.TS.MAP_STARTING;
            case MAPV_AVAILABLE:
                return Enum.TS.MAP_AVAILABLE;
        }
        return null;
    }

    private ItemStack makeSelIcon() {
        ItemStack i;
        switch (selMap) {
            case MAPV_ALL:
                i = Itens.createItemLore(Material.OAK_SIGN, 1, "&9Filter",
                        Arrays.asList("&7Click to select the next filter.", "&9", "&9◼ &bAll Maps", "&9- &fAvailable",
                                "&9- &fWaiting", "&9- &fStarting", "&9- &fSpectate"));
                break;
            case MAPV_AVAILABLE:
                i = Itens.createItemLore(Material.OAK_SIGN, 1, "&9Filter",
                        Arrays.asList("&7Click to select the next filter.", "&9", "&9- &fAll Maps", "&9◼ &bAvailable",
                                "&9- &fWaiting", "&9- &fStarting", "&9- &fSpectate"));
                break;
            case MAPV_STARTING:
                i = Itens.createItemLore(Material.OAK_SIGN, 1, "&9Filter",
                        Arrays.asList("&7Click to select the next filter.", "&9", "&9- &fAll Maps", "&9- &fAvailable",
                                "&9- &fWaiting", "&9◼ &bStarting", "&9- &fSpectate"));
                break;
            case MAPV_WAITING:
                i = Itens.createItemLore(Material.OAK_SIGN, 1, "&9Filter",
                        Arrays.asList("&7Click to select the next filter.", "&9", "&9- &fAll Maps", "&9- &fAvailable",
                                "&9◼ &bWaiting", "&9- &fStarting", "&9- &fSpectate"));
                break;
            case MAPV_SPECTATE:
                i = Itens.createItemLore(Material.OAK_SIGN, 1, "&9Filter",
                        Arrays.asList("&7Click to select the next filter.", "&9", "&9- &fAll Maps", "&9- &fAvailable",
                                "&9- &fWaiting", "&9- &fStarting", "&9◼ &bSpectate"));
                break;
            default:
                i = Itens.createItemLore(Material.OAK_SIGN, 1, "&9Filter",
                        Arrays.asList("&7Click to select the next filter.", "&9", "&9◼ &fAll Maps", "&9- &fAvailable",
                                "&9- &fWaiting", "&9- &fStarting", "&9- &fSpectate"));
                break;
        }
        return i;
    }

    public void openInventory(GamePlayer player) {
        Inventory inv = getInventory();
        InventoryView openInv = player.p.getOpenInventory();
        if (openInv != null) {
            Inventory openTop = player.p.getOpenInventory().getTopInventory();
            if (openTop != null && openTop.getType().name().equalsIgnoreCase(inv.getType().name())) {
                openTop.setContents(inv.getContents());
            } else {
                player.p.openInventory(inv);
            }
        }
    }

    public void fillChest(List<GameRoom> items, GamePlayer p) {

        inv.clear();
        display.clear();

        for (int i = 0; i < 9; i++) {
            inv.setItem(i, placeholder);
        }

        inv.setItem(45, placeholder);
        inv.setItem(46, placeholder);
        inv.setItem(47, placeholder);
        inv.setItem(48, placeholder);
        inv.setItem(49, placeholder);
        inv.setItem(50, placeholder);
        inv.setItem(51, placeholder);
        inv.setItem(52, placeholder);
        inv.setItem(53, placeholder);
        inv.setItem(36, placeholder);
        inv.setItem(44, placeholder);
        inv.setItem(9, placeholder);
        inv.setItem(17, placeholder);

        inv.setItem(18, back);
        inv.setItem(27, back);
        inv.setItem(26, next);
        inv.setItem(35, next);

        int slot = 0;
        for (ItemStack i : inv.getContents()) {
            if (i == null) {
                if (items.size() != 0) {
                    GameRoom s = items.get(0);
                    MapItem a = new MapItem(s, p);
                    inv.setItem(slot, a.icon);
                    display.put(slot, s);
                    items.remove(0);
                }
            }
            slot++;
        }

        inv.setItem(49, makeSelIcon());
    }

    public Inventory getInventory() {
        return inv;
    }

    private void register() {
        inventories.put(this.uuid, this);
    }

    private void unregister() {
        inventories.remove(this.uuid);
    }
}
