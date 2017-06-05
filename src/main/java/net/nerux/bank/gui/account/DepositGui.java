package net.nerux.bank.gui.account;

import java.sql.Date;

import net.nerux.CC;
import net.nerux.CityCore;
import net.nerux.bank.BankAccount;
import net.nerux.bank.BankManager;
import net.nerux.bank.gui.GuiListener;
import net.nerux.bank.util.item.InventoryBuilder;
import net.nerux.bank.util.reactor.OnlyInventoryReactor;
import net.nerux.economy.EcoAccount;
import net.nerux.economy.EcoManager;
import net.nerux.utils.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public final class DepositGui extends OnlyInventoryReactor {
    @SuppressWarnings("deprecation")
    ItemStack a500 = new ItemBuilder(Material.getMaterial(351)).withData((byte) 10)
            .withTitle(ChatColor.GREEN + "500$ einzahlen").addValue(ChatColor.GRAY + "Betrag", "" + ChatColor.AQUA + 500)
            .build();
    @SuppressWarnings("deprecation")
    ItemStack a1000 = new ItemBuilder(Material.getMaterial(351)).withData((byte) 10)
            .withTitle(ChatColor.GREEN + "1000$ einzahlen")
            .addValue(ChatColor.GRAY + "Betrag", "" + ChatColor.AQUA + 1000).build();
    @SuppressWarnings("deprecation")
    ItemStack a5000 = new ItemBuilder(Material.getMaterial(351)).withData((byte) 10)
            .withTitle(ChatColor.GREEN + "5000$ einzahlen")
            .addValue(ChatColor.GRAY + "Betrag", "" + ChatColor.AQUA + 5000).build();
    @SuppressWarnings("deprecation")
    ItemStack a10000 = new ItemBuilder(Material.getMaterial(351)).withData((byte) 10)
            .withTitle(ChatColor.GREEN + "10000$ einzahlen")
            .addValue(ChatColor.GRAY + "Betrag", "" + ChatColor.AQUA + 10000).build();

    public DepositGui(final String path) {
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
        BankAccount account = BankManager.instance.getBankAccount(player);
        EcoAccount ecoAcc = EcoManager.instance.getEcoAccount(player);

        double balance = Double.parseDouble(new ItemBuilder(clicked).getValue("Betrag"));
        if (account == null) {
            player.sendMessage(CityCore.bankpr + "Du hast kein Bank-Konto.");
            player.playSound(player.getLocation(), Sound.ANVIL_BREAK, 100.0F, 100.0F);
            player.closeInventory();
            return;
        }
        if (ecoAcc.getBalance() <= 0.0D) {
            player.sendMessage(CityCore.bankpr + "Du hast kein Geld!");
            player.playSound(player.getLocation(), Sound.ANVIL_BREAK, 100.0F, 100.0F);
            return;
        }
        if (ecoAcc.getBalance() >= balance) {
            account.addBalance(balance);
            ecoAcc.removeBalance(balance);
            player.sendMessage(CityCore.bankpr + "Du hast "
                    + CC.spz(new StringBuilder(String.valueOf(balance)).append("$").toString()) + " eingezahlt.");
            player.playSound(player.getLocation(), Sound.LEVEL_UP, 100.0F, 100.0F);
            ItemStack kontostand = new ItemBuilder(Material.GOLD_INGOT).withTitle(ChatColor.YELLOW + "Dein Kontostand:  "
                    + ChatColor.RED + ChatColor.BOLD + account.getRoundedBalance() + "$").build();
            player.getOpenInventory().setItem(13, kontostand);
            return;
        }
        player.sendMessage(CityCore.bankpr + "Du hast nicht genügend Geld.");
        player.playSound(player.getLocation(), Sound.ANVIL_BREAK, 100.0F, 100.0F);

        player.closeInventory();
    }

    public Inventory getInventory(final Player player) {
        BankAccount account = BankManager.instance.getBankAccount(player);
        EcoAccount ecoAcc = EcoManager.instance.getEcoAccount(player);

        Inventory inv = new InventoryBuilder(getTitle(), 5).setBasic().build();
        if (account == null) {
            return inv;
        }
        ItemStack kontostand = new ItemBuilder(Material.GOLD_INGOT).withTitle(ChatColor.YELLOW + "Dein Kontostand: "
                + ChatColor.RED + ChatColor.BOLD + account.getRoundedBalance() + "$").build();

        @SuppressWarnings("deprecation")
        ItemStack aAll = new ItemBuilder(Material.getMaterial(351)).withData((byte) 14)
                .withTitle(ChatColor.RED + "Alles einzahlen")
                .addValue(ChatColor.GRAY + "Betrag", "" + ChatColor.AQUA + Math.floor(ecoAcc.getBalance())).build();
        CityCore.fillInv(inv);
        inv.setItem(19, this.a500);
        inv.setItem(21, this.a1000);
        inv.setItem(23, this.a5000);
        inv.setItem(25, this.a10000);
        inv.setItem(13, kontostand);
        inv.setItem(31, aAll);

        return inv;
    }

    public void back(final InventoryClickEvent e, final Player player) {
        player.openInventory(GuiListener.getReactorByPath("Bank/Menu/Konto").getInventory(player));
    }
}
