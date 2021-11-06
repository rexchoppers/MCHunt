package codox.uk.mchunt.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import codox.uk.mchunt.menus.MenuMain;
import org.bukkit.entity.Player;

import java.sql.SQLException;

@CommandAlias("mchunt|mch")
public class CommandMCHunt extends BaseCommand {
    @Default()
    @Description("Opens the MCHunt GUI")
    public static void defaultCommand(Player player, String[] args) {
        // TODO
            // Check if the player is in a game
            // Check if the player is in the lobby

        (new MenuMain()).open(player);
    }
}
