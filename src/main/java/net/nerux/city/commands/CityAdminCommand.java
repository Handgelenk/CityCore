package net.nerux.city.commands;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import net.nerux.CC;
import net.nerux.CityCore;
import net.nerux.city.mayor.MayorManager;
import net.nerux.city.plot.Plot;
import net.nerux.city.town.ArchitectManager;
import net.nerux.city.town.TownManager;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.IOException;

/**
 * Created by Ben on 25.05.17.
 */
public final class CityAdminCommand implements CommandExecutor {

    static WorldGuardPlugin worldGuardPlugin = CityCore.getWorldGuard();
    static WorldEditPlugin worldEditPlugin = CityCore.getWorldEdit();

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        Player player = (Player) commandSender;

        if (!player.hasPermission("city.admin"))
            return true;
        if (worldEditPlugin == null) {
            player.sendMessage("§cAuf dem Server muss das Plugin §4WorldEdit §cinstalliert sein!");
            return true;
        }
        if (worldGuardPlugin == null) {
            player.sendMessage("§cAuf dem Server muss das Plugin §4WorldGuard §cinstalliert sein!");
            return true;
        }

        if (args.length == 0) {
            player.sendMessage(CC.getPreLine("CityAdmin"));
            player.sendMessage(CC.getCommandString("town createplot <PRICE>", "Erstellt einen Plot!"));
            player.sendMessage(CC.getCommandString("town createtown <TOWNNAME>", "Erstellt einen Plot!"));
            player.sendMessage(CC.getCommandString("town setspawn <TOWNNAME>", "Erstellt den Spawn der Stadt"));
            player.sendMessage(CC.getCommandString("town setmayor <TOWNNAME> <PLAYER>", "Ernennt den Bürgermeister der Stadt!"));
            player.sendMessage(CC.getCommandString("town addarchitect <TOWNNAME> <PLAYER>", "Fügt einen Architekten hinzu!"));
            player.sendMessage(CC.getCommandString("town removearchitect <TOWNNAME> <PLAYER>", "Entfernt einen Architekten!"));
            player.sendMessage(CC.getPreLine("CityAdmin"));
        } else if (args.length == 2) {

            if (args[0].equalsIgnoreCase("createplot")) {
                try {
                    int price = Integer.parseInt(args[1]);
                    Plot.createPlot(player, price);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (NumberFormatException e) {
                    player.sendMessage(CityCore.pr + "§b" + args[1] + " §7ist keine Zahl oder sie ist zu groß..");
                    e.printStackTrace();
                }

            } else if (args[0].equalsIgnoreCase("createtown")) {
                String id = args[1];
                TownManager.createTown(id, player);
            } else if (args[0].equalsIgnoreCase("setspawn")) {
                String id = args[1];

                System.out.println(id);

                if (!TownManager.exists(id)) {
                    player.sendMessage(CityCore.pr + "Diese Stadt gibt es nicht!");
                    return true;
                }

                TownManager.setSpawn(player.getLocation(), id);
                player.sendMessage(CityCore.pr + "Du hast den Spawn gesetzt!");
            }

        } else if (args.length == 3) {

            if (args[0].equalsIgnoreCase("setmayor")) {
                String id = args[1];
                Player owner = Bukkit.getPlayer(args[2]);

                System.out.println(id);
                System.out.println(owner.getName());

                if (owner == null)
                    return true;

                if (!TownManager.exists(id)) {
                    player.sendMessage(CityCore.pr + "Diese Stadt gibt es nicht!");
                    return true;
                }

                MayorManager.setMayor(owner, id);
                player.sendMessage(CityCore.pr + "Du hast den Owner gesetzt!");
            } else if (args[0].equalsIgnoreCase("addarchitect")) {
                String id = args[1];
                Player owner = Bukkit.getPlayer(args[2]);
                System.out.println(id);
                if (ArchitectManager.isArchitect(owner.getUniqueId().toString())) {
                    player.sendMessage(CityCore.pr + "Dieser Spieler ist schon Architekt von §b" + ArchitectManager.getEmployer(owner.getUniqueId().toString()));
                    return true;
                }

                if (owner == null) {
                    player.sendMessage(CityCore.pr + "Der Spieler muss online sein!");
                    return true;
                }

                if (!TownManager.exists(id)) {
                    player.sendMessage(CityCore.pr + "Die Stadt existiert nicht!");
                    return true;
                }
                ArchitectManager.addArchitekt(id, owner.getUniqueId().toString(), player.getWorld());
                player.sendMessage(CityCore.pr + "§b" + owner.getName() + " §7ist nun Architekt von §b" + id);

            } else if (args[0].equalsIgnoreCase("removearchitect")) {
                String id = args[1];
                OfflinePlayer owner = Bukkit.getOfflinePlayer(args[2]);

                if (!ArchitectManager.isArchitect(owner.getUniqueId().toString())) {
                    player.sendMessage(CityCore.pr + "Dieser Spieler ist kein Architekt!");
                    return true;
                }

                if (!TownManager.exists(id)) {
                    player.sendMessage(CityCore.pr + "Die Stadt existiert nicht!");
                    return true;
                }

                ArchitectManager.removeArchitect(id, owner.getUniqueId().toString(), player.getWorld());
                player.sendMessage(CityCore.pr + "§b" + owner.getName() + " §7ist nun kein Architekt mehr von §b" + id);

            }

        }

        return false;
    }
}
