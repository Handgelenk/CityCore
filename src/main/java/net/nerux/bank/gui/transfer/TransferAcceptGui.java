package net.nerux.bank.gui.transfer;

import net.nerux.CityCore;
import net.nerux.bank.BankAccount;
import net.nerux.bank.BankManager;
import net.nerux.bank.gui.GuiListener;
import net.nerux.bank.util.item.InventoryBuilder;
import net.nerux.bank.util.reactor.OnlyInventoryReactor;
import net.nerux.utils.ItemBuilder;
import net.nerux.utils.MathUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class TransferAcceptGui extends OnlyInventoryReactor {
    public TransferAcceptGui(String path) {
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
        ItemBuilder item = new ItemBuilder(clicked);

        double balance = Double.parseDouble(item.getValue("Betrag"));

        Player target = Bukkit.getPlayer(item.getValue("Spieler"));
        if (target == null) {
            player.sendMessage(CityCore.bankpr + "Dieser Spieler ist nicht existent!");
            player.closeInventory();
            return;
        }
        BankAccount targetAccount = BankManager.instance.getBankAccount(target);

        BankAccount playerAccount = BankManager.instance.getBankAccount(player);
        if (player == target) {
            player.sendMessage(CityCore.bankpr + "Du kannst dir selber kein Geld überweisen.");
            player.closeInventory();
            return;
        }
        if (targetAccount == null) {
            player.sendMessage(CityCore.bankpr + "Dieser Spieler besitzt kein Bank-Konto.");
            player.closeInventory();
            return;
        }
        if (playerAccount == null) {
            player.sendMessage(CityCore.bankpr + "Du hast kein Bank-Konto.");
            player.closeInventory();
            return;
        }
        if (playerAccount.hasEnoughMoney(balance)) {
            playerAccount.setBalance(playerAccount.getBalance() - balance);
            targetAccount.addBalance(balance);

            player.sendMessage(CityCore.bankpr + "Du hast " + ChatColor.AQUA + target.getName() + ChatColor.GRAY + ", "
                    + MathUtils.round(balance) + "$ überwiesen!");
            player.sendMessage(CityCore.bankpr + ChatColor.YELLOW + "Dein neuer Kontostand: " + ChatColor.AQUA
                    + playerAccount.getRoundedBalance() + "$");

            target.sendMessage(CityCore.bankpr + "Der Spieler " + ChatColor.AQUA + player.getName() + ChatColor.GRAY
                    + " hat dir " + MathUtils.round(balance) + "$ überwiesen!");
            target.sendMessage(CityCore.bankpr + ChatColor.YELLOW + "Dein neuer Kontostand: " + ChatColor.AQUA
                    + targetAccount.getRoundedBalance() + "$");
            player.playSound(player.getLocation(), Sound.LEVEL_UP, 100.0F, 100.0F);
            target.playSound(target.getLocation(), Sound.LEVEL_UP, 100.0F, 100.0F);
        } else {
            player.sendMessage(CityCore.bankpr + "Du hast nicht genügend Geld auf deinem Konto.");
        }
        player.closeInventory();
    }

    @Deprecated
    public Inventory getInventory(final Player player) {
        return null;
    }

    public void back(InventoryClickEvent e, Player player) {
        player.openInventory(GuiListener.getReactorByPath("Bank/Menu").getInventory(player));
    }

    public Inventory getInventory(final Player player, final ItemBuilder item) {
        Inventory inv = new InventoryBuilder(getTitle(), 5).setBasic().build();
        item.withTitle("" + ChatColor.AQUA + ChatColor.BOLD + "Möchtest du überweisen?");
        CityCore.fillInv(inv);
        inv.setItem(22, item.build());
        return inv;
    }
}
