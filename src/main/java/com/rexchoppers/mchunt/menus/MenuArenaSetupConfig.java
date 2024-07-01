package com.rexchoppers.mchunt.menus;

import com.rexchoppers.mchunt.MCHunt;
import com.rexchoppers.mchunt.events.internal.ArenaSetupUpdatedEvent;
import com.rexchoppers.mchunt.managers.ArenaManager;
import com.rexchoppers.mchunt.managers.LocalizationManager;
import com.rexchoppers.mchunt.models.ArenaSetup;
import com.rexchoppers.mchunt.util.Format;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import fr.minuskube.inv.content.Pagination;
import fr.minuskube.inv.content.SlotIterator;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.rexchoppers.mchunt.util.PlayerUtil.sendPlayerAudibleMessage;
import static com.rexchoppers.mchunt.util.PlayerUtil.sendPlayerError;

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
            ArenaSetup arenaSetup = plugin.getArenaSetupManager()
                    .getArenaSetupByPlayerUuid(
                            plugin.getArenaSetupManager().getArenaSetups(),
                            player.getUniqueId()).orElse(null);

            inventoryContents.set(0, 0, ClickableItem.of(plugin.getItemManager().itemArenaSetupBlocks().build(), e -> {
                new MenuArenaSetupBlockSelection(plugin).getInventory().open(player);
            }));

            inventoryContents.set(0, 1, ClickableItem.of(plugin.getItemManager().itemArenaSetupSetName().build(), e -> {
                new AnvilGUI.Builder()
                        .onClose(stateSnapshot -> {
                            String name = stateSnapshot.getText();

                            if (name == null || name.isEmpty()) {
                                sendPlayerError(
                                        player,
                                        new LocalizationManager(MCHunt.getCurrentLocale())
                                                .getMessage(
                                                        "arena.setup.name_not_empty"
                                                )
                                );
                                return;
                            }

                            // Check if the name is a duplicate
                            ArenaManager arenaManager = plugin.getArenaManager();
                            if (arenaManager.getArenaByName(arenaManager.getArenas(), name).isPresent()) {
                                sendPlayerError(
                                        player,
                                        new LocalizationManager(MCHunt.getCurrentLocale())
                                                .getMessage(
                                                        "arena.setup.name_duplicate", name
                                                )
                                );
                                return;
                            }

                            // If no name changes were made, stop
                            if (name.equals(arenaSetup.getArenaName())) {
                                return;
                            }

                            arenaSetup.setArenaName(name);
                            plugin.getArenaSetupManager().updateArenaSetup(arenaSetup);

                            plugin.getEventBusManager().publishEvent(new ArenaSetupUpdatedEvent(
                                    arenaSetup.getUUID()
                            ));

                            sendPlayerAudibleMessage(
                                    player,
                                    new LocalizationManager(MCHunt.getCurrentLocale())
                                            .getMessage(
                                                    "arena.setup.name_set", name
                                            )
                            );
                        })
                        .onClick((slot, stateSnapshot) -> {
                            if (slot != AnvilGUI.Slot.OUTPUT) {
                                return Collections.emptyList();
                            }

                            return Arrays.asList(AnvilGUI.ResponseAction.close());
                        })
                        .text(arenaSetup.getArenaName() == null || arenaSetup.getArenaName().isEmpty() ? "Arena" : arenaSetup.getArenaName())
                        .title("Enter Arena Name")
                        .plugin(plugin)
                        .open(player);
            }));

            inventoryContents.set(0, 2, ClickableItem.of(plugin.getItemManager().itemArenaSetupParameters().build(), e -> {

            }));
        }

        @Override
        public void update(Player player, InventoryContents inventoryContents) {

        }
    }
}