package dev.sirius.client.gui.clickgui;

import dev.sirius.client.SiriusClient;
import dev.sirius.client.module.Category;
import dev.sirius.client.module.Module;
import dev.sirius.client.module.setting.Setting;
import dev.sirius.client.util.ColorUtil;
import dev.sirius.client.util.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ClickGUI extends GuiScreen {

    private final List<CategoryPanel> panels = new ArrayList<>();
    private Module selectedModule = null;
    private float animationProgress = 0f;
    private boolean closing = false;
    private int scrollOffset = 0;

    public ClickGUI() {
        for (Category category : Category.values()) {
            panels.add(new CategoryPanel(category));
        }
    }

    @Override
    public void initGui() {
        closing = false;
        animationProgress = 0f;

        int panelY = 40;
        for (CategoryPanel panel : panels) {
            panel.setY(panelY);
            panel.init(SiriusClient.instance.getModuleManager());
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        if (closing) {
            animationProgress -= partialTicks * 0.15f;
            if (animationProgress <= 0f) {
                Minecraft.getMinecraft().displayGuiScreen(null);
                return;
            }
        } else {
            if (animationProgress < 1f) {
                animationProgress += partialTicks * 0.15f;
                if (animationProgress > 1f) animationProgress = 1f;
            }
        }

        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        int screenWidth = sr.getScaledWidth();
        int screenHeight = sr.getScaledHeight();

        // Dark overlay
        RenderUtil.drawRect(0, 0, screenWidth, screenHeight, ColorUtil.BG_OVERLAY);

        // Title
        int titleAlpha = (int) (animationProgress * 255);
        int titleColor = ColorUtil.withAlpha(ColorUtil.DARK_RED, titleAlpha);
        drawCenteredString(fontRendererObj, "\u2726 SIRIUS CLIENT", screenWidth / 2, 10, titleColor);
        drawCenteredString(fontRendererObj, "v" + SiriusClient.VERSION, screenWidth / 2, 22, ColorUtil.withAlpha(ColorUtil.SILVER, titleAlpha));

        // Left panel: categories and modules
        int panelX = (int) (20 * animationProgress);
        int panelWidth = 160;

        for (CategoryPanel panel : panels) {
            panel.draw(this, panelX, panelWidth, mouseX, mouseY, animationProgress);
        }

        // Right panel: settings for selected module
        if (selectedModule != null) {
            drawSettingsPanel(screenWidth, screenHeight, mouseX, mouseY);
        }

        // HUD elements preview (draggable when ClickGUI is open)
        for (Module module : SiriusClient.instance.getModuleManager().getModules()) {
            if (module.isHudModule() && module.isEnabled()) {
                int hudX = module.getHudX();
                int hudY = module.getHudY();
                int hudW = module.getHudWidth();
                int hudH = module.getHudHeight();

                // Draw drag handle background
                RenderUtil.drawRect(hudX - 1, hudY - 1, hudW + 2, hudH + 2, ColorUtil.withAlpha(ColorUtil.DARK_RED, 80));
                module.renderHUD();
            }
        }

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    private void drawSettingsPanel(int screenWidth, int screenHeight, int mouseX, int mouseY) {
        int settingsX = screenWidth - 220;
        int settingsY = 40;
        int settingsW = 200;

        // Panel background
        RenderUtil.drawRoundedRect(settingsX, settingsY, settingsW, 300, 3, ColorUtil.PANEL_BG);

        // Title
        drawString(fontRendererObj, selectedModule.getName() + " Settings", settingsX + 10, settingsY + 8, ColorUtil.DARK_RED);
        drawString(fontRendererObj, selectedModule.getDescription(), settingsX + 10, settingsY + 20, ColorUtil.SILVER);

        int yOffset = settingsY + 40;
        for (Setting<?> setting : selectedModule.getSettings()) {
            if (setting.isBoolean()) {
                boolean val = (Boolean) setting.getValue();
                int toggleColor = val ? ColorUtil.MODULE_ENABLED : ColorUtil.MODULE_BG;
                RenderUtil.drawRoundedRect(settingsX + 10, yOffset, settingsW - 20, 18, 2, toggleColor);
                drawString(fontRendererObj, setting.getName(), settingsX + 15, yOffset + 5, ColorUtil.WHITE);
                drawString(fontRendererObj, val ? "ON" : "OFF", settingsX + settingsW - 40, yOffset + 5, val ? 0xFF00FF00 : 0xFFFF0000);
                yOffset += 24;
            } else if (setting.isNumber()) {
                drawString(fontRendererObj, setting.getName(), settingsX + 15, yOffset + 2, ColorUtil.SILVER);
                yOffset += 14;

                // Slider
                RenderUtil.drawRect(settingsX + 10, yOffset, settingsW - 20, 6, ColorUtil.MODULE_BG);
                if (setting.getMin() != null && setting.getMax() != null) {
                    float min, max, current;
                    if (setting.getValue() instanceof Integer) {
                        min = ((Integer) setting.getMin()).floatValue();
                        max = ((Integer) setting.getMax()).floatValue();
                        current = ((Integer) setting.getValue()).floatValue();
                    } else if (setting.getValue() instanceof Float) {
                        min = (Float) setting.getMin();
                        max = (Float) setting.getMax();
                        current = (Float) setting.getValue();
                    } else {
                        min = ((Double) setting.getMin()).floatValue();
                        max = ((Double) setting.getMax()).floatValue();
                        current = ((Double) setting.getValue()).floatValue();
                    }
                    float percent = (current - min) / (max - min);
                    int sliderWidth = (int) ((settingsW - 20) * percent);
                    RenderUtil.drawRect(settingsX + 10, yOffset, sliderWidth, 6, ColorUtil.DARK_RED);
                    drawString(fontRendererObj, String.valueOf(setting.getValue()), settingsX + settingsW - 40, yOffset - 2, ColorUtil.WHITE);
                }
                yOffset += 16;
            } else if (setting.isColor()) {
                drawString(fontRendererObj, setting.getName(), settingsX + 15, yOffset + 2, ColorUtil.SILVER);
                yOffset += 14;
                String colorHex = (String) setting.getValue();
                int color = ColorUtil.fromHex(colorHex);
                RenderUtil.drawRect(settingsX + 10, yOffset, 30, 14, color);
                RenderUtil.drawBorderedRect(settingsX + 10, yOffset, 30, 14, 1, ColorUtil.SILVER, color);
                drawString(fontRendererObj, colorHex, settingsX + 48, yOffset + 3, ColorUtil.WHITE);
                yOffset += 22;
            } else if (setting.isString()) {
                drawString(fontRendererObj, setting.getName() + ": " + setting.getValue(), settingsX + 15, yOffset + 2, ColorUtil.SILVER);
                yOffset += 18;
            }
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        // Check HUD element dragging
        for (Module module : SiriusClient.instance.getModuleManager().getModules()) {
            if (module.isHudModule() && module.isEnabled()) {
                int hx = module.getHudX();
                int hy = module.getHudY();
                int hw = module.getHudWidth();
                int hh = module.getHudHeight();
                if (mouseX >= hx && mouseX <= hx + hw && mouseY >= hy && mouseY <= hy + hh) {
                    module.setDragging(true);
                    module.setDragOffsetX(mouseX - hx);
                    module.setDragOffsetY(mouseY - hy);
                    return;
                }
            }
        }

        // Check settings panel interactions
        if (selectedModule != null) {
            ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
            int settingsX = sr.getScaledWidth() - 220;
            int settingsY = 40;
            int settingsW = 200;
            int yOffset = settingsY + 40;

            for (Setting<?> setting : selectedModule.getSettings()) {
                if (setting.isBoolean()) {
                    if (mouseX >= settingsX + 10 && mouseX <= settingsX + settingsW - 10
                            && mouseY >= yOffset && mouseY <= yOffset + 18) {
                        @SuppressWarnings("unchecked")
                        Setting<Boolean> boolSetting = (Setting<Boolean>) setting;
                        boolSetting.setValue(!boolSetting.getValue());
                        return;
                    }
                    yOffset += 24;
                } else if (setting.isNumber()) {
                    yOffset += 14;
                    if (mouseX >= settingsX + 10 && mouseX <= settingsX + settingsW - 10
                            && mouseY >= yOffset && mouseY <= yOffset + 6) {
                        handleSliderClick(setting, settingsX + 10, settingsW - 20, mouseX);
                        return;
                    }
                    yOffset += 16;
                } else if (setting.isColor()) {
                    yOffset += 36;
                } else {
                    yOffset += 18;
                }
            }
        }

        // Check category panel clicks
        for (CategoryPanel panel : panels) {
            Module clicked = panel.mouseClicked(mouseX, mouseY, mouseButton);
            if (clicked != null) {
                if (mouseButton == 0) {
                    clicked.toggle();
                } else if (mouseButton == 1) {
                    selectedModule = (selectedModule == clicked) ? null : clicked;
                }
                return;
            }
        }

        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @SuppressWarnings("unchecked")
    private void handleSliderClick(Setting<?> setting, int sliderX, int sliderWidth, int mouseX) {
        float percent = Math.max(0, Math.min(1, (float) (mouseX - sliderX) / sliderWidth));
        if (setting.getValue() instanceof Integer) {
            Setting<Integer> s = (Setting<Integer>) setting;
            int range = s.getMax() - s.getMin();
            s.setValue(s.getMin() + (int) (range * percent));
        } else if (setting.getValue() instanceof Float) {
            Setting<Float> s = (Setting<Float>) setting;
            float range = s.getMax() - s.getMin();
            s.setValue(s.getMin() + range * percent);
        } else if (setting.getValue() instanceof Double) {
            Setting<Double> s = (Setting<Double>) setting;
            double range = s.getMax() - s.getMin();
            s.setValue(s.getMin() + range * percent);
        }
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        for (Module module : SiriusClient.instance.getModuleManager().getModules()) {
            if (module.isDragging()) {
                module.setDragging(false);
            }
        }
        super.mouseReleased(mouseX, mouseY, state);
    }

    @Override
    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        for (Module module : SiriusClient.instance.getModuleManager().getModules()) {
            if (module.isDragging()) {
                module.setHudX(mouseX - module.getDragOffsetX());
                module.setHudY(mouseY - module.getDragOffsetY());
            }
        }

        // Slider dragging in settings panel
        if (selectedModule != null && clickedMouseButton == 0) {
            ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
            int settingsX = sr.getScaledWidth() - 220;
            int settingsY = 40;
            int settingsW = 200;
            int yOffset = settingsY + 40;

            for (Setting<?> setting : selectedModule.getSettings()) {
                if (setting.isBoolean()) {
                    yOffset += 24;
                } else if (setting.isNumber()) {
                    yOffset += 14;
                    if (mouseX >= settingsX + 10 && mouseX <= settingsX + settingsW - 10
                            && mouseY >= yOffset - 4 && mouseY <= yOffset + 10) {
                        handleSliderClick(setting, settingsX + 10, settingsW - 20, mouseX);
                    }
                    yOffset += 16;
                } else if (setting.isColor()) {
                    yOffset += 36;
                } else {
                    yOffset += 18;
                }
            }
        }

        super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        int scroll = Mouse.getEventDWheel();
        if (scroll != 0) {
            scrollOffset += (scroll > 0) ? -15 : 15;
            for (CategoryPanel panel : panels) {
                panel.setScrollOffset(scrollOffset);
            }
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (keyCode == Keyboard.KEY_RSHIFT || keyCode == Keyboard.KEY_ESCAPE) {
            closing = true;
            return;
        }
        super.keyTyped(typedChar, keyCode);
    }

    @Override
    public void onGuiClosed() {
        SiriusClient.instance.getConfigManager().save(SiriusClient.instance.getModuleManager());
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
