package com.rexchoppers.mchunt.menus;

import com.rexchoppers.mchunt.MCHunt;
import com.rexchoppers.mchunt.events.internal.PlayerLeftArenaEvent;
import com.rexchoppers.mchunt.models.Arena;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import org.bukkit.entity.Player;

public class MenuInGame extends MenuBase {
    public MenuInGame(MCHunt plugin) {
        this.setPlugin(plugin);

        this.setInventory(SmartInventory.builder()
                .id("mchunt:menuInGame")
                .manager(plugin.getInventoryManager())
                .provider(new MenuInGameProvider())
                .size(1, 9)
                .title("MCHunt - In Game")
                .closeable(true)
                .build());
    }

    private class MenuInGameProvider implements InventoryProvider {
        @Override
        public void init(Player player, InventoryContents inventoryContents) {
            Arena arena = getPlugin().getArenaManager().getArenaPlayerIsIn(player.getUniqueId()).orElse(null);

            if (arena == null) {
                player.closeInventory();
                return;
            }

            // Item to leave the arena
            inventoryContents.set(0, 7, ClickableItem.of(getPlugin().getItemManager().itemArenaLeave().build(), e -> {
                arena.removePlayer(player.getUniqueId());
                player.closeInventory();

                getPlugin().getEventBusManager().publishEvent(new PlayerLeftArenaEvent(arena.getUUID(), player.getUniqueId()));
            }));
        }
    }
}
