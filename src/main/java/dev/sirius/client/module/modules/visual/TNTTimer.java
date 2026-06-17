package dev.sirius.client.module.modules.visual;

import dev.sirius.client.module.Category;
import dev.sirius.client.module.Module;
import dev.sirius.client.util.ColorUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class TNTTimer extends Module {

    public TNTTimer() {
        super("TNT Timer", "Shows countdown above lit TNT", Category.VISUAL, 0);
    }

    @SubscribeEvent
    public void onRenderWorldLast(RenderWorldLastEvent event) {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.theWorld == null) return;

        RenderManager rm = mc.getRenderManager();

        for (Entity entity : mc.theWorld.loadedEntityList) {
            if (entity instanceof EntityTNTPrimed) {
                EntityTNTPrimed tnt = (EntityTNTPrimed) entity;
                int fuse = tnt.fuse;
                float seconds = fuse / 20.0f;
                String text = String.format("%.1fs", seconds);

                double x = tnt.lastTickPosX + (tnt.posX - tnt.lastTickPosX) * event.partialTicks - rm.viewerPosX;
                double y = tnt.lastTickPosY + (tnt.posY - tnt.lastTickPosY) * event.partialTicks - rm.viewerPosY + tnt.height + 0.5;
                double z = tnt.lastTickPosZ + (tnt.posZ - tnt.lastTickPosZ) * event.partialTicks - rm.viewerPosZ;

                renderNameTag(text, x, y, z, seconds);
            }
        }
    }

    private void renderNameTag(String text, double x, double y, double z, float seconds) {
        Minecraft mc = Minecraft.getMinecraft();
        float scale = 0.02666667f;

        GlStateManager.pushMatrix();
        GlStateManager.translate((float) x, (float) y, (float) z);
        GlStateManager.rotate(-mc.getRenderManager().playerViewY, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(mc.getRenderManager().playerViewX, 1.0f, 0.0f, 0.0f);
        GlStateManager.scale(-scale, -scale, scale);
        GlStateManager.disableLighting();
        GlStateManager.depthMask(false);
        GlStateManager.disableDepth();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);

        int textWidth = mc.fontRendererObj.getStringWidth(text);
        int color = seconds > 2f ? ColorUtil.WHITE : seconds > 1f ? 0xFFFFAA00 : 0xFFFF0000;

        // Background
        net.minecraft.client.gui.Gui.drawRect(-textWidth / 2 - 2, -2, textWidth / 2 + 2, 10, ColorUtil.withAlpha(ColorUtil.DARK_BLACK, 180));
        mc.fontRendererObj.drawStringWithShadow(text, -textWidth / 2f, 0, color);

        GlStateManager.enableDepth();
        GlStateManager.depthMask(true);
        GlStateManager.enableLighting();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }
}
