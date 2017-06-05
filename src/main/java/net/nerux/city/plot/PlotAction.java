package net.nerux.city.plot;

import com.google.common.util.concurrent.CycleDetectingLockFactory;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.bukkit.selections.Selection;
import com.sk89q.worldguard.bukkit.RegionContainer;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.domains.DefaultDomain;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import net.nerux.CityCore;
import net.nerux.city.User;
import net.nerux.city.UserManager;
import net.nerux.city.mayor.Mayor;
import net.nerux.city.mayor.MayorManager;
import net.nerux.economy.EcoAccount;
import net.nerux.economy.EcoManager;
import net.nerux.scoreboard.ScoreboardData;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import javax.smartcardio.CardTerminal;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * Created by Ben on 25.05.17.
 */
public final class PlotAction {
    static WorldGuardPlugin worldGuardPlugin = CityCore.getWorldGuard();
    static WorldEditPlugin worldEditPlugin = CityCore.getWorldEdit();

    public static void claimPlot(final Player player, final String id) {
        if (worldGuardPlugin == null) return;
        File plotFile = Plot.getPlotFile(id);
        YamlConfiguration cfg = YamlConfiguration.loadConfiguration(plotFile);
        if (!plotFile.exists())
            return;
        User user = UserManager.instance.getUser(player);
        EcoAccount acc = EcoManager.instance.getEcoAccount(player);
        if (user == null | acc == null)
            return;
        if (Plot.isForSale(id) && cfg.isSet("price")) {
            if (!Plot.hasOwner(id)) {
                int price = Plot.getPrice(id);
                double balance = acc.getBalance();
                double after = balance - price;
                if (user.getGsMenge() >= user.getMaxGs()) {
                    player.sendMessage(CityCore.pr + "Du hast schon das Limit deiner Grundstücke erreicht!");
                    return;
                }
                if (balance < price) {
                    player.sendMessage(CityCore.pr + "§7Dir fehlen §b" + (price - balance) + "$ §7für dieses Grundstück!");
                    return;
                }

                System.out.println(ScoreboardData.getTownName(player.getLocation()));
                System.out.println(MayorManager.getMayorUUID(ScoreboardData.getTownName(player.getLocation())));

                if (MayorManager.getMayorUUID(ScoreboardData.getTownName(player.getLocation())) != null) {
                    String uuid = MayorManager.getMayorUUID(ScoreboardData.getTownName(player.getLocation()));
                    EcoAccount mayorAcc = EcoManager.instance.getEcoAccount(uuid);
                    if (mayorAcc != null) {
                        mayorAcc.addBalance(price);
                        Player plotSeller = Bukkit.getPlayer(uuid);
                        if (plotSeller != null) {
                            plotSeller.sendMessage(CityCore.pr + "Dein Grundstück §b" + id + " §7wurde an §b" + player.getName() + " verkauft!");
                        }
                    }
                }

                acc.removeBalance(price);
                user.addGsMenge(1);
                player.sendMessage(CityCore.pr + "Du hast dir erfolgreich das Grundstück §b" + id + " §7gekauft!");
            } else {
                String ownerUUID = Plot.getOwnerUniqueID(id);
                User ownerUser = UserManager.instance.getUser(ownerUUID);
                EcoAccount ownerAcc = EcoManager.instance.getEcoAccount(ownerUUID);

                if (ownerAcc == null | ownerUser == null)
                    return;

                int price = Plot.getPrice(id);
                int balance = (int) acc.getBalance();
                double after = balance - price;


                if (user.getGsMenge() >= user.getMaxGs()) {
                    player.sendMessage(CityCore.pr + "Du hast schon das Limit deiner Grundstücke erreicht!");
                    return;
                }
                if (balance < price) {
                    player.sendMessage(CityCore.pr + "§7Dir fehlen §b" + (price - balance) + "$ §7für dieses Grundstück!");
                    return;
                }

                acc.setBalance(after);
                ownerAcc.addBalance(price);
                user.addGsMenge(1);
                ownerUser.subtractGsMenge(1);
                player.sendMessage(CityCore.pr + "Du hast dir erfolgreich das Grundstück §b" + id + " §7gekauft!");

            }

            ProtectedRegion region = worldGuardPlugin.getRegionContainer().get(player.getWorld()).getRegion(id);
            DefaultDomain domain = new DefaultDomain();
            domain.addPlayer(player.getUniqueId());
            region.setOwners(domain);
            region.setMembers(domain);
            worldGuardPlugin.getRegionManager(player.getWorld()).addRegion(region);

            cfg.set("price", null);
            cfg.set("owner", player.getUniqueId().toString());
            try {
                cfg.save(plotFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void offerPlot(final Player player, final String id, final int price) {
        if (worldGuardPlugin == null) return;
        File plotFile = Plot.getPlotFile(id);
        YamlConfiguration cfg = YamlConfiguration.loadConfiguration(plotFile);
        if (!plotFile.exists())
            return;
        if (worldGuardPlugin == null)
            return;
        User user = UserManager.instance.getUser(player);
        EcoAccount acc = EcoManager.instance.getEcoAccount(player);
        if (user == null | acc == null)
            return;

        cfg.set("price", price);
        try {
            cfg.save(plotFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        player.sendMessage(CityCore.pr + "Der Chunk §b§L" + id + " §7steht nun für §b§l" + price + " §7zum Verkauf!");

    }

    public static int getRegionAmount(final Location loc) {
        RegionManager c = worldGuardPlugin.getRegionContainer().get(loc.getWorld());
        ApplicableRegionSet regionsAtLoc = c.getApplicableRegions(loc);
        return regionsAtLoc.size();
    }

    public static boolean hasRegions(final Player player) {
        if (worldEditPlugin.getSelection(player) == null) {
            return true;
        }
        Selection selection = worldEditPlugin.getSelection(player);

        Location we1 = selection.getMaximumPoint();

        RegionManager c = worldGuardPlugin.getRegionContainer().get(we1.getWorld());
        ApplicableRegionSet regionsAtLoc = c.getApplicableRegions(we1);
        return (!regionsAtLoc.getRegions().isEmpty());
    }

    public static void addMember(final Player player, final String plotID, final World world, final OfflinePlayer target) {
        if (target == null)
            return;
        UUID uuid = target.getUniqueId();

        if (!Plot.getPlotFile(plotID).exists()) {
            player.sendMessage(CityCore.pr + "Der Plot existiert nicht!");
            return;
        }

        if (StringUtils.isNumeric(plotID)) {
            RegionContainer rc = worldGuardPlugin.getRegionContainer();
            ProtectedRegion region = rc.get(world).getRegion(plotID);
            if (region != null) {
                DefaultDomain members = region.getMembers();
                members.addPlayer(uuid);
                region.setMembers(members);
                worldGuardPlugin.getRegionManager(world).addRegion(region);
                player.sendMessage(CityCore.pr + "Du hast §b" + target.getName() + " §7zum Plot §b" + plotID + " §7hinzugefügt!");
            } else {
                player.sendMessage(CityCore.pr + "Der PLOT konnte nicht gefunden werden");
            }
        }
    }

    public static void removeMember(final Player player, final String plotID, final World world, final OfflinePlayer target) {
        if (target == null)
            return;
        UUID uuid = target.getUniqueId();
        if (!Plot.getPlotFile(plotID).exists()) {
            player.sendMessage(CityCore.pr + "Der Plot existiert nicht!");
            return;
        }
        if (StringUtils.isNumeric(plotID)) {
            RegionContainer rc = worldGuardPlugin.getRegionContainer();
            ProtectedRegion region = rc.get(world).getRegion(plotID);
            if (region != null) {
                DefaultDomain members = region.getMembers();
                members.removePlayer(uuid);
                region.setMembers(members);
                worldGuardPlugin.getRegionManager(world).addRegion(region);
            } else {
                player.sendMessage(CityCore.pr + "Der PLOT konnte nicht gefunden werden");
            }
        }
    }
}
