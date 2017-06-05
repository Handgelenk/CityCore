package net.nerux.bank;

import net.nerux.CityCore;
import net.nerux.utils.MathUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class ZinsManager {

    public static ZinsManager instance;

    private BukkitRunnable runner;
    private Stats stats;

    public ZinsManager() {
        instance = this;

        runner = new BukkitRunnable() {

            @Override
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {

                    switch (stats) {
                        case BREAK:
                            return;
                        case STOP:
                            return;
                        default:
                            break;
                    }

                    BankAccount account = BankManager.instance.getBankAccount(player);
                    if (account == null) {
                        return;
                    }
                    if (account.getCount() >= 60) {
                        account.setCount(0);
                        double more = 0.015 * account.getBalance();
                        account.addBalance(more);
                        player.sendMessage(CityCore.bankpr + "Du hast " + ChatColor.AQUA + MathUtils.round(more) + "$"
                                + ChatColor.GRAY + " Zinsgutschrift erhalten, dein Kontostand beträgt nun "
                                + ChatColor.AQUA + MathUtils.round(account.getBalance()) + "$");
                    } else {
                        account.addCount();
                    }
                }

            }
        };

    }

    public Stats getStats() {
        return stats;
    }

    public void start() {
        stats = Stats.RUN;
        runner.runTaskTimerAsynchronously(CityCore.instance, 0, 20 * 60);
    }

    public void stop() {
        stats = Stats.STOP;
        runner.cancel();
    }

    private static enum Stats {
        RUN, BREAK, STOP;
    }

}
