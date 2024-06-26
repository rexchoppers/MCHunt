package com.rexchoppers.mchunt.managers;

import com.rexchoppers.mchunt.MCHunt;
import com.rexchoppers.mchunt.items.ItemBuilder;
import com.rexchoppers.mchunt.permissions.Permissions;
import com.rexchoppers.mchunt.util.Format;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;

public class ItemManager {
    private final MCHunt plugin;

    public ItemManager(MCHunt plugin) {
        this.plugin = plugin;
    }

    public void setArenaSetupItems(Player player) {
        player.getInventory().setItem(0, itemArenaSetupSelection().build());
    }

    public String getItemAction(ItemStack itemStack) {
        NamespacedKey key = new NamespacedKey(this.plugin, "action");
        ItemMeta itemMeta = itemStack.getItemMeta();

        if(itemMeta == null) {
            return null;
        }

        PersistentDataContainer container = itemMeta.getPersistentDataContainer();

        if(container.has(key , PersistentDataType.STRING)) {
            String s = container.get(key, PersistentDataType.STRING);
            return s;
        }

        return null;
    }

    public ItemBuilder itemNavigateToAdmin() {
        return new ItemBuilder(this.plugin)
                .setMaterial(Material.CRAFTING_TABLE)
                .setAmount(1)
                .setName(Format.processString("%B&6Enter Admin Menu"))
                .setPermission(Permissions.PERMISSION_ADMIN.getPermission())
                .setLores(new ArrayList<String>() {{
                    add("");
                    add(Format.processString("%b%tClick to enter the admin menu"));
                }});
    }

    public ItemBuilder itemEnterArenaSetup() {
        return new ItemBuilder(this.plugin)
                .setMaterial(Material.BOOK)
                .setAmount(1)
                .setName(Format.processString(Format.processString("%n%BEnter Arena Setup Mode")))
                .setPermission(Permissions.PERMISSION_ADMIN.getPermission())
                .setLores(new ArrayList<String>() {{
                    add("");
                    add(Format.processString("%tClick to enter arena setup mode"));
                    add(Format.processString("%tYour inventory and player state will"));
                    add(Format.processString("%tbe stored whilst you're in setup mode"));
                }});
    }

    public ItemBuilder itemArenaSetupSelection() {
        return new ItemBuilder(this.plugin)
                .setMaterial(Material.STICK)
                .setAmount(1)
                .setName(Format.processString("%n%BBoundary Selection"))
                .setPermission(Permissions.PERMISSION_ADMIN.getPermission())
                .setAction("mchunt.boundarySelection")
                .setLores(new ArrayList<String>() {{
                    add("");
                    add(Format.processString("%tBoundary selection mode"));
                    add("");
                    add(Format.processString("%aLEFT %tclick to select the first point"));
                    add(Format.processString("%aRIGHT %tclick to select the second point"));
                }});
    }
}
