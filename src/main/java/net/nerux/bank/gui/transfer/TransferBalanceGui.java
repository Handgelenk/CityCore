package net.nerux.bank.gui.transfer;

import net.nerux.CityCore;
import net.nerux.bank.BankAccount;
import net.nerux.bank.BankManager;
import net.nerux.bank.gui.GuiListener;
import net.nerux.bank.util.item.InventoryBuilder;
import net.nerux.bank.util.reactor.OnlyInventoryReactor;
import net.nerux.utils.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class TransferBalanceGui extends OnlyInventoryReactor {
    public TransferBalanceGui(String path) {
        super(path);
    }

    @SuppressWarnings("deprecation")
    protected void click(final InventoryClickEvent e, final ItemStack clicked, final Player player) {
        e.setCancelled(true);
        if ((clicked == null) || (clicked.getType() == Material.AIR)) {
            return;
        }
        player.openInventory(((TransferAcceptGui) GuiListener.getReactorByPath("Bank/Menu/Transfer/Accept"))
                .getInventory(player));
    }

    @SuppressWarnings("deprecation")
    public Inventory getInventory(final Player player, final Player target) {
        BankAccount account = BankManager.instance.getBankAccount(player);

        Inventory inv = new InventoryBuilder(getTitle(), 5).setBasic().build();
        if (account == null) {
            return inv;
        }
        ItemStack a500 = new ItemBuilder(Material.getMaterial(351)).withData((byte) 1)
                .withTitle(ChatColor.RED + "500$ überweisen")
                .addValue(ChatColor.GRAY + "Spieler", ChatColor.AQUA + target.getName())
                .addValue(ChatColor.GRAY + "Betrag", "" + ChatColor.AQUA + 500).build();

        ItemStack a1000 = new ItemBuilder(Material.getMaterial(351)).withData((byte) 1)
                .withTitle(ChatColor.RED + "1000$ überweisen")
                .addValue(ChatColor.GRAY + "Spieler", ChatColor.AQUA + target.getName())
                .addValue(ChatColor.GRAY + "Betrag", "" + ChatColor.AQUA + 1000).build();

        ItemStack a5000 = new ItemBuilder(Material.getMaterial(351)).withData((byte) 1)
                .withTitle(ChatColor.RED + "5000$ überweisen")
                .addValue(ChatColor.GRAY + "Spieler", ChatColor.AQUA + target.getName())
                .addValue(ChatColor.GRAY + "Betrag", "" + ChatColor.AQUA + 5000).build();

        ItemStack a10000 = new ItemBuilder(Material.getMaterial(351)).withData((byte) 1)
                .withTitle(ChatColor.RED + "10000$ überweisen")
                .addValue(ChatColor.GRAY + "Spieler", ChatColor.AQUA + target.getName())
                .addValue(ChatColor.GRAY + "Betrag", "" + ChatColor.AQUA + 10000).build();

        ItemStack aAll = new ItemBuilder(Material.getMaterial(351)).withData((byte) 1)
                .withTitle(ChatColor.RED + "Alles überweisen")
                .addValue(ChatColor.GRAY + "Spieler", ChatColor.AQUA + target.getName())
                .addValue(ChatColor.GRAY + "Betrag", "" + ChatColor.AQUA + Math.floor(account.getBalance())).build();

        ItemStack kontostand = new ItemBuilder(Material.GOLD_INGOT).withTitle(ChatColor.YELLOW + "Dein Kontostand: "
                + ChatColor.RED + ChatColor.BOLD + account.getRoundedBalance() + "$").build();
        CityCore.fillInv(inv);
        inv.setItem(19, a500);
        inv.setItem(21, a1000);
        inv.setItem(23, a5000);
        inv.setItem(25, a10000);
        inv.setItem(27, kontostand);
        inv.setItem(31, aAll);

        return inv;
    }

    public void back(final InventoryClickEvent e, final Player player) {
        player.openInventory(GuiListener.getReactorByPath("Bank/Menu").getInventory(player));
    }

    @Deprecated
    public Inventory getInventory(final Player player) {
        return null;
    }
}
