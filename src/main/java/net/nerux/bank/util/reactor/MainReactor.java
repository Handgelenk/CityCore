package net.nerux.bank.util.reactor;

import net.nerux.utils.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

abstract public class MainReactor {


    protected static final String A = ItemBuilder.UNICORN + "" + ChatColor.ITALIC;

    private String title;

    private String path;

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public String getPath() {
        return path;
    }

    public void setPath(final String path) {
        this.path = path;
    }

    public MainReactor(final String path) {
        this.path = path;
        this.title = A + path;
    }

    abstract public Inventory getInventory(final Player player);

    public abstract void react(final InventoryClickEvent e, final ItemStack clicked, final Player player);

    public abstract void back(final InventoryClickEvent e, final Player player);

}
