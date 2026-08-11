package dev.elrol.arrowlib.client.screens;

import dev.elrol.arrowlib.client.screens.widgets.HorizontalColorSlider;
import dev.elrol.arrowlib.client.screens.widgets.VerticalColorSlider;
import dev.elrol.arrowlib.libs.ArrowColorUtils;
import dev.elrol.arrowlib.libs.ArrowGuiUtils;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.function.Consumer;

/**
 * A mod-agnostic color picker UI screen that provides interactive RGB color adjustment.
 * Supports both horizontal and vertical slider layouts with live color previewing
 * and passes the selected ARGB integer color back via a consumer callback.
 */
public class ColorPickerScreen extends Screen {

    private int r, g, b;
    private final Screen parent;
    private final boolean horizontal;
    private final Consumer<Integer> callback;

    private static final int colorSliderShort = 16;
    private static final int colorSliderLong = 110;
    private static final int colorSliderSpacing = 36;

    /**
     * Constructs a default horizontal color picker screen.
     *
     * @param parent         The parent screen to return to upon closing or saving.
     * @param initialColor   The starting ARGB integer color value.
     * @param onColorPicked  The consumer callback triggered with the final ARGB integer color when saved.
     */
    public ColorPickerScreen(Screen parent, int initialColor, Consumer<Integer> onColorPicked) {
        this(parent, initialColor, true, onColorPicked);
    }

    /**
     * Constructs a color picker screen with a configurable layout orientation.
     *
     * @param parent         The parent screen to return to upon closing or saving.
     * @param initialColor   The starting ARGB integer color value.
     * @param horizontal     {@code true} for stacked horizontal sliders; {@code false} for side-by-side vertical sliders.
     * @param onColorPicked  The consumer callback triggered with the final ARGB integer color when saved.
     */
    public ColorPickerScreen(Screen parent, int initialColor, boolean horizontal, Consumer<Integer> onColorPicked) {
        super(Component.translatable("gui.arrowlib.color_picker.title"));
        this.parent = parent;
        r = (initialColor >> 16) & 0xFF;
        g = (initialColor >> 8) & 0xFF;
        b = initialColor & 0xFF;
        this.horizontal = horizontal;
        this.callback = onColorPicked;
    }

    /**
     * Calculates the current full ARGB color integer based on the individual slider channel values.
     *
     * @return The compiled ARGB color integer.
     */
    public int getCurrentColor() {
        return ArrowColorUtils.toARGB(r, g, b);
    }

    @Override
    protected void init() {
        int centerX = width / 2;
        int centerY = height / 2;

        if (horizontal) {
            int sliderW = colorSliderLong;
            int sliderH = colorSliderShort;
            int startX = centerX - 110;
            int startY = centerY - 45;

            // Red Slider
            addRenderableWidget(new HorizontalColorSlider(
                    startX, startY, sliderW, sliderH, 0, r / 255.0,
                    this::getCurrentColor, val -> this.r = (int) (val * 255)));

            // Green Slider
            addRenderableWidget(new HorizontalColorSlider(
                    startX, startY + colorSliderSpacing, sliderW, sliderH, 1, g / 255.0,
                    this::getCurrentColor, val -> this.g = (int) (val * 255)));

            // Blue Slider
            addRenderableWidget(new HorizontalColorSlider(
                    startX, startY + (colorSliderSpacing * 2), sliderW, sliderH, 2, b / 255.0,
                    this::getCurrentColor, val -> this.b = (int) (val * 255)));
        } else {
            int sliderW = colorSliderShort;
            int sliderH = colorSliderLong;
            int startX = centerX - 100;
            int startY = centerY - 55;

            // Red Slider
            addRenderableWidget(new VerticalColorSlider(
                    startX, startY, sliderW, sliderH, 0, r / 255.0,
                    this::getCurrentColor, val -> this.r = (int) (val * 255)));

            // Green Slider
            addRenderableWidget(new VerticalColorSlider(
                    startX + colorSliderSpacing, startY, sliderW, sliderH, 1, g / 255.0,
                    this::getCurrentColor, val -> this.g = (int) (val * 255)));

            // Blue Slider
            addRenderableWidget(new VerticalColorSlider(
                    startX + (colorSliderSpacing * 2), startY, sliderW, sliderH, 2, b / 255.0,
                    this::getCurrentColor, val -> this.b = (int) (val * 255)));
        }

        // Save Button (Centered below layout)
        addRenderableWidget(ArrowGuiUtils.createButton("gui.arrowlib.button.save", button -> {
            callback.accept(getCurrentColor());
            assert minecraft != null;
            minecraft.setScreen(parent);
        }, centerX - 50, centerY + 65, 100, 20).build());
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float particleTick) {
        super.render(guiGraphics, mouseX, mouseY, particleTick);

        int centerX = width / 2;
        int centerY = height / 2;

        int redTextColor = 0xFF000000 | (r << 16);
        int greenTextColor = 0xFF000000 | (g << 8);
        int blueTextColor = 0xFF000000 | b;

        if (horizontal) {
            int startX = centerX - 110;
            int startY = centerY - 45;

            guiGraphics.drawString(font, "R: " + r, startX - 42, startY + 4, redTextColor);
            guiGraphics.drawString(font, "G: " + g, startX - 42, startY + 4 + colorSliderSpacing, greenTextColor);
            guiGraphics.drawString(font, "B: " + b, startX - 42, startY + 4 + (colorSliderSpacing * 2), blueTextColor);

            // Preview Box on Right
            int previewX = centerX + 25;
            int previewY = centerY - 45;
            int previewSize = 72;

            guiGraphics.fill(previewX - 2, previewY - 2, previewX + previewSize + 2, previewY + previewSize + 2, 0xFF000000);
            guiGraphics.fill(previewX - 1, previewY - 1, previewX + previewSize + 1, previewY + previewSize + 1, 0xFFFFFFFF);
            guiGraphics.fill(previewX, previewY, previewX + previewSize, previewY + previewSize, getCurrentColor());
        } else {
            int startX = centerX - 100;
            int startY = centerY - 55;

            // Labels above vertical sliders
            guiGraphics.drawString(font, "R: " + r, startX - 4, startY - 12, redTextColor);
            guiGraphics.drawString(font, "G: " + g, startX + colorSliderSpacing - 4, startY - 12, greenTextColor);
            guiGraphics.drawString(font, "B: " + b, startX + (colorSliderSpacing * 2) - 4, startY - 12, blueTextColor);

            // Preview Box on Right
            int previewX = centerX + 20;
            int previewY = centerY - 36;
            int previewSize = 72;

            guiGraphics.fill(previewX - 2, previewY - 2, previewX + previewSize + 2, previewY + previewSize + 2, 0xFF000000);
            guiGraphics.fill(previewX - 1, previewY - 1, previewX + previewSize + 1, previewY + previewSize + 1, 0xFFFFFFFF);
            guiGraphics.fill(previewX, previewY, previewX + previewSize, previewY + previewSize, getCurrentColor());
        }
    }

    @Override
    public void onClose() {
        assert minecraft != null;
        minecraft.setScreen(parent);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}