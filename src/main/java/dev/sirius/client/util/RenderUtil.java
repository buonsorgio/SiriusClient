package dev.sirius.client.util;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import org.lwjgl.opengl.GL11;

public class RenderUtil {

    public static void drawRect(double x, double y, double width, double height, int color) {
        Gui.drawRect((int) x, (int) y, (int) (x + width), (int) (y + height), color);
    }

    public static void drawBorderedRect(double x, double y, double width, double height, float borderWidth, int borderColor, int fillColor) {
        drawRect(x, y, width, height, fillColor);
        drawRect(x, y, width, borderWidth, borderColor);
        drawRect(x, y + height - borderWidth, width, borderWidth, borderColor);
        drawRect(x, y, borderWidth, height, borderColor);
        drawRect(x + width - borderWidth, y, borderWidth, height, borderColor);
    }

    public static void drawRoundedRect(double x, double y, double width, double height, double radius, int color) {
        drawRect(x + radius, y, width - radius * 2, height, color);
        drawRect(x, y + radius, radius, height - radius * 2, color);
        drawRect(x + width - radius, y + radius, radius, height - radius * 2, color);
        drawFilledCircle(x + radius, y + radius, radius, color);
        drawFilledCircle(x + width - radius, y + radius, radius, color);
        drawFilledCircle(x + radius, y + height - radius, radius, color);
        drawFilledCircle(x + width - radius, y + height - radius, radius, color);
    }

    public static void drawFilledCircle(double cx, double cy, double radius, int color) {
        float a = (float) (color >> 24 & 255) / 255.0f;
        float r = (float) (color >> 16 & 255) / 255.0f;
        float g = (float) (color >> 8 & 255) / 255.0f;
        float b = (float) (color & 255) / 255.0f;

        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(r, g, b, a);

        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer wr = tessellator.getWorldRenderer();
        wr.begin(GL11.GL_TRIANGLE_FAN, DefaultVertexFormats.POSITION);
        wr.pos(cx, cy, 0).endVertex();
        for (int i = 0; i <= 360; i += 5) {
            double rad = Math.toRadians(i);
            wr.pos(cx + Math.sin(rad) * radius, cy + Math.cos(rad) * radius, 0).endVertex();
        }
        tessellator.draw();

        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void drawGradientRect(double x, double y, double width, double height, int startColor, int endColor) {
        float sa = (float) (startColor >> 24 & 255) / 255.0f;
        float sr = (float) (startColor >> 16 & 255) / 255.0f;
        float sg = (float) (startColor >> 8 & 255) / 255.0f;
        float sb = (float) (startColor & 255) / 255.0f;

        float ea = (float) (endColor >> 24 & 255) / 255.0f;
        float er = (float) (endColor >> 16 & 255) / 255.0f;
        float eg = (float) (endColor >> 8 & 255) / 255.0f;
        float eb = (float) (endColor & 255) / 255.0f;

        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.shadeModel(GL11.GL_SMOOTH);

        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer wr = tessellator.getWorldRenderer();
        wr.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
        wr.pos(x + width, y, 0).color(sr, sg, sb, sa).endVertex();
        wr.pos(x, y, 0).color(sr, sg, sb, sa).endVertex();
        wr.pos(x, y + height, 0).color(er, eg, eb, ea).endVertex();
        wr.pos(x + width, y + height, 0).color(er, eg, eb, ea).endVertex();
        tessellator.draw();

        GlStateManager.shadeModel(GL11.GL_FLAT);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }

    public static void startScissor(int x, int y, int width, int height) {
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        net.minecraft.client.Minecraft mc = net.minecraft.client.Minecraft.getMinecraft();
        int scaleFactor = new net.minecraft.client.gui.ScaledResolution(mc).getScaleFactor();
        GL11.glScissor(x * scaleFactor, mc.displayHeight - (y + height) * scaleFactor, width * scaleFactor, height * scaleFactor);
    }

    public static void stopScissor() {
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
    }
}
