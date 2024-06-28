package com.rexchoppers.mchunt.events;

import com.rexchoppers.mchunt.MCHunt;
import com.rexchoppers.mchunt.exceptions.ArenaSetupNotFoundException;
import com.rexchoppers.mchunt.managers.ArenaManager;
import com.rexchoppers.mchunt.managers.ArenaSetupManager;
import com.rexchoppers.mchunt.managers.LocalizationManager;
import com.rexchoppers.mchunt.menus.MenuArenaSetupBlockSelection;
import com.rexchoppers.mchunt.menus.MenuArenaSetupConfig;
import com.rexchoppers.mchunt.models.ArenaSetup;
import com.rexchoppers.mchunt.util.BoundaryUtil;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
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
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.UUID;

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

    String[] restrictDropItemActions = {
            "mchunt.setup.arenaName",
            "mchunt.setup.boundarySelection",
            "mchunt.setup.arenaSign",
            "mchunt.setup.cancelArenaSetup",
            "mchunt.setup.saveArenaSetup",
    };

    String[] restrictClickItemActions = {
            "mchunt.setup.arenaName",
            "mchunt.setup.boundarySelection",
            "mchunt.setup.arenaSign",
            "mchunt.setup.cancelArenaSetup",
            "mchunt.setup.saveArenaSetup",
    };

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) throws ArenaSetupNotFoundException {
        Player player = event.getPlayer();
        ItemStack itemInMainHand = player.getInventory().getItemInMainHand();

        if (isEmptyItem(itemInMainHand)) {
            return;
        }

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

        String action = this.plugin.getItemManager().getItemAction(itemInMainHand);

        if (action != null) {
            switch (action) {
                case "mchunt.setup.arenaConfig":
                    if (event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
                        new MenuArenaSetupConfig(this.plugin).getInventory().open(player);
                        event.setCancelled(true);
                    }
                    break;
                case "mchunt.setup.arenaName":
                    if (event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
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
                                    ArenaManager arenaManager = this.plugin.getArenaManager();
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
                                    this.plugin.getArenaSetupManager().updateArenaSetup(arenaSetup);

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
                                .plugin(this.plugin)
                                .open(player);
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

        ItemStack item = event.getCurrentItem();

        if (item == null || item.getType().equals(Material.AIR)) {
            return;
        }

        String action = this.plugin.getItemManager().getItemAction(item);

        if (action != null && Arrays.asList(this.restrictClickItemActions).contains(action)) {
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
            switch (action) {
                case "mchunt.setup.arenaSign":
                    arenaSetup.appendArenaSign(event.getBlockPlaced().getLocation());
                    this.plugin.getArenaSetupManager().updateArenaSetup(arenaSetup);

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

        if (action != null && Arrays.asList(this.restrictDropItemActions).contains(action)) {
            event.setCancelled(true);
            sendPlayerError(
                    player,
                    new LocalizationManager(MCHunt.getCurrentLocale())
                            .getMessage(
                                    "player.setup.cannot_drop_setup_items"
                            ));
        }
    }
}
