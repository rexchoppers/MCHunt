package com.rexchoppers.mchunt.menus;

import com.rexchoppers.mchunt.MCHunt;
import fr.minuskube.inv.SmartInventory;
import org.bukkit.entity.Player;

public class MenuBase {
    private MCHunt plugin;
    private SmartInventory inventory;

    public void open(Player player) {
        this.inventory.open(player);
    }

    public void close(Player player) {
        this.inventory.close(player);
    }

    public void setInventory(SmartInventory inventory) {
        this.inventory = inventory;
    }

    public SmartInventory getInventory() {
        return this.inventory;
    }

    public void setPlugin(MCHunt plugin) {
        this.plugin = plugin;
    }

    public MCHunt getPlugin() {
        return this.plugin;
    }
}
