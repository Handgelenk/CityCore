package net.nerux;

import org.bukkit.ChatColor;

/**
 * Created by Ben on 23.05.17.
 */
public enum CC {

    TITLE, NORMAL, SPEZIAL, CLING,

    RIGHT_SMALL_ARROW, RIGHT_NORMAL_ARROW, RIGHT_BIG_ARROW, RIGHT_HUGE_ARROW, RIGHT_FILLED_ARROW,

    HEART, BIG_HEART, HALF_HEART,

    STAR, BIG_STAR,

    POINT, SQUARE,

    CROSS, PLUS, DEATH,

    NUMBER_1, NUMBER_2, NUMBER_3, NUMBER_4, NUMBER_5, NUMBER_6, NUMBER_7, NUMBER_8, NUMBER_9, NUMBER_10,

    RIGHT_TRIANGLE, LEFT_TRIANGLE, UP_TRIANGLE, DOWN_TRIANGLE;

    @Override
    public String toString() {

        String cc = "";

        switch (this) {
            case TITLE:
                cc = ChatColor.YELLOW + cc;
                break;
            case NORMAL:
                cc = ChatColor.GRAY + cc;
                break;
            case SPEZIAL:
                cc = ChatColor.AQUA + cc;
                break;
            case CLING:
                cc = ChatColor.DARK_AQUA + cc;

            case RIGHT_SMALL_ARROW:
                cc = '\u27BC' + cc;
                break;
            case RIGHT_NORMAL_ARROW:
                cc = '➜' + cc;
                break;
            case RIGHT_BIG_ARROW:
                cc = '\u27BD' + cc;
                break;
            case RIGHT_HUGE_ARROW:
                cc = '➤' + cc;
                break;
            case RIGHT_FILLED_ARROW:
                cc = '➲' + cc;
                break;

            case PLUS:
                cc = '✚' + cc;
                break;
            case CROSS:
                cc = '✖' + cc;
                break;
            case DEATH:
                cc = '✞' + cc;
                break;

            case HEART:
                cc = '\uF5A4' + cc;
                break;
            case BIG_HEART:
                cc = '\u2764' + cc;
                break;
            case HALF_HEART:
                cc = '�' + cc;
                break;

            case SQUARE:
                cc = '\u2588' + cc;
                break;
            case POINT:
                cc = '?' + cc;
                break;
            case BIG_STAR:
                cc = '✦' + cc;
                break;
            case STAR:
                cc = '✪' + cc;
                break;

            case RIGHT_TRIANGLE:
                cc = '\u25BA' + cc;
                break;
            case LEFT_TRIANGLE:
                cc = '\u25C4' + cc;
                break;
            case UP_TRIANGLE:
                cc = '\u25B2' + cc;
                break;
            case DOWN_TRIANGLE:
                cc = '\u25BC' + cc;
                break;

            case NUMBER_1:
                cc = '➊' + cc;
                break;
            case NUMBER_2:
                cc = '➋' + cc;
                break;
            case NUMBER_3:
                cc = '➌' + cc;
                break;
            case NUMBER_4:
                cc = '?' + cc;
                break;
            case NUMBER_5:
                cc = '➎' + cc;
                break;
            case NUMBER_6:
                cc = '?' + cc;
                break;
            case NUMBER_7:
                cc = '?' + cc;
                break;
            case NUMBER_8:
                cc = '➑' + cc;
                break;
            case NUMBER_9:
                cc = '➒' + cc;
                break;
            case NUMBER_10:
                cc = '➓' + cc;
                break;
        }
        return cc;
    }

    public static String getCommandString(String commandName, String description) {
        return "§e- /" + commandName + " §7" + description;
    }

    public static String getTitle(String title) {
        return ChatColor.DARK_AQUA + "[" + ChatColor.YELLOW + ChatColor.BOLD + title + ChatColor.DARK_AQUA + "] "
                + ChatColor.GRAY;
    }

    public static String spz(Object spezial) {
        return ChatColor.AQUA + spezial.toString() + ChatColor.GRAY;
    }

    public static String getPreLine(String title) {
        return CC.SPEZIAL + "" + ChatColor.STRIKETHROUGH + "-------------------" + ChatColor.RESET + " "
                + CC.getTitle(title) + CC.SPEZIAL + "" + ChatColor.STRIKETHROUGH + "-------------------";
    }

    public static final String COLOR_RESET = "\u001B[0m";
    public static final String COLOR_BLACK = "\u001B[30m";
    public static final String COLOR_RED = "\u001B[31m";
    public static final String COLOR_GREEN = "\u001B[32m";
    public static final String COLOR_YELLOW = "\u001B[33m";
    public static final String COLOR_BLUE = "\u001B[34m";
    public static final String COLOR_PURPLE = "\u001B[35m";
    public static final String COLOR_CYAN = "\u001B[36m";
    public static final String COLOR_WHITE = "\u001B[37m";


}
