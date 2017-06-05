package net.nerux.city.mayor;

import com.google.common.base.Preconditions;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldguard.bukkit.RegionContainer;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.domains.DefaultDomain;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import net.nerux.CityCore;
import net.nerux.city.town.Town;
import net.nerux.city.town.TownManager;
import net.nerux.scoreboard.ScoreboardData;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.UUID;


/**
 * Created by Ben on 31.05.17.
 **/
public final class MayorManager {

    private Mayor mayor;

    static File path = new File("plugins/CityCore/TownData");
    static File mayorsFile = new File(path, "mayors.yml");
    static YamlConfiguration cfg = YamlConfiguration.loadConfiguration(mayorsFile);

    static WorldGuardPlugin worldGuardPlugin = CityCore.getWorldGuard();
    static WorldEditPlugin worldEditPlugin = CityCore.getWorldEdit();

    public MayorManager(final Mayor mayor) {
        Preconditions.checkNotNull(mayor, "The mayor cannot be null!");
        this.mayor = mayor;
    }

    public Mayor getMayor() {
        return mayor;
    }

    public void setMayor(Mayor mayor) {
        this.mayor = mayor;
    }

    public static Mayor getMayor(final String townName) {

        if (cfg.isSet(townName)) {
            String mayorUUID = cfg.getString(townName);
            Mayor mayor = new Mayor(UUID.fromString(mayorUUID), townName);
            return mayor;
        }
        return null;
    }

    public static String getMayorUUID(final String townName) {
        System.out.println(townName);
        if (cfg.isSet(townName)) {
            return cfg.getString(townName);
        }
        return null;
    }

    public static Town getTownFromMayor(final Mayor mayor) {
        Preconditions.checkNotNull(mayor, "The mayor cannot be null!");
        Town town = TownManager.getTown(mayor.getTownName());
        return town;
    }

    public static boolean isMayorFromTownAtLocation(final UUID uuid, final Location location) {

        String plotID = ScoreboardData.getChunkID(location);

        RegionContainer container = worldGuardPlugin.getRegionContainer();
        RegionManager regions = container.get(location.getWorld());
        ProtectedRegion pr = regions.getRegion(plotID);

        if (pr == null)
            return false;

        return pr.getOwners().getUniqueIds().contains(uuid);
    }

    public static void setMayor(final Player player, final String townName) {
        if (!path.exists()) {
            path.mkdirs();
        }
        if (!mayorsFile.exists()) {
            try {
                mayorsFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        String uuid = player.getUniqueId().toString();

        RegionContainer container = worldGuardPlugin.getRegionContainer();
        RegionManager regions = container.get(player.getWorld());
        ProtectedRegion pr = regions.getRegion(townName);

        DefaultDomain domain = pr.getMembers();
        domain.addPlayer(player.getUniqueId());
        pr.setMembers(domain);
        regions.addRegion(pr);

        cfg.set(townName.toLowerCase(), uuid);
        try {
            cfg.save(mayorsFile);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}
