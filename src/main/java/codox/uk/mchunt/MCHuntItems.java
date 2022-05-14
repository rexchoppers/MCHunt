package codox.uk.mchunt;

import codox.uk.mchunt.enums.Permissions;
import codox.uk.mchunt.items.ItemBuilder;
import codox.uk.mchunt.util.Format;
import org.bukkit.Material;

import java.util.ArrayList;

public class MCHuntItems {
    public static final ItemBuilder ITEM_MENU_ADMIN_SECTION = new ItemBuilder()
            .setMaterial(Material.CRAFTING_TABLE)
            .setAmount(1)
            .setName(Format.processString("%B&6Enter Admin Menu"))
            .setPermission(Permissions.PERMISSION_ADMIN.toString())
            .setLores(new ArrayList<String>() {{
                add("");
                add(Format.processString("%b%tClick to enter the admin menu"));
            }});

    public static final ItemBuilder ITEM_MENU_BACK = new ItemBuilder()
            .setMaterial(Material.REDSTONE_TORCH)
            .setAmount(1)
            .setName(Format.processString("%BBack"))
            .setLores(new ArrayList<String>() {{
                add("");
            }});
}
