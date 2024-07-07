package com.rexchoppers.mchunt.events;

import com.rexchoppers.mchunt.MCHunt;
import com.rexchoppers.mchunt.events.internal.ArenaSetupPlayerJoinedEvent;
import com.rexchoppers.mchunt.events.internal.ArenaSetupUpdatedEvent;
import com.rexchoppers.mchunt.exceptions.ArenaSetupNotFoundException;
import com.rexchoppers.mchunt.items.ItemBuilder;
import com.rexchoppers.mchunt.managers.ArenaManager;
import com.rexchoppers.mchunt.managers.ArenaSetupManager;
import com.rexchoppers.mchunt.managers.LocalizationManager;
import com.rexchoppers.mchunt.menus.MenuArenaSetupBlockSelection;
import com.rexchoppers.mchunt.menus.MenuArenaSetupConfig;
import com.rexchoppers.mchunt.menus.MenuArenaSetupToolSelection;
import com.rexchoppers.mchunt.models.ArenaSetup;
import com.rexchoppers.mchunt.signs.ScrollingSign;
import com.rexchoppers.mchunt.util.BoundaryUtil;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;

import static com.rexchoppers.mchunt.util.PlayerUtil.sendPlayerAudibleMessage;
import static com.rexchoppers.mchunt.util.PlayerUtil.sendPlayerError;

public class ArenaSetupEventHandler implements Listener {
    private final MCHunt plugin;

    private HashMap<UUID, Long> lastInteractTimes = new HashMap<>();

    public ArenaSetupEventHandler(MCHunt plugin) {
        this.plugin = plugin;
    }

    public boolean isEmptyItem(ItemStack item) {
        return item == null || item.getType().isAir();
    }

    public boolean isBlockClickedEmpty(PlayerInteractEvent event) {
        return event.getClickedBlock() == null || event.getClickedBlock().getType().isAir();
    }

