package dev.sirius.client.util;

import java.awt.*;

public class ColorUtil {

    public static final int DARK_BLACK = 0xFF0A0A0A;
    public static final int DARK_RED = 0xFF8B0000;
    public static final int SILVER = 0xFFC0C0C0;
    public static final int WHITE = 0xFFFFFFFF;

    public static final int BG_OVERLAY = 0xD90A0A0A; // ~85% opacity
    public static final int PANEL_BG = 0xFF141414;
    public static final int MODULE_BG = 0xFF1A1A1A;
    public static final int MODULE_HOVER = 0xFF2A1A1A;
    public static final int MODULE_ENABLED = 0xFF8B0000;

    public static int toARGB(int r, int g, int b, int a) {
        return (a << 24) | (r << 16) | (g << 8) | b;
    }

    public static int toARGB(int r, int g, int b) {
        return toARGB(r, g, b, 255);
    }

    public static int withAlpha(int color, int alpha) {
        return (color & 0x00FFFFFF) | (alpha << 24);
    }

    public static int getRed(int color) { return (color >> 16) & 0xFF; }
    public static int getGreen(int color) { return (color >> 8) & 0xFF; }
    public static int getBlue(int color) { return color & 0xFF; }
    public static int getAlpha(int color) { return (color >> 24) & 0xFF; }

    public static int fromHex(String hex) {
        if (hex.startsWith("#")) hex = hex.substring(1);
        if (hex.length() == 6) hex = "FF" + hex;
        return (int) Long.parseLong(hex, 16);
    }

    public static String toHex(int color) {
        return String.format("#%06X", (color & 0xFFFFFF));
    }

    public static int interpolate(int color1, int color2, float factor) {
        int r1 = getRed(color1), g1 = getGreen(color1), b1 = getBlue(color1), a1 = getAlpha(color1);
        int r2 = getRed(color2), g2 = getGreen(color2), b2 = getBlue(color2), a2 = getAlpha(color2);
        int r = (int) (r1 + (r2 - r1) * factor);
        int g = (int) (g1 + (g2 - g1) * factor);
        int b = (int) (b1 + (b2 - b1) * factor);
        int a = (int) (a1 + (a2 - a1) * factor);
        return toARGB(r, g, b, a);
    }

    public static int hsbToRGB(float hue, float saturation, float brightness) {
        return Color.HSBtoRGB(hue, saturation, brightness) | 0xFF000000;
    }

    public static float[] rgbToHSB(int color) {
        return Color.RGBtoHSB(getRed(color), getGreen(color), getBlue(color), null);
    }
}
