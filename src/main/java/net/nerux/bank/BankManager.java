package net.nerux.bank;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.UUID;

import net.nerux.CC;
import net.nerux.CityCore;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public final class BankManager {
    public static BankManager instance;
    public final static File path = new File("plugins/CityCore/UserData");
    public static final String fileName = "JUMBOO_BANK_DATA.json";
    public static final double START_BALANCE = 1000.0D;
    public static final double START_ZINS = 0.001D;
    private final HashSet<BankAccount> ACCOUNTS;

    public BankManager() {
        instance = this;
        this.ACCOUNTS = new HashSet<BankAccount>();
    }

    public HashSet<BankAccount> getBankAccounts() {
        return this.ACCOUNTS;
    }

    @SuppressWarnings("unchecked")
    public void saveAll() {
        JSONArray array = new JSONArray();
        for (BankAccount bankAccount : this.ACCOUNTS) {
            array.add(bankAccount.serialize());
        }
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
            public void run() {
                File file = new File(path, fileName);
                if (!file.exists()) {
                    System.out.println("Es sind keine Bank Daten vorhanden.");
                    return;
                }
                JSONParser parser = new JSONParser();
                try {
                    JSONArray ob = (JSONArray) parser.parse(new FileReader(file));
                    for (Object object : ob) {
                        if ((object instanceof JSONObject)) {
                            BankAccount account = new BankAccount((JSONObject) object);
                            BankManager.this.ACCOUNTS.add(account);
                        }
                    }
                    System.out.println(CC.COLOR_GREEN + "Es wurden " + BankManager.this.ACCOUNTS.size()
                            + " Konten geladen." + CC.COLOR_RESET);
                } catch (IOException | ParseException e) {
                    e.printStackTrace();
                }
            }
        }.runTaskAsynchronously(CityCore.instance);
    }

    public BankAccount getBankAccount(String uuid) {
        for (BankAccount bankAccount : this.ACCOUNTS) {
            if (bankAccount.getOwner().equalsIgnoreCase(uuid)) {
                return bankAccount;
            }
        }
        return null;
    }

    public BankAccount getBankAccount(final Player player) {
        return getBankAccount(player.getUniqueId().toString());
    }

    public BankAccount getBankAccount(final OfflinePlayer player) {
        return getBankAccount(player.getUniqueId().toString());
    }

    public BankAccount getBankAccount(final UUID uuid) {
        return getBankAccount(uuid.toString());
    }

    public boolean createBankAccount(String uuid) {
        BankAccount account = getBankAccount(uuid);
        if (account != null) {
            return false;
        }
        account = new BankAccount(uuid, null, 1000.0D, 0.001D);
        this.ACCOUNTS.add(account);
        return true;
    }

    public boolean createBankAccount(final Player player) {
        return createBankAccount(player.getUniqueId().toString());
    }

    public boolean createBankAccount(final OfflinePlayer player) {
        return createBankAccount(player.getUniqueId().toString());
    }

    public boolean createBankAccount(final UUID uuid) {
        return createBankAccount(uuid.toString());
    }

    public boolean resetBankAccount(final String uuid) {
        BankAccount account = getBankAccount(uuid);
        if (account == null) {
            return false;
        }
        account.setBalance(500);
        account.setZins(0.001);
        account.setCredit(null);
        return true;
    }

    public boolean resetBankAccount(final Player player) {
        return resetBankAccount(player.getUniqueId().toString());
    }

    public boolean resetBankAccount(final OfflinePlayer player) {
        return resetBankAccount(player.getUniqueId().toString());
    }

    public boolean resetBankAccount(final UUID uuid) {
        return resetBankAccount(uuid.toString());
    }

    public boolean resetCredit(String uuid) {
        BankAccount account = getBankAccount(uuid);
        if (account == null) {
            return false;
        }
        account.setCredit(null);
        return true;
    }

    public boolean resetCredit(final Player player) {
        return resetCredit(player.getUniqueId().toString());
    }

    public boolean resetCredit(final OfflinePlayer player) {
        return resetCredit(player.getUniqueId().toString());
    }

    public boolean resetCredit(final UUID uuid) {
        return resetCredit(uuid.toString());
    }
}