    /**
     * Make sure ALL arena setup signs cannot be right clicked by any player
     * @param event
     */
    @EventHandler
    public void onPlayerRightClickArenaSetupSign(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        if (event.getClickedBlock() == null) {
            return;
        }

        if (!event.getClickedBlock().getType().equals(Material.OAK_WALL_SIGN) ||
            !event.getClickedBlock().getType().equals(Material.OAK_SIGN)) {
            return;
        }

        List<ArenaSetup> arenaSetups = this.plugin.getArenaSetupManager().getArenaSetups();

        for (ArenaSetup arenaSetup : arenaSetups) {
            if (arenaSetup.getArenaSigns() != null) {
                for (Location location : arenaSetup.getArenaSigns()) {
                    if (location.equals(event.getClickedBlock().getLocation())) {
                        event.setCancelled(true);
                        return;
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerInteractSpawnBlocks(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        // Get location
        // Get if location is either in: hiderSpawns, seekerSpawns, lobbySpawn, afterGameSpawn
        // If so, remove from the list

        ArenaSetup arenaSetup = this.plugin.getArenaSetupManager()
                .getArenaSetupByPlayerUuid(
                        plugin.getArenaSetupManager().getArenaSetups(),
                        player.getUniqueId()).orElse(null);

        if (arenaSetup == null) {
            return;
        }

        /*
        Check if the player has right clicked on a location
        that is a spawn point. If so, remove it from the list
         */
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        if (event.getClickedBlock() == null) {
            return;
        }

        for (Location location : arenaSetup.getHiderSpawns()) {
            if(location == null) {
                continue;
            }

            if (location.equals(event.getClickedBlock().getLocation())) {
                arenaSetup.removeHiderSpawn(location);
                this.plugin.getArenaSetupManager().updateArenaSetup(arenaSetup);

                sendPlayerAudibleMessage(
                        player,
                        new LocalizationManager(MCHunt.getCurrentLocale())
                                .getMessage(
                                        "arena.setup.hider_spawn_removed",
                                        location.getWorld().getName(),
                                        Double.toString(location.getX()),
                                        Double.toString(location.getY()),
                                        Double.toString(location.getZ())
                                )
                );
                return;
            }
        }

        // Seeker spawns
        for (Location location : arenaSetup.getSeekerSpawns()) {
            if(location == null) {
                continue;
            }

            if (location.equals(event.getClickedBlock().getLocation())) {
                arenaSetup.removeSeekerSpawn(location);
                this.plugin.getArenaSetupManager().updateArenaSetup(arenaSetup);

                sendPlayerAudibleMessage(
                        player,
                        new LocalizationManager(MCHunt.getCurrentLocale())
                                .getMessage(
                                        "arena.setup.seeker_spawn_removed",
                                        location.getWorld().getName(),
                                        Double.toString(location.getX()),
                                        Double.toString(location.getY()),
                                        Double.toString(location.getZ())
                                )
                );
                return;
            }
        }

        // Lobby spawn
        if (arenaSetup.getLobbySpawn() != null && arenaSetup.getLobbySpawn().equals(event.getClickedBlock().getLocation())) {
            arenaSetup.setLobbySpawn(null);
            this.plugin.getArenaSetupManager().updateArenaSetup(arenaSetup);

            player.getInventory().setItem(0, plugin.getItemManager().itemArenaSetupLobbySpawn().build());

            sendPlayerAudibleMessage(
                    player,
                    new LocalizationManager(MCHunt.getCurrentLocale())
                            .getMessage(
                                    "arena.setup.lobby_spawn_removed",
                                    event.getClickedBlock().getLocation().getWorld().getName(),
                                    Double.toString(event.getClickedBlock().getLocation().getX()),
                                    Double.toString(event.getClickedBlock().getLocation().getY()),
                                    Double.toString(event.getClickedBlock().getLocation().getZ())
                            )
            );
            return;
        }

        if (arenaSetup.getAfterGameSpawn() != null && arenaSetup.getAfterGameSpawn().equals(event.getClickedBlock().getLocation())) {
            arenaSetup.setAfterGameSpawn(null);
            this.plugin.getArenaSetupManager().updateArenaSetup(arenaSetup);

            player.getInventory().setItem(0, plugin.getItemManager().itemArenaSetupAfterGameSpawn().build());

            sendPlayerAudibleMessage(
                    player,
                    new LocalizationManager(MCHunt.getCurrentLocale())
                            .getMessage(
                                    "arena.setup.after_game_spawn_removed",
                                    event.getClickedBlock().getLocation().getWorld().getName(),
                                    Double.toString(event.getClickedBlock().getLocation().getX()),
                                    Double.toString(event.getClickedBlock().getLocation().getY()),
                                    Double.toString(event.getClickedBlock().getLocation().getZ())
                            )
            );
            return;
        }
    }

    @EventHandler
    public void onPlayerInteractItems(PlayerInteractEvent event) throws ArenaSetupNotFoundException {
        Player player = event.getPlayer();
        ItemStack itemInMainHand = player.getInventory().getItemInMainHand();

        if (isEmptyItem(itemInMainHand)) {
            return;
        }

        String action = this.plugin.getItemManager().getItemAction(itemInMainHand);

        // Item actions
        if (action != null) {
            ArenaSetup arenaSetup = this.plugin.getArenaSetupManager()
                    .getArenaSetupByPlayerUuid(
                            plugin.getArenaSetupManager().getArenaSetups(),
                            player.getUniqueId()).orElse(null);

            if (arenaSetup == null) {
                throw new ArenaSetupNotFoundException();
            }

            long lastTime = lastInteractTimes.getOrDefault(player.getUniqueId(), 0L);
            long currentTime = System.currentTimeMillis();

            if (currentTime - lastTime < 500) {
                event.setCancelled(true);
                return;
            }

            lastInteractTimes.put(player.getUniqueId(), currentTime);

            switch (action) {
                case "mchunt.setup.arenaConfig":
                    if (event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
                        new MenuArenaSetupConfig(this.plugin).getInventory().open(player);
                        event.setCancelled(true);
                    }
                    break;
                case "mchunt.setup.toolSelection":
                    if (event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
                        new MenuArenaSetupToolSelection(this.plugin).getInventory().open(player);
                        event.setCancelled(true);
                    }
                    break;
                // Boundary selection
                case "mchunt.setup.boundarySelection":
                    if (isBlockClickedEmpty(event)) {
                        return;
                    }

                    BoundaryUtil boundaryUtil = new BoundaryUtil();

                    // Clear any pre-existing boundaries for the user
                    boundaryUtil.clearTemporaryBoundary(player, arenaSetup);

                    // Left click = Boundary point 1
                    if (event.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
                        arenaSetup.setLocationBoundaryPoint1(event.getClickedBlock().getLocation());
                        this.plugin.getArenaSetupManager().updateArenaSetup(arenaSetup);
                        sendPlayerAudibleMessage(
                                player,
                                new LocalizationManager(MCHunt.getCurrentLocale())
                                        .getMessage(
                                                "arena.setup.boundary_set",
                                                "1",
                                                event.getClickedBlock().getLocation().getWorld().getName(),
                                                Double.toString(event.getClickedBlock().getLocation().getX()),
                                                Double.toString(event.getClickedBlock().getLocation().getY()),
                                                Double.toString(event.getClickedBlock().getLocation().getZ())
                                        )
                        );
                    }

                    // Right click = Boundary point 2
                    if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
                        arenaSetup.setLocationBoundaryPoint2(event.getClickedBlock().getLocation());
                        this.plugin.getArenaSetupManager().updateArenaSetup(arenaSetup);
                        sendPlayerAudibleMessage(
                                player,
                                new LocalizationManager(MCHunt.getCurrentLocale())
                                        .getMessage(
                                                "arena.setup.boundary_set",
                                                "2",
                                                event.getClickedBlock().getLocation().getWorld().getName(),
                                                Double.toString(event.getClickedBlock().getLocation().getX()),
                                                Double.toString(event.getClickedBlock().getLocation().getY()),
                                                Double.toString(event.getClickedBlock().getLocation().getZ())
                                        )
                        );
                    }

                /*
                  Check if the 2 boundary points are set and are in the same world.
                  If not, set them as null and warn player
                 */
                    if (arenaSetup.getLocationBoundaryPoint1() != null &&
                            arenaSetup.getLocationBoundaryPoint2() != null) {
                        if (!arenaSetup.getLocationBoundaryPoint1().getWorld().getName().equals(arenaSetup.getLocationBoundaryPoint2().getWorld().getName())) {
                            sendPlayerError(
                                    player,
                                    new LocalizationManager(MCHunt.getCurrentLocale())
                                            .getMessage(
                                                    "arena.setup.boundary_world_mismatch"
                                            )
                            );
                            arenaSetup.setLocationBoundaryPoint1(null);
                            arenaSetup.setLocationBoundaryPoint2(null);
                        } else {
                            boundaryUtil.drawArenaBoundary(
                                    player,
                                    arenaSetup
                            );
                        }
                    }

                    this.plugin.getArenaSetupManager().updateArenaSetup(arenaSetup);
                    event.setCancelled(true);
                    break;
                default:
                    break;
            }
        }
    }

    @EventHandler
    public void onPlayerJointEvent(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        ArenaSetup arenaSetup = this.plugin.getArenaSetupManager()
                .getArenaSetupByPlayerUuid(
                        plugin.getArenaSetupManager().getArenaSetups(),
                        player.getUniqueId()).orElse(null);

        if (arenaSetup == null) {
            return;
        }

        this.plugin.getEventBusManager().publishEvent(new ArenaSetupPlayerJoinedEvent(
                arenaSetup.getUUID(),
                player.getUniqueId()
        ));
    }

    @EventHandler
    public void onInventoryClickEvent(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) {
            return;
        }

        ArenaSetup arenaSetup = this.plugin.getArenaSetupManager()
                .getArenaSetupByPlayerUuid(
                        plugin.getArenaSetupManager().getArenaSetups(),
                        player.getUniqueId()).orElse(null);

        if (arenaSetup == null) {
            return;
        }

        String[] restrictClickItemActions = {
                plugin.getItemManager().itemArenaSetupToolSelection().getAction(),
                plugin.getItemManager().itemArenaSetupConfig().getAction(),
                plugin.getItemManager().itemArenaSetupActions().getAction(),
        };

        ItemStack item = event.getCurrentItem();

        if (item == null || item.getType().equals(Material.AIR)) {
            return;
        }

        String action = this.plugin.getItemManager().getItemAction(item);

        if (action != null && (Arrays.asList(restrictClickItemActions).contains(action))) {
            player.setItemOnCursor(null);

            player.updateInventory();
            event.setCancelled(true);

            player.closeInventory();

            sendPlayerError(
                    player,
                    new LocalizationManager(MCHunt.getCurrentLocale())
                            .getMessage(
                                    "player.setup.cannot_move_setup_items"
                            ));
        }
    }

    @EventHandler
    public void onBlockPlaceEvent(BlockPlaceEvent event) {
        Player player = event.getPlayer();

        ArenaSetup arenaSetup = this.plugin.getArenaSetupManager()
                .getArenaSetupByPlayerUuid(
                        plugin.getArenaSetupManager().getArenaSetups(),
                        player.getUniqueId()).orElse(null);

        if (arenaSetup == null) {
            return;
        }

        ItemStack itemInHand = player.getInventory().getItemInMainHand();
        String action = this.plugin.getItemManager().getItemAction(itemInHand);

        if (action != null) {
            BlockData blockData = event.getBlockPlaced().getBlockData().clone();

            switch (action) {
                case "mchunt.setup.arenaSign":
                    arenaSetup.appendArenaSign(event.getBlockPlaced().getLocation());
                    this.plugin.getArenaSetupManager().updateArenaSetup(arenaSetup);

                    this.plugin.getEventBusManager().publishEvent(new ArenaSetupUpdatedEvent(
                            arenaSetup.getUUID()
                    ));

                    sendPlayerAudibleMessage(
                            player,
                            new LocalizationManager(MCHunt.getCurrentLocale())
                                    .getMessage(
                                            "arena.setup.sign_set",
                                            event.getBlockPlaced().getLocation().getWorld().getName(),
                                            Double.toString(event.getBlockPlaced().getLocation().getX()),
                                            Double.toString(event.getBlockPlaced().getLocation().getY()),
                                            Double.toString(event.getBlockPlaced().getLocation().getZ())
                                    )
                    );
                    break;
                case "mchunt.setup.lobbySpawn":
                    if (arenaSetup.getLobbySpawn() != null) {
                        sendPlayerError(
                                player,
                                new LocalizationManager(MCHunt.getCurrentLocale())
                                        .getMessage(
                                                "arena.setup.lobby_spawn_already_set"
                                        )
                        );
                        event.setCancelled(true);
                        player.getInventory().remove(itemInHand);
                        return;
                    }

                    arenaSetup.setLobbySpawn(event.getBlockPlaced().getLocation());
                    this.plugin.getArenaSetupManager().updateArenaSetup(arenaSetup);

                    sendPlayerAudibleMessage(
                            player,
                            new LocalizationManager(MCHunt.getCurrentLocale())
                                    .getMessage(
                                            "arena.setup.lobby_spawn_set",
                                            event.getBlockPlaced().getLocation().getWorld().getName(),
                                            Double.toString(event.getBlockPlaced().getLocation().getX()),
                                            Double.toString(event.getBlockPlaced().getLocation().getY()),
                                            Double.toString(event.getBlockPlaced().getLocation().getZ())
                                    )
                    );

                    player.getInventory().remove(itemInHand);
                    event.setCancelled(true);
                    break;
                case "mchunt.setup.hiderSpawn":
                    arenaSetup.appendHiderSpawn(event.getBlockPlaced().getLocation());
                    this.plugin.getArenaSetupManager().updateArenaSetup(arenaSetup);

                    sendPlayerAudibleMessage(
                            player,
                            new LocalizationManager(MCHunt.getCurrentLocale())
                                    .getMessage(
                                            "arena.setup.hider_spawn_set",
                                            event.getBlockPlaced().getLocation().getWorld().getName(),
                                            Double.toString(event.getBlockPlaced().getLocation().getX()),
                                            Double.toString(event.getBlockPlaced().getLocation().getY()),
                                            Double.toString(event.getBlockPlaced().getLocation().getZ())
                                    )
                    );

                    event.setCancelled(true);
                    break;
                case "mchunt.setup.seekerSpawn":
                    arenaSetup.appendSeekerSpawn(event.getBlockPlaced().getLocation());
                    this.plugin.getArenaSetupManager().updateArenaSetup(arenaSetup);

                    sendPlayerAudibleMessage(
                            player,
                            new LocalizationManager(MCHunt.getCurrentLocale())
                                    .getMessage(
                                            "arena.setup.seeker_spawn_set",
                                            event.getBlockPlaced().getLocation().getWorld().getName(),
                                            Double.toString(event.getBlockPlaced().getLocation().getX()),
                                            Double.toString(event.getBlockPlaced().getLocation().getY()),
                                            Double.toString(event.getBlockPlaced().getLocation().getZ())
                                    )
                    );

                    event.setCancelled(true);
                    break;
                case "mchunt.setup.afterGameSpawn":
                    if (arenaSetup.getAfterGameSpawn() != null) {
                        sendPlayerError(
                                player,
                                new LocalizationManager(MCHunt.getCurrentLocale())
                                        .getMessage(
                                                "arena.setup.after_game_spawn_already_set"
                                        )
                        );
                        event.setCancelled(true);
                        player.getInventory().remove(itemInHand);
                        return;
                    }

                    arenaSetup.setAfterGameSpawn(event.getBlockPlaced().getLocation());
                    this.plugin.getArenaSetupManager().updateArenaSetup(arenaSetup);

                    sendPlayerAudibleMessage(
                            player,
                            new LocalizationManager(MCHunt.getCurrentLocale())
                                    .getMessage(
                                            "arena.setup.after_game_spawn_set",
                                            event.getBlockPlaced().getLocation().getWorld().getName(),
                                            Double.toString(event.getBlockPlaced().getLocation().getX()),
                                            Double.toString(event.getBlockPlaced().getLocation().getY()),
                                            Double.toString(event.getBlockPlaced().getLocation().getZ())
                                    )
                    );

                    player.getInventory().remove(itemInHand);
                    event.setCancelled(true);
                    break;
            }
        }
    }

    @EventHandler
    public void onArenaSetupSignBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();

        if (!event.getBlock().getType().equals(Material.OAK_SIGN) ||
            !event.getBlock().getType().equals(Material.OAK_WALL_SIGN)) {
            return;
        }

        for (ArenaSetup arenaSetup : this.plugin.getArenaSetupManager().getArenaSetups()) {
            if (arenaSetup.getArenaSigns() == null) {
                continue;
            }

            for (Location location : arenaSetup.getArenaSigns()) {
                if (!location.equals(event.getBlock().getLocation())) continue;
                if(arenaSetup.getPlayerUuid() == player.getUniqueId()) continue;

                sendPlayerError(
                        player,
                        new LocalizationManager(MCHunt.getCurrentLocale())
                                .getMessage(
                                        "arena.setup.cannot_break_signs"
                                )
                );

                event.setCancelled(true);
                return;
            }
        }
    }

    @EventHandler
    public void onBlockBreakEvent(BlockBreakEvent event) {
        Player player = event.getPlayer();

        ArenaSetup arenaSetup = this.plugin.getArenaSetupManager()
                .getArenaSetupByPlayerUuid(
                        plugin.getArenaSetupManager().getArenaSetups(),
                        player.getUniqueId()).orElse(null);

        if (arenaSetup == null) {
            return;
        }

        Location blockLocation = event.getBlock().getLocation();

        if (arenaSetup.getArenaSigns() != null) {
            for (Location location : arenaSetup.getArenaSigns()) {
                if (location.equals(blockLocation)) {
                    arenaSetup.removeArenaSign(location);
                    this.plugin.getArenaSetupManager().updateArenaSetup(arenaSetup);

                    sendPlayerAudibleMessage(
                            player,
                            new LocalizationManager(MCHunt.getCurrentLocale())
                                    .getMessage(
                                            "arena.setup.sign_removed",
                                            blockLocation.getWorld().getName(),
                                            Double.toString(blockLocation.getX()),
                                            Double.toString(blockLocation.getY()),
                                            Double.toString(blockLocation.getZ())
                                    )
                    );
                    break;
                }
            }
        }
    }

    @EventHandler
    public void onPlayerDropItemEvent(PlayerDropItemEvent event) {
        Player player = event.getPlayer();

        ArenaSetup arenaSetup = this.plugin.getArenaSetupManager()
                .getArenaSetupByPlayerUuid(
                        plugin.getArenaSetupManager().getArenaSetups(),
                        player.getUniqueId()).orElse(null);

        if (arenaSetup == null) {
            return;
        }

        Item itemDrop = event.getItemDrop();
        ItemStack itemStack = itemDrop.getItemStack();

        String action = this.plugin.getItemManager().getItemAction(itemStack);

        List<ItemBuilder> arenaItems = this.plugin.getItemManager().getHotbarArenaSetupItems();

        if (action != null) {
            for (ItemBuilder itemBuilder : arenaItems) {
                if (itemBuilder.getAction().equals(action)) {
                    event.setCancelled(true);

                    sendPlayerError(
                            player,
                            new LocalizationManager(MCHunt.getCurrentLocale())
                                    .getMessage(
                                            "player.setup.cannot_drop_setup_items"
                                    ));

                    return;
                }
            }
        }
    }
}
