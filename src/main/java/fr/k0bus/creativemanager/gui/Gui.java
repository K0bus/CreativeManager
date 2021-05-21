package fr.k0bus.creativemanager.gui;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class Gui implements Listener {
    private Inventory inv;
    private final Player player;
    private final JavaPlugin plugin;

    public Gui(Player player, JavaPlugin plugin)
    {
        this.player = player;
        this.plugin = plugin;
        Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
    }
    public abstract void initItem();

    public void show()
    {
        getPlayer().openInventory(getInv());
    }
    // Cancel dragging event
    @EventHandler
    public void onInventoryClick(final InventoryDragEvent e) {
        if (e.getInventory() == inv) {
            e.setCancelled(true);
        }
    }
    @EventHandler
    public abstract void onInventoryClick(final InventoryClickEvent e);

    @EventHandler
    public void onInventoryClose(final InventoryCloseEvent e) {}

    public Player getPlayer() {
        return player;
    }

    public Inventory getInv() {
        return inv;
    }

    public void setInv(Inventory inv) {
        this.inv = inv;
    }

    public JavaPlugin getPlugin() {
        return plugin;
    }
}
