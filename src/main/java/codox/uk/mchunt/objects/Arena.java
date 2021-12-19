package codox.uk.mchunt.objects;

import codox.uk.mchunt.enums.ArenaStatusEnum;
import codox.uk.mchunt.enums.PlayerRoleEnum;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class Arena {

    private String id;
    private String name;
    private ArenaStatusEnum status;

    // Cannot be less than 2 or more than the max
    private int minimumPlayers;

    // Cannot be less than min
    private int maximumPlayers;

    private Location seekerSpawnLocation;
    private Location hiderSpawnLocation;
    private ArrayList<Location> signLocations;

    private Location boundryPosition1;
    private Location boundryPosition2;

    private HashMap<UUID, PlayerRoleEnum> players;

    public Arena() {}

    
}
