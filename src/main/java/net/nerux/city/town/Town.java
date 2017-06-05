package net.nerux.city.town;

import com.google.common.base.Preconditions;
import org.bukkit.Location;

import java.util.Locale;
import java.util.UUID;

/**
 * Created by Ben on 23.05.17.
 */
public final class Town {

    private String name;
    private UUID owner;
    private Location spawn;


    public Town(final String name, final UUID owner, final Location spawn) {
        Preconditions.checkNotNull(name, "The name cannot be null!");

        this.name = name;
        this.owner = owner;
        this.spawn = spawn;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UUID getOwner() {
        return owner;
    }

    public void setOwner(UUID owner) {
        this.owner = owner;
    }

    public Location getSpawn() {
        return spawn;
    }

    public void setSpawn(Location spawn) {
        this.spawn = spawn;
    }

}
