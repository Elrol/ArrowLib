package dev.elrol.arrowlib.client.screens.widgets;

import dev.elrol.arrowlib.libs.ArrowColorUtils;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.network.chat.Component;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * An interactive slider widget that renders a vertical gradient bar for adjusting a specific color channel.
 */
public class VerticalColorSlider extends AbstractSliderButton {

    private final int channel;
    private final Supplier<Integer> currentColorSupplier;
    private final Consumer<Double> onValueChange;

    /**
     * Constructs a vertical color channel slider widget.
     *
     * @param x                    The X-coordinate on the screen.
     * @param y                    The Y-coordinate on the screen.
     * @param width                The width of the slider widget.
     * @param height               The height of the slider widget.
     * @param channel              The target color channel index (0 = Red, 1 = Green, 2 = Blue).
     * @param initialValue         The starting slider position normalized between 0.0 and 1.0.
     * @param colorSupplier        A supplier providing the current base ARGB integer color to display on the gradient.
     * @param onChange             A callback consumer triggered with the new normalized slider value when dragged or clicked.
     */
    public VerticalColorSlider(int x, int y, int width, int height, int channel, double initialValue, Supplier<Integer> colorSupplier, Consumer<Double> onChange) {
        super(x, y, width, height, Component.empty(), initialValue);
        this.channel = channel;
        this.currentColorSupplier = colorSupplier;
        this.onValueChange = onChange;
    }

    @Override
    protected void updateMessage() {}

    @Override
    protected void applyValue() {
        if (onValueChange != null) {
            onValueChange.accept(value);
        }
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        int curColor = currentColorSupplier.get();

        // Render black border
        guiGraphics.fill(getX() - 1, getY() - 1, getX() + width + 1, getY() + height + 1, 0xFF000000);

        // Draw vertical gradient by rendering 1px horizontal slices
        for (int i = 0; i < height; i++) {
            float ratio = (float) i / (float) (height - 1);
            int val = (int) (ratio * 255.0f);
            int sliceColor = ArrowColorUtils.setChannel(curColor, channel, val);

            guiGraphics.fill(getX(), getY() + i, getX() + width, getY() + i + 1, sliceColor);
        }

        // Calculate thumb Y position
        int handleY = getY() + (int) (this.value * (height - 4));

        // Render horizontal indicator handle
        guiGraphics.fill(getX() - 2, handleY, getX() + width + 2, handleY + 4, 0xFFFFFFFF);
        guiGraphics.fill(getX() - 1, handleY + 1, getX() + width + 1, handleY + 3, 0xFF000000);
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        setValueFromMouse(mouseY);
    }

    @Override
    protected void onDrag(double mouseX, double mouseY, double dragX, double dragY) {
        setValueFromMouse(mouseY);
    }

    private void setValueFromMouse(double mouseY) {
        double clamp = (mouseY - (double) getY()) / (double) height;
        value = Math.max(0.0, Math.min(1.0, clamp));
        applyValue();
    }
}
