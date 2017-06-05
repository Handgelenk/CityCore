package net.nerux.city.mayor;

import com.google.common.base.Preconditions;

import java.util.UUID;

/**
 * Created by Ben on 31.05.17.
 */
public final class Mayor {

    private UUID uuid;
    private String townName;

    public Mayor(final UUID uuid, final String townName) {
        Preconditions.checkNotNull(uuid, "The uuid cannot be null!");
        Preconditions.checkNotNull(townName, "The townname cannot be null!");
        this.uuid = uuid;
        this.townName = townName;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(final UUID uuid) {
        this.uuid = uuid;
    }

    public String getTownName() {
        return townName;
    }

    public void setTownName(final String townName) {
        this.townName = townName;
    }
}
