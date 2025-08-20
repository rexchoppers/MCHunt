package com.rexchoppers.mchunt.menus;

import com.rexchoppers.mchunt.MCHunt;
import com.rexchoppers.mchunt.exceptions.PlayerAlreadyInArenaSetupException;
import com.rexchoppers.mchunt.managers.LocalizationManager;
import com.rexchoppers.mchunt.models.ArenaSetup;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

import static com.rexchoppers.mchunt.util.PlayerUtil.*;

/*
Main menu for admins to access various features and settings of the plugin.
This menu is accessible only to players with the appropriate permissions.
 */
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

            // If the player is not in an arena setup, show the option to enter arena setup
            if(plugin.getArenaSetupManager().getArenaSetupForPlayer(player.getUniqueId()).isEmpty() &&
                    player.hasPermission(plugin.getItemManager().itemEnterArenaSetup().getPermission())) {
                contents.set(1, 7, ClickableItem.of(plugin.getItemManager().itemEnterArenaSetup().build(), e -> {
                    try {
                        // Create arena setup
                        ArenaSetup arenaSetup = new ArenaSetup(
                                UUID.randomUUID(),
                                player.getUniqueId(),
                                player.getInventory().getContents()
                        );

                        plugin.getArenaSetupManager().create(arenaSetup);

                        // Sort out player's inventory and set arena setup items
                        player.getInventory().clear();
                        plugin.getItemManager().setArenaSetupItems(player);
                        getInventory().close(player);
                        player.setGameMode(GameMode.CREATIVE);

                        // Send message to player
                        sendPlayerAudibleMessage(
                                player,
                                new LocalizationManager(MCHunt.getCurrentLocale())
                                        .getMessage(
                                                "arena.setup.now_in_arena_setup_mode"
                                        )
                        );

                    } catch (Exception ex) {
                        ex.printStackTrace();
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
