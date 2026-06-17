package dev.sirius.client.gui.hud;

import dev.sirius.client.SiriusClient;
import dev.sirius.client.module.Module;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class HUDEditor {

    @SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent.Post event) {
        if (event.type != RenderGameOverlayEvent.ElementType.ALL) return;
        if (Minecraft.getMinecraft().currentScreen != null) return;

        for (Module module : SiriusClient.instance.getModuleManager().getModules()) {
            if (module.isHudModule() && module.isEnabled()) {
                module.renderHUD();
            }
        }
    }
}
