package com.rexchoppers.mchunt.models;

import com.google.gson.annotations.Expose;
import com.rexchoppers.mchunt.enums.ArenaStatus;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.UUID;

public class ArenaSetup {
    @Expose
    private UUID playerUuid;

    @Expose
    private ItemStack[] inventory;


    public ArenaSetup(UUID playerUuid, ItemStack[] inventory) {
        this.playerUuid = playerUuid;
        this.inventory = inventory;
    }

    public UUID getPlayerUuid() {
        return playerUuid;
    }

    public ItemStack[] getInventory() {
        return inventory;
    }
}
