package dev.sirius.client.module.modules.hud;

import dev.sirius.client.module.Category;
import dev.sirius.client.module.Module;
import dev.sirius.client.util.ColorUtil;
import dev.sirius.client.util.RenderUtil;
import net.minecraft.client.Minecraft;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ClockModule extends Module {

    private final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

    public ClockModule() {
        super("Clock", "Shows real world time", Category.HUD, 0);
        setHudX(5);
        setHudY(85);
        setHudWidth(70);
        setHudHeight(16);
    }

    @Override
    public void renderHUD() {
        Minecraft mc = Minecraft.getMinecraft();
        String time = timeFormat.format(new Date());

        RenderUtil.drawRoundedRect(getHudX(), getHudY(), getHudWidth(), getHudHeight(), 2, ColorUtil.withAlpha(ColorUtil.DARK_BLACK, 180));
        mc.fontRendererObj.drawStringWithShadow(time, getHudX() + 4, getHudY() + 4, ColorUtil.WHITE);
    }
}
