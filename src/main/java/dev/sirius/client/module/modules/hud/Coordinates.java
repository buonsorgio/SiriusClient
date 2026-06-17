package dev.sirius.client.module.modules.hud;

import dev.sirius.client.module.Category;
import dev.sirius.client.module.Module;
import dev.sirius.client.util.ColorUtil;
import dev.sirius.client.util.RenderUtil;
import net.minecraft.client.Minecraft;

public class Coordinates extends Module {

    public Coordinates() {
        super("Coordinates", "Displays X Y Z position", Category.HUD, 0);
        setHudX(5);
        setHudY(45);
        setHudWidth(140);
        setHudHeight(16);
    }

    @Override
    public void renderHUD() {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.thePlayer == null) return;

        String coords = String.format("X: %.1f  Y: %.1f  Z: %.1f",
                mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ);

        setHudWidth(mc.fontRendererObj.getStringWidth(coords) + 8);

        RenderUtil.drawRoundedRect(getHudX(), getHudY(), getHudWidth(), getHudHeight(), 2, ColorUtil.withAlpha(ColorUtil.DARK_BLACK, 180));
        mc.fontRendererObj.drawStringWithShadow(coords, getHudX() + 4, getHudY() + 4, ColorUtil.WHITE);
    }
}
