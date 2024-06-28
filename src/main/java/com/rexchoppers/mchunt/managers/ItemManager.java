package com.rexchoppers.mchunt.managers;

import com.rexchoppers.mchunt.MCHunt;
import com.rexchoppers.mchunt.items.ItemBuilder;
import com.rexchoppers.mchunt.permissions.Permissions;
import com.rexchoppers.mchunt.util.Format;
import org.bukkit.Bukkit;
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
                Material.BARRIER.name(),
                Material.CAVE_AIR.name(),
                Material.VOID_AIR.name(),
                Material.BUBBLE_COLUMN.name(),
                Material.STRUCTURE_VOID.name(),
                Material.CHAIN_COMMAND_BLOCK.name(),
                Material.REPEATING_COMMAND_BLOCK.name(),
                Material.FIRE.name(),
                Material.SOUL_FIRE.name(),
                Material.WATER.name(),
                Material.LAVA.name(),
                Material.NETHER_PORTAL.name(),
                Material.JIGSAW.name(),
                Material.MOVING_PISTON.name(),
                Material.END_PORTAL.name(),
                Material.BARRIER.name(),
                Material.PISTON_HEAD.name(),
                Material.OAK_WALL_SIGN.name(),
                Material.SPRUCE_WALL_SIGN.name(),
                Material.BIRCH_WALL_SIGN.name(),
                Material.ACACIA_WALL_SIGN.name(),
                Material.CHERRY_WALL_SIGN.name(),
                Material.JUNGLE_WALL_SIGN.name(),
                Material.DARK_OAK_WALL_SIGN.name(),
                Material.MANGROVE_WALL_SIGN.name(),
                Material.BAMBOO_WALL_SIGN.name(),
                Material.OAK_WALL_HANGING_SIGN.name(),
                Material.SPRUCE_WALL_HANGING_SIGN.name(),
                Material.BIRCH_WALL_HANGING_SIGN.name(),
                Material.ACACIA_WALL_HANGING_SIGN.name(),
                Material.CHERRY_WALL_HANGING_SIGN.name(),
                Material.JUNGLE_WALL_HANGING_SIGN.name(),
                Material.DARK_OAK_WALL_HANGING_SIGN.name(),
                Material.MANGROVE_WALL_HANGING_SIGN.name(),
                Material.CRIMSON_WALL_HANGING_SIGN.name(),
                Material.WARPED_WALL_HANGING_SIGN.name(),
                Material.BAMBOO_WALL_HANGING_SIGN.name(),
                Material.WATER_CAULDRON.name(),
                Material.LAVA_CAULDRON.name(),
                Material.POWDER_SNOW_CAULDRON.name(),
                Material.WHITE_WALL_BANNER.name(),
                Material.ORANGE_WALL_BANNER.name(),
                Material.MAGENTA_WALL_BANNER.name(),
                Material.LIGHT_BLUE_WALL_BANNER.name(),
                Material.YELLOW_WALL_BANNER.name(),
                Material.LIME_WALL_BANNER.name(),
                Material.PINK_WALL_BANNER.name(),
                Material.GRAY_WALL_BANNER.name(),
                Material.LIGHT_GRAY_WALL_BANNER.name(),
                Material.CYAN_WALL_BANNER.name(),
                Material.PURPLE_WALL_BANNER.name(),
                Material.BLUE_WALL_BANNER.name(),
                Material.BROWN_WALL_BANNER.name(),
                Material.GREEN_WALL_BANNER.name(),
                Material.RED_WALL_BANNER.name(),
                Material.BLACK_WALL_BANNER.name(),
                Material.FROSTED_ICE.name(),
                Material.DEAD_TUBE_CORAL_WALL_FAN.name(),
                Material.DEAD_BRAIN_CORAL_WALL_FAN.name(),
                Material.DEAD_BUBBLE_CORAL_WALL_FAN.name(),
                Material.DEAD_FIRE_CORAL_WALL_FAN.name(),
                Material.DEAD_HORN_CORAL_WALL_FAN.name(),
                Material.CRIMSON_WALL_SIGN.name(),
                Material.WARPED_WALL_SIGN.name(),
                Material.CANDLE_CAKE.name(),
                Material.WHITE_CANDLE_CAKE.name(),
                Material.ORANGE_CANDLE_CAKE.name(),
                Material.MAGENTA_CANDLE_CAKE.name(),
                Material.LIGHT_BLUE_CANDLE_CAKE.name(),
                Material.YELLOW_CANDLE_CAKE.name(),
                Material.LIME_CANDLE_CAKE.name(),
                Material.PINK_CANDLE_CAKE.name(),
                Material.GRAY_CANDLE_CAKE.name(),
                Material.LIGHT_GRAY_CANDLE_CAKE.name(),
                Material.CYAN_CANDLE_CAKE.name(),
                Material.PURPLE_CANDLE_CAKE.name(),
                Material.BLUE_CANDLE_CAKE.name(),
                Material.BROWN_CANDLE_CAKE.name(),
                Material.GREEN_CANDLE_CAKE.name(),
                Material.RED_CANDLE_CAKE.name(),
                Material.BLACK_CANDLE_CAKE.name(),
                Material.BAMBOO.name(),
                Material.IRON_BARS.name(),
                Material.CHAIN.name(),
                Material.GLASS_PANE.name(),
                Material.SCULK_VEIN.name(),
                Material.WHITE_STAINED_GLASS_PANE.name(),
                Material.ORANGE_STAINED_GLASS_PANE.name(),
                Material.MAGENTA_STAINED_GLASS_PANE.name(),
                Material.LIGHT_BLUE_STAINED_GLASS_PANE.name(),
                Material.YELLOW_STAINED_GLASS_PANE.name(),
                Material.LIME_STAINED_GLASS_PANE.name(),
                Material.PINK_STAINED_GLASS_PANE.name(),
                Material.GRAY_STAINED_GLASS_PANE.name(),
                Material.LIGHT_GRAY_STAINED_GLASS_PANE.name(),
                Material.CYAN_STAINED_GLASS_PANE.name(),
                Material.PURPLE_STAINED_GLASS_PANE.name(),
                Material.BLUE_STAINED_GLASS_PANE.name(),
                Material.BROWN_STAINED_GLASS_PANE.name(),
                Material.GREEN_STAINED_GLASS_PANE.name(),
                Material.RED_STAINED_GLASS_PANE.name(),
                Material.BLACK_STAINED_GLASS_PANE.name(),
                Material.TURTLE_HELMET.name(),
                Material.SNIFFER_EGG.name(),
                Material.CONDUIT.name(),
                Material.DEAD_BRAIN_CORAL.name(),
                Material.DEAD_BUBBLE_CORAL.name(),
                Material.DEAD_FIRE_CORAL.name(),
                Material.DEAD_HORN_CORAL.name(),
                Material.DEAD_TUBE_CORAL.name(),
                Material.DEAD_TUBE_CORAL_FAN.name(),
                Material.DEAD_BRAIN_CORAL_FAN.name(),
                Material.DEAD_BUBBLE_CORAL_FAN.name(),
                Material.DEAD_FIRE_CORAL_FAN.name(),
                Material.DEAD_HORN_CORAL_FAN.name(),
                Material.SCULK_SENSOR.name(),
                Material.CALIBRATED_SCULK_SENSOR.name(),
                Material.TRAPPED_CHEST.name(),

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

            if (material.isBlock() && !material.isAir() && material.isSolid()) {
                blockMaterials.add(material);
            }
        }
        return blockMaterials;
    }

    public String formatMaterialName(Material material) {
        String name = material.name().toLowerCase().replace('_', ' ');
        String[] words = name.split("\\s+");
        StringBuilder formattedName = new StringBuilder();
        for (String word : words) {
            formattedName.append(Character.toUpperCase(word.charAt(0))).append(word.substring(1)).append(" ");
        }
        return formattedName.toString().trim();
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
                }});
    }

    public ItemBuilder itemArenaSetupBlocksToggleSelectedOn() {
        return new ItemBuilder(this.plugin)
                .setMaterial(Material.GREEN_WOOL)
                .setAmount(1)
                .setName(Format.processString("%n%BToggle Selected - %gON"))
                .setPermission(Permissions.PERMISSION_ADMIN.getPermission())
                .setAction("mchunt.toggleSelectedBlock")
                .setLores(new ArrayList<String>() {{
                    add("");
                    add(Format.processString("%tClick to show all blocks"));
                }});
    }

    public ItemBuilder itemArenaSetupBlocksToggleSelectedOff() {
        return new ItemBuilder(this.plugin)
                .setMaterial(Material.RED_WOOL)
                .setAmount(1)
                .setName(Format.processString("%n%BToggle Selected - %eOFF"))
                .setPermission(Permissions.PERMISSION_ADMIN.getPermission())
                .setAction("mchunt.toggleSelectedBlock")
                .setLores(new ArrayList<String>() {{
                    add("");
                    add(Format.processString("%tClick to show selected blocks only"));
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
