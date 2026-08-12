package dev.elrol.arrowlib.libs;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ArrowStringUtils {

    /**
     * Replaces player-specific and position placeholders in the given string template.
     * <p>
     * <b>Available Placeholders:</b>
     * <ul>
     *   <li>{@code [{prefix}name]} - Player's username (e.g. {@code "Steve"})</li>
     *   <li>{@code [{prefix}display_name]} - Player's formatted display name</li>
     *   <li>{@code [{prefix}uuid]} - Player's unique string UUID</li>
     *   <li>{@code [{prefix}max_health]} - Player's maximum health attribute value</li>
     *   <li>{@code [{prefix}dim]} - Full resource location of player's current dimension (e.g. {@code "minecraft:overworld"})</li>
     *   <li>{@code [{prefix}x]} - Player's current X block position</li>
     *   <li>{@code [{prefix}y]} - Player's current Y block position</li>
     *   <li>{@code [{prefix}z]} - Player's current Z block position</li>
     * </ul>
     * <i>Example with prefix {@code "player_"}:</i> {@code "{player_name}"} or {@code "{player_dim}"}
     *
     * @param string The string template containing placeholders.
     * @param prefix Optional namespace prefix inserted before keys (e.g., {@code "player_"}). If null, defaults to empty string.
     * @param player The target {@link Player} instance providing data.
     * @return The formatted string with all matching player placeholders replaced, or the original string if input is null.
     */
    public static String replacePlayerPlaceholders(String string, String prefix, Player player) {
        if(string == null || player == null) return string;
        String p = prefix(prefix);

        return replaceBlockPosPlaceHolders(string, prefix, player.blockPosition())
                .replace("{" + p + "name}", player.getName().getString())
                .replace("{" + p + "display_name}", player.getDisplayName().getString())
                .replace("{" + p + "uuid}", player.getUUID().toString())
                .replace("{" + p + "max_health}", String.valueOf(player.getMaxHealth()))
                .replace("{" + p + "dim}", player.level().dimension().location().toString());
    }

    /**
     * Replaces 3D spatial coordinate placeholders and arithmetic coordinate expressions in the given string template.
     * <p>
     * <b>Available Placeholders:</b>
     * <ul>
     *   <li>{@code [{prefix}x]} - Integer X coordinate</li>
     *   <li>{@code [{prefix}y]} - Integer Y coordinate</li>
     *   <li>{@code [{prefix}z]} - Integer Z coordinate</li>
     *   <li>{@code [{prefix}<x|y|z> <+|-|*|/> <N>]} - Coordinate evaluated by inline arithmetic operation with integer N</li>
     * </ul>
     * <i>Examples with prefix {@code "pos_"}:</i> {@code "{pos_x}"}, {@code "{pos_y+1}"}, {@code "{pos_z-2}"}, {@code "{pos_x*2}"}
     *
     * @param string The string template containing placeholders.
     * @param prefix Optional namespace prefix inserted before keys (e.g., {@code "pos_"}). If null, defaults to empty string.
     * @param pos    The {@link BlockPos} instance providing coordinates.
     * @return The formatted string with coordinate placeholders replaced, or the original string if input is null.
     */
    public static String replaceBlockPosPlaceHolders(String string, String prefix, BlockPos pos) {
        if(string == null || pos == null) return string;
        String p = prefix(prefix);

        String result = string
                .replace("{" + p + "x}", String.valueOf(pos.getX()))
                .replace("{" + p + "y}", String.valueOf(pos.getY()))
                .replace("{" + p + "z}", String.valueOf(pos.getZ()));

        String regex = "\\{" + Pattern.quote(p) + "([xyz])\\s*([+\\-*/])\\s*(\\d+)\\}";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(result);
        StringBuilder sb = new StringBuilder();

        while (matcher.find()) {
            int newVal = getNewVal(pos, matcher);
            matcher.appendReplacement(sb, String.valueOf(newVal));
        }
        matcher.appendTail(sb);

        return sb.toString();
    }

    /**
     * Computes the new coordinate value for a matched arithmetic placeholder expression.
     * <p>
     * Expects the matcher to contain three capture groups:
     * <ol>
     *   <li>Axis string ({@code "x"}, {@code "y"}, or {@code "z"})</li>
     *   <li>Operator string ({@code "+"}, {@code "-"}, {@code "*"}, or {@code "/"})</li>
     *   <li>Integer offset/operand</li>
     * </ol>
     *
     * @param pos     The base {@link BlockPos} providing source coordinates.
     * @param matcher The {@link Matcher} positioned at a valid coordinate expression match.
     * @return The calculated integer coordinate value. Returns the base coordinate unmodified if division by zero is attempted.
     */
    public static int getNewVal(BlockPos pos, Matcher matcher) {
        String axis = matcher.group(1);
        String op = matcher.group(2);
        int offset = Integer.parseInt(matcher.group(3));

        int baseVal = switch(axis) {
            case "x" -> pos.getX();
            case "y" -> pos.getY();
            case "z" -> pos.getZ();
            default -> 0;
        };

        return switch(op) {
            case "+" -> baseVal + offset;
            case "-" -> baseVal - offset;
            case "*" -> baseVal * offset;
            case "/" -> offset != 0 ? baseVal / offset : baseVal;
            default -> baseVal;
        };
    }

    @NotNull
    private static String prefix(String prefix) {
        return prefix == null ? "" : prefix;
    }

}
