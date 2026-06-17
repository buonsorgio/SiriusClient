package dev.sirius.client.module.modules.visual;

import dev.sirius.client.module.Category;
import dev.sirius.client.module.Module;
import dev.sirius.client.module.setting.Setting;
import dev.sirius.client.util.ColorUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class HitColor extends Module {

    private final Setting<String> hitColor;

    public HitColor() {
        super("Hit Color", "Changes hurt color flash on entities", Category.VISUAL, 0);
        hitColor = addSetting(new Setting<>("Hit Color", "#8B0000"));
    }

    @SubscribeEvent
    public void onRenderLiving(RenderLivingEvent.Pre<EntityLivingBase> event) {
        EntityLivingBase entity = event.entity;
        if (entity.hurtTime > 0 || entity.deathTime > 0) {
            int color = ColorUtil.fromHex(hitColor.getValue());
            float r = ColorUtil.getRed(color) / 255f;
            float g = ColorUtil.getGreen(color) / 255f;
            float b = ColorUtil.getBlue(color) / 255f;
            GlStateManager.color(r, g, b, 0.3f);
        }
    }

    public int getHitColorValue() {
        return ColorUtil.fromHex(hitColor.getValue());
    }
}
