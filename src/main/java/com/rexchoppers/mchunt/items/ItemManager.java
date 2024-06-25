package com.rexchoppers.mchunt.items;

import com.rexchoppers.mchunt.MCHunt;
import com.rexchoppers.mchunt.permissions.Permissions;
import com.rexchoppers.mchunt.util.Format;
import org.bukkit.Material;

import java.util.ArrayList;

public class ItemManager {
    private final MCHunt plugin;

    public ItemManager(MCHunt plugin) {
        this.plugin = plugin;
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
}
