package net.nerux.city.chestshop;

import net.nerux.CityCore;
import net.nerux.city.User;
import net.nerux.city.UserManager;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;

/**
 * Created by Ben on 05.06.17.
 */
public final class ChestManager {


    static File path = new File("plugins/CityCore/ChestShop");

    public static void createChest(final Player player, final Location location, final Material material, final int stock, final int price) {
        User user = UserManager.instance.getUser(player);

        if (user == null)
            return;
        int maxShops = user.getMaxShops();
        int shopsAmount = user.getShopAmount();

        int id = shopsAmount + 1;

        if (id > maxShops) {
            player.sendMessage(CityCore.pr + "Du darfst nicht mehr Shops machen!");
            return;
        }

        String owner = player.getUniqueId().toString();
        Chest chest = new Chest(owner, location, material, price, stock);
        createShopFile(chest, id);
        user.setShopAmount(id);

    }

    public static File getShopPath(final String uuid) {
        return new File(path, uuid);
    }

    public static void createShopFile(final Chest chest, final int id) {

        if (!path.exists()) {
            path.mkdirs();
        }

        String owner = chest.getOwner();
        Material item = chest.getItem();
        Location location = chest.getLocation();
        double price = chest.getPrice();
        double stock = chest.getstock();

        File uuidPath = getShopPath(owner);
        if (!uuidPath.exists()) {
            uuidPath.mkdirs();
        }

        File shopFile = new File(uuidPath, id + ".yml");
        YamlConfiguration cfg = YamlConfiguration.loadConfiguration(shopFile);

        if (!shopFile.exists()) {
            try {
                shopFile.createNewFile();
                cfg.set("owner", owner);
                cfg.set("item", item.toString());
                cfg.set("loc", location);
                cfg.set("price", price);
                cfg.set("stock", stock);
                cfg.save(shopFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public static boolean chestShopExists(final String owner, final int id) {
        File shopFile = new File(getShopPath(owner), id + ".yml");
        return shopFile.exists();
    }


}
