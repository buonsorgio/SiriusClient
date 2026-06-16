package dev.sirius.client.module.modules.visual;

import dev.sirius.client.module.Category;
import dev.sirius.client.module.Module;
import net.minecraft.client.Minecraft;

public class Fullbright extends Module {

    private float previousGamma;

    public Fullbright() {
        super("Fullbright", "Sets gamma to maximum", Category.VISUAL, 0);
    }

    @Override
    protected void onEnable() {
        previousGamma = Minecraft.getMinecraft().gameSettings.gammaSetting;
        Minecraft.getMinecraft().gameSettings.gammaSetting = 100.0f;
    }

    @Override
    protected void onDisable() {
        Minecraft.getMinecraft().gameSettings.gammaSetting = previousGamma;
    }
}
