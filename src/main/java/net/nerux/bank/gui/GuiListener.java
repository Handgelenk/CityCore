package net.nerux.bank.gui;

import java.util.HashSet;

import net.nerux.bank.gui.account.AccountGui;
import net.nerux.bank.gui.account.DepositGui;
import net.nerux.bank.gui.account.WithdrawGui;
import net.nerux.bank.gui.credit.CreditAcceptGui;
import net.nerux.bank.gui.credit.CreditMenuGui;
import net.nerux.bank.gui.transfer.TransferAcceptGui;
import net.nerux.bank.gui.transfer.TransferBalanceGui;
import net.nerux.bank.gui.transfer.TransferGui;
import net.nerux.bank.util.MyItems;
import net.nerux.bank.util.reactor.MainReactor;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class GuiListener implements Listener {

    private static final HashSet<MainReactor> STATIC_REACTORS = new HashSet<>();

    static {
        STATIC_REACTORS.add(new MenuGui("Bank/Menu"));

        STATIC_REACTORS.add(new AccountGui("Bank/Menu/Konto"));
        STATIC_REACTORS.add(new WithdrawGui("Bank/Menu/Konto/Abheben"));
        STATIC_REACTORS.add(new DepositGui("Bank/Menu/Konto/Einzahlen"));

        STATIC_REACTORS.add(new TransferGui("Bank/Menu/Transfer"));
        STATIC_REACTORS.add(new TransferBalanceGui("Bank/Menu/Transfer/Menge"));
        STATIC_REACTORS.add(new TransferAcceptGui("Bank/Menu/Transfer/Accept"));

        STATIC_REACTORS.add(new CreditMenuGui("Bank/Menu/Kredit"));
        STATIC_REACTORS.add(new CreditAcceptGui("Bank/Menu/Kredit/Accept"));

    }

    @EventHandler
    public void onInventoryClick(final InventoryClickEvent e) {

        Inventory inv = e.getInventory();

        String title = inv.getTitle();

        ItemStack clicked = e.getCurrentItem();

        Player user = (Player) e.getWhoClicked();
        if (user == null) {
            e.setCancelled(true);
            return;
        }

        if (clicked != null) {

            if (clicked.equals(MyItems.BLOCKED)) {

                e.setResult(Result.DENY);
                e.setCancelled(true);
                return;
            }

            if (clicked.equals(MyItems.BACK)) {
                e.setResult(Result.DENY);
                e.setCancelled(true);
                MainReactor r = getReactorByTitle(title);
                if (r != null)
                    r.back(e, user);
                return;
            }

        }

        MainReactor r = getReactorByTitle(title);
        if (r != null)
            r.react(e, clicked, user);
    }

    @EventHandler
    public void onInteract(final PlayerInteractEvent event) {

        ItemStack item = event.getItem();

        if (item == null)
            return;

        if (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK)
            return;

    }

    public static MainReactor getReactorByTitle(final String title) {

        for (MainReactor mainReactor : STATIC_REACTORS) {
            if (mainReactor.getTitle().equalsIgnoreCase(title))
                return mainReactor;
        }
        return null;
    }

    public static MainReactor getReactorByPath(final String path) {

        for (MainReactor mainReactor : STATIC_REACTORS) {
            if (mainReactor.getPath().equalsIgnoreCase(path))
                return mainReactor;
        }
        return null;
    }
}
