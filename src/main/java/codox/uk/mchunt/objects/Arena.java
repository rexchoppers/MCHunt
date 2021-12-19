package codox.uk.mchunt.objects;

import codox.uk.mchunt.enums.ArenaStatusEnum;
import codox.uk.mchunt.enums.PlayerRoleEnum;
import com.google.gson.annotations.Expose;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class Arena {
    @Expose()
    private String id;

    @Expose()
    private String name;

    @Expose()
    private ArenaStatusEnum status;

    // Cannot be less than 2 or more than the max
    @Expose()
    private int minimumPlayers;

    // Cannot be less than min
    @Expose()
    private int maximumPlayers;

    @Expose()
    private Location seekerSpawnLocation;

    @Expose()
    private Location hiderSpawnLocation;

    @Expose()
    private ArrayList<Location> signLocations;

    @Expose()
    private Location boundryPosition1;

    @Expose()
    private Location boundryPosition2;

    private HashMap<UUID, PlayerRoleEnum> players;

    public Arena() {}


}
