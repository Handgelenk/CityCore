package net.nerux.city.plot;

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
import org.bukkit.Bukkit;
import org.bukkit.Location;
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
public final class Plot {

    static WorldGuardPlugin worldGuardPlugin = CityCore.getWorldGuard();
    static WorldEditPlugin worldEditPlugin = CityCore.getWorldEdit();

    public final static File path = new File("plugins/CityCore/PlotData");

    public static void createPlot(final Player player, final int price) throws IOException {

        if (worldEditPlugin.getSelection(player) == null) {
            player.sendMessage("§aBitte markiere erst den Plot via WorldEdit!");
            return;
        }

        UUID uuid = player.getUniqueId();
        Location loc = player.getLocation();

        Selection selection = worldEditPlugin.getSelection(player);


        String id = PlotID.getNewPlotID();

        Location we1 = selection.getMaximumPoint();
        Location we2 = selection.getMinimumPoint();

        Location test1 = we1.clone();
        Location test2 = we2.clone();

        test1.setY(10);
        test2.setY(10);


        if (!MayorManager.isMayorFromTownAtLocation(uuid, we1) | !MayorManager.isMayorFromTownAtLocation(uuid, we2)) {
            if (!player.isOp() | !player.hasPermission("city.createplot")) {
                player.sendMessage(CityCore.pr + "§4Du bist kein Bürgermeister dieser Stadt oder dein PLOT schneidet eine andere Stadt!");
                return;
            }
        }

        if (test1.distance(test2) > 200) {
            player.sendMessage(CityCore.pr + "§4Das Grundstück ist zu groß!");
            return;
        }

        BlockVector bv1 = new BlockVector(we1.getX(), we1.getY(), we1.getZ());
        BlockVector bv2 = new BlockVector(we2.getX(), we2.getY(), we2.getZ());

        ProtectedRegion pr = new ProtectedCuboidRegion(id, bv1, bv2);
        RegionContainer container = worldGuardPlugin.getRegionContainer();
        RegionManager regions = container.get(player.getWorld());
        pr.setPriority(20);
        regions.addRegion(pr);

        setupPlotFile(id, price);
        PlotID.updatePlotIDs();

        player.sendMessage("Der Plot wurde erfolgreich erstellt!");
    }

    public static void deletePlot(final String id, final World w) {
        File file = getPlotFile(id);
        if (file.exists()) {
            file.delete();
        }
    }

    public static File getPlotFile(final String id) {
        return new File(path, id + ".yml");
    }

    public static void setupPlotFile(final String id, final int price) throws IOException {
        File file = new File(path, id + ".yml");
        if (!file.exists()) {
            file.createNewFile();
        }
        YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);
        cfg.set("id", id);
        cfg.set("price", price + "");
        cfg.save(file);
    }

    public static boolean isForSale(final String id) {
        File file = getPlotFile(id);
        YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);
        if (!file.exists())
            return false;
        if (cfg.isSet("price"))
            return true;
        return false;
    }

    public static int getPrice(final String id) {
        File file = getPlotFile(id);
        YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);
        if (file.exists()) {
            if (cfg.isSet("price")) {
                int price = Integer.parseInt(cfg.getString("price"));
                return price;
            }
        }
        return -100;
    }

    public static boolean hasOwner(final String id) {
        File file = getPlotFile(id);
        YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);
        if (file.exists()) {
            if (cfg.isSet("price")) {
                return cfg.isSet("owner");
            }
        }
        return false;
    }

    public static String getOwnerUniqueID(final String id) {
        File file = getPlotFile(id);
        YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);
        if (file.exists()) {
            if (cfg.isSet("owner")) {
                return cfg.getString("owner");
            }
        }
        return null;
    }
}
