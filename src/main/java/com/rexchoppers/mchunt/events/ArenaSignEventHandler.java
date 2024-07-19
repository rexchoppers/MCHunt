package com.rexchoppers.mchunt.events;

import com.rexchoppers.mchunt.MCHunt;
import com.rexchoppers.mchunt.events.internal.PlayerJoinedArenaEvent;
import com.rexchoppers.mchunt.models.Arena;
import com.rexchoppers.mchunt.models.ArenaPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.List;

public class ArenaSignEventHandler implements Listener {
    private final MCHunt plugin;

    public ArenaSignEventHandler(MCHunt plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerRightClickArenaSign(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        if (event.getClickedBlock() == null) {
            return;
        }

        if (!(event.getClickedBlock().getType().equals(Material.OAK_WALL_SIGN) ||
                event.getClickedBlock().getType().equals(Material.OAK_SIGN))) {
            return;
        }

        List<Arena> arenas = plugin.getArenaManager().getArenas();

        for (Arena arena : arenas) {
            if (arena.getArenaSigns() == null || arena.getArenaSigns().length == 0) {
                continue;
            }

            for (int i = 0; i < arena.getArenaSigns().length; i++) {
                if (event.getClickedBlock().getLocation().equals(arena.getArenaSigns()[i])) {
                    event.setCancelled(true);
                    Bukkit.broadcastMessage("Arena sign set");

                    // Add player to arena
                    arena.addPlayer(new ArenaPlayer(player.getUniqueId()));
                    player.teleport(arena.getLobbySpawn());

                    this.plugin.getEventBusManager().publishEvent(new PlayerJoinedArenaEvent(arena.getUUID(), player.getUniqueId()));

                    return;
                }
            }
        }
    }
}
