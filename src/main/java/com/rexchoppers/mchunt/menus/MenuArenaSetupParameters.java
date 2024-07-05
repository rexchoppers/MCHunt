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
                        .size(8, 9)
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
        }

        @Override
        public void update(Player player, InventoryContents inventoryContents) {

        }
    }
}