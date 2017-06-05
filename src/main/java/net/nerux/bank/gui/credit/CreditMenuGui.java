package net.nerux.bank.gui.credit;

import java.text.SimpleDateFormat;

import net.nerux.CityCore;
import net.nerux.bank.BankAccount;
import net.nerux.bank.BankManager;
import net.nerux.bank.credit.CreditType;
import net.nerux.bank.gui.GuiListener;
import net.nerux.bank.util.item.InventoryBuilder;
import net.nerux.bank.util.reactor.OnlyInventoryReactor;
import net.nerux.utils.ItemBuilder;
import net.nerux.utils.MathUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class CreditMenuGui extends OnlyInventoryReactor {
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");
    ItemStack falseStatus = new ItemBuilder(Material.NAME_TAG)
            .withTitle("" + ChatColor.YELLOW + ChatColor.BOLD + "Status")
            .addLore(ChatColor.RED + "Du hast keinen Kredit").build();
    ItemStack info = new ItemBuilder(Material.SIGN).withTitle("" + ChatColor.BLUE + ChatColor.BOLD + "Informationen")
            .addLore(ChatColor.GRAY + "Wenn du einen Kredit ausgezahlt")
            .addLore(ChatColor.GRAY + "bekommst, hast du je nach Kredit ein")
            .addLore(ChatColor.GRAY + "Datum, welches die")
            .addLore(ChatColor.GRAY + "Frist deines Zurückzahlungsdatums").addLore(ChatColor.GRAY + "darstellt.")
            .addLore(ChatColor.GRAY + "Wenn du diese Frist überziehst, wirst ")
            .addLore(ChatColor.GRAY + "du je nach Höhe des Kredits ")
            .addLore(ChatColor.GRAY + "eine gewisse Zeiteingesperrt.").build();
    ItemStack zurück = new ItemBuilder(Material.BOOK)
            .withTitle("" + ChatColor.DARK_GREEN + ChatColor.BOLD + "Zurückzahlen").build();

    public CreditMenuGui(final String path) {
        super(path);
    }

    public Inventory getInventory(final Player player) {
        Inventory inv = new InventoryBuilder(getTitle(), 5).setBasic().build();

        BankAccount account = BankManager.instance.getBankAccount(player);
        if (account == null) {
            return inv;
        }
        ItemStack stats = account.hasCredit()
                ? new ItemBuilder(Material.NAME_TAG).withTitle("" + ChatColor.YELLOW + ChatColor.BOLD + "Status")
                .addLore(ChatColor.GREEN + "Du musst bis zum " + ChatColor.YELLOW
                        + simpleDateFormat.format(account.getCredit().getBack()) + ChatColor.GREEN + ", ")
                .addLore("" + ChatColor.GOLD + ChatColor.BOLD
                        + MathUtils.round(account.getCredit().getType().hasToPay()) + "$ " + ChatColor.GREEN
                        + "zurückzahlen!")
                .build()
                : new ItemBuilder(Material.NAME_TAG).withTitle("" + ChatColor.YELLOW + ChatColor.BOLD + "Status")
                .addLore(ChatColor.RED + "Du hast keinen Kredit").build();
        CityCore.fillInv(inv);
        inv.setItem(10, stats);
        inv.setItem(13, info);
        inv.setItem(16, zurück);

        inv.setItem(19, CreditType.BASIC_1.getItem());
        inv.setItem(28, CreditType.BASIC_2.getItem());

        inv.setItem(20, CreditType.BRONZE_1.getItem());
        inv.setItem(29, CreditType.BRONZE_2.getItem());

        inv.setItem(21, CreditType.SILBER_1.getItem());
        inv.setItem(30, CreditType.SILBER_2.getItem());

        inv.setItem(22, CreditType.GOLD_1.getItem());
        inv.setItem(31, CreditType.GOLD_2.getItem());

        inv.setItem(23, CreditType.SUPREME_1.getItem());
        inv.setItem(32, CreditType.SUPREME_2.getItem());

        inv.setItem(24, CreditType.JUMBO_1.getItem());
        inv.setItem(33, CreditType.JUMBO_2.getItem());

        inv.setItem(25, CreditType.MEGA_1.getItem());
        inv.setItem(34, CreditType.MEGA_2.getItem());
        return inv;
    }

    public void back(final InventoryClickEvent e, final Player player) {
        player.openInventory(GuiListener.getReactorByPath("Bank/Menu").getInventory(player));
    }

    @SuppressWarnings("deprecation")
    protected void click(final InventoryClickEvent e, final ItemStack clicked, final Player player) {
        e.setCancelled(true);
        if ((clicked == null) || (clicked.getType() == Material.AIR)) {
            return;
        }
        if (e.getSlot() == 16) {
            BankAccount account = BankManager.instance.getBankAccount(player);
            if (account == null) {
                player.sendMessage(CityCore.bankpr + "Du hast kein Bank-Konto.");
                player.closeInventory();
                return;
            }
            if (!account.hasCredit()) {
                player.sendMessage(CityCore.bankpr + "Du hast kein Kredit.");
                player.closeInventory();
                return;
            }
            if (account.canCreditPayBack()) {
                try {
                    account.creditPayBack();
                    player.sendMessage(CityCore.bankpr + "Du hast deinen Kredit erfolgreich zurückgezahlt.");
                    player.playSound(player.getLocation(), Sound.ANVIL_BREAK, 100.0F, 100.0F);
                } catch (BankAccount.BankException e1) {
                    player.sendMessage(CityCore.bankpr + e1.getMessage());
                }
                player.closeInventory();
            } else {
                player.sendMessage(CityCore.bankpr + "Du hast nicht genügend Geld, diesen Kredit zurückzuahlen.");
                player.closeInventory();
            }
            return;
        }
        if (clicked.getTypeId() != 351) {
            return;
        }
        player.openInventory(((CreditAcceptGui) GuiListener.getReactorByPath("Bank/Menu/Kredit/Accept"))
                .getInventory(player, new ItemBuilder(clicked)));
    }
}
