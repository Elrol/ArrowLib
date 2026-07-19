package dev.elrol.arrowlib.libs;

import java.util.Random;

public class ArrowMathUtils {
    /** Local utility random sequence seed controller. */
    private static final Random random = new Random();

    /**
     * Simulates a direct percentage evaluation test against a standard random distribution check.
     *
     * @param chance Percent chance of success represented as a float value between 0.0f and 100.0f.
     * @return True if the rolling check succeeded against the targeted chance window range.
     */
    public static boolean percentChance(float chance) {
        return random.nextFloat(1.0f) <= (chance/100.0f);
    }

    /**
     * Processes a float value as a guaranteed base count plus a percentage chance for an additional count.
     * <p>
     * For example, a chance of 2.75f guarantees a count of 2, and provides a 75% chance to increment the count to 3.
     * Useful for scaling drops or multi-strike mechanics dynamically.
     *
     * @param chance The raw floating-point value representing the expected outcome (e.g., 2.75 for 275%).
     * @return The final integer count after resolving the guaranteed amount and the remaining fractional probability.
     */
    public static int confirmChance(float chance) {
        int count = (int) chance;
        float remainder = chance - count;

        if(percentChance(remainder * 100f)) count++;
        return count;
    }
}
