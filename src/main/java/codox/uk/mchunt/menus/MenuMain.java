package codox.uk.mchunt.menus;

import codox.uk.mchunt.MCHunt;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import org.bukkit.entity.Player;

public class MenuMain {
    private SmartInventory inventory;

    public MenuMain() {
        this.inventory = SmartInventory.builder()
                .id("mchuntMain")
                .manager(MCHunt.getInventoryManagerInstance())
                .provider(new MenuMainProvider())
                .size(3, 9)
                .title("MCHunt Menu")
                .closeable(true)
                .build();
    }

    private class MenuMainProvider implements InventoryProvider {
        @Override
        public void init(Player player, InventoryContents contents) {

        }

        @Override
        public void update(Player player, InventoryContents contents) {
        }
    }
}
