package net.nerux.city.town;

import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.bukkit.selections.Selection;
import com.sk89q.worldguard.bukkit.RegionContainer;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import net.nerux.CityCore;
import net.nerux.city.mayor.Mayor;
import net.nerux.city.mayor.MayorManager;
import net.nerux.city.plot.PlotAction;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * Created by Ben on 25.05.17.
 */
public final class TownManager {

    public final static File path = new File("plugins/CityCore/TownData");
    public static File townsFile = new File(path, "towns.yml");
    public static YamlConfiguration cfg = YamlConfiguration.loadConfiguration(townsFile);

    static WorldGuardPlugin worldGuardPlugin = CityCore.getWorldGuard();
    static WorldEditPlugin worldEditPlugin = CityCore.getWorldEdit();


    public static void createTownFile(final Town town) {
        if (!path.exists()) {
            path.mkdirs();
        }
        if (!townsFile.exists()) {
            try {
                townsFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        String townName = town.getName();
        Location spawn = town.getSpawn();

        cfg.set(townName + ".name", townName.toLowerCase());
        cfg.set(townName + ".spawn", spawn);
        try {
            cfg.save(townsFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void createTown(final String townName, final Player player) {

        if (worldEditPlugin.getSelection(player) == null) {
            player.sendMessage("§aBitte markiere erst den Plot via WorldEdit!=!!");
            return;

        }

        if (townName.length() > 16) {
            player.sendMessage(CityCore.pr + "Der StadtName darf nicht länger als §b16 Zeichen §7sein!");
            return;
        }

        if (!StringUtils.isAlphanumeric(townName)) {
            player.sendMessage(CityCore.pr + "Der Name enthält §4ungültige Zeichen");
            return;
        }

        if (TownManager.exists(townName)) {
            player.sendMessage(CityCore.pr + "Diese Stadt existiert bereits!");
            return;
        }

        if (StringUtils.isNumeric(townName)) {
            player.sendMessage(CityCore.pr + "Der Name darf nicht nur aus Zahlen bestehen!");
            return;
        }

        if (PlotAction.hasRegions(player)) {
            player.sendMessage("Hier sind schon Regionen!");
            return;
        }

        Selection selection = worldEditPlugin.getSelection(player);

        Location we1 = selection.getMaximumPoint();
        Location we2 = selection.getMinimumPoint();

        BlockVector bv1 = new BlockVector(we1.getX(), we1.getY(), we1.getZ());
        BlockVector bv2 = new BlockVector(we2.getX(), we2.getY(), we2.getZ());

        ProtectedRegion pr = new ProtectedCuboidRegion(townName, bv1, bv2);
        RegionContainer container = worldGuardPlugin.getRegionContainer();
        RegionManager regions = container.get(player.getWorld());
        pr.setPriority(10);
        regions.addRegion(pr);


        Town town = new Town(townName, null, null);
        createTownFile(town);

        player.sendMessage(CityCore.pr + "Die Stadt wurde erfolgreich erstellt und nennt sich §b" + townName);
        player.sendMessage(CityCore.pr + "Vergessen sie nicht den Spawn und den Bürgermeister zu setzen! " +
                "(der Bürgermeister ist keine Pflicht!)");

    }

    public static void deleteTown(final String townName) {
        if (!path.exists())
            return;
        if (!townsFile.exists())
            return;
        cfg.set(townName, null);
        cfg.set(townName + ".name", null);
        cfg.set(townName + ".spawn", null);
        try {
            cfg.save(townsFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean exists(final String townName) {
        if (!townsFile.exists()) {
            return false;
        }
        return cfg.isSet(townName + ".name");
    }

    public static Location getSpawn(final String townName) {
        if (!townsFile.exists())
            return null;
        if (!cfg.isSet(townName + ".spawn"))
            return null;
        return (Location) cfg.get(townName + ".spawn");
    }

    public static void setSpawn(final Location location, final String townName) {
        if (!townsFile.exists())
            return;
        cfg.set(townName + ".spawn", location);
        try {
            cfg.save(townsFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Town getTown(final String townName) {
        Location spawn = getSpawn(townName);
        return new Town(townName, null, spawn);
    }

}
