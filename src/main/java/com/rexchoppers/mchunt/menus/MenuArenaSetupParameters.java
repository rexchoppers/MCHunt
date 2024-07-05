package com.rexchoppers.mchunt.menus;

import com.rexchoppers.mchunt.MCHunt;
import com.rexchoppers.mchunt.events.internal.ArenaSetupUpdatedEvent;
import com.rexchoppers.mchunt.managers.ArenaManager;
import com.rexchoppers.mchunt.managers.LocalizationManager;
import com.rexchoppers.mchunt.models.ArenaSetup;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collections;

import static com.rexchoppers.mchunt.util.PlayerUtil.sendPlayerAudibleMessage;
import static com.rexchoppers.mchunt.util.PlayerUtil.sendPlayerError;

public class MenuArenaSetupParameters extends MenuBase {

    private final MCHunt plugin;

    public MenuArenaSetupParameters(MCHunt plugin) {
        this.plugin = plugin;
        this.setInventory(
                SmartInventory.builder()
                        .id("mchuntMenuArenaSetupParameters")
                        .manager(plugin.getInventoryManager())
                        .provider(new MenuArenaSetupParametersProvider())
                        .size(1, 9)
                        .title("MCHunt - Arena Parameters")
                        .closeable(true)
                        .build()
        );
    }

    private class MenuArenaSetupParametersProvider implements InventoryProvider {
        @Override
        public void init(Player player, InventoryContents inventoryContents) {
            ArenaSetup arenaSetup = plugin.getArenaSetupManager()
                    .getArenaSetupByPlayerUuid(
                            plugin.getArenaSetupManager().getArenaSetups(),
                            player.getUniqueId()).orElse(null);

            inventoryContents.set(0, 0, ClickableItem.of(plugin.getItemManager().itemArenaSetupParametersMinimumPlayers().build(), e -> {

            }));

            inventoryContents.set(0, 1, ClickableItem.of(plugin.getItemManager().itemArenaSetupParametersMaximumPlayers().build(), e -> {

            }));

            inventoryContents.set(0, 2, ClickableItem.of(plugin.getItemManager().itemArenaSetupParametersSeekerCount().build(), e -> {

            }));

            inventoryContents.set(0, 3, ClickableItem.of(plugin.getItemManager().itemArenaSetupParametersCountdownBeforeGameStart().build(), e -> {

            }));

            inventoryContents.set(0, 4, ClickableItem.of(plugin.getItemManager().itemArenaSetupParametersCountdownAfterGameEnd().build(), e -> {

            }));

            inventoryContents.set(0, 5, ClickableItem.of(plugin.getItemManager().itemArenaSetupParametersRespawnDelay().build(), e -> {

            }));
        }

        @Override
        public void update(Player player, InventoryContents inventoryContents) {

        }
    }
}