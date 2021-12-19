package codox.uk.mchunt;

import codox.uk.mchunt.enums.Permissions;
import codox.uk.mchunt.items.ItemBuilder;
import org.bukkit.Material;

import java.util.ArrayList;

public class MCHuntItems {
    public static final ItemBuilder ITEM_MENU_ADMIN_SECTION = new ItemBuilder()
            .setMaterial(Material.CRAFTING_TABLE)
            .setAmount(1)
            .setName("%B&6Enter Admin Menu")
            .setPermission(Permissions.PERMISSION_ADMIN.toString())
            .setLores(new ArrayList<String>() {{
                add("");
                add("%b%tClick to enter the admin menu");
            }});
}
