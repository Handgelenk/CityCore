package net.nerux.economy;

import com.google.common.base.Preconditions;
import net.nerux.Serializable;
import net.nerux.utils.MathUtils;
import org.json.simple.JSONObject;

/**
 * Created by Ben on 25.05.17.
 */
public final class EcoAccount {

    private String owner;
    private double balance;

    public EcoAccount(final String owner, final double balance) {
        Preconditions.checkNotNull(owner, "The owner cannot be null!");
        Preconditions.checkNotNull(balance, "The balance cannot be null!");

        this.owner = owner;
        this.balance = balance;
    }

    public EcoAccount(final JSONObject ob) {
        this.owner = (String) ob.get("owner");
        this.balance = (double) ob.get("balance");
    }

    public double getBalance() {
        return balance;
    }

    public String getRoundedBalance() {
        return MathUtils.round(getBalance());
    }

    public void setBalance(final double balance) {
        this.balance = balance;
    }

    public void addBalance(final double x) {
        setBalance(this.getBalance() + x);
    }

    public void removeBalance(final double x) {
        setBalance(this.getBalance() - x);
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(final String owner) {
        this.owner = owner;
    }

    @SuppressWarnings("unchecked")
    public JSONObject serialize() {
        JSONObject ob = Serializable.serialize();

        ob.put("owner", owner);
        ob.put("balance", balance);

        return ob;
    }

    public boolean hasEnoughMoney(final double check) {
        return balance >= check;
    }

}
