package codox.uk.mchunt.menus;

import codox.uk.mchunt.MCHunt;
import codox.uk.mchunt.MCHuntItems;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class MenuAdmin extends BaseMenu {
    public MenuAdmin() {
        this.setInventory(
                SmartInventory.builder()
                        .id("mchuntMenuAdmin")
                        .manager(MCHunt.getInventoryManagerInstance())
                        .provider(new MenuAdmin.MenuAdminProvider())
                        .size(5, 9)
                        .title("MCHunt - Admin Menu")
                        .closeable(true)
                        .build()
        );
    }

    private class MenuAdminProvider implements InventoryProvider {
        @Override
        public void init(Player player, InventoryContents contents) {
            contents.fillBorders(ClickableItem.empty(new ItemStack(Material.BLACK_STAINED_GLASS_PANE)));

            contents.set(1, 2, ClickableItem.of(MCHuntItems.ITEM_ADMIN_MENU_ENTER_ARENA_SETUP_MODE.build(), e -> {
                
            }));

            contents.set(3, 1, ClickableItem.of(MCHuntItems.ITEM_MENU_BACK.build(), e -> {
                close(player);
                (new MenuMain()).open(player);
            }));
        }

        @Override
        public void update(Player player, InventoryContents contents) {
        }
    }
}
