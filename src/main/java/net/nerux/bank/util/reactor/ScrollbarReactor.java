package net.nerux.bank.util.reactor;


import net.nerux.bank.util.MyItems;
import net.nerux.bank.util.item.InventoryBuilder;
import net.nerux.utils.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public abstract class ScrollbarReactor extends MainReactor {
    public ScrollbarReactor(String title) {
        super(title);
    }

    public void react(InventoryClickEvent e, ItemStack clicked, Player user) {
        scrollReact(e, clicked, user);

        if ((clicked == null) || (clicked.getType() == Material.AIR)) {
            return;
        }
        if (clicked.equals(MyItems.SCROLL_LEFT)) {
            update(e.getInventory(), e.getInventory().getItem(31).getAmount() - 1, user);
            return;
        }
        if (clicked.equals(MyItems.SCROLL_RIGHT)) {
            update(e.getInventory(), e.getInventory().getItem(31).getAmount() + 1, user);
            return;
        }

        int i = e.getRawSlot();
        if ((9 < i) && (i < 17)) {
            clickScrollItem(e, clicked, user);
        }
    }

    protected abstract void scrollReact(InventoryClickEvent paramInventoryClickEvent, ItemStack paramItemStack,
                                        Player paramPlayer);

    protected abstract void clickScrollItem(InventoryClickEvent paramInventoryClickEvent, ItemStack paramItemStack,
                                            Player paramPlayer);

    protected abstract ItemStack getItem(Inventory paramInventory, Player paramPlayer, int paramInt);

    public void update(Inventory inv, int i, Player user) {
        if ((i > 0) && (i <= 64)) {
            if ((inv.getItem(31) != null) && (inv.getItem(16) == null) && (i >= inv.getItem(31).getAmount())) {
                return;
            }
            inv.setItem(29, MyItems.SCROLL_LEFT);
            inv.setItem(33, MyItems.SCROLL_RIGHT);
            inv.setItem(31, new ItemBuilder(MyItems.SCROLL_PAGE.clone()).withAmount(i)
                    .withTitle(ChatColor.AQUA + "Page " + i).build());

            int index = (i - 1) * 7;

            for (int j = 0; j < 7; j++) {
                inv.setItem(10 + j, getItem(inv, user, index + j));
            }
        }
    }

    public Inventory getInventory(Player user) {
        Inventory inv = org.bukkit.Bukkit.createInventory(null, 45, getTitle());

        new InventoryBuilder(inv).setBasic();

        return inv;
    }
}
