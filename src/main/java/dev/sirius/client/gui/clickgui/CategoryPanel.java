package dev.sirius.client.gui.clickgui;

import dev.sirius.client.module.Category;
import dev.sirius.client.module.Module;
import dev.sirius.client.module.ModuleManager;
import dev.sirius.client.util.ColorUtil;
import dev.sirius.client.util.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;

import java.util.ArrayList;
import java.util.List;

public class CategoryPanel {

    private final Category category;
    private final List<ModuleButton> buttons = new ArrayList<>();
    private int y;
    private int scrollOffset = 0;
    private boolean expanded = true;

    public CategoryPanel(Category category) {
        this.category = category;
    }

    public void init(ModuleManager moduleManager) {
        buttons.clear();
        for (Module module : moduleManager.getModulesByCategory(category)) {
            buttons.add(new ModuleButton(module));
        }
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setScrollOffset(int offset) {
        this.scrollOffset = offset;
    }

    public int getHeight() {
        if (!expanded) return 22;
        return 22 + buttons.size() * 22 + 4;
    }

    public void draw(net.minecraft.client.gui.GuiScreen screen, int panelX, int panelWidth, int mouseX, int mouseY, float animation) {
        FontRenderer fr = Minecraft.getMinecraft().fontRendererObj;
        int drawY = y + scrollOffset;

        // Category header
        int headerAlpha = (int) (animation * 255);
        RenderUtil.drawRoundedRect(panelX, drawY, panelWidth, 20, 2, ColorUtil.withAlpha(ColorUtil.PANEL_BG, headerAlpha));
        fr.drawStringWithShadow(category.getDisplayName(), panelX + 8, drawY + 6, ColorUtil.withAlpha(ColorUtil.DARK_RED, headerAlpha));

        String arrow = expanded ? "\u25BC" : "\u25B6";
        fr.drawStringWithShadow(arrow, panelX + panelWidth - 14, drawY + 6, ColorUtil.withAlpha(ColorUtil.SILVER, headerAlpha));

        if (!expanded) return;

        // Module buttons
        int buttonY = drawY + 22;
        for (ModuleButton button : buttons) {
            button.draw(panelX + 4, buttonY, panelWidth - 8, mouseX, mouseY, animation);
            buttonY += 22;
        }

        // Update next panel position
    }

    public Module mouseClicked(int mouseX, int mouseY, int mouseButton) {
        int drawY = y + scrollOffset;

        // Check header click for expand/collapse
        if (mouseX >= 20 && mouseX <= 180 && mouseY >= drawY && mouseY <= drawY + 20) {
            if (mouseButton == 0) {
                expanded = !expanded;
            }
            return null;
        }

        if (!expanded) return null;

        int buttonY = drawY + 22;
        for (ModuleButton button : buttons) {
            if (mouseX >= 24 && mouseX <= 172 && mouseY >= buttonY && mouseY <= buttonY + 20) {
                return button.getModule();
            }
            buttonY += 22;
        }
        return null;
    }

    public Category getCategory() {
        return category;
    }

    public boolean isExpanded() {
        return expanded;
    }
}
