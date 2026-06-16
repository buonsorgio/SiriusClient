package dev.sirius.client.event;

import dev.sirius.client.SiriusClient;
import dev.sirius.client.util.ColorUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class MainMenuHandler {

    @SubscribeEvent
    public void onDrawScreen(GuiScreenEvent.DrawScreenEvent.Post event) {
        if (!(event.gui instanceof GuiMainMenu)) return;

        Minecraft mc = Minecraft.getMinecraft();

        // "✦ SIRIUS CLIENT" branding in top-left
        // Multiple layers for glow effect
        mc.fontRendererObj.drawStringWithShadow("\u2726 SIRIUS CLIENT", 6, 6, ColorUtil.withAlpha(ColorUtil.DARK_RED, 100));
        mc.fontRendererObj.drawStringWithShadow("\u2726 SIRIUS CLIENT", 5, 5, ColorUtil.DARK_RED);

        // Version
        mc.fontRendererObj.drawStringWithShadow("v" + SiriusClient.VERSION, 5, 17, ColorUtil.SILVER);
    }
}
