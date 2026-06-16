package dev.sirius.client.module.modules.hud;

import dev.sirius.client.module.Category;
import dev.sirius.client.module.Module;
import dev.sirius.client.util.ColorUtil;
import dev.sirius.client.util.RenderUtil;
import net.minecraft.client.Minecraft;

public class FPSCounter extends Module {

    public FPSCounter() {
        super("FPS Counter", "Displays current FPS", Category.HUD, 0);
        setHudX(5);
        setHudY(5);
        setHudWidth(70);
        setHudHeight(16);
    }

    @Override
    public void renderHUD() {
        Minecraft mc = Minecraft.getMinecraft();
        int fps = Minecraft.getDebugFPS();

        RenderUtil.drawRoundedRect(getHudX(), getHudY(), getHudWidth(), getHudHeight(), 2, ColorUtil.withAlpha(ColorUtil.DARK_BLACK, 180));
        mc.fontRendererObj.drawStringWithShadow("FPS: " + fps, getHudX() + 4, getHudY() + 4, ColorUtil.WHITE);
    }
}
