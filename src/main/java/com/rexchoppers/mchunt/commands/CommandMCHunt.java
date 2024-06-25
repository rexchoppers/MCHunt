package com.rexchoppers.mchunt.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import com.rexchoppers.mchunt.MCHunt;
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
        // Open GUI
    }
}
