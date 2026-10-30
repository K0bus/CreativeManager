package fr.k0bus.creativemanager.utils;

import de.tr7zw.nbtapi.NBT;
import de.tr7zw.nbtapi.iface.ReadWriteNBT;
import org.bukkit.inventory.ItemStack;

/** Helper for NBT operations to prevent ClassNotFoundException when NBTAPI is not installed. */
public class NBTItemHelper {

  public static String getNbtKeys(ItemStack itemStack) {
    ReadWriteNBT tmp = NBT.itemStackToNBT(itemStack);
    StringBuilder nbtKey = new StringBuilder();
    for (String k : tmp.getKeys()) {
      if (!nbtKey.toString().isEmpty()) nbtKey.append(", ");
      nbtKey.append(k).append("§6");
    }
    return nbtKey.toString();
  }
}
