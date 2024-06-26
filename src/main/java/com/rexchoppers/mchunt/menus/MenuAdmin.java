package com.rexchoppers.mchunt.menus;

import com.rexchoppers.mchunt.MCHunt;
import com.rexchoppers.mchunt.exceptions.PlayerAlreadyInArenaSetupException;
import com.rexchoppers.mchunt.models.ArenaSetup;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.rexchoppers.mchunt.util.PlayerUtil.sendPlayerError;

public class MenuAdmin extends MenuBase {
    private final MCHunt plugin;

    public MenuAdmin(MCHunt plugin) {
        this.plugin = plugin;
        this.setInventory(
                SmartInventory.builder()
                        .id("mchuntMenuAdmin")
                        .manager(plugin.getInventoryManager())
                        .provider(new MenuAdmin.MenuAdminProvider())
                        .size(5, 9)
                        .title("MCHunt - Admin Menu")
                        .closeable(true)
                        .build()
        );
    }

    private class MenuAdminProvider implements InventoryProvider {
        @Override
        public void init(Player player, InventoryContents contents) {
            contents.fillBorders(ClickableItem.empty(new ItemStack(Material.BLACK_STAINED_GLASS_PANE)));

            if (player.hasPermission(plugin.getItemManager().itemEnterArenaSetup().getPermission())) {
                contents.set(1, 7, ClickableItem.of(plugin.getItemManager().itemEnterArenaSetup().build(), e -> {
                    try {
                        ArenaSetup arenaSetup = new ArenaSetup(player.getUniqueId(), player.getInventory().getContents());

                        plugin.getArenaSetupManager().createArenaSetup(arenaSetup);
                        player.getInventory().clear();
                    } catch (PlayerAlreadyInArenaSetupException ex) {
                        getInventory().close(player);
                        sendPlayerError(player, ex.getMessage());
                    }

                }));
            }
        }

        @Override
        public void update(Player player, InventoryContents contents) {
        }
    }
}
