package net.nerux.bank.gui;

import net.nerux.CityCore;
import net.nerux.bank.util.item.InventoryBuilder;
import net.nerux.bank.util.reactor.OnlyInventoryReactor;
import net.nerux.utils.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class MenuGui extends OnlyInventoryReactor {
    ItemStack konto = new ItemBuilder(Material.BOOK).withTitle("" + ChatColor.DARK_GREEN + ChatColor.BOLD + "Konto")
            .addLore(ChatColor.GRAY + "Hier kannst du dein Konto managen!").build();
    ItemStack ueberweisen = new ItemBuilder(Material.NAME_TAG)
            .withTitle("" + ChatColor.GREEN + ChatColor.BOLD + "Überweisen")
            .addLore(ChatColor.GRAY + "Hier kannst du anderen").addLore(ChatColor.GRAY + "Spielern Geld überweisen!")
            .build();
    ItemStack kredite = new ItemBuilder(Material.PAPER).withTitle("" + ChatColor.AQUA + ChatColor.BOLD + "Kredite")
            .addLore(ChatColor.GRAY + "Hier kannst du dir ").addLore(ChatColor.GRAY + "einen Kredit holen!").build();

    public MenuGui(String path) {
        super(path);
    }

    protected void click(final InventoryClickEvent e, final ItemStack clicked, final Player player) {
        System.out.println("LÄUFT");
        e.setCancelled(true);
        switch (e.getSlot()) {
            case 19:
                player.openInventory(GuiListener.getReactorByPath("Bank/Menu/Konto").getInventory(player));
                break;
            case 22:
                player.openInventory(GuiListener.getReactorByPath("Bank/Menu/Transfer").getInventory(player));
                break;
            case 25:
                player.openInventory(GuiListener.getReactorByPath("Bank/Menu/Kredit").getInventory(player));
                break;
        }
    }

    public void back(final InventoryClickEvent e, final Player player) {
        player.closeInventory();
    }

    public Inventory getInventory(final Player player) {
        Inventory inv = new InventoryBuilder(getTitle(), 5).setBasic().build();
        CityCore.fillInv(inv);
        inv.setItem(19, this.konto);
        inv.setItem(22, this.ueberweisen);
        inv.setItem(25, this.kredite);

        return inv;
    }
}
