package dev.sirius.client.module.modules.utility;

import dev.sirius.client.module.Category;
import dev.sirius.client.module.Module;
import dev.sirius.client.module.setting.Setting;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ChatFilter extends Module {

    private final Setting<String> keywords;

    public ChatFilter() {
        super("Chat Filter", "Filter chat messages by keyword", Category.UTILITY, 0);
        keywords = addSetting(new Setting<>("Keywords", "spam,ad,buy"));
    }

    @SubscribeEvent
    public void onChatReceived(ClientChatReceivedEvent event) {
        if (event.type == 2) return; // action bar

        String message = event.message.getUnformattedText().toLowerCase();
        String[] filterWords = keywords.getValue().split(",");

        for (String word : filterWords) {
            String trimmed = word.trim().toLowerCase();
            if (!trimmed.isEmpty() && message.contains(trimmed)) {
                event.setCanceled(true);
                return;
            }
        }
    }
}
