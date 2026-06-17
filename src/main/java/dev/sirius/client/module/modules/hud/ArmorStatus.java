package dev.sirius.client.module.modules.hud;

import dev.sirius.client.module.Category;
import dev.sirius.client.module.Module;
import dev.sirius.client.util.ColorUtil;
import dev.sirius.client.util.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;

public class ArmorStatus extends Module {

    public ArmorStatus() {
        super("Armor Status", "Shows armor durability with icons", Category.HUD, 0);
        setHudX(5);
        setHudY(100);
        setHudWidth(60);
        setHudHeight(72);
    }

    @Override
    public void renderHUD() {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.thePlayer == null) return;

        RenderUtil.drawRoundedRect(getHudX(), getHudY(), getHudWidth(), getHudHeight(), 2, ColorUtil.withAlpha(ColorUtil.DARK_BLACK, 180));

        RenderHelper.enableGUIStandardItemLighting();
        int yOffset = 0;
        for (int i = 3; i >= 0; i--) {
            ItemStack stack = mc.thePlayer.inventory.armorInventory[i];
            if (stack != null) {
                int x = getHudX() + 4;
                int y = getHudY() + 4 + yOffset;

                mc.getRenderItem().renderItemAndEffectIntoGUI(stack, x, y);

                int durability = stack.getMaxDamage() - stack.getItemDamage();
                float percent = (float) durability / stack.getMaxDamage();
                int color = percent > 0.5f ? 0xFF00FF00 : percent > 0.25f ? 0xFFFFAA00 : 0xFFFF0000;

                GlStateManager.disableLighting();
                mc.fontRendererObj.drawStringWithShadow(String.valueOf(durability), x + 20, y + 4, color);
            }
            yOffset += 18;
        }
        RenderHelper.disableStandardItemLighting();
    }
}
