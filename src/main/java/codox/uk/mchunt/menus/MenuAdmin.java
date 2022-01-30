package codox.uk.mchunt.menus;

import codox.uk.mchunt.MCHunt;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import org.bukkit.entity.Player;

public class MenuAdmin extends BaseMenu {
    public MenuAdmin() {
        this.setInventory(
                SmartInventory.builder()
                        .id("mchuntMenuAdmin")
                        .manager(MCHunt.getInventoryManagerInstance())
                        .provider(new MenuAdmin.MenuAdminProvider())
                        .size(3, 9)
                        .title("MCHunt - Admin Menu")
                        .closeable(true)
                        .build()
        );
    }

    private class MenuAdminProvider implements InventoryProvider {
        @Override
        public void init(Player player, InventoryContents contents) {

        }

        @Override
        public void update(Player player, InventoryContents contents) {
        }
    }
}
