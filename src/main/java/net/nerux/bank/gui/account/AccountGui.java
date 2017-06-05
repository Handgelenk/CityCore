package net.nerux.bank.gui.account;


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

public final class AccountGui extends OnlyInventoryReactor {
    @SuppressWarnings("deprecation")
    ItemStack einzah = new ItemBuilder(Material.getMaterial(351)).withData((byte) 10)
            .withTitle("" + ChatColor.DARK_GREEN + ChatColor.BOLD + "Einzahlen").build();
    @SuppressWarnings("deprecation")
    ItemStack abheb = new ItemBuilder(Material.getMaterial(351)).withData((byte) 1)
            .withTitle("" + ChatColor.DARK_RED + ChatColor.BOLD + "Abheben").build();

    public AccountGui(final String path) {
        super(path);
    }

    protected void click(final InventoryClickEvent e, final ItemStack clicked, final Player player) {
        e.setCancelled(true);
        if ((clicked == null) || (clicked.getType() == Material.AIR)) {
            return;
        }
        switch (e.getSlot()) {
            case 20:
                player.openInventory(GuiListener.getReactorByPath("Bank/Menu/Konto/Einzahlen").getInventory(player));
                break;
            case 24:
                player.openInventory(GuiListener.getReactorByPath("Bank/Menu/Konto/Abheben").getInventory(player));
                break;
        }
    }

    public Inventory getInventory(final Player player) {
        BankAccount account = BankManager.instance.getBankAccount(player);

        Inventory inv = new InventoryBuilder(getTitle(), 5).setBasic().build();
        if (account == null) {
            return inv;
        }
        ItemStack kontostand = new ItemBuilder(Material.GOLD_INGOT).withTitle(ChatColor.YELLOW + "Dein Kontostand: "
                + ChatColor.RED + ChatColor.BOLD + account.getRoundedBalance() + "$").build();
        CityCore.fillInv(inv);
        inv.setItem(20, this.einzah);
        inv.setItem(22, kontostand);
        inv.setItem(24, this.abheb);

        return inv;
    }

    public void back(final InventoryClickEvent e, final Player player) {
        player.openInventory(GuiListener.getReactorByPath("Bank/Menu").getInventory(player));
    }
}
