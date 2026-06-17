package dev.sirius.client.module.modules.hud;

import dev.sirius.client.module.Category;
import dev.sirius.client.module.Module;
import dev.sirius.client.util.ColorUtil;
import dev.sirius.client.util.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetworkPlayerInfo;

public class PingDisplay extends Module {

    public PingDisplay() {
        super("Ping", "Shows current server ping", Category.HUD, 0);
        setHudX(80);
        setHudY(5);
        setHudWidth(70);
        setHudHeight(16);
    }

    @Override
    public void renderHUD() {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.thePlayer == null) return;

        int ping = 0;
        if (mc.getNetHandler() != null) {
            NetworkPlayerInfo info = mc.getNetHandler().getPlayerInfo(mc.thePlayer.getUniqueID());
            if (info != null) {
                ping = info.getResponseTime();
            }
        }

        int color;
        if (ping < 50) color = 0xFF00FF00;
        else if (ping < 100) color = 0xFFFFFF00;
        else if (ping < 200) color = 0xFFFFAA00;
        else color = 0xFFFF0000;

        String display = ping + "ms";
        RenderUtil.drawRoundedRect(getHudX(), getHudY(), getHudWidth(), getHudHeight(), 2, ColorUtil.withAlpha(ColorUtil.DARK_BLACK, 180));
        mc.fontRendererObj.drawStringWithShadow(display, getHudX() + 4, getHudY() + 4, color);
    }
}
