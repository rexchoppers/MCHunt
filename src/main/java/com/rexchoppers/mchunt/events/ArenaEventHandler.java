package com.rexchoppers.mchunt.events;

import com.rexchoppers.mchunt.MCHunt;
import com.rexchoppers.mchunt.enums.ArenaPlayerRole;
import com.rexchoppers.mchunt.enums.ArenaStatus;
import com.rexchoppers.mchunt.events.internal.HiderHasMovedEvent;
import com.rexchoppers.mchunt.events.internal.PlayerDiedEvent;
import com.rexchoppers.mchunt.events.internal.PlayerLeftArenaEvent;
import com.rexchoppers.mchunt.models.Arena;
import com.rexchoppers.mchunt.models.ArenaPlayer;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.List;
import java.util.UUID;

public record ArenaEventHandler(MCHunt plugin) implements Listener {

    /**
     * Handles player movement events.
     * This method is only used for:
     * - Hider movement
     * - Ensuring players stay within arena boundaries
     *
     * @param event
     */
    @EventHandler
    public void onPlayerMoveEvent(PlayerMoveEvent event) {
        Player serverPlayer = event.getPlayer();
        UUID playerUUID = serverPlayer.getUniqueId();

        // Check if player is in an arena
        Arena arena = plugin.getArenaManager().getArenaPlayerIsIn(playerUUID).orElse(null);

        if (arena == null) {
            return;
        }

        if (!arena.getStatus().equals(ArenaStatus.IN_PROGRESS)) {
            return;
        }

        ArenaPlayer player = arena.getPlayer(playerUUID);

        if (!plugin.getArenaManager().isPlayerInArena(playerUUID)) {
            return;
        }

        // Check player (Regardless of role) is within arena boundaries
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionManager regionManager = container.get(BukkitAdapter.adapt(serverPlayer.getWorld()));
        if (regionManager == null) return;

        ProtectedRegion region = regionManager.getRegion(arena.getRegionId());
        if (region == null) return;

        Location to = event.getTo();
        com.sk89q.worldedit.util.Location weLoc = BukkitAdapter.adapt(to);

        // If player is outside the region, and they're not respawning, cancel the event and teleport them back
        if (
                player.getRespawnCountdown() == null &&
                        arena.isSeekersReleased() &&
                        !region.contains(weLoc.getBlockX(), weLoc.getBlockY(), weLoc.getBlockZ())) {
            event.setCancelled(true);
            serverPlayer.teleport(event.getFrom());
            return;
        }

        if (player.getRole() != null && player.getRole().equals(ArenaPlayerRole.HIDER)) {
            // Ignore non-moving events to reduce server load
            if (event.getFrom().getX() == event.getTo().getX()
                    && event.getFrom().getY() == event.getTo().getY()
                    && event.getFrom().getZ() == event.getTo().getZ()) {
                return;
            }

            // Add a threshold so that small movements do not trigger the event
            double threshold = 0.2;
            if (Math.abs(event.getFrom().getX() - event.getTo().getX()) < threshold
                    && Math.abs(event.getFrom().getY() - event.getTo().getY()) < threshold
                    && Math.abs(event.getFrom().getZ() - event.getTo().getZ()) < threshold) {
                return;
            }

            if (!player.isDisguiseLocked()) {
                return;
            }

            this.plugin.getEventBusManager().publishEvent(new HiderHasMovedEvent(
                    arena,
                    player
            ));
        }
    }

    @EventHandler
    public void onPlayerQuitEvent(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        UUID playerUUID = player.getUniqueId();

        // Check if player is in an arena
        if (plugin.getArenaManager().isPlayerInArena(playerUUID)) {
            Arena arena = plugin.getArenaManager().getArenaPlayerIsIn(playerUUID).orElse(null);
            arena.removePlayer(playerUUID);

            this.plugin.getEventBusManager().publishEvent(new PlayerLeftArenaEvent(arena.getUUID(), playerUUID));
        }
    }

    @EventHandler
    public void onBlockBreakEvent(BlockBreakEvent event) {
        if (!plugin.getArenaManager().isPlayerInArena(event.getPlayer().getUniqueId())) return;

        event.setCancelled(true);
    }

    @EventHandler
    public void onBlockPlaceEvent(BlockPlaceEvent event) {
        if (!plugin.getArenaManager().isPlayerInArena(event.getPlayer().getUniqueId())) return;

        event.setCancelled(true);
    }

    @EventHandler
    public void onFoodLevelChangeEvent(FoodLevelChangeEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        if (!plugin.getArenaManager().isPlayerInArena(event.getEntity().getUniqueId())) return;

        // Prevent food level changes in arenas
        event.setCancelled(true);
    }

    @EventHandler
    public void onEntityDamageEvent(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        if (!plugin.getArenaManager().isPlayerInArena(event.getEntity().getUniqueId())) return;

        if (event.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_ATTACK)) return;

