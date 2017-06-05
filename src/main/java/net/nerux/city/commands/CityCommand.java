package net.nerux.city.commands;

import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldguard.bukkit.RegionContainer;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.domains.DefaultDomain;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import net.nerux.CC;
import net.nerux.CityCore;
import net.nerux.city.plot.Plot;
import net.nerux.city.plot.PlotAction;
import net.nerux.city.plot.PlotID;
import net.nerux.scoreboard.CityScoreboard;
import net.nerux.scoreboard.ScoreboardData;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scoreboard.Score;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by Ben on 23.05.17.
 */
public final class CityCommand implements CommandExecutor {

    static WorldGuardPlugin worldGuardPlugin = CityCore.getWorldGuard();
    static WorldEditPlugin worldEditPlugin = CityCore.getWorldEdit();

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        Player player = (Player) commandSender;

        RegionContainer container = worldGuardPlugin.getRegionContainer();
        RegionManager regions = container.get(player.getWorld());

        if (worldEditPlugin == null) {
            player.sendMessage("WE IST NULL");
            return true;
        }
        if (worldGuardPlugin == null) {
            player.sendMessage("WG IST NULL");
            return true;
        }
        if (args.length == 0) {
            player.sendMessage(" ");
            player.sendMessage(CC.getPreLine("CITY"));

            player.sendMessage(CC.getCommandString("/city kaufen", "Kauft ein Grundstück!"));
            player.sendMessage(CC.getCommandString("/city verkaufen <PREIS>", "Kauft ein Grundstück!"));
            player.sendMessage(CC.getCommandString("/city member add <PlotID> <Spieler>", "Fügt einen Spieler zu deinem Grundstück hinzu!"));
            player.sendMessage(CC.getCommandString("/city member remove <PlotID> <Spieler>", "Entfernt einen Spieler von deinem Grundstück!"));
            player.sendMessage(CC.getCommandString("/city plotinfo", "Zeigt dir die PlotInfo an!"));
            player.sendMessage(CC.getPreLine("CITY"));
            player.sendMessage(" ");

        } else if (args.length == 1) {
            if (args[0].equalsIgnoreCase("kaufen")) {
                String id = ScoreboardData.getChunkID(player.getLocation());
                if (StringUtils.isNumeric(id)) {
                    PlotAction.claimPlot(player, id);
                }
            } else if (args[0].equalsIgnoreCase("plotinfo")) {
                String plotID = ScoreboardData.getChunkID(player.getLocation());
                if (regions.getRegion(plotID) != null) {
                    ProtectedRegion pr = regions.getRegion(plotID);
                    player.sendMessage(CC.getPreLine(plotID));
                    for (UUID ownerUUID : pr.getOwners().getUniqueIds()) {
                        OfflinePlayer plotOwner = Bukkit.getOfflinePlayer(ownerUUID);
                        player.sendMessage(CC.getCommandString("Besitzer: ", plotOwner.getName()));
                    }
                    for (UUID memberUUID : pr.getMembers().getUniqueIds()) {
                        OfflinePlayer plotOwner = Bukkit.getOfflinePlayer(memberUUID);
                        player.sendMessage(CC.getCommandString("Member: ", plotOwner.getName()));
                    }
                    player.sendMessage(CC.getPreLine(plotID));

                }
            }
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("verkaufen")) {
                String id = PlotID.getPlotIDFromLocation(player.getLocation());
                int price = Integer.parseInt(args[1]);
                if (StringUtils.isNumeric(id)) {
                    PlotAction.offerPlot(player, id, price);
                }
            }
        } else if (args.length == 4) {
            if (args[0].equalsIgnoreCase("member")) {
                if (args[1].equalsIgnoreCase("add")) {
                    String plotID = args[2];
                    OfflinePlayer target = Bukkit.getOfflinePlayer(args[3]);
                    PlotAction.addMember(player, plotID, player.getWorld(), target);
                } else if (args[1].equalsIgnoreCase("remove")) {
                    String plotID = args[2];
                    OfflinePlayer target = Bukkit.getOfflinePlayer(args[3]);
                    PlotAction.removeMember(player, plotID, player.getWorld(), target);
                    player.sendMessage(CityCore.pr + "Du hast §b" + target.getName() + " §7vom Plot §b" + plotID + " §7entfernt!");
                }
            }

        }

        return true;
    }
}
