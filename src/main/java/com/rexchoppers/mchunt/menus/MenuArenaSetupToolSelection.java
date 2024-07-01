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

public class MenuArenaSetupToolSelection extends MenuBase {

    private final MCHunt plugin;

    public MenuArenaSetupToolSelection(MCHunt plugin) {
        this.plugin = plugin;
        this.setInventory(
                SmartInventory.builder()
                        .id("mchuntMenuArenaSetupToolSelection")
                        .manager(plugin.getInventoryManager())
                        .provider(new MenuArenaSetupToolSelection.MenuArenaSetupToolSelectionProvider())
                        .size(1, 9)
                        .title("MCHunt - Tool Selection")
                        .closeable(true)
                        .build()
        );
    }

    private class MenuArenaSetupToolSelectionProvider implements InventoryProvider {

        @Override
        public void init(Player player, InventoryContents inventoryContents) {
            inventoryContents.set(0, 0, ClickableItem.of(plugin.getItemManager().itemArenaSetupToolSelection().build(), e -> {

            }));

            inventoryContents.set(0, 1, ClickableItem.of(plugin.getItemManager().itemArenaSetupSign().build(), e -> {

            }));

            inventoryContents.set(0, 2, ClickableItem.of(plugin.getItemManager().itemArenaSetupLobbySpawn().build(), e -> {

            }));

            inventoryContents.set(0, 3, ClickableItem.of(plugin.getItemManager().itemArenaSetupHiderSpawn().build(), e -> {

            }));


            inventoryContents.set(0, 4, ClickableItem.of(plugin.getItemManager().itemArenaSetupSeekerSpawn().build(), e -> {

            }));

            inventoryContents.set(0, 5, ClickableItem.of(plugin.getItemManager().itemArenaSetupAfterGameSpawn().build(), e -> {

            }));
        }

        @Override
        public void update(Player player, InventoryContents inventoryContents) {

        }
    }
}