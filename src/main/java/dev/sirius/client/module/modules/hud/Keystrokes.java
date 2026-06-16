package dev.sirius.client.module.modules.hud;

import dev.sirius.client.module.Category;
import dev.sirius.client.module.Module;
import dev.sirius.client.util.ColorUtil;
import dev.sirius.client.util.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Mouse;

public class Keystrokes extends Module {

    public Keystrokes() {
        super("Keystrokes", "WASD + LMB/RMB keystroke display", Category.HUD, 0);
        setHudX(5);
        setHudY(220);
        setHudWidth(74);
        setHudHeight(74);
    }

    @Override
    public void renderHUD() {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.thePlayer == null) return;

        int x = getHudX();
        int y = getHudY();
        int keySize = 22;
        int gap = 2;

        // Background
        RenderUtil.drawRoundedRect(x, y, getHudWidth(), getHudHeight(), 2, ColorUtil.withAlpha(ColorUtil.DARK_BLACK, 180));

        KeyBinding forward = mc.gameSettings.keyBindForward;
        KeyBinding left = mc.gameSettings.keyBindLeft;
        KeyBinding back = mc.gameSettings.keyBindBack;
        KeyBinding right = mc.gameSettings.keyBindRight;

        // W key (top center)
        drawKey(mc, x + keySize + gap + 2, y + 2, keySize, "W", forward.isKeyDown());

        // A key (middle left)
        drawKey(mc, x + 2, y + keySize + gap + 2, keySize, "A", left.isKeyDown());

        // S key (middle center)
        drawKey(mc, x + keySize + gap + 2, y + keySize + gap + 2, keySize, "S", back.isKeyDown());

        // D key (middle right)
        drawKey(mc, x + (keySize + gap) * 2 + 2, y + keySize + gap + 2, keySize, "D", right.isKeyDown());

        // LMB (bottom left)
        drawKey(mc, x + 2, y + (keySize + gap) * 2 + 2, keySize + gap / 2 + keySize / 2, "LMB", Mouse.isButtonDown(0));

        // RMB (bottom right)
        drawKey(mc, x + keySize + gap + keySize / 2 + gap + 2, y + (keySize + gap) * 2 + 2, keySize + gap / 2 + keySize / 2, "RMB", Mouse.isButtonDown(1));
    }

    private void drawKey(Minecraft mc, int x, int y, int size, String label, boolean pressed) {
        int bg = pressed ? ColorUtil.DARK_RED : ColorUtil.withAlpha(ColorUtil.MODULE_BG, 200);
        int textColor = pressed ? ColorUtil.WHITE : ColorUtil.SILVER;

        RenderUtil.drawRoundedRect(x, y, size, 22, 2, bg);
        int textWidth = mc.fontRendererObj.getStringWidth(label);
        mc.fontRendererObj.drawStringWithShadow(label, x + (size - textWidth) / 2f, y + 7, textColor);
    }
}
