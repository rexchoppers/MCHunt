package com.rexchoppers.mchunt.menus;

import com.rexchoppers.mchunt.MCHunt;
import com.rexchoppers.mchunt.managers.LocalizationManager;
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

import static com.rexchoppers.mchunt.util.PlayerUtil.sendPlayerAudibleMessage;
import static com.rexchoppers.mchunt.util.PlayerUtil.sendPlayerError;

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
            inventoryContents.set(0, 0, ClickableItem.of(plugin.getItemManager().itemArenaSetupSelection().build(), e -> {
                player.getInventory().setItem(0, plugin.getItemManager().itemArenaSetupSelection().build());

                sendPlayerAudibleMessage(
                        player,
                        MCHunt.getLocalization().getMessage("arena.setup.updated_tool_selection")
                );
            }));

            inventoryContents.set(0, 1, ClickableItem.of(plugin.getItemManager().itemArenaSetupSign().build(), e -> {
                player.getInventory().setItem(0, plugin.getItemManager().itemArenaSetupSign().build());

                sendPlayerAudibleMessage(
                        player,
                        MCHunt.getLocalization().getMessage("arena.setup.updated_tool_selection")
                );
            }));

            inventoryContents.set(0, 2, ClickableItem.of(plugin.getItemManager().itemArenaSetupLobbySpawn().build(), e -> {
                ArenaSetup arenaSetup = plugin.getArenaSetupManager()
                        .getArenaSetupForPlayer(player.getUniqueId()).orElse(null);

                if(arenaSetup.getLobbySpawn() != null) {
                    // Warn the user the lobby spawn is already set
                    sendPlayerError(
                            player,
                            MCHunt.getLocalization().getMessage("arena.setup.lobby_spawn_already_set")
                    );
                    getInventory().close(player);
                } else {
                    player.getInventory().setItem(0, plugin.getItemManager().itemArenaSetupLobbySpawn().build());

                    sendPlayerAudibleMessage(
                            player,
                            MCHunt.getLocalization().getMessage("arena.setup.updated_tool_selection")
                    );
                }
            }));

            inventoryContents.set(0, 3, ClickableItem.of(plugin.getItemManager().itemArenaSetupHiderSpawn().build(), e -> {
                player.getInventory().setItem(0, plugin.getItemManager().itemArenaSetupHiderSpawn().build());

                sendPlayerAudibleMessage(
                        player,
                        MCHunt.getLocalization().getMessage("arena.setup.updated_tool_selection")
                );
            }));


            inventoryContents.set(0, 4, ClickableItem.of(plugin.getItemManager().itemArenaSetupSeekerSpawn().build(), e -> {
                player.getInventory().setItem(0, plugin.getItemManager().itemArenaSetupSeekerSpawn().build());

                sendPlayerAudibleMessage(
                        player,
                        MCHunt.getLocalization().getMessage("arena.setup.updated_tool_selection")
                );
            }));

            inventoryContents.set(0, 5, ClickableItem.of(plugin.getItemManager().itemArenaSetupAfterGameSpawn().build(), e -> {
                ArenaSetup arenaSetup = plugin.getArenaSetupManager()
                        .getArenaSetupForPlayer(player.getUniqueId()).orElse(null);

                if(arenaSetup.getAfterGameSpawn() != null) {
                    // Warn the user the after game spawn is already set
                    sendPlayerError(
                            player,
                            MCHunt.getLocalization().getMessage("arena.setup.after_game_spawn_already_set")
                    );
                    getInventory().close(player);
                } else {
                    player.getInventory().setItem(0, plugin.getItemManager().itemArenaSetupAfterGameSpawn().build());

                    sendPlayerAudibleMessage(
                            player,
                            MCHunt.getLocalization().getMessage("arena.setup.updated_tool_selection")
                    );
                }
            }));
        }

        @Override
        public void update(Player player, InventoryContents inventoryContents) {

        }
    }
}