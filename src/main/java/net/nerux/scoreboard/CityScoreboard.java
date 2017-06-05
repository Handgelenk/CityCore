package net.nerux.scoreboard;


import net.nerux.city.User;
import net.nerux.city.UserManager;
import net.nerux.economy.EcoAccount;
import net.nerux.economy.EcoManager;
import net.nerux.scoreboard.api.ScoreboardUtil;
import org.bukkit.entity.Player;

public final class CityScoreboard {

    public static void updateScoreboard(final Player p) {

        User user = UserManager.instance.getUser(p);
        EcoAccount acc = EcoManager.instance.getEcoAccount(p);
        if (user == null | acc == null) return;


        String plotID = ScoreboardData.getChunkID(p.getLocation());

        if (ScoreboardData.hasOwner(plotID, p.getLocation())) {
            ScoreboardUtil.unrankedSidebarDisplay(p,
                    new String[]{"§B§lNerux.net", " ", "§7§lBesitzer:",
                            "§b§l" + ScoreboardData.getOwner(plotID, p.getWorld()),
                            "    ", "§7§lBargeld:", "§e§l" + acc.getRoundedBalance() + "$", "     ", "§7§lPlot-ID:",
                            "§6§l" + ScoreboardData.getChunkID(p.getLocation()).toUpperCase(),
                            "         ", "§7Preis:", "§d§l" + ScoreboardData.getPrice(plotID)});
        } else {
            ScoreboardUtil.unrankedSidebarDisplay(p,
                    new String[]{"§B§lNerux.net", " ", "§7§lChunk-Typ:",
                            ScoreboardData.getChunkType(p.getLocation()),
                            "    ", "§7§lBargeld:", "§e§l" + acc.getRoundedBalance() + "$", "     ", "§7§lPlot-ID:",
                            "§6§l" + ScoreboardData.getChunkID(p.getLocation()).toUpperCase(),
                            "         ", "§7Preis:", "§d§l" + ScoreboardData.getPrice(plotID)});
        }


    }

    // public static void updateScoreboard2(Player p) {
    //
    // int x = p.getLocation().getChunk().getX();
    // int z = p.getLocation().getChunk().getZ();
    //
    // File chunkFile = CityCore.instrance.getChunkFile(x, z);
    // FileConfiguration chunkFileCfg =
    // YamlConfiguration.loadConfiguration(chunkFile);
    //
    // String chunkOwner = chunkFileCfg.getString("owner");
    //
    // File townFile = CityCore.instrance.getTownFile(x, z);
    // EcoAccount ecoAccount = EcoManager.instance.getEcoAccount(p);
    //
    // if (ecoAccount == null)
    // return;
    //
    // String balance = "§e§l" + ecoAccount.getRoundedBalance();
    // String prio = "§d§l" + CityCore.instrance.getPriority(x, z);
    //
    // String chunkString = "§e§l" + x + ", " + z;
    //
    // if (chunkFile.exists()) {
    // for (User allUsers : UserManager.instance.getUsers()) {
    // if (allUsers.getUuid().equalsIgnoreCase(chunkOwner)) {
    // String userOwner = allUsers.getName();
    // if (!ScoreboardData.isZuVerkaufen(x, z)) {
    // p.setScoreboard(new SidebarBuilder(p).setDisplay("§B§LSettla.org")
    // .setScores(" ", "§7§lChunk-Type:", ScoreboardData.getChunkType(x, z), "
    // ",
    // "§7§lChunk:", "§e§l" + chunkString, " ", "§7§lMoney:", balance, " ",
    // "§7§lOwner:", "§d§l" + userOwner)
    // .build());
    // } else {
    // p.setScoreboard(new SidebarBuilder(p).setDisplay("§B§LSettla.org")
    // .setScores(" ", "§7§lZu verkaufen:", "§e§l" + ScoreboardData.getPrice(x,
    // z) + "$",
    // " ", "§7§lChunk:", "§e§l" + chunkString, " ", "§7§lMoney:", balance,
    // " ", "§7§lOwner:", "§d§l" + userOwner)
    // .build());
    // }
    // }
    //
    // }
    // } else if (townFile.exists()) {
    // p.setScoreboard(new SidebarBuilder(p).setDisplay("§B§LSettla.org")
    // .setScores(" ", "§7§lChunk-Type:", ScoreboardData.getChunkType(x, z), "
    // ", "§7§lChunk:",
    // "§e§l" + chunkString, " ", "§7§lMoney:", balance, " ", "§7§lPriority:",
    // prio)
    // .build());
    // } else {
    // p.setScoreboard(new SidebarBuilder(p).setDisplay("§B§LSettla.org")
    // .setScores(" ", "§7§lChunk-Type:", ScoreboardData.getChunkType(x, z), "
    // ", "§7§lChunk:",
    // "§e§l" + chunkString, " ", "§7§lMoney:", balance, " ", "§7§lPriority:",
    // prio)
    // .build());
    // }
    //
    // }

}
