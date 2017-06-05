package net.nerux.bank;

import net.nerux.bank.gui.GuiListener;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;

/**
 * Created by Ben on 03.06.17.
 */
public final class BankListener implements Listener {

    @EventHandler(priority = EventPriority.HIGH)
    public void onBänkerHurt(EntityDamageEvent e) {
        if ((e.getEntity() instanceof Villager)) {
            Villager v = (Villager) e.getEntity();
            if (v.getName().equalsIgnoreCase("" + ChatColor.RED + ChatColor.BOLD + "Bänker")) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onhit(EntityDamageByEntityEvent e) {
        if ((e.getEntity() instanceof Villager)) {
            Villager v = (Villager) e.getEntity();
            if (v.getName().equalsIgnoreCase("" + ChatColor.RED + ChatColor.BOLD + "Bänker")) {
                e.setCancelled(true);
                if ((e.getDamager() instanceof Player)) {
                    Player p = (Player) e.getDamager();

                    p.openInventory(GuiListener.getReactorByPath("Bank/Menu").getInventory(p));
                    p.playSound(p.getLocation(), Sound.LEVEL_UP, 100.0F, 100.0F);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onInteract(PlayerInteractEntityEvent e) {
        Player player = e.getPlayer();
        BankAccount acc = BankManager.instance.getBankAccount(player);
        if (acc == null) {
            player.sendMessage(ChatColor.GRAY + "Es sind keine Bankdaten vorhanden!");
            return;
        }
        if ((e.getRightClicked().getType().equals(EntityType.VILLAGER))
                && (e.getRightClicked().getName().equalsIgnoreCase("" + ChatColor.RED + ChatColor.BOLD + "Bänker"))) {
            e.setCancelled(true);

            player.openInventory(GuiListener.getReactorByPath("Bank/Menu").getInventory(player));
            player.playSound(player.getLocation(), Sound.LEVEL_UP, 100.0F, 100.0F);
        }
    }


}
