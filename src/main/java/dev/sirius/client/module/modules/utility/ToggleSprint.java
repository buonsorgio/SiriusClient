package dev.sirius.client.module.modules.utility;

import dev.sirius.client.module.Category;
import dev.sirius.client.module.Module;
import dev.sirius.client.module.setting.Setting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class ToggleSprint extends Module {

    private final Setting<Boolean> holdMode;
    private boolean sprinting = false;

    public ToggleSprint() {
        super("Toggle Sprint", "Hold/toggle sprint mode", Category.UTILITY, 0);
        holdMode = addSetting(new Setting<>("Hold Mode", false));
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;

        Minecraft mc = Minecraft.getMinecraft();
        if (mc.thePlayer == null || mc.currentScreen != null) return;

        if (holdMode.getValue()) {
            mc.thePlayer.setSprinting(mc.thePlayer.moveForward > 0 && !mc.thePlayer.isSneaking());
        } else {
            KeyBinding sprintKey = mc.gameSettings.keyBindSprint;
            if (sprintKey.isKeyDown()) {
                sprinting = !sprinting;
            }
            if (sprinting && mc.thePlayer.moveForward > 0 && !mc.thePlayer.isSneaking()) {
                mc.thePlayer.setSprinting(true);
            }
        }
    }
}
