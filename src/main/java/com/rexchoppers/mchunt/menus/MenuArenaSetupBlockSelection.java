package com.rexchoppers.mchunt.menus;

import com.rexchoppers.mchunt.MCHunt;
import com.rexchoppers.mchunt.models.ArenaSetup;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import fr.minuskube.inv.content.Pagination;
import fr.minuskube.inv.content.SlotIterator;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class MenuArenaSetupBlockSelection extends MenuBase {

    private final MCHunt plugin;

    public MenuArenaSetupBlockSelection(MCHunt plugin) {
        this.plugin = plugin;
        this.setInventory(
                SmartInventory.builder()
                        .id("mchuntMenuArenaSetupBlockSelection")
                        .manager(plugin.getInventoryManager())
                        .provider(new MenuArenaSetupBlockSelection.MenuArenaSetupBlockSelectionProvider())
                        .size(6, 9)
                        .title("MCHunt - Block Selection")
                        .closeable(true)
                        .build()
        );
    }

    private class MenuArenaSetupBlockSelectionProvider implements InventoryProvider {

        @Override
        public void init(Player player, InventoryContents inventoryContents) {
            Pagination pagination = inventoryContents.pagination();

            // Get block materials already selected for the arena
            ArenaSetup arenaSetup = plugin.getArenaSetupManager()
                    .getArenaSetupByPlayerUuid(
                            plugin.getArenaSetupManager().getArenaSetups(),
                            player.getUniqueId()).orElse(null);

            if (arenaSetup == null) {
                // Throw something here, this is bad
            }

            // Get blocks already selected for the arena
            String[] arenaBlocks = arenaSetup.getArenaBlocks();

            // Get all block materials
            List<Material> blockMaterials = plugin.getItemManager().getBlockMaterials();

            // Create a new itemstack for each block material and mark it as enchanted if it's already selected
            List<ClickableItem> items = new ArrayList<>();

            for (Material material : blockMaterials) {
                ItemStack itemStack = new ItemStack(material);
                ItemMeta meta = itemStack.getItemMeta(); // Retrieve the current ItemMeta

                boolean isSelected = false;
                if (arenaBlocks != null) {
                    for (String arenaBlock : arenaBlocks) {
                        if (arenaBlock.equals(material.toString())) {
                            isSelected = true;
                            break;
                        }
                    }
                }

                if (isSelected) {
                    meta.addEnchant(Enchantment.LURE, 1, true); // Add an enchantment
                    itemStack.setItemMeta(meta); // Set the modified meta back to the ItemStack
                }

                items.add(ClickableItem.of(itemStack, e -> {
                    // Add or remove block from arena setup
                    if (arenaBlocks != null) {
                        List<String> newArenaBlocks = new ArrayList<>();
                        for (String arenaBlock : arenaBlocks) {
                            if (!arenaBlock.equals(material.toString())) {
                                newArenaBlocks.add(arenaBlock);
                            }
                        }

                        if (newArenaBlocks.size() == arenaBlocks.length) {
                            newArenaBlocks.add(material.toString());
                        }
                        arenaSetup.setArenaBlocks(newArenaBlocks.toArray(new String[0]));
                    } else {
                        arenaSetup.setArenaBlocks(new String[]{material.toString()});
                    }
                }));
            }


            pagination.setItems(items.toArray(new ClickableItem[0]));
            pagination.setItemsPerPage(45);

            pagination.addToIterator(inventoryContents.newIterator(SlotIterator.Type.HORIZONTAL, 0, 0));

            inventoryContents.set(5, 3, ClickableItem.of(new ItemStack(Material.ARROW),
                    e -> getInventory().open(player, pagination.previous().getPage())));
            inventoryContents.set(5, 5, ClickableItem.of(new ItemStack(Material.ARROW),
                    e -> getInventory().open(player, pagination.next().getPage())));
        }

        @Override
        public void update(Player player, InventoryContents inventoryContents) {

        }
    }
}