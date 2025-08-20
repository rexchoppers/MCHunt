package com.rexchoppers.mchunt.events;

import com.rexchoppers.mchunt.MCHunt;
import com.rexchoppers.mchunt.managers.LocalizationManager;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;
import static com.rexchoppers.mchunt.util.PlayerUtil.sendPlayerError;

public record DroppableEventHandler(MCHunt plugin) implements Listener {

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        Player player = event.getPlayer();

        Item itemDrop = event.getItemDrop();
        ItemStack itemStack = itemDrop.getItemStack();

        boolean droppable = this.plugin.getItemManager().getItemDroppable(itemStack);

        if (!droppable) {
            event.setCancelled(true);
            sendPlayerError(player, new LocalizationManager(MCHunt.getCurrentLocale())
                    .getMessage(
                            "player.cannot_drop_item"
                    ));
            itemDrop.remove();
        }
    }
}
