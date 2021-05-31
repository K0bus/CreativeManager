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

/**
 * GUI abstract class.
 *
 * @see org.bukkit.event.Listener
 */
public abstract class Gui implements Listener {
    private Inventory inv;
    private final Player player;
    private final JavaPlugin plugin;

    /**
     * Instantiates a new Gui.
     *
     * @param player the player.
     * @param plugin the plugin.
     */
    public Gui(Player player, JavaPlugin plugin) {
        this.player = player;
        this.plugin = plugin;
        Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
    }

    /**
     * Init item.
     */
    public abstract void initItem();

    /**
     * Show.
     */
    public void show() {
        getPlayer().openInventory(getInv());
    }

    /**
     * On inventory click.
     * Cancel dragging event
     *
     * @param e the event.
     */
    @EventHandler
    public void onInventoryClick(final InventoryDragEvent e) {
        if (e.getInventory() == inv) {
            e.setCancelled(true);
        }
    }

    /**
     * On inventory click.
     *
     * @param e the event.
     */
    @EventHandler
    public abstract void onInventoryClick(final InventoryClickEvent e);

    /**
     * On inventory close.
     *
     * @param e the event.
     */
    @EventHandler
    public void onInventoryClose(final InventoryCloseEvent e) {
    }

    /**
     * Gets player.
     *
     * @return the player.
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Gets inventory.
     *
     * @return the inventory.
     */
    public Inventory getInv() {
        return inv;
    }

    /**
     * Sets inventory.
     *
     * @param inv the inventory.
     */
    public void setInv(Inventory inv) {
        this.inv = inv;
    }

    /**
     * Gets plugin.
     *
     * @return the plugin.
     */
    public JavaPlugin getPlugin() {
        return plugin;
    }
}
