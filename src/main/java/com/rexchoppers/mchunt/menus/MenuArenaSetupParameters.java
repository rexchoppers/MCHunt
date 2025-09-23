package com.rexchoppers.mchunt.menus;

import com.rexchoppers.mchunt.MCHunt;
import com.rexchoppers.mchunt.events.internal.ArenaSetupUpdatedEvent;
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
                        .size(1, 9)
                        .title("MCHunt - Arena Parameters")
                        .closeable(true)
                        .build()
        );
    }

    private class MenuArenaSetupParametersProvider implements InventoryProvider {
        @Override
        public void init(Player player, InventoryContents inventoryContents) {
            ArenaSetup arenaSetup = plugin.getArenaSetupManager()
                    .getArenaSetupForPlayer(player.getUniqueId()).orElse(null);

            inventoryContents.set(0, 0, ClickableItem.of(plugin.getItemManager().itemArenaSetupParametersMinimumPlayers().build(), e -> {
                new AnvilGUI.Builder()
                        .onClick((slot, stateSnapshot) -> {
                            if (slot != AnvilGUI.Slot.OUTPUT) {
                                return Collections.emptyList();
                            }

                            try {
                                int minimumPlayers = Integer.parseInt(stateSnapshot.getText());
                                if (minimumPlayers < 2) {
                                    sendPlayerError(
                                            player,
                                            MCHunt.getLocalization()
                                                    .getMessage(
                                                            "arena.setup.minimum_players_not_less_than_two"
                                                    )
                                    );

                                    return Collections.emptyList();
                                }

                                if (minimumPlayers > arenaSetup.getMaximumPlayers()) {
                                    sendPlayerError(
                                            player,
                                            MCHunt.getLocalization()
                                                    .getMessage(
                                                            "arena.setup.minimum_players_not_greater_than_maximum_players"
                                                    )
                                    );

                                    return Collections.emptyList();
                                }

                                arenaSetup.setMinimumPlayers(minimumPlayers);
                                plugin.getArenaSetupManager().update(arenaSetup);
                                plugin.getEventBusManager().publishEvent(new ArenaSetupUpdatedEvent(
                                        arenaSetup.getUUID()
                                ));

                                sendPlayerAudibleMessage(
                                        player,
                                        MCHunt.getLocalization()
                                                .getMessage(
                                                        "arena.setup.value_set", Integer.toString(minimumPlayers)
                                                )
                                );
                            } catch (Exception exception) {
                                sendPlayerError(
                                        player,
                                        MCHunt.getLocalization()
                                                .getMessage(
                                                        "arena.setup.not_a_number"
                                                )
                                );
                                return Arrays.asList(AnvilGUI.ResponseAction.replaceInputText(Integer.toString(arenaSetup.getMinimumPlayers())));
                            }

                            return Arrays.asList(
                                    AnvilGUI.ResponseAction.close(),
                                    AnvilGUI.ResponseAction.run(() -> {
                                        getInventory().open(player);
                                    })
                            );
                        })
                        .text(Integer.toString(arenaSetup.getMinimumPlayers()))
                        .title("Set Min Players")
                        .plugin(plugin)
                        .open(player);
            }));

            inventoryContents.set(0, 1, ClickableItem.of(plugin.getItemManager().itemArenaSetupParametersMaximumPlayers().build(), e -> {
                new AnvilGUI.Builder()
                        .onClick((slot, stateSnapshot) -> {
                            if (slot != AnvilGUI.Slot.OUTPUT) {
                                return Collections.emptyList();
                            }

                            try {
                                int maximumPlayers = Integer.parseInt(stateSnapshot.getText());

                                if (maximumPlayers < 2) {
                                    sendPlayerError(
                                            player,
                                            MCHunt.getLocalization()
                                                    .getMessage(
                                                            "arena.setup.maximum_players_not_less_than_two"
                                                    )
                                    );

                                    return Collections.emptyList();
                                }

                                arenaSetup.setMaximumPlayers(maximumPlayers);
                                plugin.getArenaSetupManager().update(arenaSetup);
                                plugin.getEventBusManager().publishEvent(new ArenaSetupUpdatedEvent(
                                        arenaSetup.getUUID()
                                ));

                                sendPlayerAudibleMessage(
                                        player,
                                        MCHunt.getLocalization()
                                                .getMessage(
                                                        "arena.setup.value_set", Integer.toString(maximumPlayers)
                                                )
                                );
                            } catch (Exception exception) {
                                sendPlayerError(
                                        player,
                                        MCHunt.getLocalization()
                                                .getMessage(
                                                        "arena.setup.not_a_number"
                                                )
                                );

                                return Arrays.asList(AnvilGUI.ResponseAction.replaceInputText(Integer.toString(arenaSetup.getMaximumPlayers())));
                            }

                            return Arrays.asList(
                                    AnvilGUI.ResponseAction.close(),
                                    AnvilGUI.ResponseAction.run(() -> {
                                        getInventory().open(player);
                                    })
                            );
                        })
                        .text(Integer.toString(arenaSetup.getMaximumPlayers()))
                        .title("Set Max Players")
                        .plugin(plugin)
                        .open(player);
            }));

            inventoryContents.set(0, 2, ClickableItem.of(plugin.getItemManager().itemArenaSetupParametersSeekerCount().build(), e -> {
                new AnvilGUI.Builder()
                        .onClick((slot, stateSnapshot) -> {
                            if (slot != AnvilGUI.Slot.OUTPUT) {
                                return Collections.emptyList();
                            }

                            try {
                                int seekerCount = Integer.parseInt(stateSnapshot.getText());

                                if (seekerCount < 1) {
                                    sendPlayerError(
                                            player,
                                            MCHunt.getLocalization()
                                                    .getMessage(
                                                            "arena.setup.seeker_count_not_less_than_one"
                                                    )
                                    );

                                    return Collections.emptyList();
                                }

                                arenaSetup.setSeekerCount(seekerCount);
                                plugin.getArenaSetupManager().update(arenaSetup);
                                plugin.getEventBusManager().publishEvent(new ArenaSetupUpdatedEvent(
                                        arenaSetup.getUUID()
                                ));

                                sendPlayerAudibleMessage(
                                        player,
                                        MCHunt.getLocalization()
                                                .getMessage(
                                                        "arena.setup.value_set", Integer.toString(seekerCount)
                                                )
                                );
                            } catch (Exception exception) {
                                sendPlayerError(
                                        player,
                                        MCHunt.getLocalization()
                                                .getMessage(
                                                        "arena.setup.not_a_number"
                                                )
                                );

                                return Arrays.asList(AnvilGUI.ResponseAction.replaceInputText(Integer.toString(arenaSetup.getSeekerCount())));
                            }

                            return Arrays.asList(
                                    AnvilGUI.ResponseAction.close(),
                                    AnvilGUI.ResponseAction.run(() -> {
                                        getInventory().open(player);
                                    })
                            );
                        })
                        .text(Integer.toString(arenaSetup.getSeekerCount()))
                        .title("Set Seeker Count")
                        .plugin(plugin)
                        .open(player);
            }));

            inventoryContents.set(0, 3, ClickableItem.of(plugin.getItemManager().itemArenaSetupParametersCountdownBeforeGameStart().build(), e -> {
                new AnvilGUI.Builder()
                        .onClick((slot, stateSnapshot) -> {
                            if (slot != AnvilGUI.Slot.OUTPUT) {
                                return Collections.emptyList();
                            }

                            try {
                                int countdownBeforeStart = Integer.parseInt(stateSnapshot.getText());

                                if (countdownBeforeStart < 1) {
                                    sendPlayerError(
                                            player,
                                            MCHunt.getLocalization()
                                                    .getMessage(
                                                            "arena.setup.countdown_before_start_not_less_than_one"
                                                    )
                                    );

                                    return Collections.emptyList();
                                }


                                arenaSetup.setCountdownBeforeStart(countdownBeforeStart);
                                plugin.getArenaSetupManager().update(arenaSetup);
                                plugin.getEventBusManager().publishEvent(new ArenaSetupUpdatedEvent(
                                        arenaSetup.getUUID()
                                ));

                                sendPlayerAudibleMessage(
                                        player,
                                        MCHunt.getLocalization()
                                                .getMessage(
                                                        "arena.setup.value_set", Integer.toString(countdownBeforeStart)
                                                )
                                );
                            } catch (Exception exception) {
                                sendPlayerError(
                                        player,
                                        MCHunt.getLocalization()
                                                .getMessage(
                                                        "arena.setup.not_a_number"
                                                )
                                );

                                return Arrays.asList(AnvilGUI.ResponseAction.replaceInputText(Integer.toString(arenaSetup.getCountdownBeforeStart())));
                            }

                            return Arrays.asList(
                                    AnvilGUI.ResponseAction.close(),
                                    AnvilGUI.ResponseAction.run(() -> {
                                        getInventory().open(player);
                                    })
                            );
                        })
                        .text(Integer.toString(arenaSetup.getCountdownBeforeStart()))
                        .title("Set Countdown Before Start")
                        .plugin(plugin)
                        .open(player);
            }));

            inventoryContents.set(0, 4, ClickableItem.of(plugin.getItemManager().itemArenaSetupParametersCountdownAfterGameEnd().build(), e -> {
                new AnvilGUI.Builder()
                        .onClick((slot, stateSnapshot) -> {
                            if (slot != AnvilGUI.Slot.OUTPUT) {
                                return Collections.emptyList();
                            }

                            try {
                                int countdownAfterGameEnd = Integer.parseInt(stateSnapshot.getText());

                                if (countdownAfterGameEnd < 1) {
                                    sendPlayerError(
                                            player,
                                            MCHunt.getLocalization()
                                                    .getMessage(
                                                            "arena.setup.countdown_after_game_end_not_less_than_one"
                                                    )
                                    );

                                    return Collections.emptyList();
                                }


                                arenaSetup.setCountdownAfterEnd(countdownAfterGameEnd);
                                plugin.getArenaSetupManager().update(arenaSetup);
                                plugin.getEventBusManager().publishEvent(new ArenaSetupUpdatedEvent(
                                        arenaSetup.getUUID()
                                ));

                                sendPlayerAudibleMessage(
                                        player,
                                        MCHunt.getLocalization()
                                                .getMessage(
                                                        "arena.setup.value_set", Integer.toString(countdownAfterGameEnd)
                                                )
                                );
                            } catch (Exception exception) {
                                sendPlayerError(
                                        player,
                                        MCHunt.getLocalization()
                                                .getMessage(
                                                        "arena.setup.not_a_number"
                                                )
                                );

                                return Arrays.asList(AnvilGUI.ResponseAction.replaceInputText(Integer.toString(arenaSetup.getCountdownAfterEnd())));
                            }

                            return Arrays.asList(
                                    AnvilGUI.ResponseAction.close(),
                                    AnvilGUI.ResponseAction.run(() -> {
                                        getInventory().open(player);
                                    })
                            );
                        })
                        .text(Integer.toString(arenaSetup.getCountdownAfterEnd()))
                        .title("Set Countdown After End")
                        .plugin(plugin)
                        .open(player);
            }));

            inventoryContents.set(0, 5, ClickableItem.of(plugin.getItemManager().itemArenaSetupParametersRespawnDelay().build(), e -> {
                new AnvilGUI.Builder()
                        .onClick((slot, stateSnapshot) -> {
                            if (slot != AnvilGUI.Slot.OUTPUT) {
                                return Collections.emptyList();
                            }

                            try {
                                int respawnDelay = Integer.parseInt(stateSnapshot.getText());

                                if (respawnDelay < 1) {
                                    sendPlayerError(
                                            player,
                                            MCHunt.getLocalization()
                                                    .getMessage(
                                                            "arena.setup.respawn_delay_not_less_than_one"
                                                    )
                                    );

                                    return Collections.emptyList();
                                }


                                arenaSetup.setRespawnDelay(respawnDelay);
                                plugin.getArenaSetupManager().update(arenaSetup);
                                plugin.getEventBusManager().publishEvent(new ArenaSetupUpdatedEvent(
                                        arenaSetup.getUUID()
                                ));

                                sendPlayerAudibleMessage(
                                        player,
                                        MCHunt.getLocalization()
                                                .getMessage(
                                                        "arena.setup.value_set", Integer.toString(respawnDelay)
                                                )
                                );
                            } catch (Exception exception) {
                                sendPlayerError(
                                        player,
                                        MCHunt.getLocalization()
                                                .getMessage(
                                                        "arena.setup.not_a_number"
                                                )
                                );

                                return Arrays.asList(AnvilGUI.ResponseAction.replaceInputText(Integer.toString(arenaSetup.getRespawnDelay())));
                            }

                            return Arrays.asList(
                                    AnvilGUI.ResponseAction.close(),
                                    AnvilGUI.ResponseAction.run(() -> {
                                        getInventory().open(player);
                                    })
                            );
                        })
                        .text(Integer.toString(arenaSetup.getRespawnDelay()))
                        .title("Set Respawn Delay")
                        .plugin(plugin)
                        .open(player);
            }));

            inventoryContents.set(0, 6, ClickableItem.of(plugin.getItemManager().itemArenaSetupParametersSeekerReleaseDelay().build(), e -> {
                new AnvilGUI.Builder()
                        .onClick((slot, stateSnapshot) -> {
                            if (slot != AnvilGUI.Slot.OUTPUT) {
                                return Collections.emptyList();
                            }

                            try {
                                int seekerReleaseDelay = Integer.parseInt(stateSnapshot.getText());

                                if (seekerReleaseDelay < 1) {
                                    sendPlayerError(
                                            player,
                                            MCHunt.getLocalization()
                                                    .getMessage(
                                                            "arena.setup.seeker_release_delay_not_less_than_one"
                                                    )
                                    );

                                    return Collections.emptyList();
                                }


                                arenaSetup.setSeekerReleaseDelay(seekerReleaseDelay);
                                plugin.getArenaSetupManager().update(arenaSetup);
                                plugin.getEventBusManager().publishEvent(new ArenaSetupUpdatedEvent(
                                        arenaSetup.getUUID()
                                ));

                                sendPlayerAudibleMessage(
                                        player,
                                        MCHunt.getLocalization()
                                                .getMessage(
                                                        "arena.setup.value_set", Integer.toString(seekerReleaseDelay)
                                                )
                                );
                            } catch (Exception exception) {
                                sendPlayerError(
                                        player,
                                        MCHunt.getLocalization()
                                                .getMessage(
                                                        "arena.setup.not_a_number"
                                                )
                                );

                                return Arrays.asList(AnvilGUI.ResponseAction.replaceInputText(Integer.toString(arenaSetup.getSeekerReleaseDelay())));
                            }

                            return Arrays.asList(
                                    AnvilGUI.ResponseAction.close(),
                                    AnvilGUI.ResponseAction.run(() -> {
                                        getInventory().open(player);
                                    })
                            );
                        })
                        .text(Integer.toString(arenaSetup.getSeekerReleaseDelay()))
                        .title("Set Seeker Release Delay")
                        .plugin(plugin)
                        .open(player);
            }));

            inventoryContents.set(0, 7, ClickableItem.of(plugin.getItemManager().itemArenaSetupParametersHiderStillTime().build(), e -> {
                new AnvilGUI.Builder()
                        .onClick((slot, stateSnapshot) -> {
                            if (slot != AnvilGUI.Slot.OUTPUT) {
                                return Collections.emptyList();
                            }

                            try {
                                int hiderStillTime = Integer.parseInt(stateSnapshot.getText());

                                if (hiderStillTime < 1) {
                                    sendPlayerError(
                                            player,
                                            MCHunt.getLocalization()
                                                    .getMessage(
                                                            "arena.setup.hider_still_time_not_less_than_one"
                                                    )
                                    );

                                    return Collections.emptyList();
                                }


                                arenaSetup.setHiderStillTime(hiderStillTime);
                                plugin.getArenaSetupManager().update(arenaSetup);
                                plugin.getEventBusManager().publishEvent(new ArenaSetupUpdatedEvent(
                                        arenaSetup.getUUID()
                                ));

                                sendPlayerAudibleMessage(
                                        player,
                                        MCHunt.getLocalization()
                                                .getMessage(
                                                        "arena.setup.value_set", Integer.toString(hiderStillTime)
                                                )
                                );
                            } catch (Exception exception) {
                                sendPlayerError(
                                        player,
                                        MCHunt.getLocalization()
                                                .getMessage(
                                                        "arena.setup.not_a_number"
                                                )
                                );

                                return Arrays.asList(AnvilGUI.ResponseAction.replaceInputText(Integer.toString(arenaSetup.getHiderStillTime())));
                            }

                            return Arrays.asList(
                                    AnvilGUI.ResponseAction.close(),
                                    AnvilGUI.ResponseAction.run(() -> {
                                        getInventory().open(player);
                                    })
                            );
                        })
                        .text(Integer.toString(arenaSetup.getHiderStillTime()))
                        .title("Set Hider Still Time (Seconds)")
                        .plugin(plugin)
                        .open(player);
            }));

            inventoryContents.set(0, 8, ClickableItem.of(plugin.getItemManager().itemArenaSetupParametersGameLength().build(), e -> {
                new AnvilGUI.Builder()
                        .onClick((slot, stateSnapshot) -> {
                            if (slot != AnvilGUI.Slot.OUTPUT) {
                                return Collections.emptyList();
                            }

                            try {
                                int gameLength = Integer.parseInt(stateSnapshot.getText());

                                if (gameLength < 1) {
                                    sendPlayerError(
                                            player,
                                            MCHunt.getLocalization()
                                                    .getMessage(
                                                            "arena.setup_game_length_not_less_than_one"
                                                    )
                                    );

                                    return Collections.emptyList();
                                }


                                arenaSetup.setGameLength(gameLength);
                                plugin.getArenaSetupManager().update(arenaSetup);
                                plugin.getEventBusManager().publishEvent(new ArenaSetupUpdatedEvent(
                                        arenaSetup.getUUID()
                                ));

                                sendPlayerAudibleMessage(
                                        player,
                                        MCHunt.getLocalization()
                                                .getMessage(
                                                        "arena.setup.value_set", Integer.toString(gameLength)
                                                )
                                );
                            } catch (Exception exception) {
                                sendPlayerError(
                                        player,
                                        MCHunt.getLocalization()
                                                .getMessage(
                                                        "arena.setup.not_a_number"
                                                )
                                );

                                return Arrays.asList(AnvilGUI.ResponseAction.replaceInputText(Integer.toString(arenaSetup.getGameLength())));
                            }

                            return Arrays.asList(
                                    AnvilGUI.ResponseAction.close(),
                                    AnvilGUI.ResponseAction.run(() -> {
                                        getInventory().open(player);
                                    })
                            );
                        })
                        .text(Integer.toString(arenaSetup.getGameLength()))
                        .title("Set Game Length (Seconds)")
                        .plugin(plugin)
                        .open(player);
            }));
        }

        @Override
        public void update(Player player, InventoryContents inventoryContents) {

        }
    }
}