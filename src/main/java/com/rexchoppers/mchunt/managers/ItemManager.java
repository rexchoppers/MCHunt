package com.rexchoppers.mchunt.managers;

import com.rexchoppers.mchunt.MCHunt;
import com.rexchoppers.mchunt.items.ItemBuilder;
import com.rexchoppers.mchunt.permissions.Permissions;
import com.rexchoppers.mchunt.util.Format;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class ItemManager {
    private final MCHunt plugin;

    public ItemManager(MCHunt plugin) {
        this.plugin = plugin;
    }

    public void setArenaSetupItems(Player player) {
        player.getInventory().setItem(0, itemArenaSetupSelection().build());
        player.getInventory().setItem(1, itemArenaSetupSetName().build());
        player.getInventory().setItem(2, itemArenaSetupSign().build());
        player.getInventory().setItem(3, itemArenaSetupBlocks().build());
        player.getInventory().setItem(7, itemArenaSetupCancel().build());
        player.getInventory().setItem(8, itemArenaSetupSave().build());
    }

    public String getItemAction(ItemStack itemStack) {
        NamespacedKey key = new NamespacedKey(this.plugin, "action");
        ItemMeta itemMeta = itemStack.getItemMeta();

        if(itemMeta == null) {
            return null;
        }

        PersistentDataContainer container = itemMeta.getPersistentDataContainer();

        if(container.has(key , PersistentDataType.STRING)) {
            String s = container.get(key, PersistentDataType.STRING);
            return s;
        }

        return null;
    }

    public List<Material> getBlockMaterials() {
        String[] excludedBlocks = {
                Material.AIR.name(),
        };

        List<Material> blockMaterials = new ArrayList<>();
        for (Material material : Material.values()) {
            boolean excluded = false;

            for (String excludedBlock : excludedBlocks) {
                if (material.name().equals(excludedBlock)) {
                    excluded = true;
                    break;
                }
            }

            if (excluded) {
                continue;
            }

            if (material.isBlock()) {
                blockMaterials.add(material);
            }
        }
        return blockMaterials;
    }

    // Items
    public ItemBuilder itemNavigateToAdmin() {
        return new ItemBuilder(this.plugin)
                .setMaterial(Material.CRAFTING_TABLE)
                .setAmount(1)
                .setName(Format.processString("%B&6Enter Admin Menu"))
                .setPermission(Permissions.PERMISSION_ADMIN.getPermission())
                .setLores(new ArrayList<String>() {{
                    add("");
                    add(Format.processString("%b%tClick to enter the admin menu"));
                }});
    }

    public ItemBuilder itemEnterArenaSetup() {
        return new ItemBuilder(this.plugin)
                .setMaterial(Material.BOOK)
                .setAmount(1)
                .setName(Format.processString(Format.processString("%n%BEnter Arena Setup Mode")))
                .setPermission(Permissions.PERMISSION_ADMIN.getPermission())
                .setLores(new ArrayList<String>() {{
                    add("");
                    add(Format.processString("%tClick to enter arena setup mode"));
                    add(Format.processString("%tYour inventory and player state will"));
                    add(Format.processString("%tbe stored whilst you're in setup mode"));
                }});
    }

    public ItemBuilder itemArenaSetupSelection() {
        return new ItemBuilder(this.plugin)
                .setMaterial(Material.STICK)
                .setAmount(1)
                .setName(Format.processString("%n%BBoundary Selection"))
                .setPermission(Permissions.PERMISSION_ADMIN.getPermission())
                .setAction("mchunt.boundarySelection")
                .setLores(new ArrayList<String>() {{
                    add("");
                    add(Format.processString("%tBoundary selection mode"));
                    add("");
                    add(Format.processString("%aLEFT %tclick to select the first point"));
                    add(Format.processString("%aRIGHT %tclick to select the second point"));
                }});
    }

    public ItemBuilder itemArenaSetupSetName() {
        return new ItemBuilder(this.plugin)
                .setMaterial(Material.NAME_TAG)
                .setAmount(1)
                .setName(Format.processString("%n%BArena Name"))
                .setPermission(Permissions.PERMISSION_ADMIN.getPermission())
                .setAction("mchunt.arenaName")
                .setLores(new ArrayList<String>() {{
                    add("");
                    add(Format.processString("%tSet the name of the arena"));
                }});
    }

    public ItemBuilder itemArenaSetupSign() {
        return new ItemBuilder(this.plugin)
                .setMaterial(Material.OAK_SIGN)
                .setAmount(1)
                .setName(Format.processString("%n%BArena Sign"))
                .setPermission(Permissions.PERMISSION_ADMIN.getPermission())
                .setAction("mchunt.arenaSign")
                .setLores(new ArrayList<String>() {{
                    add("");
                    add(Format.processString("%tSet the arena signs"));
                }});
    }

    public ItemBuilder itemArenaSetupBlocks() {
        return new ItemBuilder(this.plugin)
                .setMaterial(Material.CHEST)
                .setAmount(1)
                .setName(Format.processString("%n%BArena Blocks"))
                .setPermission(Permissions.PERMISSION_ADMIN.getPermission())
                .setAction("mchunt.arenaBlocks")
                .setLores(new ArrayList<String>() {{
                    add("");
                    add(Format.processString("%tSet the blocks for the arena"));
                    add("");
                    add(Format.processString("%aLEFT %tclick to select a block"));
                    add(Format.processString("%aRIGHT %tclick to remove a block"));
                }});
    }

    public ItemBuilder itemArenaSetupCancel() {
        return new ItemBuilder(this.plugin)
                .setMaterial(Material.REDSTONE)
                .setAmount(1)
                .setName(Format.processString("%n%BCancel"))
                .setPermission(Permissions.PERMISSION_ADMIN.getPermission())
                .setAction("mchunt.cancelArenaSetup")
                .setLores(new ArrayList<String>() {{
                    add("");
                    add(Format.processString("%tCancel the current arena setup"));
                }});
    }

    public ItemBuilder itemArenaSetupSave() {
        return new ItemBuilder(this.plugin)
                .setMaterial(Material.EMERALD)
                .setAmount(1)
                .setName(Format.processString("%n%BSave"))
                .setPermission(Permissions.PERMISSION_ADMIN.getPermission())
                .setAction("mchunt.saveArenaSetup")
                .setLores(new ArrayList<String>() {{
                    add("");
                    add(Format.processString("%tSave the current arena setup"));
                }});
    }
}
