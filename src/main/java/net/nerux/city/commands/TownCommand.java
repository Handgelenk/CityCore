package net.nerux.city.commands;

import com.sk89q.worldedit.bukkit.selections.Selection;
import net.nerux.CC;
import net.nerux.CityCore;
import net.nerux.city.mayor.MayorManager;
import net.nerux.city.plot.Plot;
import net.nerux.city.plot.PlotAction;
import net.nerux.city.plot.PlotID;
import net.nerux.city.town.ArchitectManager;
import net.nerux.city.town.Town;
import net.nerux.city.town.TownManager;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Ben on 23.05.17.
 */
public final class TownCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        /** Player p = (Player) commandSender;
         if (!p.hasPermission("city.admin"))
         return true;

         if (args.length == 0) {

         p.sendMessage(CC.getPreLine("Town"));
         p.sendMessage(CC.getCommandString("town createplot <PRICE>", "Erstellt einen Plot!"));
         p.sendMessage(CC.getCommandString("town createtown <TOWNNAME>", "Erstellt einen Plot!"));
         p.sendMessage(CC.getCommandString("town setspawn <TOWNNAME>", "Erstellt den Spawn der Stadt"));
         p.sendMessage(CC.getCommandString("town setmayor <TOWNNAME> <PLAYER>", "Ernennt den Bürgermeister der Stadt!"));
         p.sendMessage(CC.getCommandString("town addarchitect <TOWNNAME> <PLAYER>", "Fügt einen Architekten hinzu!"));
         p.sendMessage(CC.getCommandString("town removearchitect <TOWNNAME> <PLAYER>", "Entfernt einen Architekten!"));
         p.sendMessage(CC.getPreLine("Town"));

         } else if (args.length == 2) {

         if (args[0].equalsIgnoreCase("createplot")) {
         try {
         int price = Integer.parseInt(args[1]);
         Plot.createPlot(p, price);
         } catch (IOException e) {
         e.printStackTrace();
         } catch (NumberFormatException e) {
         p.sendMessage(CityCore.pr + "§b" + args[1] + " §7ist keine Zahl oder sie ist zu groß..");
         e.printStackTrace();
         }

         } else if (args[0].equalsIgnoreCase("createtown")) {
         String id = args[1];
         TownManager.createTown(id, p);
         } else if (args[0].equalsIgnoreCase("setspawn")) {
         String id = args[1];

         System.out.println(id);

         if (!TownManager.exists(id)) {
         p.sendMessage(CityCore.pr + "Diese Stadt gibt es nicht!");
         return true;
         }

         TownManager.setSpawn(p.getLocation(), id);
         p.sendMessage(CityCore.pr + "Du hast den Spawn gesetzt!");
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
         p.sendMessage(CityCore.pr + "Diese Stadt gibt es nicht!");
         return true;
         }

         MayorManager.setMayor(owner, id);
         p.sendMessage(CityCore.pr + "Du hast den Owner gesetzt!");
         } else if (args[0].equalsIgnoreCase("addarchitect")) {
         String id = args[1];
         Player owner = Bukkit.getPlayer(args[2]);
         System.out.println(id);
         if (ArchitectManager.isArchitect(owner.getUniqueId().toString())) {
         p.sendMessage(CityCore.pr + "Dieser Spieler ist schon Architekt von §b" + ArchitectManager.getEmployer(owner.getUniqueId().toString()));
         return true;
         }

         if (owner == null) {
         p.sendMessage(CityCore.pr + "Der Spieler muss online sein!");
         return true;
         }

         if (!TownManager.exists(id)) {
         p.sendMessage(CityCore.pr + "Die Stadt existiert nicht!");
         return true;
         }
         ArchitectManager.addArchitekt(id, owner.getUniqueId().toString(), p.getWorld());
         p.sendMessage(CityCore.pr + "§b" + owner.getName() + " §7ist nun Architekt von §b" + id);

         } else if (args[0].equalsIgnoreCase("removearchitect")) {
         String id = args[1];
         OfflinePlayer owner = Bukkit.getOfflinePlayer(args[2]);

         if (!ArchitectManager.isArchitect(owner.getUniqueId().toString())) {
         p.sendMessage(CityCore.pr + "Dieser Spieler ist kein Architekt!");
         return true;
         }

         if (!TownManager.exists(id)) {
         p.sendMessage(CityCore.pr + "Die Stadt existiert nicht!");
         return true;
         }

         ArchitectManager.removeArchitect(id, owner.getUniqueId().toString(), p.getWorld());
         p.sendMessage(CityCore.pr + "§b" + owner.getName() + " §7ist nun kein Architekt mehr von §b" + id);

         }

         }


         */
        return true;
    }

}
