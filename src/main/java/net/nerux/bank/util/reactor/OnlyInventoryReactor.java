package net.nerux.bank.util.reactor;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;


public abstract class OnlyInventoryReactor extends MainReactor {
    public OnlyInventoryReactor(String path) {
        super(path);
    }

    public void react(final InventoryClickEvent e, final ItemStack clicked, final Player player) {
        e.setCancelled(true);

        if ((clicked == null) || (clicked.getType() == org.bukkit.Material.AIR)) {
            return;
        }
        int i = e.getRawSlot();
        if ((i >= 0) && (i < e.getInventory().getSize())) {
            click(e, clicked, player);
        }
    }

    protected abstract void click(final InventoryClickEvent paramInventoryClickEvent, final ItemStack paramItemStack,
                                  Player paramPlayer);
}
