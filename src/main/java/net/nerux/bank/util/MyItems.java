package net.nerux.bank.util;


import net.nerux.utils.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class MyItems {
    public static final ItemStack BACK = new ItemBuilder(Material.BARRIER).addEnchantEffect()
            .withTitleLore(ChatColor.WHITE + "Back", ChatColor.WHITE + "Goes to the previous menu.").build();
    public static final ItemStack SCROLL_RIGHT = new ItemBuilder(Material.INK_SACK).withData((byte) 8)
            .addEnchantEffect()/* 16 */.withTitle(ChatColor.AQUA + "Next Page").build();
    public static final ItemStack SCROLL_LEFT = new ItemBuilder(Material.INK_SACK).withData((byte) 8)
            .addEnchantEffect()/* 18 */.withTitle(ChatColor.AQUA + "Previous Page").build();
    public static final ItemStack SCROLL_PAGE = new ItemBuilder(Material.PAPER, 1, (byte) 0).build();
    @SuppressWarnings("deprecation")
    public static final ItemStack BLOCKED = new ItemBuilder(Material.STAINED_GLASS_PANE, 1, DyeColor.BLACK.getData())
            .withTitle("" + ChatColor.RED + ChatColor.BOLD + '⛉').build();
}
