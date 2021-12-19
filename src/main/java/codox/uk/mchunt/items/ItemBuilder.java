package codox.uk.mchunt.items;

import codox.uk.mchunt.MCHunt;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;

public class ItemBuilder implements Cloneable {
    private String name;
    private String action;
    private String tag;
    private Material material;
    private int amount;
    private int type;
    private ArrayList<String> lores;

    /**
     * Check if a item needs a permission to
     * be viewed
     */
    private String permission;

    public ItemBuilder() {
    }

    public ItemStack build() {
        final ItemStack item = new ItemStack(this.material, this.amount);

        final ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(this.name);
        meta.setLore(lores);

        if (this.action != null) {
            NamespacedKey key = new NamespacedKey(MCHunt.getInstance(), "action");
            meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, this.action);
        }

        if (this.tag != null) {
            NamespacedKey key = new NamespacedKey(MCHunt.getInstance(), "tag");
            meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, this.tag);
        }

        item.setItemMeta(meta);

        return item;
    }

    public ItemBuilder setName(String name) {
        // this.name = Format.format(name);
        this.name = name;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public ItemBuilder setMaterial(Material material) {
        this.material = material;
        return this;
    }

    public ItemBuilder setAmount(int amount) {
        this.amount = amount;
        return this;
    }

    public ItemBuilder setAction(String action) {
        this.action = action;
        return this;
    }

    public ItemBuilder setType(int type) {
        this.type = type;
        return this;
    }

    public String getAction() {
        return this.action;
    }

    public ItemBuilder setLores(ArrayList<String> lores) {
        // this.lores = lores.stream().map(Format::format).collect(Collectors.toCollection(ArrayList::new));
        this.lores = lores;
        return this;
    }

    public ItemBuilder setPermission(String permission) {
        this.permission = permission;
        return this;
    }

    public String getPermission() {
        return this.permission;
    }

    public ItemBuilder clone() {
        try {
            return (ItemBuilder) super.clone();
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}