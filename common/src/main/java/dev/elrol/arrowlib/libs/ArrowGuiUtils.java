package dev.elrol.arrowlib.libs;

import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

/**
 * Utility class for streamlined Minecraft GUI component creation and positioning.
 * Provides helper factory methods for building {@link Button} instances using localized component keys.
 */
public class ArrowGuiUtils {

    /**
     * Creates a standard {@link Button.Builder} initialized with a translatable text component.
     *
     * @param label   The localization key or raw text for the button label.
     * @param onPress The action callback to execute when the button is clicked.
     * @return A pre-configured {@link Button.Builder} ready for layout bounds positioning.
     */
    @NotNull
    public static Button.Builder createButton(String label, Button.OnPress onPress) {
        return Button.builder(Component.translatable(label), onPress);
    }

    /**
     * Creates a fully dimensioned {@link Button.Builder} with explicit screen bounds.
     *
     * @param label   The localization key or raw text for the button label.
     * @param onPress The action callback to execute when the button is clicked.
     * @param x       The X-coordinate on the screen for the top-left corner.
     * @param y       The Y-coordinate on the screen for the top-left corner.
     * @param width   The width of the button in pixels.
     * @param height  The height of the button in pixels.
     * @return A configured {@link Button.Builder} positioned within the requested screen bounds.
     */
    @NotNull
    public static Button.Builder createButton(String label, Button.OnPress onPress, int x, int y, int width, int height) {
        return createButton(label, onPress).bounds(x, y, width, height);
    }

}