package net.nerux.city.plot;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldguard.bukkit.RegionContainer;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import net.nerux.CityCore;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.file.YamlConfigurationOptions;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;

/**
 * Created by Ben on 25.05.17.
 */
public final class PlotID {

    static WorldGuardPlugin worldGuardPlugin = CityCore.getWorldGuard();
    static WorldEditPlugin worldEditPlugin = CityCore.getWorldEdit();

    static File pathFile = new File("plugins/CityCore/PlotData");


    public static String getNewPlotID() {
        if (!pathFile.exists()) {
            pathFile.mkdir();
        }
        File file = getPlotIDFile();
        YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            cfg.set("id", 1);
            try {
                cfg.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        int id = cfg.getInt("id");
        return "" + id;
    }

    public static void updatePlotIDs() {
        File pathFile = new File("Plots");
        if (!pathFile.exists()) {
            pathFile.mkdir();
        }
        File file = getPlotIDFile();
        YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            cfg.set("id", 1);
            try {
                cfg.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }
        int id = cfg.getInt("id");
        int newId = id + 1;
        cfg.set("id", newId);
        try {
            cfg.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static File getPlotIDFile() {
        if (!pathFile.exists()) {
            pathFile.mkdir();
        }
        return new File(pathFile, "plots.yml");
    }

    public static String getPlotIDFromLocation(final Location loc) {
        RegionManager c = worldGuardPlugin.getRegionContainer().get(loc.getWorld());
        ApplicableRegionSet regionsAtLoc = c.getApplicableRegions(loc);
        String s = regionsAtLoc.getRegions().toString();
        regionsAtLoc.getRegions();
        for (ProtectedRegion r : regionsAtLoc.getRegions()) {
            String id = r.getId();
            return id;
        }
        return s;
    }

}
