package dev.sirius.client.event;

import dev.sirius.client.SiriusClient;
import dev.sirius.client.gui.clickgui.ClickGUI;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;

public class InputHandler {

    private final SiriusClient client;
    private final ClickGUI clickGUI;
    private boolean wasRShiftPressed = false;

    public InputHandler(SiriusClient client) {
        this.client = client;
        this.clickGUI = new ClickGUI();
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        if (Minecraft.getMinecraft().theWorld == null) return;

        boolean rShiftPressed = Keyboard.isKeyDown(Keyboard.KEY_RSHIFT);
        if (rShiftPressed && !wasRShiftPressed) {
            if (Minecraft.getMinecraft().currentScreen instanceof ClickGUI) {
                Minecraft.getMinecraft().displayGuiScreen(null);
            } else if (Minecraft.getMinecraft().currentScreen == null) {
                Minecraft.getMinecraft().displayGuiScreen(clickGUI);
            }
        }
        wasRShiftPressed = rShiftPressed;
    }

    public ClickGUI getClickGUI() {
        return clickGUI;
    }
}
