package net.nerux;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

public abstract class Command implements CommandExecutor, TabCompleter {

    private String command;
    private String[] alias;
    private List<Command> subCommandList;

    public Command(String command) {
        this(command, new String[]{});
    }

    public Command(String command, String... alias) {
        this.command = command;
        this.alias = alias;
        this.subCommandList = new ArrayList<Command>();
    }

    public abstract boolean execute(CommandSender sender, org.bukkit.command.Command cmd, String[] args, Player player);


    public abstract String getDescription();

    public abstract String getUsage();

    public abstract boolean onlyOnlinePlayer();

    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {

        Player Player = sender instanceof Player ? (Player) sender : null;

        if (command == null || command.getName().equalsIgnoreCase(this.command)) {
            if (args.length > 0) {
                for (String arg : args) {
                    for (Command subCommand : subCommandList) {
                        if (subCommand.getCommandName().equalsIgnoreCase(arg)
                                || Arrays.asList(subCommand.getAlias()).contains(arg)) {
                            String[] newArgs = new String[args.length - 1];
                            for (int i = 1; i < args.length; i++)
                                newArgs[i - 1] = args[i];
                            return subCommand.onCommand(sender, null, arg, newArgs);
                        }
                    }
                }

                if (this.onlyOnlinePlayer()) {
                    if (Player == null) {
                        sender.sendMessage(CityCore.pr + "Dazu hast du keine Berechtigung");
                        return true;
                    }
                }

                return this.execute(sender, command, args, Player);


            } else {

                if (this.onlyOnlinePlayer()) {
                    if (Player == null) {
                        sender.sendMessage(CityCore.pr + "Dazu hast du keine Berechtigung");
                        return true;
                    }
                }

                return this.execute(sender, command, args, Player);
            }
        }
        return false;
    }

    public String getCommandName() {
        return this.command;
    }

    public String[] getAlias() {
        return this.alias;
    }

    public List<Command> getSubCommand() {
        return subCommandList;
    }

    public boolean addSubCommand(Command subCommand) {
        if (subCommandList.contains(subCommand))
            return false;
        for (Command sc : subCommandList)
            if (sc.getCommandName().equalsIgnoreCase(subCommand.getCommandName()))
                return false;
        subCommandList.add(subCommand);
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {

        if (args.length == 0) {
            return null;
        }


        if (args.length == 1) {
            List<String> list = new ArrayList<>();

            Player Player = sender instanceof Player ? (Player) sender : null;

            for (Command c : getSubCommand()) {

                if (c.onlyOnlinePlayer())
                    if (Player == null)
                        continue;

                list.add(c.getCommandName());
            }
            return list.size() == 0 ? null : list;
        }


        for (String arg : args) {
            for (Command subCommand : subCommandList) {
                if (subCommand.getCommandName().equalsIgnoreCase(arg) || Arrays.asList(subCommand.getAlias()).contains(arg)) {

                    String[] newArgs = new String[args.length - 1];

                    for (int i = 1; i < args.length; i++)
                        newArgs[i - 1] = args[i];

                    return subCommand.onTabComplete(sender, command, label, newArgs);
                }
            }
        }
        return null;
    }
}
