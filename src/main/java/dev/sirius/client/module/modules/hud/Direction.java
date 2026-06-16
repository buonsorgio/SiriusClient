package dev.sirius.client.module.modules.hud;

import dev.sirius.client.module.Category;
import dev.sirius.client.module.Module;
import dev.sirius.client.util.ColorUtil;
import dev.sirius.client.util.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.util.MathHelper;

public class Direction extends Module {

    private static final String[] DIRECTIONS = {"S", "SW", "W", "NW", "N", "NE", "E", "SE"};

    public Direction() {
        super("Direction", "Shows cardinal direction", Category.HUD, 0);
        setHudX(5);
        setHudY(65);
        setHudWidth(80);
        setHudHeight(16);
    }

    @Override
    public void renderHUD() {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.thePlayer == null) return;

        float yaw = MathHelper.wrapAngleTo180_float(mc.thePlayer.rotationYaw);
        int index = (int) ((yaw + 202.5f) / 45.0f) & 7;
        String direction = DIRECTIONS[index];
        String display = direction + " (" + String.format("%.1f", yaw) + "\u00B0)";

        setHudWidth(mc.fontRendererObj.getStringWidth(display) + 8);

        RenderUtil.drawRoundedRect(getHudX(), getHudY(), getHudWidth(), getHudHeight(), 2, ColorUtil.withAlpha(ColorUtil.DARK_BLACK, 180));
        mc.fontRendererObj.drawStringWithShadow(display, getHudX() + 4, getHudY() + 4, ColorUtil.WHITE);
    }
}
