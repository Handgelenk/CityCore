package net.nerux.bank.util.item;

import net.nerux.bank.util.MyItems;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public final class InventoryBuilder {
    private Inventory inv;
    private int line;

    public InventoryBuilder(String title, int line) {
        this.line = line;

        setInv(Bukkit.createInventory(null, line * 9, title));
    }

    public InventoryBuilder(Inventory inv) {
        setInv(inv);
        this.line = (inv.getSize() / 9);
    }

    public InventoryBuilder setItem(int line, int column, ItemStack item) {
        if ((line > 0) && (line <= this.line)) {
            if ((column > 0) && (column < 10)) {
                int a = (line - 1) * 9 + (column - 1);
                getInv().setItem(a, item);
            }
        }

        return this;
    }

    public ItemStack getItem(int line, int column) {
        if ((line > 0) && (line <= this.line)) {
            if ((column > 0) && (column < 10)) {
                return getInv().getItem((line - 1) * 9 + (column - 1));
            }
        }

        return null;
    }

    public InventoryBuilder fillLine(int line, ItemStack item) {
        if ((line > 0) && (line <= this.line)) {
            for (int i = 0; i < 9; i++) {
                int a = (line - 1) * 9 + i;
                if (getInv().getItem(a) == null) {
                    getInv().setItem(a, item);
                }
            }
        }

        return this;
    }

    public InventoryBuilder fillRow(int column, ItemStack item) {
        if ((column > 0) && (column < 10)) {
            for (int i = 0; i < this.line; i++) {
                int a = i * 9 + (column - 1);
                if (getInv().getItem(a) == null) {
                    getInv().setItem(a, item);
                }
            }
        }

        return this;
    }

    public InventoryBuilder setBasic() {
        setItem(1, 9, MyItems.BACK);

        return this;
    }

    public InventoryBuilder clear() {
        getInv().clear();
        return this;
    }

    public Inventory build() {
        return getInv();
    }

    public Inventory getInv() {
        return this.inv;
    }

    public void setInv(Inventory inv) {
        this.inv = inv;
    }
}