        // Prevent all damage in arenas
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerInteractEvent(PlayerInteractEvent event) {
        Player serverPlayer = event.getPlayer();

        if (!plugin.getArenaManager().isPlayerInArena(serverPlayer.getUniqueId())) return;

        if (
                (event.getAction() == Action.LEFT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_BLOCK) &&
                        event.getClickedBlock() != null &&
                        !event.getClickedBlock().getType().equals(Material.AIR)
        ) {
            List<Material> blockedInteractMaterials = List.of(
                    Material.ENCHANTING_TABLE,
                    Material.CRAFTING_TABLE,
                    Material.ANVIL,
                    Material.CHIPPED_ANVIL,
                    Material.DAMAGED_ANVIL,
                    Material.FURNACE,
                    Material.BLAST_FURNACE,
                    Material.SMOKER,
                    Material.BREWING_STAND,
                    Material.CAULDRON,
                    Material.LECTERN,
                    Material.BARREL,
                    Material.GRINDSTONE,
                    Material.LOOM,
                    Material.STONECUTTER,
                    Material.BELL,
                    Material.NOTE_BLOCK,
                    Material.JUKEBOX,
                    Material.DISPENSER,
                    Material.DROPPER,
                    Material.HOPPER,
                    Material.TRAPPED_CHEST,
                    Material.CHEST,
                    Material.ENDER_CHEST,
                    Material.SHULKER_BOX,
                    Material.WHITE_SHULKER_BOX,
                    Material.ORANGE_SHULKER_BOX,
                    Material.MAGENTA_SHULKER_BOX,
                    Material.LIGHT_BLUE_SHULKER_BOX,
                    Material.YELLOW_SHULKER_BOX,
                    Material.LIME_SHULKER_BOX,
                    Material.PINK_SHULKER_BOX,
                    Material.GRAY_SHULKER_BOX,
                    Material.LIGHT_GRAY_SHULKER_BOX,
                    Material.CYAN_SHULKER_BOX,
                    Material.PURPLE_SHULKER_BOX,
                    Material.BLUE_SHULKER_BOX,
                    Material.BROWN_SHULKER_BOX,
                    Material.GREEN_SHULKER_BOX,
                    Material.RED_SHULKER_BOX,
                    Material.BLACK_SHULKER_BOX,
                    Material.FLOWER_POT,
                    Material.ITEM_FRAME,
                    Material.GLOW_ITEM_FRAME,
                    Material.PAINTING
            );

            if (blockedInteractMaterials.contains(event.getClickedBlock().getType())) {
                event.setCancelled(true);
            }
        }
    }

    /**
     * Handles player vs player damage events.
     * <p>
     * TODO: Hook into the kill event later on. There will be a way to
     * bypass the screen and force respawn
     *
     */
    @EventHandler
    public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        if (!(event.getDamager() instanceof Player)) return;

        Player damagedServerPlayer = (Player) event.getEntity();
        Player damagerServerPlayer = (Player) event.getDamager();

        if (!plugin.getArenaManager().isPlayerInArena(damagedServerPlayer.getUniqueId())) return;
        if (!plugin.getArenaManager().isPlayerInArena(damagerServerPlayer.getUniqueId())) return;

        // Make sure both players are in the same arena
        Arena damagedPlayerArena = plugin.getArenaManager().getArenaPlayerIsIn(damagedServerPlayer.getUniqueId()).orElse(null);
        Arena damagerPlayerArena = plugin.getArenaManager().getArenaPlayerIsIn(damagerServerPlayer.getUniqueId()).orElse(null);

        if (damagedPlayerArena == null || damagerPlayerArena == null) return;

        if (!damagedPlayerArena.getUUID().equals(damagerPlayerArena.getUUID())) return;

        // Don't allow any damage if the arena is not in progress
        if (!damagedPlayerArena.getStatus().equals(ArenaStatus.IN_PROGRESS)) {
            event.setCancelled(true);
            return;
        }

        ArenaPlayer damagedPlayer = damagedPlayerArena.getPlayer(damagedServerPlayer.getUniqueId());
        ArenaPlayer damagerPlayer = damagerPlayerArena.getPlayer(damagerServerPlayer.getUniqueId());

        // Don't allow damage if both players are on the same team
        if (damagedPlayer.getRole() != null && damagerPlayer.getRole() != null &&
                damagedPlayer.getRole().equals(damagerPlayer.getRole())
        ) {
            event.setCancelled(true);
            return;
        }

        // Play a sound to the damaged player to indicate they've been hit
        damagedServerPlayer.getWorld().playSound(damagedServerPlayer.getLocation(), Sound.ENTITY_PLAYER_HURT, 1, 1);

        // If a seeker has enough damage to kill a hider, trigger the kill event
        if (
                event.getDamage() >= damagedServerPlayer.getHealth() &&
                        damagedPlayer.getRole() != null &&
                        damagedPlayer.getRole().equals(ArenaPlayerRole.HIDER) &&
                        damagerPlayer.getRole() != null &&
                        damagerPlayer.getRole().equals(ArenaPlayerRole.SEEKER)
        ) {
            this.plugin().getEventBusManager().publishEvent(new PlayerDiedEvent(
                    damagedPlayerArena,
                    damagedPlayer
            ));
            Bukkit.broadcastMessage("A hider has been killed by a seeker!");
        }
    }
}
