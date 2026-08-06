package dev.elrol.arrowlib.libs;

import com.mojang.logging.LogUtils;
import net.minecraft.world.level.material.MapColor;
import org.slf4j.Logger;

import java.awt.*;

/**
 * Color manipulation and parsing utilities for translating hex codes, AWT {@link Color}s,
 * and matching custom RGB values to vanilla Minecraft {@link MapColor} definitions.
 */
public class ArrowColorUtils {
    /** Logger instance for color conversion parsing errors. */
    private static final Logger LOGGER = LogUtils.getLogger();

    /**
     * Unpacks an ARGB/RGB integer into an array of 3 integer channels [Red, Green, Blue] (0-255).
     */
    public static int[] unpackRGB(int argb) {
        int r = (argb >> 16) & 0xFF;
        int g = (argb >> 8) & 0xFF;
        int b = argb & 0xFF;
        return new int[]{ r, g, b };
    }

    /**
     * Parses a hex string (e.g., "#FF0000", "0x00FF00", or "FF0000") into an ARGB integer.
     * If no alpha channel is detected in the input string, it defaults to full opacity (0xFF000000).
     *
     * @param hex The raw hexadecimal string to be evaluated.
     * @return The parsed ARGB integer color, or {@code 0xffffffff} (solid white) if parsing fails.
     */
    public static int parseHex(String hex) {
        if(hex != null && !hex.isEmpty()) {
            try {
                String cleaned = hex.replace("#", "").replace("0x", "");
                int color = Integer.parseUnsignedInt(cleaned, 16);

                if(cleaned.length() <= 6) {
                    color |= 0xFF000000;
                }
                return color;
            } catch (NumberFormatException e) {
                LOGGER.error(e.getMessage());
            }
        }
        return 0xffffffff;
    }

    /**
     * Determines the closest vanilla {@link MapColor} match for a given raw RGB integer.
     * Uses a 3D Euclidean color distance formula to compare the inputs against all 64 base map colors.
     * Helpful when dynamically rendering custom map markers, UI elements, or block properties on standard vanilla maps.
     *
     * @param hexInt The ARGB/RGB integer color value to be matched.
     * @return The closest matching vanilla {@link MapColor} instance, or {@link MapColor#NONE} if no match is found.
     */
    public static MapColor mapColorOfHex(int hexInt) {
        int r1 = (hexInt >> 16) & 0xFF;
        int g1 = (hexInt >> 8) & 0xFF;
        int b1 = hexInt & 0xFF;

        MapColor bestMatch = MapColor.NONE;
        double minDistance = Double.MAX_VALUE;

        for(int i = 0; i < 64; i++) {
            MapColor current = MapColor.byId(i);
            if(current.equals(MapColor.NONE)) continue;

            int r2 = (current.col >> 16) & 0xFF;
            int g2 = (current.col >> 8) & 0xFF;
            int b2 = current.col & 0xFF;

            double distance = Math.pow(r2 - r1, 2) + Math.pow(g2 - g1, 2) + Math.pow(b2 - b1, 2);

            if(distance < minDistance) {
                minDistance = distance;
                bestMatch = current;
            }
        }

        return bestMatch;
    }

    /**
     * Multiplies the RGB value of the color, not changing the alpha
     * @param color The color to change
     * @param multiplier The amount to multiply
     * @return The new color
     */
    public static Color multiply(Color color, float multiplier) {
        return new Color(
                Math.min(255, Math.max(0, (int)(color.getRed() * multiplier))),
                Math.min(255, Math.max(0, (int)(color.getGreen() * multiplier))),
                Math.min(255, Math.max(0, (int)(color.getBlue() * multiplier))),
                color.getAlpha());
    }

    /**
     * Adds the RGB values from one to the other
     * @param c1 The base color
     * @param c2 The color to use the RGB of
     * @return The new color with the alpha of c1
     */
    public static Color merge(Color c1, Color c2) {
        return new Color(
                Math.min(255, Math.max(0, c1.getRed() + c2.getRed())),
                Math.min(255, Math.max(0, c1.getGreen() + c2.getGreen())),
                Math.min(255, Math.max(0, c1.getBlue() + c2.getBlue())),
                c1.getAlpha());
    }

    /**
     * Replaces a single RGB channel (0 = Red, 1 = Green, 2 = Blue) with a new 0-255 value,
     * leaving the other two channels and the original Alpha intact.
     *
     * @param argb The source ARGB integer color.
     * @param channel The target channel index (0 = Red, 1 = Green, 2 = Blue).
     * @param value The new channel value (0-255).
     * @return The mutated ARGB integer color.
     */
    public static int setChannel(int argb, int channel, int value) {
        int a = (argb >> 24) & 0xFF;
        int r = (argb >> 16) & 0xFF;
        int g = (argb >> 8) & 0xFF;
        int b = argb & 0xFF;

        if (channel == 0) r = Math.max(0, Math.min(255, value));
        else if (channel == 1) g = Math.max(0, Math.min(255, value));
        else if (channel == 2) b = Math.max(0, Math.min(255, value));

        return (a << 24) | (r << 16) | (g << 8) | b;
    }

    /**
     * Converts an ARGB integer into a 3-element float array containing Red, Green, and Blue normalized to 0.0f - 1.0f.
     * Useful for level rendering shaders and line box renderers.
     *
     * @param argb The raw integer color.
     * @return An array containing {Red, Green, Blue} floats.
     */
    public static float[] argbToFloatRGB(int argb) {
        float r = ((argb >> 16) & 0xFF) / 255.0f;
        float g = ((argb >> 8) & 0xFF) / 255.0f;
        float b = (argb & 0xFF) / 255.0f;
        return new float[]{ r, g, b };
    }

    /**
     * Constructs a solid ARGB integer from individual 0-255 RGB channels (defaults to 100% opacity).
     */
    public static int toARGB(int r, int g, int b) {
        return 0xFF000000 | ((r & 0xFF) << 16) | ((g & 0xFF) << 8) | (b & 0xFF);
    }
}
