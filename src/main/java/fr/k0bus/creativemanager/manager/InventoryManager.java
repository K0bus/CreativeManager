package fr.k0bus.creativemanager.manager;

import fr.k0bus.creativemanager.CreativeManager;
import fr.k0bus.creativemanager.settings.UserData;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Inventory manager class.
 */
public class InventoryManager {
    private final Player p;
    private final UserData cm;
    private final CreativeManager plugin;

    /**
     * Instantiates a new Inventory manager.
     *
     * @param p        the player.
     * @param instance the instance.
     */
    public InventoryManager(Player p, CreativeManager instance) {
        this.p = p;
        this.plugin = instance;
        this.cm = new UserData(p, plugin);
    }

    /**
     * Item stack array to base 64 string.
     *
     * @param items the items.
     * @return the string.
     * @throws IllegalStateException the illegal state exception.
     */
    public static String itemStackArrayToBase64(ItemStack[] items) throws IllegalStateException {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);

            // Write the size of the inventory
            dataOutput.writeInt(items.length);

            // Save every element in the list
            for (ItemStack item : items) {
                dataOutput.writeObject(item);
            }

            // Serialize that array
            dataOutput.close();
            return Base64Coder.encodeLines(outputStream.toByteArray());
        } catch (Exception e) {
            throw new IllegalStateException("Unable to save item stacks.", e);
        }
    }

    /**
     * Gets player.
     *
     * @return the player.
     */
    public Player getPlayer() {
        return this.p;
    }

    /**
     * Load inventory.
     *
     * @param gm the game mode.
     */
    public void loadInventory(GameMode gm) {
        String gm_name = gm.name();
        if (cm.contains(gm_name + ".content") && cm.isString(gm_name + ".content") && cm.contains(gm_name + ".armor") && cm.isString(gm_name + ".armor")) {
            try {
                p.getInventory().setContents(this.itemStackArrayFromBase64(cm.getString(gm_name + ".content")));
                p.getInventory().setArmorContents(this.itemStackArrayFromBase64(cm.getString(gm_name + ".armor")));
            } catch (IOException e) {
                plugin.getLogger().severe(e.getMessage());
            }
            if (plugin.getConfig().getBoolean("log"))
                this.plugin.getLogger().info("Load inventory of user " + p.getName() + " in file " + p.getUniqueId() + ".yml for gamemode " + gm_name);
        } else {
            p.getInventory().clear();
            if (plugin.getConfig().getBoolean("log"))
                this.plugin.getLogger().info("Clear inventory for " + p.getName() + " (" + p.getUniqueId() + ") because no saved inventory found for gamemode " + gm_name);
        }

    }

    /**
     * Save inventory.
     *
     * @param gm the game mode.
     */
    public void saveInventory(GameMode gm) {
        String gm_name = gm.name();
        String[] encoded = this.playerInventoryToBase64(p.getInventory());
        cm.set(gm_name + ".content", encoded[0]);
        cm.set(gm_name + ".armor", encoded[1]);
        if (cm.contains(gm_name + ".content") && cm.isString(gm_name + ".content") && cm.contains(gm_name + ".armor") && cm.isString(gm_name + ".armor")) {
            cm.save();
            if (plugin.getConfig().getBoolean("log"))
                this.plugin.getLogger().info("Save inventory of user " + p.getName() + " in file " + p.getUniqueId() + ".yml for gamemode " + gm_name);
        }
    }

    /**
     * Player inventory to base 64 string.
     *
     * @param playerInventory the player inventory.
     * @return the string [ ]
     * @throws IllegalStateException the illegal state exception.
     */
    private String[] playerInventoryToBase64(PlayerInventory playerInventory) throws IllegalStateException {
        //get the main content part, this doesn't return the armor
        String content = this.toBase64(playerInventory);
        String armor = itemStackArrayToBase64(playerInventory.getArmorContents());

        return new String[]{content, armor};
    }

    /**
     * Special thanks to Comphenix in the Bukkit forums or also known
     * as aadnk on GitHub.
     *
     * <a href="https://gist.github.com/aadnk/8138186">Original Source</a>
     *
     * @param inventory the inventory
     * @return the string
     * @throws IllegalStateException the illegal state exception
     */
    private String toBase64(Inventory inventory) throws IllegalStateException {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);

            // Write the size of the inventory
            dataOutput.writeInt(inventory.getSize());

            // Save every element in the list
            for (int i = 0; i < inventory.getSize(); i++) {
                dataOutput.writeObject(inventory.getItem(i));
            }

            // Serialize that array
            dataOutput.close();
            return Base64Coder.encodeLines(outputStream.toByteArray());
        } catch (Exception e) {
            throw new IllegalStateException("Unable to save item stacks.", e);
        }
    }

    /**
     * Item stack array from base 64 item stack.
     *
     * @param data the data.
     * @return the item stack.
     * @throws IOException the io exception.
     */
    private ItemStack[] itemStackArrayFromBase64(String data) throws IOException {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
            ItemStack[] items = new ItemStack[dataInput.readInt()];

            // Read the serialized inventory.
            for (int i = 0; i < items.length; i++) {
                items[i] = (ItemStack) dataInput.readObject();
            }

            dataInput.close();
            return items;
        } catch (ClassNotFoundException e) {
            throw new IOException("Unable to decode class type.", e);
        }
    }
}