package net.nerux.city.town;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldguard.bukkit.RegionContainer;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.domains.DefaultDomain;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import net.nerux.CityCore;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * Created by Ben on 25.05.17.
 */
public final class ArchitectManager {


    public final static File path = new File("plugins/CityCore/TownData");
    public static File architects = new File(path, "architects.yml");
    public static YamlConfiguration cfg = YamlConfiguration.loadConfiguration(architects);

    static WorldGuardPlugin worldGuardPlugin = CityCore.getWorldGuard();
    static WorldEditPlugin worldEditPlugin = CityCore.getWorldEdit();


    public static boolean canBuild(final String uuid, final String townName) {
        if (cfg.isSet(uuid)) {
            return cfg.getString(uuid).equalsIgnoreCase(townName);
        }
        return false;
    }

    public static boolean isArchitect(final String uuid) {
        return cfg.isSet(uuid);
    }

    public static String getEmployer(final String uuid) {
        if (isArchitect(uuid)) {
            return cfg.getString(uuid);
        }
        return null;
    }

    public static void addArchitekt(final String townName, final String uuid, final World world) {
        if (path.exists()) {
            path.mkdir();
        }
        if (!architects.exists()) {
            try {
                architects.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }


        RegionContainer container = worldGuardPlugin.getRegionContainer();
        RegionManager regions = container.get(world);
        ProtectedRegion town = regions.getRegion(townName);
        DefaultDomain members = town.getMembers();
        members.addPlayer(uuid);
        town.setMembers(members);
        regions.addRegion(town);

        if (town == null)
            return;


        cfg.set(uuid, townName);
        try {
            cfg.save(architects);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void removeArchitect(final String townName, final String uuid, final World world) {
        if (path.exists()) {
            path.mkdir();
        }
        if (!architects.exists()) {
            try {
                architects.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        RegionContainer container = worldGuardPlugin.getRegionContainer();
        RegionManager regions = container.get(world);
        ProtectedRegion town = regions.getRegion(townName);
        DefaultDomain members = town.getMembers();
        members.removePlayer(uuid);
        town.setMembers(members);
        regions.addRegion(town);

        cfg.set(uuid, null);
        try {
            cfg.save(architects);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
