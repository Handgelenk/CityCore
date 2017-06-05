package net.nerux;


import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.nerux.bank.*;
import net.nerux.bank.gui.GuiListener;
import net.nerux.bank.util.MyItems;
import net.nerux.city.Listeners;
import net.nerux.city.User;
import net.nerux.city.UserManager;
import net.nerux.city.commands.CityAdminCommand;
import net.nerux.city.commands.CityCommand;
import net.nerux.city.commands.MayorCommand;
import net.nerux.city.commands.TownCommand;
import net.nerux.economy.EcoAccount;
import net.nerux.economy.EcoManager;
import net.nerux.economy.EconomyCommand;
import net.nerux.scoreboard.CityScoreboard;
import net.nerux.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Ben on 23.05.17.
 */
public final class CityCore extends JavaPlugin implements Listener {

    public static CityCore instance;

    public static String pr = "§7[§b§lCity§7] §7";
    public static String ecopr = "§7[§b§lEconomy§7] §7";
    public static String bankpr = "§7[§b§lBank§7] §7";


    private List<Player> players = new ArrayList<Player>();


    @Override
    public void onEnable() {
        instance = this;

        UserManager um = new UserManager();
        EcoManager em = new EcoManager();
        BankManager bm = new BankManager();


        um.loadAll();
        em.loadAll();
        bm.loadAll();

        ZinsManager zm = new ZinsManager();
        zm.start();

        startScoreboardTimer();
        //startSaveTimer();
        Bukkit.getPluginManager().registerEvents(new Listeners(), this);
        Bukkit.getPluginManager().registerEvents(this, this);
        Bukkit.getPluginManager().registerEvents(new BankListener(), this);
        Bukkit.getPluginManager().registerEvents(new GuiListener(), this);

        getCommand("city").setExecutor(new CityCommand());
        getCommand("cityadmin").setExecutor(new CityAdminCommand());
        getCommand("mayor").setExecutor(new MayorCommand());
        getCommand("town").setExecutor(new TownCommand());
        getCommand("economy").setExecutor(new EconomyCommand());

        registerCommand(new BankCommand("bank"));

        for (Player player : Bukkit.getOnlinePlayers()) {
            players.add(player);
        }
    }

    @Override
    public void onDisable() {
        UserManager.instance.saveAll();
        EcoManager.instance.saveAll();
        BankManager.instance.saveAll();
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        User user = UserManager.instance.getUser(p);
        EcoAccount acc = EcoManager.instance.getEcoAccount(p);
        BankAccount bankAccount = BankManager.instance.getBankAccount(p);

        if (user == null) {
            UserManager.instance.createUser(p);
        }
        if (acc == null) {
            EcoManager.instance.createEcoAccount(p);
        }
        if (bankAccount == null) {
            BankManager.instance.createBankAccount(p);
        }
        players.add(p);
        if (Bukkit.getOnlinePlayers().size() == 1 | Bukkit.getOnlinePlayers().size() == 0) {
            startScoreboardTimer();
        }
        if (user != null) {
            user.setName(p.getName());
        }
    }

    @EventHandler
    public void unregisterPlayer(PlayerQuitEvent event) {
        players.remove(event.getPlayer());
    }


    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(final ArrayList<Player> players) {
        this.players = players;
    }

    public static void startScoreboardTimer() {
        System.out.println("[DEBUG] Thread wurde gestartet!");
        new BukkitRunnable() {

            @Override
            public void run() {
                if (Bukkit.getOnlinePlayers().size() == 0) {
                    cancel();
                    System.out.println("[DEBUG] Thread wurde gestoppt!");
                    return;
                }
                for (Player players : Bukkit.getOnlinePlayers()) {
                    CityScoreboard.updateScoreboard(players);
                }
            }
        }.runTaskTimer(instance, 0, 10);
    }

    public static void startSaveTimer() {
        System.out.println("[AutoSave] AutoSave wurde gestartet!");
        new BukkitRunnable() {

            @Override
            public void run() {
                UserManager.instance.saveAll();
                EcoManager.instance.saveAll();
                System.out.println("[AutoSave] Datas saved by managers");
            }
        }.runTaskTimer(instance, 0, 20 * 5);
    }

    public static int getPing(final Player p) {
        CraftPlayer pingp = (CraftPlayer) p;
        EntityPlayer pinge = pingp.getHandle();
        return pinge.ping;
    }


    public static void fillInv(Inventory inv) {
        ItemStack glass = new ItemBuilder(Material.STAINED_GLASS_PANE).withData((byte) 0).withTitle(" ").build();
        for (int i = 0; i < 45; i++) {
            if (inv.getSize() >= 45) {
                if (i != 0) {
                    inv.setItem(i, glass);
                } else {
                    inv.setItem(i, MyItems.BACK);
                }
            }

        }
    }

    public boolean registerCommand(Command command) {
        String commandName = command.getCommandName();
        String[] commandAlias = command.getAlias();

        getCommand(commandName).setExecutor(command);
        getCommand(commandName).setAliases(Arrays.asList(commandAlias));
        return true;
    }

    public static WorldGuardPlugin getWorldGuard() {
        Plugin wgp = Bukkit.getServer().getPluginManager().getPlugin("WorldGuard");
        WorldGuardPlugin worldGuardPlugin = (WorldGuardPlugin) wgp;
        if (worldGuardPlugin == null)
            return null;
        return worldGuardPlugin;
    }

    public static WorldEditPlugin getWorldEdit() {
        Plugin wep = Bukkit.getServer().getPluginManager().getPlugin("WorldEdit");
        WorldEditPlugin worldEditPlugin = (WorldEditPlugin) wep;
        if (worldEditPlugin == null)
            return null;
        return worldEditPlugin;
    }


}
