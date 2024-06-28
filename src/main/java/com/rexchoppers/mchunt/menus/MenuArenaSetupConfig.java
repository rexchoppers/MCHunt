package com.rexchoppers.mchunt.menus;

import com.rexchoppers.mchunt.MCHunt;
import com.rexchoppers.mchunt.models.ArenaSetup;
import com.rexchoppers.mchunt.util.Format;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import fr.minuskube.inv.content.Pagination;
import fr.minuskube.inv.content.SlotIterator;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class MenuArenaSetupConfig extends MenuBase {

    private final MCHunt plugin;

    public MenuArenaSetupConfig(MCHunt plugin) {
        this.plugin = plugin;
        this.setInventory(
                SmartInventory.builder()
                        .id("mchuntMenuArenaSetupConfig")
                        .manager(plugin.getInventoryManager())
                        .provider(new MenuArenaSetupConfigProvider())
                        .size(1, 9)
                        .title("MCHunt - Arena Config")
                        .closeable(true)
                        .build()
        );
    }

    private class MenuArenaSetupConfigProvider implements InventoryProvider {
        @Override
        public void init(Player player, InventoryContents inventoryContents) {
            inventoryContents.set(0, 0, ClickableItem.of(plugin.getItemManager().itemArenaSetupBlocks().build(), e -> {
                new MenuArenaSetupBlockSelection(plugin).getInventory().open(player);
            }));
        }

        @Override
        public void update(Player player, InventoryContents inventoryContents) {

        }
    }
}