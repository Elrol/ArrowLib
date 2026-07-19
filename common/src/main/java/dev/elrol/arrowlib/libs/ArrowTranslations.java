package dev.elrol.arrowlib.libs;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

/**
 * Text component factory and localization helper utilities.
 * <p>
 * This class automatically strip italics from generated components, making it ideal
 * for building custom item tooltips where vanilla defaults force text formatting to be italicized.
 * It provides standard shorthand categories for green messages, red errors, warning nodes, and info nodes.
 */
public class ArrowTranslations {

    /**
     * Creates a fresh, editable component copy from a base text piece and explicitly disables italicization.
     * Useful for normalizing tooltip formatting.
     *
     * @param text The base text element to clean.
     * @return A new {@link MutableComponent} instance guaranteed to display without italics.
     */
    public static MutableComponent removeItalic(Component text) {
        return MutableComponent.create(text.getContents()).setStyle(text.getStyle().withItalic(false));
    }

    /**
     * Generates a flat, non-localized plain text component.
     *
     * @param string The raw string characters to display.
     * @return A non-italicized literal text component.
     */
    public static MutableComponent literal(String string) {
        return removeItalic(Component.literal(string));
    }

    /**
     * Resolves a localized text node configuration path utilizing dynamic replacement variables.
     *
     * @param node The translation key string (e.g., "block.example.description").
     * @param args Formatting arguments to merge into the localized text structure.
     * @return A non-italicized localized component.
     */
    public static MutableComponent translate(String node, Object... args) {
        return removeItalic(Component.translatable(node, args));
    }

    /**
     * Resolves a simple localized text node configuration path.
     *
     * @param node The translation key string.
     * @return A non-italicized localized component.
     */
    public static MutableComponent translate(String node) {
        return removeItalic(Component.translatable(node));
    }

    /**
     * Attempts to resolve a localized text node path, falling back to a provided default string if missing.
     *
     * @param node     The translation key string.
     * @param fallback The raw fallback string to use if the key isn't present in translation files.
     * @return A non-italicized text component resolving the best match.
     */
    public static MutableComponent translateFallback(String node, String fallback) {
        return removeItalic(Component.translatableWithFallback(node, fallback));
    }

    /**
     * Attempts to resolve a localized text node path with variables, falling back to a default string if missing.
     *
     * @param node     The translation key string.
     * @param fallback The raw fallback string to use if the key isn't present.
     * @param args     Formatting arguments to inject into the active output.
     * @return A non-italicized text component resolving the best match.
     */
    public static MutableComponent translateFallback(String node, String fallback, Object... args) {
        return removeItalic(Component.translatableWithFallback(node, fallback, args));
    }

    /** Generates a standard message element prefixed with "arrow.message." styled in {@link ChatFormatting#GREEN}. */
    public static MutableComponent msg(String node) {
        return translate("arrow.message." + node).withStyle(ChatFormatting.GREEN);
    }

    /** Generates a dynamic message element prefixed with "arrow.message." styled in {@link ChatFormatting#GREEN}. */
    public static MutableComponent msg(String node, Object... args) {
        return translate("arrow.message." + node, args).withStyle(ChatFormatting.GREEN);
    }

    /** Generates a message element with fallback prefixed with "arrow.message." styled in {@link ChatFormatting#GREEN}. */
    public static MutableComponent msgFallback(String node, String fallback) {
        return translateFallback("arrow.message." + node, fallback).withStyle(ChatFormatting.GREEN);
    }

    /** Generates a dynamic message element with fallback prefixed with "arrow.message." styled in {@link ChatFormatting#GREEN}. */
    public static MutableComponent msgFallback(String node, String fallback, Object... args) {
        return translateFallback("arrow.message." + node, fallback, args).withStyle(ChatFormatting.GREEN);
    }

    /** Generates an informative text element prefixed with "arrow.info.". */
    public static MutableComponent info(String node) {
        return translate("arrow.info." + node);
    }

    /** Generates a dynamic informative text element prefixed with "arrow.info.". */
    public static MutableComponent info(String node, Object... args) {
        return translate("arrow.info." + node, args);
    }

    /** Generates an informative text element with fallback prefixed with "arrow.info.". */
    public static MutableComponent infoFallback(String node, String fallback) {
        return translateFallback("arrow.info." + node, fallback);
    }

    /** Generates a dynamic informative text element with fallback prefixed with "arrow.info.". */
    public static MutableComponent infoFallback(String node, String fallback, Object... args) {
        return translateFallback("arrow.info." + node, fallback, args);
    }

    /** Generates an alert warning element prefixed with "arrow.warn.". */
    public static MutableComponent warn(String node) {
        return translate("arrow.warn." + node);
    }

    /** Generates a dynamic alert warning element prefixed with "arrow.warn.". */
    public static MutableComponent warn(String node, Object... args) {
        return translate("arrow.warn." + node, args);
    }

    /** Generates an alert warning element with fallback prefixed with "arrow.warn.". */
    public static MutableComponent warnFallback(String node, String fallback) {
        return translateFallback("arrow.warn." + node, fallback);
    }

    /** Generates a dynamic alert warning element with fallback prefixed with "arrow.warn.". */
    public static MutableComponent warnFallback(String node, String fallback, Object... args) {
        return translateFallback("arrow.warn." + node, fallback, args);
    }

    /** Generates an error message element prefixed with "arrow.error." styled in {@link ChatFormatting#RED}. */
    public static MutableComponent err(String node) {
        return translate("arrow.error." + node).withStyle(ChatFormatting.RED);
    }

    /** Generates a dynamic error message element prefixed with "arrow.error." styled in {@link ChatFormatting#RED}. */
    public static MutableComponent err(String node, Object... args) {
        return translate("arrow.error." + node, args).withStyle(ChatFormatting.RED);
    }

    /** Generates an error message element with fallback prefixed with "arrow.error." styled in {@link ChatFormatting#RED}. */
    public static MutableComponent errFallback(String node, String fallback) {
        return translateFallback("arrow.error." + node, fallback).withStyle(ChatFormatting.RED);
    }

    /** Generates a dynamic error message element with fallback prefixed with "arrow.error." styled in {@link ChatFormatting#RED}. */
    public static MutableComponent errFallback(String node, String fallback, Object... args) {
        return translateFallback("arrow.error." + node, fallback, args).withStyle(ChatFormatting.RED);
    }

}
