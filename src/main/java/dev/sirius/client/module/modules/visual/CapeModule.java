package dev.sirius.client.module.modules.visual;

import dev.sirius.client.module.Category;
import dev.sirius.client.module.Module;
import dev.sirius.client.module.setting.Setting;

public class CapeModule extends Module {

    private final Setting<Boolean> showCape;

    public CapeModule() {
        super("Cape", "Custom cape renderer", Category.VISUAL, 0);
        showCape = addSetting(new Setting<>("Show Cape", true));
    }

    public boolean shouldShowCape() {
        return isEnabled() && showCape.getValue();
    }
}
