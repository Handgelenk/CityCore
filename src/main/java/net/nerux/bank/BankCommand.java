package net.nerux.bank;

import net.nerux.CC;
import net.nerux.CityCore;
import net.nerux.Command;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;

import java.util.Date;

public final class BankCommand extends Command {
    public BankCommand(String command) {
        super(command);
        addSubCommand(new Info("info"));
        addSubCommand(new Reset("reset"));
        addSubCommand(new ResetCredit("reset-credit"));
        addSubCommand(new SetMoney("set-money"));
        addSubCommand(new SetVillager("set-bänker"));
    }

    public boolean execute(CommandSender sender, org.bukkit.command.Command cmd, String[] args, final Player player) {
        sender.sendMessage(" ");
        sender.sendMessage(CC.getPreLine("Bank"));
        for (Command sub : getSubCommand()) {
            sender.sendMessage(ChatColor.YELLOW + "- " + getUsage() + " " + sub.getUsage() + "  " + ChatColor.GRAY
                    + sub.getDescription());
        }
        sender.sendMessage(CC.getPreLine("Bank"));
        sender.sendMessage(" ");
        return false;
    }

    public String getDescription() {
        return null;
    }

    public String getUsage() {
        return "/" + getCommandName();
    }

    public boolean onlyOnlinePlayer() {
        return false;
    }

    private static class Info extends Command {

        public Info(String command) {
            super(command);
        }

        @SuppressWarnings("deprecation")
        public boolean execute(CommandSender sender, org.bukkit.command.Command cmd, String[] args, Player player) {
            Date today = new Date(System.currentTimeMillis());
            if (args.length == 0) {
                if (player == null) {
                    sender.sendMessage("Du bist die Console.");
                    return false;
                }

                if (!player.hasPermission("city.bank.info")) {
                    player.sendMessage(CityCore.bankpr + "Dazu hast du keine Berechtigung!");
                    return true;
                }

                BankAccount account = BankManager.instance.getBankAccount(player);

                if (account == null) {
                    sender.sendMessage("");
                    sender.sendMessage(CityCore.bankpr + "Du besitzt derzeit noch kein Bank-Konto.");
                    sender.sendMessage("");
                } else {
                    sender.sendMessage("");
                    sender.sendMessage(CityCore.bankpr + "Bank-Konto von " + ChatColor.AQUA + player.getName());
                    sender.sendMessage(
                            ChatColor.GRAY + " Guthaben: " + ChatColor.AQUA + account.getRoundedBalance() + "$");
                    sender.sendMessage(ChatColor.GRAY + " Kredit: " + ChatColor.AQUA
                            + (account.hasCredit() ? account.getCredit().getType() : "Kein Kredit"));
                    if (account.hasCredit()) {
                        sender.sendMessage(ChatColor.GRAY + " Überzogen: " + ChatColor.AQUA
                                + (account.getCredit().getBack().before(today) ? ChatColor.DARK_GREEN + "Yes"
                                : ChatColor.RED + "No"));
                    }

                    sender.sendMessage("");
                    return true;
                }

                return true;
            }

            OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);

            if (target == null) {
                sender.sendMessage(CityCore.bankpr + "Dieser Spieler existiert nicht.");
                return false;
            }

            BankAccount account = BankManager.instance.getBankAccount(target);

            if (account == null) {
                sender.sendMessage("");
                sender.sendMessage(CityCore.bankpr + "Der Spieler " + ChatColor.RED + target.getName() + ChatColor.GRAY
                        + " hat kein Bank Konto...");
                sender.sendMessage("");
            } else {
                sender.sendMessage("");
                sender.sendMessage(CityCore.bankpr + "Bank-Konto von " + ChatColor.AQUA + target.getName());
                sender.sendMessage(ChatColor.GRAY + " Guthaben: " + ChatColor.AQUA + account.getRoundedBalance() + "$");
                sender.sendMessage(ChatColor.GRAY + " Kredit: " + ChatColor.AQUA
                        + (account.hasCredit() ? account.getCredit().getType() : "Kein Kredit"));
                if (account.hasCredit()) {
                    sender.sendMessage(ChatColor.GRAY + " Überzogen: " + ChatColor.AQUA
                            + (account.getCredit().getBack().before(today) ? ChatColor.DARK_GREEN + "Yes"
                            : ChatColor.RED + "No"));
                }

                sender.sendMessage("");
            }

            return false;
        }

        public String getDescription() {
            return "Zeigt dir Infos an";
        }

        public String getUsage() {
            return getCommandName();
        }

