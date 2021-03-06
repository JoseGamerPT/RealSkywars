package pt.josegamerpt.realskywars.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import pt.josegamerpt.realskywars.RealSkywars;

import java.util.*;

public class PlayerInput implements Listener {

    private static final Map<UUID, PlayerInput> inputs = new HashMap<>();
    private final UUID uuid;

    private final ArrayList<String> texts = Text
            .addColor(Arrays.asList("&l&9Type in chat your input", "&fType &4cancel &fto cancel"));

    private final InputRunnable runGo;
    private final InputRunnable runCancel;
    private final BukkitTask taskId;
    private final Boolean inputMode;

    public PlayerInput(Player p, InputRunnable correct, InputRunnable cancel) {
        this.uuid = p.getUniqueId();
        p.closeInventory();
        this.inputMode = true;
        this.runGo = correct;
        this.runCancel = cancel;
        this.taskId = new BukkitRunnable() {
            public void run() {
                p.sendTitle(texts.get(0), texts.get(1), 0, 21, 0);
            }
        }.runTaskTimer(RealSkywars.pl, 0L, 20);

        this.register();
    }

    public PlayerInput(Player p, InputRunnable correct, InputRunnable cancel, String titl1, String titl2) {
        this.uuid = p.getUniqueId();
        p.closeInventory();
        this.inputMode = true;
        this.runGo = correct;
        this.runCancel = cancel;
        this.taskId = new BukkitRunnable() {
            public void run() {
                p.sendTitle(Text.addColor(titl1), Text.addColor(titl2), 0, 21, 0);
            }
        }.runTaskTimer(RealSkywars.pl, 0L, 20);

        this.register();
    }

    public static Listener getListener() {
        return new Listener() {
            @EventHandler
            public void onPlayerChat(AsyncPlayerChatEvent event) {
                String input = event.getMessage();
                UUID uuid = event.getPlayer().getUniqueId();
                if (inputs.containsKey(uuid)) {
                    PlayerInput current = inputs.get(uuid);
                    if (current.inputMode) {
                        event.setCancelled(true);
                        try {
                            if (input.equalsIgnoreCase("cancel")) {
                                event.getPlayer().sendMessage("Input canceled.");
                                current.taskId.cancel();
                                event.getPlayer().sendTitle("", "", 0, 1, 0);
                                Bukkit.getScheduler().scheduleSyncDelayedTask(RealSkywars.pl, () -> current.runCancel.run(input), 3);
                                current.unregister();
                                return;
                            }

                            current.taskId.cancel();
                            Bukkit.getScheduler().scheduleSyncDelayedTask(RealSkywars.pl, () -> current.runGo.run(input), 3);
                            event.getPlayer().sendTitle("", "", 0, 1, 0);
                            current.unregister();
                        } catch (Exception e) {
                            event.getPlayer().sendMessage("§cAn error ocourred. Contact JoseGamer_PT on Spigot.com");
                            e.printStackTrace();
                        }
                    }
                }
            }

        };
    }

    private void register() {
        inputs.put(this.uuid, this);
    }

    private void unregister() {
        inputs.remove(this.uuid);
    }

    @FunctionalInterface
    public interface InputRunnable {
        void run(String input);
    }
}