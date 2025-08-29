package com.rexchoppers.mchunt.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import com.rexchoppers.mchunt.MCHunt;
import com.rexchoppers.mchunt.menus.MenuInGame;
import com.rexchoppers.mchunt.menus.MenuMain;
import org.bukkit.entity.Player;

@CommandAlias("mchunt|mch")
public class CommandMCHunt extends BaseCommand {
    private final MCHunt plugin;

    public CommandMCHunt(MCHunt plugin) {
        this.plugin = plugin;
    }

    @Default()
    @Description("Opens the MCHunt GUI")
    public void defaultCommand(Player player, String[] args) {
        // Check if the player is in an arena. If so, open the in-game menu instead.
        if (plugin.getArenaManager().getArenaPlayerIsIn(player.getUniqueId()).isPresent()) {
            (new MenuInGame(this.plugin)).getInventory().open(player);
            return;
        }

        // Open the main menu
        (new MenuMain(plugin)).open(player);
    }
}
