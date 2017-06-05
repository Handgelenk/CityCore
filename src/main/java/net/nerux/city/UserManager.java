package net.nerux.city;

import net.nerux.CC;
import net.nerux.CityCore;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.UUID;

/**
 * Created by Ben on 23.05.17.
 */
public final class UserManager {

    public static UserManager instance;
    public final static File path = new File("plugins/CityCore/UserData");
    public final String filename = "JUMBOO_USER_DATA.json";
    private final HashSet<User> ACCOUNTS;


    public UserManager() {
        instance = this;
        this.ACCOUNTS = new HashSet<User>();
    }

    public HashSet<User> getUsers() {
        return this.ACCOUNTS;
    }

    @SuppressWarnings("unchecked")
    public void saveAll() {
        if (!path.exists()) {
            path.mkdirs();
        }
        JSONArray array = new JSONArray();
        HashSet<User> accounts = (HashSet<User>) this.ACCOUNTS.clone();
        for (User User : accounts) {
            array.add(User.serialize());
        }
        File file = new File(path, filename);
        try {
            FileWriter fw = new FileWriter(file);
            fw.write(array.toJSONString());
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadAll() {
        new BukkitRunnable() {

            @Override
            public void run() {
                File file = new File(path, filename);
                if (!file.exists()) {
                    System.out.println(CC.COLOR_GREEN + "Es sind keine User-Daten vorhanden.");
                    return;
                }
                JSONParser parser = new JSONParser();
                try {
                    JSONArray ob = (JSONArray) parser.parse(new FileReader(file));
                    for (Object object : ob) {
                        if ((object instanceof JSONObject)) {
                            User account = new User((JSONObject) object);
                            UserManager.this.ACCOUNTS.add(account);
                        }
                    }
                    System.out.println(CC.COLOR_GREEN + "Es wurden " + UserManager.this.ACCOUNTS.size()
                            + " User geladen." + CC.COLOR_RESET);
                } catch (IOException | ParseException e) {
                    e.printStackTrace();
                }
            }
        }.runTaskAsynchronously(CityCore.instance);
    }

    public User getUser(final String uuid) {
        for (User User : this.ACCOUNTS) {
            if (User.getUser().equalsIgnoreCase(uuid)) {
                return User;
            }
        }
        return null;
    }

    public User getUser(final Player player) {
        return getUser(player.getUniqueId().toString());
    }

    public User getUser(final OfflinePlayer player) {
        return getUser(player.getUniqueId().toString());
    }

    public User getUser(final UUID uuid) {
        return getUser(uuid.toString());
    }

    public boolean createUser(final String uuid) {
        User account = getUser(uuid);
        if (account != null) {
            return false;
        }
        account = new User(uuid, "", 0, 10, 0, 5);
        this.ACCOUNTS.add(account);
        return true;
    }

    public boolean createUser(final Player player) {
        return createUser(player.getUniqueId().toString());
    }

    public boolean createUser(final OfflinePlayer player) {
        return createUser(player.getUniqueId().toString());
    }

    public boolean createUser(final UUID uuid) {
        return createUser(uuid.toString());
    }

}
