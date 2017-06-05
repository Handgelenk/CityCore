package net.nerux.city.commands;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldguard.bukkit.RegionContainer;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import net.nerux.CC;
import net.nerux.CityCore;
import net.nerux.city.User;
import net.nerux.city.UserManager;
import net.nerux.city.mayor.MayorManager;
import net.nerux.city.plot.Plot;
import net.nerux.city.town.ArchitectManager;
import net.nerux.city.town.Town;
import net.nerux.city.town.TownManager;
import net.nerux.economy.EcoAccount;
import net.nerux.economy.EcoManager;
import net.nerux.scoreboard.ScoreboardData;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;

/**
 * Created by Ben on 25.05.17.
 */
public final class MayorCommand implements CommandExecutor {

    static WorldGuardPlugin worldGuardPlugin = CityCore.getWorldGuard();
    static WorldEditPlugin worldEditPlugin = CityCore.getWorldEdit();

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        Player p = (Player) commandSender;

        Location loc = p.getLocation();

        String localTownName = ScoreboardData.getTownName(p.getLocation()).toLowerCase();
        String uuid = p.getUniqueId().toString();

        RegionContainer container = worldGuardPlugin.getRegionContainer();
        RegionManager regions = container.get(p.getWorld());

        if (MayorManager.getMayor(localTownName) == null && !p.isOp()) {
            p.sendMessage(CityCore.pr + "Diese Stadt hat keinen Bürgermeister!");
            return true;
        }

        if (MayorManager.getMayor(localTownName) != null) {
            if (!MayorManager.getMayor(localTownName).equals(uuid) && !p.isOp() && !p.hasPermission("city.admin")) {
                p.sendMessage(CityCore.pr + "Du bist nicht der Bürgermeister der Stadt §b" + localTownName);
                p.sendMessage(CityCore.pr + "Du musst in deiner Stadt sein um diesen Befehl ausführen zu können!");
                return true;
            }
        }


        Town town = TownManager.getTown(localTownName);
        if (town == null)
            return true;

        if (args.length == 0) {
            p.sendMessage(" ");
            p.sendMessage(CC.getPreLine("Mayor"));
            p.sendMessage(CC.getCommandString("mayor createplot <Preis>", "Erstellt ein neues Grundstück aus der via WE markierten" +
                    " Region (nach Markierung //expand vert eingeben und dann erst den Plot erstellen!)"));
            p.sendMessage(CC.getCommandString("mayor info <SpielerName>", "Zeigt dir Informationen über einen Spieler an!"));
            p.sendMessage(CC.getCommandString("mayor plotinfo", "Zeigt dir Informationen über den aktuellen Plot an!"));
            p.sendMessage(CC.getCommandString("mayor addarchitekt <SpielerName>", "Stellt einen Spieler als Architekt ein!"));
            p.sendMessage(CC.getCommandString("mayor firearchitekt <SpielerNme>", "Feuert einen Architekten"));
            p.sendMessage(CC.getPreLine("Mayor"));
            p.sendMessage(" ");
        } else if (args.length == 1) {
            if (args[0].equalsIgnoreCase("plotinfo")) {
                String plotID = ScoreboardData.getChunkID(loc);
                if (regions.getRegion(plotID) != null) {
                    ProtectedRegion pr = regions.getRegion(plotID);
                    p.sendMessage(CC.getPreLine(plotID));
                    for (UUID ownerUUID : pr.getOwners().getUniqueIds()) {
                        OfflinePlayer plotOwner = Bukkit.getOfflinePlayer(ownerUUID);
                        p.sendMessage(CC.getCommandString("Besitzer: ", plotOwner.getName()));
                    }
                    for (UUID memberUUID : pr.getMembers().getUniqueIds()) {
                        OfflinePlayer plotOwner = Bukkit.getOfflinePlayer(memberUUID);
                        p.sendMessage(CC.getCommandString("Member: ", plotOwner.getName()));
                    }
                    p.sendMessage(CC.getPreLine(plotID));

                }
            }
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

            } else if (args[0].equalsIgnoreCase("firearchitect")) {

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

            } else if (args[0].equalsIgnoreCase("info")) {
                System.out.println(
                        args[1]
                );
                for (User all : UserManager.instance.getUsers()) {
                    if (all != null) {
                        if (all.getName().equalsIgnoreCase(args[1])) {
                            EcoAccount ecoacc = EcoManager.instance.getEcoAccount(all.getUuid());
                            // BankAccount bankacc = BankManager.instance.getBankAccount(all.getUuid());

                            if (ecoacc == null) return true;

                            p.sendMessage(" ");
                            p.sendMessage(CC.getPreLine(all.getName()));
                            p.sendMessage("§bChunks: §7" + all.getGsMenge() + "/" + all.getMaxGs());
                            p.sendMessage("§bBargeld: §7" + ecoacc.getRoundedBalance() + "$");
                            p.sendMessage(CC.getPreLine(all.getName()));
                            p.sendMessage(" ");

                        } else {
                            p.sendMessage(CityCore.pr + "Diesen Spieler gibt es nicht!");
                        }
                    } else {
                        p.sendMessage(CityCore.pr + "Diesen Spieler gibt es nicht!");
                    }
                }


            }
        }


        return true;
    }
}