        public boolean onlyOnlinePlayer() {
            return false;
        }
    }

    private static class Reset extends Command {
        public Reset(String command) {
            super(command);
        }

        @SuppressWarnings("deprecation")
        public boolean execute(CommandSender sender, org.bukkit.command.Command cmd, String[] args, Player player) {
            if (args.length == 0) {
                if (player == null) {
                    sender.sendMessage("Du bist die Console.");
                    return false;
                }

                BankManager.instance.resetBankAccount(player);
                sender.sendMessage(CityCore.bankpr + "Du hast das Konto resetet!");

                return true;
            }

            OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);

            if (target == null) {
                sender.sendMessage("Dieser Spieler Existiert nci...sad.as");
                return false;
            }

            BankAccount account = BankManager.instance.getBankAccount(target);

            if (account == null) {
                sender.sendMessage("");
                sender.sendMessage("Der Spieler " + target.getName() + " hat kein Bank Konto...");
                sender.sendMessage("");
            } else {
                BankManager.instance.resetBankAccount(target);
                sender.sendMessage(CityCore.bankpr + "Du hast das Konto resetet!");
            }

            return false;
        }

        public String getDescription() {
            return "Resetet einen Bankaccount";
        }

        public String getUsage() {
            return getCommandName();
        }

        public boolean onlyOnlinePlayer() {
            return false;
        }
    }

    private static class ResetCredit extends Command {
        public ResetCredit(String command) {
            super(command);
        }

        @SuppressWarnings("deprecation")
        public boolean execute(CommandSender sender, org.bukkit.command.Command cmd, String[] args, Player player) {
            if (args.length == 0) {
                if (player == null) {
                    sender.sendMessage("Du bist die Console.");
                    return false;
                }
                BankAccount account = BankManager.instance.getBankAccount(player);

                if (account == null) {
                    sender.sendMessage("");
                    sender.sendMessage(CityCore.bankpr + "Du besitzt derzeit noch kein Bank-Konto.");
                    sender.sendMessage("");
                } else {
                    account.setCredit(null);
                    sender.sendMessage(CityCore.bankpr + "Dein Kredit wurde resetet!");
                }

                return true;
            }

            OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
            if (target == null) {
                sender.sendMessage(CityCore.bankpr + "Dieser Spieler Existiert nicht!");
                return false;
            }

			/* 260 */
            BankAccount account = BankManager.instance.getBankAccount(target);
            if (account == null) {
                sender.sendMessage("");
                sender.sendMessage(CityCore.bankpr + "Der Spieler " + ChatColor.AQUA + target.getName() + ChatColor.GRAY
                        + " hat kein Bank Konto...");
                sender.sendMessage("");
            } else {
                account.setCredit(null);
                sender.sendMessage(CityCore.bankpr + "Kredit erfolgreich resetet!");
            }

            return false;
        }

        public String getDescription() {
            return "Löscht einen Kredit";
        }

        public String getUsage() {
            return getCommandName();
        }

        public boolean onlyOnlinePlayer() {
            return false;
        }
    }

    private static class SetMoney extends Command {
        public SetMoney(String command) {
            super(command);
        }

        @SuppressWarnings("deprecation")
        public boolean execute(CommandSender sender, org.bukkit.command.Command cmd, String[] args, Player player) {
            if (args.length == 2) {
                OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
                double i = Double.parseDouble(args[1]);

                if (target == null) {
                    sender.sendMessage(CityCore.bankpr + "Dieser Spieler Existiert nihct!");
                    return false;
                }

                BankAccount account = BankManager.instance.getBankAccount(target);

                if (account == null) {
                    sender.sendMessage("");
                    sender.sendMessage(CityCore.bankpr + "Der Spieler " + ChatColor.AQUA + target.getName() + ChatColor.GRAY
                            + " hat kein Bank Konto...");
                    sender.sendMessage("");
                } else {
                    account.setBalance(i);
                    sender.sendMessage(CityCore.bankpr + "Du hast das Geld von " + ChatColor.AQUA + target.getName()
                            + ChatColor.GRAY + " auf " + ChatColor.AQUA + ChatColor.BOLD + i + "$ " + ChatColor.GRAY
                            + " gesetzt!");
                }
            }

            return false;
        }

        public String getDescription() {
            return "Setzt das Geld eines Spielers";
        }

        public String getUsage() {
            return getCommandName();
        }

        public boolean onlyOnlinePlayer() {
            return false;
        }
    }

    private static class SetVillager extends Command {
        public SetVillager(String command) {
            super(command);
        }

        @SuppressWarnings("deprecation")
        public boolean execute(CommandSender sender, org.bukkit.command.Command cmd, String[] args, Player player) {
            Player p = (Player) sender;
            Location loc = p.getLocation();
            Villager v = (Villager) loc.getWorld().spawnCreature(p.getLocation(), EntityType.VILLAGER);

            v.setCustomName("" + ChatColor.RED + ChatColor.BOLD + "Bänker");
            v.setCustomNameVisible(true);
            v.setFireTicks(0);
            v.setHealth(20.0D);
            v.setAdult();
            p.sendMessage(CityCore.bankpr + "Du hast den" + ChatColor.AQUA + " Bänker  " + ChatColor.GRAY + "gesetzt!");
            return true;
        }

        public String getDescription() {
            return "Setzt den Bänker";
        }

        public String getUsage() {
            return getCommandName();
        }

        public boolean onlyOnlinePlayer() {
            return false;
        }
    }

}
