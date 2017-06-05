package net.nerux.scoreboard;


import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import net.nerux.CityCore;
import net.nerux.city.plot.Plot;
import net.nerux.city.plot.PlotID;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.UUID;


public final class ScoreboardData {

    static WorldGuardPlugin worldGuardPlugin = CityCore.getWorldGuard();
    static WorldEditPlugin worldEditPlugin = CityCore.getWorldEdit();


    public static String getChunkType(final Location location) {
        RegionManager c = worldGuardPlugin.getRegionContainer().get(location.getWorld());
        ApplicableRegionSet regionsAtLoc = c.getApplicableRegions(location);

        String plotID = PlotID.getPlotIDFromLocation(location);
        File plotFile = Plot.getPlotFile(plotID);

        if (StringUtils.isNumeric(plotID)) {
            return "§b§lGrundstück";
        } else {
            if (plotID.length() > 2) {
                return "§b§LStadt";
            } else {
                return "§2§lWildnis";
            }
        }
    }

    public static String getChunkID(final Location location) {
        RegionManager c = worldGuardPlugin.getRegionContainer().get(location.getWorld());
        ApplicableRegionSet regionsAtLoc = c.getApplicableRegions(location);
        for (ProtectedRegion r : regionsAtLoc.getRegions()) {
            String id = r.getId();
            if (StringUtils.isNumeric(id)) {
                return id;
            }
        }
        return getTownName(location);
    }

    public static boolean isForSell(final String id) {
        YamlConfiguration cfg = YamlConfiguration.loadConfiguration(Plot.getPlotFile(id));
        return cfg.isSet("price");
    }

    public static String getPrice(final String id) {
        YamlConfiguration cfg = YamlConfiguration.loadConfiguration(Plot.getPlotFile(id));
        if (cfg.isSet("price")) {
            return cfg.get("price") + "$";
        } else {
            return "-";
        }
    }

    public static String getTownName(final Location location) {
        RegionManager c = worldGuardPlugin.getRegionContainer().get(location.getWorld());
        ApplicableRegionSet regionsAtLoc = c.getApplicableRegions(location);
        String s = regionsAtLoc.getRegions().toString();
        for (ProtectedRegion r : regionsAtLoc.getRegions()) {
            String id = r.getId();
            if (!id.equalsIgnoreCase("__global__") && !StringUtils.isNumeric(id)) {
                return id;
            }
        }
        return "§2§lWildniss";
    }

    public static String getOwner(final String id, final World w) {
        RegionManager c = worldGuardPlugin.getRegionContainer().get(w);
        ProtectedRegion region = c.getRegion(id);

        for (UUID uuid : region.getOwners().getUniqueIds()) {
            OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);
            if (player == null)
                return "";
            return player.getName();
        }
        return "";
    }

    public static boolean hasOwner(final String id, final Location location) {
        RegionManager c = worldGuardPlugin.getRegionContainer().get(location.getWorld());
        if (c.getRegion(id) == null)
            return false;
        ProtectedRegion region = c.getRegion(id);

        return (!region.getOwners().getUniqueIds().isEmpty());
    }


}
