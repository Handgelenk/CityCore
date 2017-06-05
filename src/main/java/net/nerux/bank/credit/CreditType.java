package net.nerux.bank.credit;

import net.nerux.utils.ItemBuilder;
import net.nerux.utils.MathUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public enum CreditType {
    BASIC_1("Basic Kredit 1", 5000.0D, 0.02D, 4, 3), BASIC_2("Basic Kredit 2", 10000.0D, 0.02D, 6, 3), BRONZE_1(
            "Bronze Kredit 1", 20000.0D, 0.02D, 8,
            14), BRONZE_2("Bronze Kredit 2", 25000.0D, 0.02D, 12, 14), SILBER_1("Silber Kredit 1", 35000.0D, 0.02D, 14,
            7), SILBER_2("Silber Kredit 2", 45000.0D, 0.02D, 16, 7), GOLD_1("Gold Kredit 1", 55000.0D, 0.02D,
            18, 11), GOLD_2("Gold Kredit 2", 65000.0D, 0.02D, 20, 11), SUPREME_1("Supreme Kredit 1",
            75000.0D, 0.02D, 22,
            12), SUPREME_2("Supreme Kredit 2", 90000.0D, 0.02D, 24, 12), BIGDEAL_1(
            "Big Kredit 1", 100000.0D, 0.02D, 26,
            1), BIGDEAL_2("Big Kredit 2", 125000.0D, 0.02D, 28, 1), JUMBO_1(
            "Jumbo Kredit 1", 130000.0D, 0.02D, 30,
            4), JUMBO_2("Jumbo Kredit 2", 150000.0D, 0.02D, 32, 4), MEGA_1(
            "Mega Kredit 1", 175000.0D, 0.02D, 34,
            10), MEGA_2("Mega Kredit 2", 200000.0D, 0.02D, 36, 10);

    private final String name;
    private final double balance;
    private final double zins;
    private int days;
    private byte data;
    private ItemStack item;

    private CreditType(String name, double balance, double zins, int days, int data) {
        this.name = name;
        this.balance = balance;
        this.zins = zins;
        this.days = days;
        this.data = ((byte) data);
    }

    public int getDays() {
        return this.days;
    }

    public void setDays(int days) {
        this.days = days;
    }

    public String getName() {
        return this.name;
    }

    public double getBalance() {
        return this.balance;
    }

    public String getRoundedBalance() {
        return MathUtils.round(getBalance());
    }

    public double getZins() {
        return this.zins;
    }

    public double hasToPay() {
        return getBalance() + getBalance() * getZins();
    }

    @SuppressWarnings("deprecation")
    public ItemStack getItem() {
        return this.item == null ? (this.item = new ItemBuilder(Material.getMaterial(351)).withData(this.data)
                .withTitle("" + ChatColor.WHITE + ChatColor.BOLD + getName())
                .addValue(ChatColor.GRAY + "Auszahlung", "" + ChatColor.AQUA + this.balance + "$")
                .addValue(ChatColor.GRAY + "Frist", "" + ChatColor.AQUA + this.days + " Tage")
                .addValue(ChatColor.GRAY + "Zins", "" + ChatColor.AQUA + this.zins * 100.0D + "%")
                .addValue(ChatColor.GRAY + "Zurückzahlen", ChatColor.AQUA + MathUtils.round(hasToPay()) + "$").build())
                : this.item;
    }

    public static CreditType getCreditTypeByName(String name) {
        CreditType[] arrayOfCreditType;
        int j = (arrayOfCreditType = values()).length;
        for (int i = 0; i < j; i++) {
            CreditType type = arrayOfCreditType[i];
            if (type.getName().equalsIgnoreCase(name)) {
                return type;
            }
        }
        return null;
    }
}
