package dev.sirius.client.module.modules.hud;

import dev.sirius.client.module.Category;
import dev.sirius.client.module.Module;
import dev.sirius.client.util.ColorUtil;
import dev.sirius.client.util.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

import java.util.Collection;

public class PotionEffects extends Module {

    public PotionEffects() {
        super("Potion Effects", "Displays active potion effects", Category.HUD, 0);
        setHudX(5);
        setHudY(180);
        setHudWidth(130);
        setHudHeight(16);
    }

    @Override
    public void renderHUD() {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.thePlayer == null) return;

        Collection<PotionEffect> effects = mc.thePlayer.getActivePotionEffects();
        if (effects.isEmpty()) return;

        int totalHeight = effects.size() * 16 + 4;
        setHudHeight(totalHeight);

        RenderUtil.drawRoundedRect(getHudX(), getHudY(), getHudWidth(), getHudHeight(), 2, ColorUtil.withAlpha(ColorUtil.DARK_BLACK, 180));

        int yOffset = 0;
        for (PotionEffect effect : effects) {
            Potion potion = Potion.potionTypes[effect.getPotionID()];
            if (potion == null) continue;

            String name = I18n.format(potion.getName());
            int amplifier = effect.getAmplifier();
            if (amplifier > 0) {
                name += " " + toRoman(amplifier + 1);
            }

            int duration = effect.getDuration();
            String durationStr = formatDuration(duration);

            int nameColor = potion.isBadEffect() ? 0xFFFF5555 : ColorUtil.WHITE;
            mc.fontRendererObj.drawStringWithShadow(name, getHudX() + 4, getHudY() + 2 + yOffset, nameColor);
            mc.fontRendererObj.drawStringWithShadow(durationStr, getHudX() + getHudWidth() - mc.fontRendererObj.getStringWidth(durationStr) - 4, getHudY() + 2 + yOffset, ColorUtil.SILVER);

            yOffset += 16;
        }
    }

    private String formatDuration(int ticks) {
        int seconds = ticks / 20;
        int minutes = seconds / 60;
        seconds %= 60;
        return String.format("%d:%02d", minutes, seconds);
    }

    private String toRoman(int num) {
        String[] romanNumerals = {"I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX", "X"};
        if (num >= 1 && num <= 10) return romanNumerals[num - 1];
        return String.valueOf(num);
    }
}
