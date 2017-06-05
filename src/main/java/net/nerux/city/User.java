package net.nerux.city;

import net.nerux.Serializable;
import org.bukkit.entity.Player;
import org.json.simple.JSONObject;

/**
 * Created by Ben on 23.05.17.
 */
public final class User {
    public static User instance;

    private String uuid;
    private String name;
    private int gsMenge;
    private int maxGs;
    private int shopAmount;
    private int maxShops;

    public User(final String uuid, final String name, final int gsMenge, final int maxGs, final int shopAmount, final int maxShops) {
        this.uuid = uuid;
        this.name = name;
        this.gsMenge = gsMenge;
        this.maxGs = maxGs;
        this.shopAmount = shopAmount;
        this.maxShops = maxShops;
    }

    public User(final JSONObject ob) {
        this.uuid = (String) ob.get("owner");
        this.name = (String) ob.get("name");
        this.gsMenge = (int) (long) ob.get("gsmenge");
        this.maxGs = (int) (long) ob.get("maxgs");
        this.shopAmount = (int) (long) ob.get("shopamount");
        this.maxShops = (int) (long) ob.get("maxshops");
    }

    public String getUser() {
        return uuid;
    }

    public void setUser(final String uuid) {
        this.uuid = uuid;
    }

    public int getGsMenge() {
        return gsMenge;
    }

    public void setGsMenge(final int gsMenge) {
        this.gsMenge = gsMenge;
    }

    public void addGsMenge(final int x) {
        this.gsMenge = gsMenge + x;
    }

    public void subtractGsMenge(final int x) {
        this.gsMenge = gsMenge - x;
    }

    public int getMaxGs() {
        return maxGs;
    }

    public void setMaxGs(final int maxGs) {
        this.maxGs = maxGs;
    }


    public String getUuid() {
        return uuid;
    }

    public void setUuid(final String uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public int getShopAmount() {
        return shopAmount;
    }

    public void setShopAmount(int shopAmount) {
        this.shopAmount = shopAmount;
    }

    public int getMaxShops() {
        return maxShops;
    }

    public void setMaxShops(int maxShops) {
        this.maxShops = maxShops;
    }

    public boolean isHisGrundstuck(final Player player, final String id) {
        return false;
    }


    @SuppressWarnings("unchecked")
    public JSONObject serialize() {
        JSONObject ob = Serializable.serialize();
        ob.put("owner", uuid);
        ob.put("name", name);
        ob.put("gsmenge", gsMenge);
        ob.put("maxgs", maxGs);
        ob.put("shopamount", shopAmount);
        ob.put("maxshops", maxShops);
        return ob;
    }
}
