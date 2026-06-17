package dev.sirius.client.module.modules.visual;

import dev.sirius.client.module.Category;
import dev.sirius.client.module.Module;
import dev.sirius.client.module.setting.Setting;
import dev.sirius.client.util.ColorUtil;
import dev.sirius.client.util.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class CustomCrosshair extends Module {

    private final Setting<String> style;
    private final Setting<String> crosshairColor;
    private final Setting<Integer> size;
    private final Setting<Integer> gap;
    private final Setting<Integer> thickness;

    public CustomCrosshair() {
        super("Crosshair", "Custom crosshair styles", Category.VISUAL, 0);
        style = addSetting(new Setting<>("Style", "cross")); // "classic", "dot", "cross"
        crosshairColor = addSetting(new Setting<>("Crosshair Color", "#FFFFFF"));
        size = addSetting(new Setting<>("Size", 6, 2, 20));
        gap = addSetting(new Setting<>("Gap", 3, 0, 10));
        thickness = addSetting(new Setting<>("Thickness", 1, 1, 5));
    }

    @SubscribeEvent
    public void onRenderCrosshair(RenderGameOverlayEvent.Pre event) {
        if (event.type != RenderGameOverlayEvent.ElementType.CROSSHAIRS) return;
        event.setCanceled(true);

        Minecraft mc = Minecraft.getMinecraft();
        ScaledResolution sr = new ScaledResolution(mc);
        int centerX = sr.getScaledWidth() / 2;
        int centerY = sr.getScaledHeight() / 2;
        int color = ColorUtil.fromHex(crosshairColor.getValue());
        int s = size.getValue();
        int g = gap.getValue();
        int t = thickness.getValue();

        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();

        String currentStyle = style.getValue().toLowerCase();

        switch (currentStyle) {
            case "dot":
                RenderUtil.drawFilledCircle(centerX, centerY, t + 1, color);
                break;
            case "cross":
                // Top
                RenderUtil.drawRect(centerX - t / 2.0, centerY - g - s, t, s, color);
                // Bottom
                RenderUtil.drawRect(centerX - t / 2.0, centerY + g, t, s, color);
                // Left
                RenderUtil.drawRect(centerX - g - s, centerY - t / 2.0, s, t, color);
                // Right
                RenderUtil.drawRect(centerX + g, centerY - t / 2.0, s, t, color);
                break;
            case "classic":
            default:
                // Simple + shape
                RenderUtil.drawRect(centerX - s, centerY, s * 2 + 1, 1, color);
                RenderUtil.drawRect(centerX, centerY - s, 1, s * 2 + 1, color);
                break;
        }

        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }
}
