package com.rexchoppers.mchunt.menus;

import com.rexchoppers.mchunt.MCHunt;
import com.rexchoppers.mchunt.events.internal.ArenaCreatedEvent;
import com.rexchoppers.mchunt.events.internal.ArenaSetupDiscardedEvent;
import com.rexchoppers.mchunt.events.internal.ArenaSetupPlayerJoinedEvent;
import com.rexchoppers.mchunt.events.internal.ArenaSetupUpdatedEvent;
import com.rexchoppers.mchunt.managers.ArenaManager;
import com.rexchoppers.mchunt.managers.LocalizationManager;
import com.rexchoppers.mchunt.models.Arena;
import com.rexchoppers.mchunt.models.ArenaSetup;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
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

            inventoryContents.set(0, 7, ClickableItem.of(plugin.getItemManager().itemArenaSetupDiscardChanges().build(), e -> {
                plugin.getArenaSetupManager().removeArenaSetup(arenaSetup.getUUID());
                plugin.getEventBusManager().publishEvent(new ArenaSetupDiscardedEvent(arenaSetup));

                player.closeInventory();

                sendPlayerAudibleMessage(
                        player,
                        new LocalizationManager(MCHunt.getCurrentLocale())
                                .getMessage(
                                        "arena.setup.discarded"
                                )
                );
            }));

            inventoryContents.set(0, 8, ClickableItem.of(plugin.getItemManager().itemArenaSetupSave().build(), e -> {
                // Validate input before creation

                // Area Signs
                if (arenaSetup.getArenaSigns().length < 1) {
                    sendPlayerError(
                            player,
                            new LocalizationManager(MCHunt.getCurrentLocale())
                                    .getMessage(
                                            "arena.setup.signs_not_enough"
                                    )
                    );
                    return;
                }

                // Arena Blocks
                if (arenaSetup.getArenaBlocks().length < 1) {
                    sendPlayerError(
                            player,
                            new LocalizationManager(MCHunt.getCurrentLocale())
                                    .getMessage(
                                            "arena.setup.blocks_not_enough"
                                    )
                    );
                    return;
                }

                // Lobby spawn
                if (arenaSetup.getLobbySpawn() == null) {
                    sendPlayerError(
                            player,
                            new LocalizationManager(MCHunt.getCurrentLocale())
                                    .getMessage(
                                            "arena.setup.lobby_spawn_not_set"
                                    )
                    );
                    return;
                }

                // After game spawn
                if (arenaSetup.getAfterGameSpawn() == null) {
                    sendPlayerError(
                            player,
                            new LocalizationManager(MCHunt.getCurrentLocale())
                                    .getMessage(
                                            "arena.setup.after_game_spawn_not_set"
                                    )
                    );
                    return;
                }

                // Hider spawns
                if (arenaSetup.getHiderSpawns().length < 1) {
                    sendPlayerError(
                            player,
                            new LocalizationManager(MCHunt.getCurrentLocale())
                                    .getMessage(
                                            "arena.setup.hider_spawns_not_enough"
                                    )
                    );
                    return;
                }

                // Seeker spawns
                if (arenaSetup.getSeekerSpawns().length < 1) {
                    sendPlayerError(
                            player,
                            new LocalizationManager(MCHunt.getCurrentLocale())
                                    .getMessage(
                                            "arena.setup.seeker_spawns_not_enough"
                                    )
                    );
                    return;
                }

                // Boundary point 1
                if (arenaSetup.getLocationBoundaryPoint1() == null) {
                    sendPlayerError(
                            player,
                            new LocalizationManager(MCHunt.getCurrentLocale())
                                    .getMessage(
                                            "arena.setup.boundary_point_1_not_set"
                                    )
                    );
                    return;
                }

                // Boundary point 2
                if (arenaSetup.getLocationBoundaryPoint2() == null) {
                    sendPlayerError(
                            player,
                            new LocalizationManager(MCHunt.getCurrentLocale())
                                    .getMessage(
                                            "arena.setup.boundary_point_2_not_set"
                                    )
                    );
                    return;
                }

                // Check if WorldGuard region can be setup
                BlockVector3 min = BlockVector3.at(
                        arenaSetup.getLocationBoundaryPoint1().getX(),
                        arenaSetup.getLocationBoundaryPoint1().getY(),
                        arenaSetup.getLocationBoundaryPoint1().getZ()
                );

                BlockVector3 max = BlockVector3.at(
                        arenaSetup.getLocationBoundaryPoint2().getX(),
                        arenaSetup.getLocationBoundaryPoint2().getY(),
                        arenaSetup.getLocationBoundaryPoint2().getZ()
                );

                String regionId = "mchunt_" + arenaSetup.getUUID().toString();

                ProtectedCuboidRegion region = new ProtectedCuboidRegion(regionId, min, max);

                RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
                RegionManager regions = container.get(BukkitAdapter.adapt(arenaSetup.getLocationBoundaryPoint1().getWorld()));

                if (regions == null) {
                    sendPlayerError(
                            player,
                            new LocalizationManager(MCHunt.getCurrentLocale())
                                    .getMessage(
                                            "arena.setup.worldguard_region_manager_not_found"
                                    )
                    );
                    return;
                }

                // Check if the region intersects with any existing regions
                ApplicableRegionSet set = regions.getApplicableRegions(region);

                // Loop through all regions and check if the region intersects with any of them
                if(!set.getRegions().isEmpty()) {
                    for(ProtectedRegion protectedRegion : set.getRegions()) {
                        if(protectedRegion.getId().contains("mchunt_")) {
                            sendPlayerError(
                                    player,
                                    new LocalizationManager(MCHunt.getCurrentLocale())
                                            .getMessage(
                                                    "arena.setup.selection_intersects_with_existing_arena_region"
                                            )
                            );
                            return;
                        }
                    }
                }

                regions.addRegion(region);

                // Save the arena setup
                Arena arena = new Arena(
                        arenaSetup.getUUID(),
                        arenaSetup.getArenaName(),
                        regionId,
                        player.getUniqueId(),
                        arenaSetup.getArenaSigns(),
                        arenaSetup.getArenaBlocks(),
                        arenaSetup.getLobbySpawn(),
                        arenaSetup.getHiderSpawns(),
                        arenaSetup.getSeekerSpawns(),
                        arenaSetup.getAfterGameSpawn(),
                        arenaSetup.getMinimumPlayers(),
                        arenaSetup.getMaximumPlayers(),
                        arenaSetup.getSeekerCount(),
                        arenaSetup.getCountdownBeforeStart(),
                        arenaSetup.getCountdownAfterEnd(),
                        arenaSetup.getRespawnDelay(),
                        arenaSetup.getLocationBoundaryPoint1(),
                        arenaSetup.getLocationBoundaryPoint2()
                );

                plugin.getArenaManager().createArena(arena);

                plugin.getArenaSetupManager().removeArenaSetup(arenaSetup.getUUID());
                plugin.getEventBusManager().publishEvent(new ArenaCreatedEvent(
                        arenaSetup,
                        arena
                ));

                sendPlayerAudibleMessage(
                        player,
                        new LocalizationManager(MCHunt.getCurrentLocale())
                                .getMessage(
                                        "arena.setup.created",
                                        arena.getName()
                                )

                );

                player.closeInventory();
            }));
        }

        @Override
        public void update(Player player, InventoryContents inventoryContents) {

        }
    }
}