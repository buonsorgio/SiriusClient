package dev.sirius.client.module.modules.hud;

import dev.sirius.client.module.Category;
import dev.sirius.client.module.Module;
import dev.sirius.client.module.setting.Setting;
import dev.sirius.client.util.ColorUtil;
import dev.sirius.client.util.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.boss.BossStatus;

public class BossBarModule extends Module {

    private final Setting<Boolean> customStyle;

    public BossBarModule() {
        super("BossBar", "Custom styled boss bar", Category.HUD, 0);
        customStyle = addSetting(new Setting<>("Custom Style", true));
    }

    @Override
    public void renderHUD() {
        if (!customStyle.getValue()) return;
        if (BossStatus.bossName == null || BossStatus.statusBarTime <= 0) return;

        Minecraft mc = Minecraft.getMinecraft();
        ScaledResolution sr = new ScaledResolution(mc);
        int screenWidth = sr.getScaledWidth();

        int barWidth = 182;
        int barX = (screenWidth - barWidth) / 2;
        int barY = 12;

        // Background
        RenderUtil.drawRoundedRect(barX - 2, barY - 2, barWidth + 4, 18, 2, ColorUtil.withAlpha(ColorUtil.DARK_BLACK, 200));

        // Health bar
        float healthPercent = BossStatus.healthScale;
        int filledWidth = (int) (barWidth * healthPercent);

        RenderUtil.drawRect(barX, barY + 8, barWidth, 5, ColorUtil.withAlpha(0xFF333333, 200));
        RenderUtil.drawRect(barX, barY + 8, filledWidth, 5, ColorUtil.DARK_RED);

        // Boss name
        String name = BossStatus.bossName;
        int nameWidth = mc.fontRendererObj.getStringWidth(name);
        mc.fontRendererObj.drawStringWithShadow(name, barX + (barWidth - nameWidth) / 2f, barY, ColorUtil.WHITE);
    }
}
