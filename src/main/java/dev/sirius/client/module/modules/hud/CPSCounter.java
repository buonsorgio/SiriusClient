package dev.sirius.client.module.modules.hud;

import dev.sirius.client.module.Category;
import dev.sirius.client.module.Module;
import dev.sirius.client.util.ColorUtil;
import dev.sirius.client.util.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Mouse;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CPSCounter extends Module {

    private final List<Long> leftClicks = new ArrayList<>();
    private final List<Long> rightClicks = new ArrayList<>();
    private boolean leftWasDown = false;
    private boolean rightWasDown = false;

    public CPSCounter() {
        super("CPS Counter", "Shows left and right click CPS", Category.HUD, 0);
        setHudX(5);
        setHudY(25);
        setHudWidth(90);
        setHudHeight(16);
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        if (Minecraft.getMinecraft().currentScreen != null) return;

        boolean leftDown = Mouse.isButtonDown(0);
        boolean rightDown = Mouse.isButtonDown(1);

        if (leftDown && !leftWasDown) {
            leftClicks.add(System.currentTimeMillis());
        }
        if (rightDown && !rightWasDown) {
            rightClicks.add(System.currentTimeMillis());
        }

        leftWasDown = leftDown;
        rightWasDown = rightDown;

        long now = System.currentTimeMillis();
        pruneOld(leftClicks, now);
        pruneOld(rightClicks, now);
    }

    private void pruneOld(List<Long> list, long now) {
        Iterator<Long> it = list.iterator();
        while (it.hasNext()) {
            if (now - it.next() > 1000) it.remove();
        }
    }

    @Override
    public void renderHUD() {
        Minecraft mc = Minecraft.getMinecraft();
        int leftCPS = leftClicks.size();
        int rightCPS = rightClicks.size();

        RenderUtil.drawRoundedRect(getHudX(), getHudY(), getHudWidth(), getHudHeight(), 2, ColorUtil.withAlpha(ColorUtil.DARK_BLACK, 180));
        mc.fontRendererObj.drawStringWithShadow(leftCPS + " | " + rightCPS + " CPS", getHudX() + 4, getHudY() + 4, ColorUtil.WHITE);
    }
}
