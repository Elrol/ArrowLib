package dev.elrol.arrowlib.client.screens.widgets;

import dev.elrol.arrowlib.libs.ArrowColorUtils;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.network.chat.Component;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * An interactive slider widget that renders a horizontal gradient bar for adjusting a specific color channel.
 */
public class HorizontalColorSlider extends AbstractSliderButton {

    private final int channel;
    private final Supplier<Integer> currentColorSupplier;
    private final Consumer<Double> onValueChange;

    /**
     * Constructs a horizontal color channel slider widget.
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
    public HorizontalColorSlider(int x, int y, int width, int height, int channel, double initialValue, Supplier<Integer> colorSupplier, Consumer<Double> onChange) {
        super(x, y, width, height, Component.empty(), initialValue);
        this.channel = channel;
        this.currentColorSupplier = colorSupplier;
        this.onValueChange = onChange;
    }

    @Override
    protected void updateMessage() {}

    @Override
    protected void applyValue() {
        if(onValueChange != null) {
            onValueChange.accept(value);
        }
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        int curColor = currentColorSupplier.get();

        // Render black border
        guiGraphics.fill(getX() - 1, getY() - 1, getX() + width + 1, getY() + height + 1, 0xFF000000);

        // Draw horizontal gradient by rendering 1px vertical slices
        for (int i = 0; i < width; i++) {
            float ratio = (float) i / (float) (width - 1);
            int val = (int) (ratio * 255.0f);
            int sliceColor = ArrowColorUtils.setChannel(curColor, channel, val);

            guiGraphics.fill(getX() + i, getY(), getX() + i + 1, getY() + height, sliceColor);
        }

        // Calculate thumb X position
        int handleX = getX() + (int) (this.value * (width - 4));

        // Render vertical indicator handle
        guiGraphics.fill(handleX, getY() - 2, handleX + 4, getY() + height + 2, 0xFFFFFFFF);
        guiGraphics.fill(handleX + 1, getY() - 1, handleX + 3, getY() + height + 1, 0xFF000000);
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        setValueFromMouse(mouseX);
    }

    @Override
    protected void onDrag(double mouseX, double mouseY, double dragX, double dragY) {
        setValueFromMouse(mouseX);
    }

    private void setValueFromMouse(double mouseX) {
        double clamp = (mouseX - (double) getX()) / (double) width;
        value = Math.max(0.0, Math.min(1.0, clamp));
        applyValue();
    }
}
