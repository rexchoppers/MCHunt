package com.rexchoppers.mchunt.menus;

import com.rexchoppers.mchunt.MCHunt;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class MenuMain extends MenuBase {
    private final MCHunt plugin;

    public MenuMain(MCHunt plugin) {
        this.plugin = plugin;
        this.setInventory(
                SmartInventory.builder()
                        .id("mchuntMain")
                        .manager(plugin.getInventoryManager())
                        .provider(new MenuMainProvider())
                        .size(3, 9)
                        .title("MCHunt Menu")
                        .closeable(true)
                        .build()
        );
    }

    private class MenuMainProvider implements InventoryProvider {
        @Override
        public void init(Player player, InventoryContents contents) {
            contents.fillBorders(ClickableItem.empty(new ItemStack(Material.BLACK_STAINED_GLASS_PANE)));

            if (player.hasPermission(plugin.getItemManager().itemNavigateToAdmin().getPermission())) {
                contents.set(1, 7, ClickableItem.of(plugin.getItemManager().itemNavigateToAdmin().build(), e -> {
                    // (new MenuAdmin(plugin)).open(player);
                }));
            }
        }

        @Override
        public void update(Player player, InventoryContents contents) {
        }
    }
}
