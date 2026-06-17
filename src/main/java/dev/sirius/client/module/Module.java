package dev.sirius.client.module;

import dev.sirius.client.SiriusClient;
import dev.sirius.client.module.setting.Setting;
import net.minecraftforge.common.MinecraftForge;

import java.util.ArrayList;
import java.util.List;

public abstract class Module {

    private final String name;
    private final String description;
    private final Category category;
    private boolean enabled;
    private int keybind;
    private final List<Setting<?>> settings = new ArrayList<>();

    // HUD position for HUD modules
    private int hudX = 5;
    private int hudY = 5;
    private int hudWidth = 80;
    private int hudHeight = 20;
    private boolean dragging = false;
    private int dragOffsetX, dragOffsetY;

    public Module(String name, String description, Category category, int keybind) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.keybind = keybind;
        this.enabled = false;
    }

    public void toggle() {
        setEnabled(!enabled);
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        if (enabled) {
            onEnable();
            MinecraftForge.EVENT_BUS.register(this);
        } else {
            onDisable();
            try {
                MinecraftForge.EVENT_BUS.unregister(this);
            } catch (Exception ignored) {}
        }
        SiriusClient.instance.getConfigManager().save(SiriusClient.instance.getModuleManager());
    }

    protected void onEnable() {}
    protected void onDisable() {}

    public String getName() { return name; }
    public String getDescription() { return description; }
    public Category getCategory() { return category; }
    public boolean isEnabled() { return enabled; }
    public int getKeybind() { return keybind; }
    public void setKeybind(int keybind) { this.keybind = keybind; }
    public List<Setting<?>> getSettings() { return settings; }

    protected <T> Setting<T> addSetting(Setting<T> setting) {
        settings.add(setting);
        return setting;
    }

    // HUD positioning
    public int getHudX() { return hudX; }
    public int getHudY() { return hudY; }
    public int getHudWidth() { return hudWidth; }
    public int getHudHeight() { return hudHeight; }
    public void setHudX(int x) { this.hudX = x; }
    public void setHudY(int y) { this.hudY = y; }
    public void setHudWidth(int w) { this.hudWidth = w; }
    public void setHudHeight(int h) { this.hudHeight = h; }

    public boolean isDragging() { return dragging; }
    public void setDragging(boolean d) { this.dragging = d; }
    public int getDragOffsetX() { return dragOffsetX; }
    public int getDragOffsetY() { return dragOffsetY; }
    public void setDragOffsetX(int x) { this.dragOffsetX = x; }
    public void setDragOffsetY(int y) { this.dragOffsetY = y; }

    public boolean isHudModule() {
        return category == Category.HUD;
    }

    public void renderHUD() {}
}
