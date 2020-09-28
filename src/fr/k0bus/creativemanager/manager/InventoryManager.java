package fr.k0bus.creativemanager.manager;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import fr.k0bus.creativemanager.Main;
import fr.k0bus.creativemanager.type.ConfigType;

public class InventoryManager {

    Player p;
    ConfigManager cm;
    Main plugin;

    public InventoryManager(Player p, Main instance){
        this.p = p;
        this.plugin = instance;
        this.cm = new ConfigManager(p.getUniqueId()+".yml", new File(instance.getDataFolder(), "data"), instance, ConfigType.SAVE);
    }
    public Player getPlayer()
    {
        return this.p;
    }
    public void loadInventory(GameMode gm)
    {
        String gm_name = gm.name();
        if(cm.getConfig().contains(gm_name+".content") && cm.getConfig().isString(gm_name+".content") && cm.getConfig().contains(gm_name+".armor") && cm.getConfig().isString(gm_name+".armor"))
        {
            try {
                p.getInventory().setContents(this.itemStackArrayFromBase64(cm.getConfig().getString(gm_name+".content")));
                p.getInventory().setArmorContents(this.itemStackArrayFromBase64(cm.getConfig().getString(gm_name+".armor")));
            } catch (IOException e) {
                plugin.getLogger().log(Level.SEVERE, e.getMessage());
            }
            this.plugin.getLogger().log(Level.INFO, "Load inventory of user " + p.getName() + " in file " + p.getUniqueId() + ".yml for gamemode " + gm_name);
        }
        else
        {
            p.getInventory().clear();
            this.plugin.getLogger().log(Level.INFO, "Clear inventory for " + p.getName() + " (" + p.getUniqueId() + ") because no saved inventory found for gamemode " + gm_name);
        }

    }
    public void saveInventory(GameMode gm)
    {
        String gm_name = gm.name();
        String[] encoded = this.playerInventoryToBase64(p.getInventory());
        cm.getConfig().set(gm_name+".content", encoded[0]);
        cm.getConfig().set(gm_name+".armor", encoded[1]);
        if(cm.getConfig().contains(gm_name+".content") && cm.getConfig().isString(gm_name+".content") && cm.getConfig().contains(gm_name+".armor") && cm.getConfig().isString(gm_name+".armor"))
        {
            cm.saveConfig();
            this.plugin.getLogger().log(Level.INFO, "Save inventory of user " + p.getName() + " in file " + p.getUniqueId() + ".yml for gamemode " + gm_name);
        }
    }

    private String[] playerInventoryToBase64(PlayerInventory playerInventory) throws IllegalStateException {
    	//get the main content part, this doesn't return the armor
    	String content = this.toBase64(playerInventory);
    	String armor = itemStackArrayToBase64(playerInventory.getArmorContents());
    	
    	return new String[] { content, armor };
    }

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
     * Special thanks to Comphenix in the Bukkit forums or also known
     * as aadnk on GitHub.
     * 
     * <a href="https://gist.github.com/aadnk/8138186">Original Source</a>
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
    
    private ItemStack[] itemStackArrayFromBase64(String data) throws IOException {
    	try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
            ItemStack[] items = new ItemStack[dataInput.readInt()];
    
            // Read the serialized inventory
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