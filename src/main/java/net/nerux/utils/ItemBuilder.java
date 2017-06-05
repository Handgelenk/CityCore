package net.nerux.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public final class ItemBuilder {

    public static final char UNICORN = '\u26C9';
    public static final char ARROW = '\u27BC';

    private ItemStack item;
    private ItemMeta meta;

    public static final String UNDEFINED = "UNDEFIEND_VALUE";
    public static final String SPLITTER = UNICORN + ": ";

    public ItemBuilder(Material mat) {
        this(new ItemStack(mat));
    }

    public ItemBuilder(Material mat, int amount) {
        this(new ItemStack(mat, amount));
    }

    public ItemBuilder(Material mat, int amount, byte data) {
        this(new ItemStack(mat, amount, data));
    }

    public ItemBuilder(ItemStack item) {
        this.item = item;
        meta = item.getItemMeta();
    }

    public ItemBuilder addEnchantEffect() {
        meta.addEnchant(Enchantment.DURABILITY, 0, false);
        item.setItemMeta(meta);
        item.removeEnchantment(Enchantment.DURABILITY);
        return this;
    }

    public ItemBuilder withItem(ItemStack item) {
        this.item = item;
        this.meta = item.getItemMeta();
        return this;
    }

    public ItemBuilder addEnchant(Enchantment ench, int lvl) {
        meta.addEnchant(ench, lvl, false);
        item.setItemMeta(meta);
        return this;

    }

    public String getTitle() {
        return meta.getDisplayName();
    }

    public ItemBuilder withMaterial(Material mat) {
        item.setType(mat);
        return this;
    }

    public ItemBuilder withData(byte data) {
        item.setDurability(data);
        return this;
    }

    public ItemBuilder withAmount(int amount) {
        item.setAmount(amount);
        return this;
    }

    public ItemBuilder addLore(String lore) {
        List<String> current = meta.getLore();

        if (current == null) {
            current = new ArrayList<String>();
        }

        current.add(lore);
        meta.setLore(current);

        return this;
    }

    public ItemBuilder addLore(String... lore) {
        for (String s : lore) {
            addLore(s);
        }
        return this;
    }

    public ItemBuilder withTitle(String title) {
        meta.setDisplayName(title);
        return this;
    }

    public ItemBuilder withTitleLore(String title, String lore) {
        withTitle(title);
        addLore(lore);
        return this;
    }

    public ItemBuilder withTitleLore(String title, String... lore) {
        withTitle(title);
        for (String s : lore) {
            addLore(s);
        }
        return this;
    }

    public ItemStack build() {
        item.setItemMeta(meta);
        return item;
    }

    public ItemBuilder addValue(String type, String value) {
        addLore(type + SPLITTER + value);
        return this;
    }

    public ItemBuilder addValue(String type, double value) {
        return addValue(type, "" + value);
    }

    public boolean hasValue(String type) {
        return getValue(type) != UNDEFINED;
    }

    public ItemBuilder withValue(String type, String value) {
        List<String> currentLore = meta.getLore();
        if (currentLore == null)
            currentLore = new ArrayList<>();

        String correctedType = ChatColor.stripColor(type.toUpperCase());

        for (int i = 0; i < currentLore.size(); i++) {
            String s = currentLore.get(i);
            String corrected = ChatColor.stripColor(s);
            if (corrected.toUpperCase().startsWith(correctedType)) {
                currentLore.add(i, type + SPLITTER + value);
                meta.setLore(currentLore);
                return this;
            }
        }
        return addValue(type, value);
    }

    public ItemBuilder withValue(String type, double value) {
        return withValue(type, "" + value);
    }

    public String getValue(String type) {
        type = ChatColor.stripColor(type).toUpperCase();
        List<String> lore = meta.getLore();

        if (lore == null)
            return null;

        for (String s : lore) {

            String corrected = ChatColor.stripColor(s);
            if (corrected.toUpperCase().startsWith(type)) {
                String[] p = corrected.split(SPLITTER);
                String value = p[1];
                return value;
            }

        }
        return null;
    }

}