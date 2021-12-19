package codox.uk.mchunt.menus;

import codox.uk.mchunt.MCHunt;
import codox.uk.mchunt.MCHuntItems;
import codox.uk.mchunt.enums.Permissions;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class MenuMain extends BaseMenu {
    public MenuMain() {
        this.setInventory(
                SmartInventory.builder()
                        .id("mchuntMain")
                        .manager(MCHunt.getInventoryManagerInstance())
                        .provider(new MenuMainProvider())
                        .size(3, 9)
                        .title("MCHunt Menu")
                        .closeable(true)
                        .build()
        );
    }

    private class MenuMainProvider implements InventoryProvider {
        @Override
        public void init(Player player, InventoryContents contents) {
            contents.fillBorders(ClickableItem.empty(new ItemStack(Material.BLACK_STAINED_GLASS_PANE)));

            if (player.hasPermission(MCHuntItems.ITEM_MENU_ADMIN_SECTION.getPermission())) {
                contents.set(1, 7, ClickableItem.of(MCHuntItems.ITEM_MENU_ADMIN_SECTION.build(), e -> {
                    
                }));
            }
        }

        @Override
        public void update(Player player, InventoryContents contents) {
        }
    }
}
