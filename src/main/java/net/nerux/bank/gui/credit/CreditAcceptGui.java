package net.nerux.bank.gui.credit;

import java.sql.Date;
import java.text.SimpleDateFormat;

import net.nerux.CityCore;
import net.nerux.bank.BankAccount;
import net.nerux.bank.BankManager;
import net.nerux.bank.credit.Credit;
import net.nerux.bank.credit.CreditType;
import net.nerux.bank.gui.GuiListener;
import net.nerux.bank.util.item.InventoryBuilder;
import net.nerux.bank.util.reactor.OnlyInventoryReactor;
import net.nerux.utils.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class CreditAcceptGui extends OnlyInventoryReactor {
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");

    public CreditAcceptGui(final String path) {
        super(path);
    }

    @SuppressWarnings("deprecation")
    protected void click(final InventoryClickEvent e, final ItemStack clicked, final Player player) {
        e.setCancelled(true);
        if ((clicked == null) || (clicked.getType() == Material.AIR)) {
            return;
        }
        if (clicked.getTypeId() != 351) {
            return;
        }
        ItemBuilder ib = new ItemBuilder(clicked);

        CreditType credit = CreditType.getCreditTypeByName(ChatColor.stripColor(ib.getTitle()));
        if (credit == null) {
            player.sendMessage(CityCore.bankpr + "Fehler, dies sollte nicht passieren.");
            player.playSound(player.getLocation(), Sound.ANVIL_BREAK, 100.0F, 100.0F);
            player.closeInventory();
            return;
        }
        BankAccount acc = BankManager.instance.getBankAccount(player);
        if (acc == null) {
            player.sendMessage(CityCore.bankpr + "Du hast kein Bank-Konto.");
            player.playSound(player.getLocation(), Sound.ANVIL_BREAK, 100.0F, 100.0F);
            player.closeInventory();
            return;
        }
        if (acc.hasCredit()) {
            player.sendMessage(CityCore.bankpr + ChatColor.RED + "Du hast bereits ein Kredit mit der Bank abgeschlossen.");
            player.closeInventory();
            player.playSound(player.getLocation(), Sound.ANVIL_BREAK, 100.0F, 100.0F);
            return;
        }
        int i = 0;
        acc.setCredit(new Credit(credit, new Date(System.currentTimeMillis()), i));
        acc.addBalance(credit.getBalance());
        Credit kredit = acc.getCredit();

        if (kredit.getType().getName().equalsIgnoreCase("Basic Kredit 1")) {
            kredit.setStrafe(10);
        } else if (kredit.getType().getName().equalsIgnoreCase("Basic Kredit 2")) {
            kredit.setStrafe(15);
        } else if (kredit.getType().getName().equalsIgnoreCase("Bronze Kredit 1")) {
            kredit.setStrafe(20);
        } else if (kredit.getType().getName().equalsIgnoreCase("Bronze Kredit 2")) {
            kredit.setStrafe(20);
        } else if (kredit.getType().getName().equalsIgnoreCase("Silber Kredit 1")) {
            kredit.setStrafe(25);
        } else if (kredit.getType().getName().equalsIgnoreCase("Silber Kredit 2")) {
            kredit.setStrafe(30);
        } else if (kredit.getType().getName().equalsIgnoreCase("Gold Kredit 1")) {
            kredit.setStrafe(35);
        } else if (kredit.getType().getName().equalsIgnoreCase("Gold Kredit 2")) {
            kredit.setStrafe(50);
        } else if (kredit.getType().getName().equalsIgnoreCase("Supreme Kredit 1")) {
            kredit.setStrafe(70);
        } else if (kredit.getType().getName().equalsIgnoreCase("Supreme Kredit 2")) {
            kredit.setStrafe(90);
        } else if (kredit.getType().getName().equalsIgnoreCase("Jumbo Kredit 1")) {
            kredit.setStrafe(110);
        } else if (kredit.getType().getName().equalsIgnoreCase("Jumbo Kredit 2")) {
            kredit.setStrafe(125);
        } else if (kredit.getType().getName().equalsIgnoreCase("Mega Kredit 1")) {
            kredit.setStrafe(160);
        } else if (kredit.getType().getName().equalsIgnoreCase("Mega Kredit 2")) {
            kredit.setStrafe(180);
        }
        player.sendMessage(CityCore.bankpr + "Du hast erfolgreich einen Kredit von " + ChatColor.AQUA + credit.getBalance()
                + "$  " + ChatColor.GRAY + "auf dein Konto überwiesen bekommen! Du musst bis zum " + ChatColor.AQUA
                + simpleDateFormat.format(acc.getCredit().getBack()) + ChatColor.GRAY + " einen Betrag von "
                + ChatColor.AQUA + credit.hasToPay() + "$  " + ChatColor.GRAY
                + "bei der Bank zurückgezahlt haben! Ansonsten drohen sie eingesperrt zu werden!");
        player.playSound(player.getLocation(), Sound.LEVEL_UP, 100.0F, 100.0F);

        player.closeInventory();

        player.closeInventory();
    }

    @Deprecated
    public Inventory getInventory(final Player player) {
        return null;
    }

    public void back(InventoryClickEvent e, Player player) {
        player.openInventory(GuiListener.getReactorByPath("Bank/Menu/Kredit").getInventory(player));
    }

    public Inventory getInventory(final Player player, final ItemBuilder item) {
        Inventory inv = new InventoryBuilder(getTitle(), 5).setBasic().build();
        CityCore.fillInv(inv);
        inv.setItem(22, item.build());
        return inv;
    }
}
