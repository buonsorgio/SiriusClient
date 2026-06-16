package dev.sirius.client.gui.clickgui;

import dev.sirius.client.module.Module;
import dev.sirius.client.util.ColorUtil;
import dev.sirius.client.util.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;

public class ModuleButton {

    private final Module module;
    private float hoverAnimation = 0f;
    private float enableAnimation = 0f;

    public ModuleButton(Module module) {
        this.module = module;
    }

    public void draw(int x, int y, int width, int mouseX, int mouseY, float globalAnimation) {
        FontRenderer fr = Minecraft.getMinecraft().fontRendererObj;
        boolean hovered = mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + 20;

        // Smooth hover animation
        if (hovered) {
            hoverAnimation = Math.min(1f, hoverAnimation + 0.1f);
        } else {
            hoverAnimation = Math.max(0f, hoverAnimation - 0.1f);
        }

        // Smooth enable animation
        if (module.isEnabled()) {
            enableAnimation = Math.min(1f, enableAnimation + 0.08f);
        } else {
            enableAnimation = Math.max(0f, enableAnimation - 0.08f);
        }

        int alpha = (int) (globalAnimation * 255);

        // Background
        int bgColor = ColorUtil.withAlpha(
                ColorUtil.interpolate(ColorUtil.MODULE_BG, ColorUtil.MODULE_HOVER, hoverAnimation),
                alpha
        );
        RenderUtil.drawRoundedRect(x, y, width, 20, 2, bgColor);

        // Enabled glow effect
        if (enableAnimation > 0) {
            int glowAlpha = (int) (enableAnimation * alpha * 0.3f);
            RenderUtil.drawRoundedRect(x, y, width, 20, 2, ColorUtil.withAlpha(ColorUtil.DARK_RED, glowAlpha));

            // Left accent bar
            int barAlpha = (int) (enableAnimation * alpha);
            RenderUtil.drawRect(x, y + 2, 2, 16, ColorUtil.withAlpha(ColorUtil.DARK_RED, barAlpha));
        }

        // Module name
        int textColor = module.isEnabled()
                ? ColorUtil.withAlpha(ColorUtil.WHITE, alpha)
                : ColorUtil.withAlpha(ColorUtil.SILVER, alpha);
        fr.drawStringWithShadow(module.getName(), x + 8, y + 6, textColor);

        // Toggle indicator
        int toggleX = x + width - 20;
        int toggleY = y + 5;
        int toggleBg = module.isEnabled()
                ? ColorUtil.withAlpha(ColorUtil.DARK_RED, alpha)
                : ColorUtil.withAlpha(0xFF333333, alpha);
        RenderUtil.drawRoundedRect(toggleX, toggleY, 14, 10, 5, toggleBg);

        int knobX = module.isEnabled() ? toggleX + 7 : toggleX + 2;
        RenderUtil.drawFilledCircle(knobX + 3, toggleY + 5, 4, ColorUtil.withAlpha(ColorUtil.WHITE, alpha));
    }

    public Module getModule() {
        return module;
    }
}
