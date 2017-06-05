package net.nerux.city.chestshop;

import com.google.common.base.Preconditions;
import org.bukkit.Location;
import org.bukkit.Material;


/**
 * Created by Ben on 05.06.17.
 */
public final class Chest {

    private String owner;
    private Location location;
    private Material item;
    private double price;
    private int stock;

    public Chest(final String owner, final Location location, final Material item, final double price, final int stock) {
        Preconditions.checkNotNull(owner, "The owner cannot be null!");
        Preconditions.checkNotNull(location, "The location cannot be null!");
        Preconditions.checkNotNull(item, "The item cannot be null!");
        Preconditions.checkNotNull(stock, "The stock cannot be null!");

        this.owner = owner;
        this.location = location;
        this.item = item;
        this.price = price;
        this.stock = stock;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Material getItem() {
        return item;
    }

    public void setItem(Material item) {
        this.item = item;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getstock() {
        return stock;
    }

    public void setstock(int stock) {
        this.stock = stock;
    }
}
