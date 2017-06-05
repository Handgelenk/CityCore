package net.nerux.economy;

import javafx.print.PageLayout;
import net.nerux.CC;
import net.nerux.CityCore;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.swing.text.PlainDocument;

/**
 * Created by Ben on 31.05.17.
 */
public final class EconomyCommand implements CommandExecutor {

    String prefix = CityCore.ecopr;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        Player player = (Player) sender;

        if (!player.hasPermission("city.economy.admin")) {
            player.sendMessage(prefix + "Du hast nicht die Rechte dazu!");
            return true;
        }

        if (args.length == 0) {
            player.sendMessage("");
            player.sendMessage(CC.getPreLine("ECONOMY"));
            player.sendMessage(CC.getCommandString("eco info <Player>", "Zeigt den Kontostand eines Spielers!"));
            player.sendMessage(CC.getCommandString("eco reset <Player>", "Setzt den Kontostand eines Spielers zurück!"));
            player.sendMessage(CC.getCommandString("eco add <Player> <ANZAHL>", "Addet den Kontostand eines Spielers!"));
            player.sendMessage(CC.getCommandString("eco remove <Player> <ANZAHL>", "Removed den Kontostand eines Spielers!"));
            player.sendMessage(CC.getCommandString("eco set <Player> <ANZAHL>", "Setzt den Kontostand eines Spielers!"));

            player.sendMessage(CC.getPreLine("ECONOMY"));
            player.sendMessage("");

        } else if (args.length == 1) {

        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("info")) {

                OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);

                if (target == null) {
                    sender.sendMessage(prefix + "Dieser Spieler existiert nicht.");
                    return false;
                }

                EcoAccount account = EcoManager.instance.getEcoAccount(target);

                if (account == null) {
                    sender.sendMessage("");
                    sender.sendMessage(
                            prefix + "Der Spieler §c" + target.getName() + ChatColor.GRAY + " hat kein Economy-Konto...");
                    sender.sendMessage("");
                } else {
                    sender.sendMessage("");
                    sender.sendMessage(prefix + "Bargeld von " + ChatColor.AQUA + target.getName() + ChatColor.GRAY
                            + ": " + ChatColor.GRAY + account.getRoundedBalance() + "$");

                    sender.sendMessage("");
                }
            } else if (args[0].equalsIgnoreCase("reset")) {

                OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);

                if (target == null) {
                    sender.sendMessage(prefix + "Dieser Spieler Existiert nicht!");
                    return false;
                }

                EcoAccount account = EcoManager.instance.getEcoAccount(target);

                if (account == null) {
                    sender.sendMessage("");
                    sender.sendMessage(
                            prefix + "" + ChatColor.AQUA + args[1] + ChatColor.GRAY + " hat kein EconomyKonto!");
                    sender.sendMessage("");
                } else {
                    EcoManager.instance.resetEcoAccount(target);
                    sender.sendMessage(
                            prefix + "Du hast das Konto von " + ChatColor.AQUA + target.getName() + ChatColor.GRAY + " resetet!");
                }
            }
        } else if (args.length == 3) {
            if (args[0].equalsIgnoreCase("add")) {
                try {
                    OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
                    double i = Double.parseDouble(args[2]);

                    if (target == null) {
                        sender.sendMessage(prefix + "Dieser Spieler Existiert nicht!");
                        return false;
                    }

                    EcoAccount account = EcoManager.instance.getEcoAccount(target);

                    if (account == null) {
                        sender.sendMessage("");
                        sender.sendMessage(prefix + "Der Spieler " + ChatColor.AQUA + target.getName()
                                + ChatColor.GRAY + " hat kein Economy-Konto...");
                        sender.sendMessage("");
                    } else {
                        account.addBalance(i);
                        sender.sendMessage(prefix + "Du hast das Geld von " + ChatColor.AQUA + target.getName()
                                + ChatColor.GRAY + " auf " + ChatColor.AQUA + i + "$ " + ChatColor.DARK_GRAY
                                + " gesetzt!");
                    }
                } catch (NumberFormatException ex) {
                    ex.printStackTrace();
                    player.sendMessage(prefix + "Nur Zahlen!");
                }

            } else if (args[0].equalsIgnoreCase("remove")) {
                try {
                    OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
                    double i = Double.parseDouble(args[2]);

                    if (target == null) {
                        sender.sendMessage(prefix + "Dieser Spieler Existiert nicht!");
                        return false;
                    }

                    EcoAccount account = EcoManager.instance.getEcoAccount(target);

                    if (account == null) {
                        sender.sendMessage("");
                        sender.sendMessage(prefix + "Der Spieler " + ChatColor.AQUA + target.getName()
                                + ChatColor.GRAY + " hat kein Economy-Konto...");
                        sender.sendMessage("");
                    } else {
                        account.removeBalance(i);
                        sender.sendMessage(prefix + "Du hast das Geld von " + ChatColor.AQUA + target.getName()
                                + ChatColor.GRAY + " auf " + ChatColor.AQUA + i + "$ " + ChatColor.DARK_GRAY
                                + " gesetzt!");
                    }
                } catch (NumberFormatException ex) {
                    ex.printStackTrace();
                    player.sendMessage(prefix + "Nur Zahlen!");
                }

            } else if (args[0].equalsIgnoreCase("set")) {
                try {
                    OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
                    double i = Double.parseDouble(args[2]);

                    if (target == null) {
                        sender.sendMessage(prefix + "Dieser Spieler Existiert nicht!");
                        return false;
                    }

                    EcoAccount account = EcoManager.instance.getEcoAccount(target);

                    if (account == null) {
                        sender.sendMessage("");
                        sender.sendMessage(prefix + "Der Spieler " + ChatColor.AQUA + target.getName()
                                + ChatColor.GRAY + " hat kein Economy-Konto...");
                        sender.sendMessage("");
                    } else {
                        account.setBalance(i);
                        sender.sendMessage(prefix + "Du hast das Geld von " + ChatColor.AQUA + target.getName()
                                + ChatColor.GRAY + " auf " + ChatColor.AQUA + i + "$ " + ChatColor.DARK_GRAY
                                + " gesetzt!");
                    }
                } catch (NumberFormatException ex) {
                    ex.printStackTrace();
                    player.sendMessage(prefix + "Nur Zahlen!");
                }

            }
        }


        return true;
    }
}
