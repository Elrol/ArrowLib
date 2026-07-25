package dev.elrol.arrowlib.libs;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.player.Player;

import java.util.Collections;
import java.util.List;

/**
 * Utility class for executing server-side commands in various permission contexts.
 */
public class ArrowServerUtils {

    /**
     * Executes a list of commands as the server console.
     * Output is suppressed from server logs.
     *
     * @param server   The {@link MinecraftServer} instance.
     * @param commands The list of command strings to execute.
     */
    public static void executeServerCommands(MinecraftServer server, List<String> commands) {
        if(server == null || commands == null || commands.isEmpty()) return;

        executeCommand(
                server.createCommandSourceStack().withSuppressedOutput(),
                server.getCommands(),
                commands
        );
    }

    /**
     * Executes a single command as the server console.
     * Output is suppressed from server logs.
     *
     * @param server  The {@link MinecraftServer} instance.
     * @param command The command string to execute.
     */
    public static void executeServerCommand(MinecraftServer server, String command) {
        executeServerCommands(server, Collections.singletonList(command));
    }

    /**
     * Executes a list of commands in the context of a player, using their current permission level.
     * Output is suppressed from server logs.
     *
     * @param player   The {@link Player} executing the commands.
     * @param commands The list of command strings to execute.
     */
    public static void executePlayerCommands(Player player, List<String> commands) {
        if(player == null || player.getServer() == null || commands == null || commands.isEmpty()) return;

        executeCommand(
                player.createCommandSourceStack().withSuppressedOutput(),
                player.getServer().getCommands(),
                commands
        );
    }

    /**
     * Executes a single command in the context of a player, using their current permission level.
     * Output is suppressed from server logs.
     *
     * @param player  The {@link Player} executing the command.
     * @param command The command string to execute.
     */
    public static void executePlayerCommand(Player player, String command) {
        executePlayerCommands(player, Collections.singletonList(command));
    }

    /**
     * Executes a list of commands in the context of a player, but bypasses normal permission checks
     * by granting OP level 4 privileges. Useful for triggering elevated reward actions on behalf of players.
     * Output is suppressed from server logs.
     *
     * @param player   The {@link Player} context for command execution.
     * @param commands The list of command strings to execute.
     */
    public static void executeOperatorCommands(Player player, List<String> commands) {
        if(player == null || player.getServer() == null || commands == null || commands.isEmpty()) return;

        executeCommand(
                player.createCommandSourceStack().withPermission(4).withSuppressedOutput(),
                player.getServer().getCommands(),
                commands
        );
    }

    /**
     * Executes a single command in the context of a player, granting OP level 4 privileges.
     * Output is suppressed from server logs.
     *
     * @param player  The {@link Player} context for command execution.
     * @param command The command string to execute.
     */
    public static void executeOperatorCommand(Player player, String command) {
        executeOperatorCommands(player, Collections.singletonList(command));
    }

    /**
     * Core execution helper that iterates through a list of commands, strips leading slashes,
     * and dispatches them through the provided {@link Commands} manager.
     *
     * @param source         The {@link CommandSourceStack} representing context and permissions.
     * @param commandManager The server's {@link Commands} dispatcher.
     * @param commands       The list of command strings to execute.
     */
    public static void executeCommand(CommandSourceStack source, Commands commandManager, List<String> commands) {
        if(source == null || commandManager == null || commands == null) return;

        commands.forEach(command -> {
            if(command == null || command.isBlank()) return;
            if(command.startsWith("/"))
                commandManager.performPrefixedCommand(source, command.substring(1));
            else
                commandManager.performPrefixedCommand(source, command);
        });
    }
}
