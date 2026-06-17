package dev.sirius.client.module.modules.utility;

import dev.sirius.client.module.Category;
import dev.sirius.client.module.Module;
import dev.sirius.client.module.setting.Setting;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;

public class Zoom extends Module {

    private final Setting<Float> zoomFov;
    private final Setting<Float> scrollStep;
    private float originalFov;
    private float currentZoom;
    private boolean zooming = false;

    public Zoom() {
        super("Zoom", "C key zoom with scroll adjustment", Category.UTILITY, Keyboard.KEY_C);
        zoomFov = addSetting(new Setting<>("Zoom FOV", 30.0f, 5.0f, 90.0f));
        scrollStep = addSetting(new Setting<>("Scroll Step", 5.0f, 1.0f, 15.0f));
    }

    @Override
    protected void onEnable() {
        Minecraft mc = Minecraft.getMinecraft();
        originalFov = mc.gameSettings.fovSetting;
        currentZoom = zoomFov.getValue();
        zooming = true;
        mc.gameSettings.smoothCamera = true;
    }

    @Override
    protected void onDisable() {
        Minecraft mc = Minecraft.getMinecraft();
        mc.gameSettings.fovSetting = originalFov;
        mc.gameSettings.smoothCamera = false;
        zooming = false;
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        if (!zooming) return;

        Minecraft.getMinecraft().gameSettings.fovSetting = currentZoom;
    }

    @SubscribeEvent
    public void onMouseScroll(MouseEvent event) {
        if (!zooming) return;
        if (event.dwheel == 0) return;

        event.setCanceled(true);

        float step = scrollStep.getValue();
        if (event.dwheel > 0) {
            currentZoom = Math.max(5.0f, currentZoom - step);
        } else {
            currentZoom = Math.min(90.0f, currentZoom + step);
        }
    }
}
