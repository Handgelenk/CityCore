package net.nerux.bank;

import java.sql.Date;

import net.nerux.Serializable;
import net.nerux.bank.credit.Credit;
import net.nerux.utils.MathUtils;
import org.json.simple.JSONObject;

public class BankAccount implements Serializable {

    private Credit credit;
    private double balance;
    private double zins;
    private String owner;
    private int count;

    private Stats stats = Stats.UNDEFINED;

    public BankAccount(final String owner, final Credit credit, final double balance, final double zins) {
        super();
        this.owner = owner;
        this.credit = credit;
        this.balance = balance;
        this.zins = zins;
    }

    public BankAccount(final JSONObject ob) {

        this.owner = (String) ob.get("owner");
        this.credit = ob.get("credit") == null ? null : new Credit((JSONObject) ob.get("credit"));
        this.zins = (double) ob.get("zins");
        this.balance = (double) ob.get("balance");
        this.count = (int) (long) ob.get("count");

    }

    public Credit getCredit() {
        return credit;
    }

    public void setCredit(Credit credit) {
        this.credit = credit;
    }

    public boolean canCreditPayBack() {
        return hasCredit() ? balance >= credit.getType().hasToPay() : false;
    }

    public void creditPayBack() throws BankException {

        setBalance(getBalance() - credit.getType().hasToPay());
        credit = null;
        count = 0;
        updateStats();
    }

    public double getBalance() {
        return balance;
    }

    public String getRoundedBalance() {
        return MathUtils.round(getBalance());
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public void addBalance(double x) {
        setBalance(this.getBalance() + x);
    }

    public double getZins() {
        return zins;
    }

    public void setZins(double zins) {
        this.zins = zins;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void addCount() {
        this.count = this.count + 1;
    }

    @SuppressWarnings("unchecked")
    public JSONObject serialize() {
        JSONObject ob = Serializable.serialize();

        ob.put("owner", owner);
        ob.put("credit", credit == null ? null : credit.serialize());
        ob.put("zins", zins);
        ob.put("balance", balance);
        ob.put("count", count);

        return ob;
    }

    public boolean hasEnoughMoney(double check) {
        return balance >= check;
    }

    public boolean hasCredit() {
        return getCredit() != null;
    }

    public boolean creditOverTimed() {
        return hasCredit() ? getCredit().getBack().after(new Date(System.currentTimeMillis())) : false;
    }

    private static enum Stats {
        FREEZE, ROGER, UNDEFINED;
    }

    public Stats stats() {
        return stats == Stats.UNDEFINED ? creditOverTimed() ? (stats = Stats.FREEZE) : (stats = Stats.ROGER) : stats;
    }

    public void updateStats() {
        stats = Stats.UNDEFINED;
        stats();
    }

    public static class BankException extends Exception {

        private static final long serialVersionUID = 1L;

        public BankException(String msg) {
            super(msg);
        }

    }
}
