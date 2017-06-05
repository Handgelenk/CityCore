package net.nerux.bank.gui.transfer;

import net.nerux.CityCore;
import net.nerux.bank.gui.GuiListener;
import net.nerux.bank.util.MyItems;
import net.nerux.bank.util.item.InventoryBuilder;
import net.nerux.bank.util.reactor.OnlyInventoryReactor;
import net.nerux.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class TransferGui extends OnlyInventoryReactor {
    public TransferGui(final String path) {
        super(path);
    }

    public Inventory getInventory(final Player user) {
        InventoryBuilder ib = new InventoryBuilder(getTitle(), 6);

        ib.setItem(6, 9, MyItems.BACK);
        Inventory inv = ib.build();
        update(inv, 1, user);

        return inv;
    }

    protected void click(final InventoryClickEvent e, final ItemStack clicked, final Player user) {
        if ((clicked == null) || (clicked.getType() == Material.AIR)) {
            return;
        }
        if (clicked.equals(MyItems.SCROLL_LEFT)) {
            update(e.getInventory(), e.getInventory().getItem(49).getAmount() - 1, user);
            return;
        }
        if (clicked.equals(MyItems.SCROLL_RIGHT)) {
            Inventory inv = e.getClickedInventory();
            if ((inv.getItem(44) == null)
                    || ((inv.getItem(44) != null) && (inv.getItem(44).getType() == Material.AIR))) {
                return;
            }
            update(e.getInventory(), e.getInventory().getItem(49).getAmount() + 1, user);
            return;
        }
        if (clicked.getType() != Material.SKULL_ITEM) {
            return;
        }
        Player target = Bukkit.getPlayer(new ItemBuilder(clicked).getValue("Name"));
        if (target == null) {
            user.sendMessage(CityCore.bankpr + "Dieser Spieler ist offline geganen....");
            return;
        }
        user.openInventory(((TransferBalanceGui) GuiListener.getReactorByPath("Bank/Menu/Transfer/Menge"))
                .getInventory(user, target));
    }

    public void back(final InventoryClickEvent e, final Player user) {
        user.openInventory(GuiListener.getReactorByPath("Bank/Menu").getInventory(user));
    }

    private void update(final Inventory inv, final int i, final Player user) {
        if ((i > 0) && (i <= 64)) {
            if ((inv.getItem(51) != null) && (inv.getItem(47) == null) && (i >= inv.getItem(49).getAmount())) {
                return;
            }
            CityCore.fillInv(inv);
            inv.setItem(47, MyItems.SCROLL_LEFT);
            inv.setItem(51, MyItems.SCROLL_RIGHT);
            inv.setItem(49, new ItemBuilder(MyItems.SCROLL_PAGE.clone()).withAmount(i)
                    .withTitle(ChatColor.AQUA + "Page " + i).build());

            int index = (i - 1) * 5 * 9;
            for (int j = 0; j < 45; j++) {
                inv.setItem(j, getItem(user, index + j + 1));
            }
        }
    }

    private ItemStack getItem(Player user, int i) {
        i--;

        Player player = CityCore.instance.getPlayers().size() > i ? (Player) CityCore.instance.getPlayers().get(i)
                : null;
        if (player == null) {
            return null;
        }
        ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        SkullMeta meta = (SkullMeta) skull.getItemMeta();
        meta.setOwner(player.getName());
        skull.setItemMeta(meta);

        ItemBuilder ib = new ItemBuilder(skull);

        ib.withTitle(ChatColor.AQUA + "Spieler");
        ib.addValue(ChatColor.GRAY + "Name", player.getName());

        return ib.build();
    }
}
