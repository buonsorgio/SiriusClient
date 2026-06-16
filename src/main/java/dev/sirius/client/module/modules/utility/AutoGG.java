package dev.sirius.client.module.modules.utility;

import dev.sirius.client.module.Category;
import dev.sirius.client.module.Module;
import dev.sirius.client.module.setting.Setting;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class AutoGG extends Module {

    private final Setting<String> message;
    private final Setting<Integer> delay;
    private long lastTrigger = 0;

    public AutoGG() {
        super("AutoGG", "Sends a message when a game ends", Category.UTILITY, 0);
        message = addSetting(new Setting<>("Message", "gg"));
        delay = addSetting(new Setting<>("Delay (ms)", 1000, 0, 5000));
    }

    @SubscribeEvent
    public void onChatReceived(ClientChatReceivedEvent event) {
        if (event.type == 2) return;

        String chatMessage = event.message.getUnformattedText();
        long now = System.currentTimeMillis();

        if (now - lastTrigger < 5000) return;

        if (isGameEndMessage(chatMessage)) {
            lastTrigger = now;
            new Thread(() -> {
                try {
                    Thread.sleep(delay.getValue());
                    Minecraft.getMinecraft().thePlayer.sendChatMessage(message.getValue());
                } catch (InterruptedException ignored) {}
            }).start();
        }
    }

    private boolean isGameEndMessage(String msg) {
        String lower = msg.toLowerCase();
        return lower.contains("winner") ||
               lower.contains("you died") ||
               lower.contains("game over") ||
               lower.contains("victory") ||
               lower.contains("you won") ||
               lower.contains("game ended") ||
               lower.contains("1st place") ||
               lower.contains("1st killer");
    }
}
