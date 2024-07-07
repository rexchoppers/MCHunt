package com.rexchoppers.mchunt.menus;

import com.rexchoppers.mchunt.MCHunt;
import com.rexchoppers.mchunt.events.internal.ArenaSetupDiscardedEvent;
import com.rexchoppers.mchunt.events.internal.ArenaSetupPlayerJoinedEvent;
import com.rexchoppers.mchunt.events.internal.ArenaSetupUpdatedEvent;
import com.rexchoppers.mchunt.managers.ArenaManager;
import com.rexchoppers.mchunt.managers.LocalizationManager;
import com.rexchoppers.mchunt.models.ArenaSetup;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collections;

import static com.rexchoppers.mchunt.util.PlayerUtil.sendPlayerAudibleMessage;
import static com.rexchoppers.mchunt.util.PlayerUtil.sendPlayerError;

public class MenuArenaSetupActions extends MenuBase {

    private final MCHunt plugin;

    public MenuArenaSetupActions(MCHunt plugin) {
        this.plugin = plugin;
        this.setInventory(
                SmartInventory.builder()
                        .id("mchuntMenuArenaSetupActions")
                        .manager(plugin.getInventoryManager())
                        .provider(new MenuArenaSetupActionsProvider())
                        .size(1, 9)
                        .title("MCHunt - Actions")
                        .closeable(true)
                        .build()
        );
    }

    private class MenuArenaSetupActionsProvider implements InventoryProvider {
        @Override
        public void init(Player player, InventoryContents inventoryContents) {
            ArenaSetup arenaSetup = plugin.getArenaSetupManager()
                    .getArenaSetupByPlayerUuid(
                            plugin.getArenaSetupManager().getArenaSetups(),
                            player.getUniqueId()).orElse(null);

            // Delete the arena setup from file so no logic will be executed on it


            inventoryContents.set(7, 0, ClickableItem.of(plugin.getItemManager().itemArenaSetupDiscardChanges().build(), e -> {
                plugin.getArenaSetupManager().removeArenaSetup(arenaSetup.getUUID());

                plugin.getEventBusManager().publishEvent(new ArenaSetupDiscardedEvent(arenaSetup));
            }));

            inventoryContents.set(8, 0, ClickableItem.of(plugin.getItemManager().itemArenaSetupSave().build(), e -> {

            }));
        }

        @Override
        public void update(Player player, InventoryContents inventoryContents) {

        }
    }
}