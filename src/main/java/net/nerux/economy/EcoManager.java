package net.nerux.economy;

import net.nerux.CC;
import net.nerux.CityCore;
import net.nerux.city.User;
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
 * Created by Ben on 25.05.17.
 */
public final class EcoManager {

    public static EcoManager instance;

    public final static File path = new File("plugins/CityCore/UserData");
    public final static String fileName = "JUMBOO_ECO_DATA.json";
    public final static double START_BALANCE = 1000;

    private final HashSet<EcoAccount> ACCOUNTS;

    public EcoManager() {
        instance = this;
        ACCOUNTS = new HashSet<>();
    }

    public HashSet<EcoAccount> getEcoAccounts() {
        return ACCOUNTS;
    }

    @SuppressWarnings("unchecked")
    public void saveAll() {

        if (!path.exists()) {
            path.mkdirs();
        }

        JSONArray array = new JSONArray();
        HashSet<EcoAccount> accounts = (HashSet<EcoAccount>) this.ACCOUNTS.clone();
        for (EcoAccount EcoAccount : accounts)
            array.add(EcoAccount.serialize());

        File file = new File(path, fileName);

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

                File file = new File(path, fileName);

                if (!file.exists()) {
                    System.out.println("Es sind keine EconomyDaten vorhanden!");
                    return;
                }

                JSONParser parser = new JSONParser();

                try {
                    JSONArray ob = (JSONArray) parser.parse(new FileReader(file));

                    for (Object object : ob) {

                        if (object instanceof JSONObject) {
                            EcoAccount account = new EcoAccount((JSONObject) object);
                            ACCOUNTS.add(account);
                        }

                    }

                    System.out.println(
                            CC.COLOR_GREEN + "Es wurden " + ACCOUNTS.size() + " EconomyKonten geladen." + CC.COLOR_RESET);

                } catch (IOException | ParseException e) {
                    e.printStackTrace();
                }

            }
        }.runTaskAsynchronously(CityCore.instance);

    }

    public boolean accountExists(final Player player) {
        if (getEcoAccount(player) == null) {
            return false;
        } else {
            return true;
        }

    }

    public EcoAccount getEcoAccount(final String uuid) {
        for (EcoAccount EcoAccount : ACCOUNTS) {
            if (EcoAccount.getOwner().equalsIgnoreCase(uuid))
                return EcoAccount;
        }
        return null;
    }

    public EcoAccount getEcoAccount(final Player player) {
        return getEcoAccount(player.getUniqueId().toString());
    }

    public EcoAccount getEcoAccount(final OfflinePlayer player) {
        return getEcoAccount(player.getUniqueId().toString());
    }

    public EcoAccount getEcoAccount(final UUID uuid) {
        return getEcoAccount(uuid.toString());
    }

    public boolean createEcoAccount(final String uuid) {

        EcoAccount account = getEcoAccount(uuid);

        if (account != null)
            return false;
        account = new EcoAccount(uuid, START_BALANCE);
        ACCOUNTS.add(account);
        return true;
    }

    public boolean createEcoAccount(final Player player) {
        return createEcoAccount(player.getUniqueId().toString());
    }

    public boolean createEcoAccount(final OfflinePlayer player) {
        return createEcoAccount(player.getUniqueId().toString());
    }

    public boolean createEcoAccount(final UUID uuid) {
        return createEcoAccount(uuid.toString());
    }

    public boolean resetEcoAccount(final String uuid) {

        EcoAccount account = getEcoAccount(uuid);

        if (account == null)
            return false;

        account.setBalance(START_BALANCE);
        return true;
    }

    public boolean resetEcoAccount(final Player player) {
        return resetEcoAccount(player.getUniqueId().toString());
    }

    public boolean resetEcoAccount(final OfflinePlayer player) {
        return resetEcoAccount(player.getUniqueId().toString());
    }

    public boolean resetEcoAccount(final UUID uuid) {
        return resetEcoAccount(uuid.toString());
    }


}
