package codox.uk.mchunt.objects;

import org.bukkit.Location;

import java.util.ArrayList;
import java.util.UUID;

public class Arena {

    private String id;
    private String name;
    private ArrayList<Location> signLocations;

    public Arena() {
        this.id = UUID.randomUUID().toString();
    }
}
