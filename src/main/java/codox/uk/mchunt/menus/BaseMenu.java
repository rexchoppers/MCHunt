package codox.uk.mchunt.menus;

import fr.minuskube.inv.SmartInventory;
import org.bukkit.entity.Player;

public class BaseMenu {

    private SmartInventory inventory;

    public void open(Player player) {
        this.inventory.open(player);
    }

    public void setInventory(SmartInventory inventory) {
        this.inventory = inventory;
    }

    public SmartInventory getInventory() {
        return this.inventory;
    }
}
