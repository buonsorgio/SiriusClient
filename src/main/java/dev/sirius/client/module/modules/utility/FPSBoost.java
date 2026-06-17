package dev.sirius.client.module.modules.utility;

import dev.sirius.client.module.Category;
import dev.sirius.client.module.Module;
import dev.sirius.client.module.setting.Setting;
import net.minecraft.client.Minecraft;

public class FPSBoost extends Module {

    private final Setting<Boolean> reduceParticles;
    private final Setting<Boolean> disableBlockBreakParticles;
    private final Setting<Boolean> optimizeRenderDistance;
    private final Setting<Integer> maxParticles;

    private int previousParticleSetting;
    private int previousRenderDistance;

    public FPSBoost() {
        super("FPS Boost", "Performance optimizations", Category.UTILITY, 0);
        reduceParticles = addSetting(new Setting<>("Reduce Particles", true));
        disableBlockBreakParticles = addSetting(new Setting<>("No Block Particles", false));
        optimizeRenderDistance = addSetting(new Setting<>("Optimize Render Dist", false));
        maxParticles = addSetting(new Setting<>("Max Particles", 1, 0, 2)); // 0=All, 1=Decreased, 2=Minimal
    }

    @Override
    protected void onEnable() {
        Minecraft mc = Minecraft.getMinecraft();
        previousParticleSetting = mc.gameSettings.particleSetting;
        previousRenderDistance = mc.gameSettings.renderDistanceChunks;

        if (reduceParticles.getValue()) {
            mc.gameSettings.particleSetting = maxParticles.getValue();
        }

        if (optimizeRenderDistance.getValue() && mc.gameSettings.renderDistanceChunks > 8) {
            mc.gameSettings.renderDistanceChunks = 8;
        }
    }

    @Override
    protected void onDisable() {
        Minecraft mc = Minecraft.getMinecraft();
        mc.gameSettings.particleSetting = previousParticleSetting;
        if (optimizeRenderDistance.getValue()) {
            mc.gameSettings.renderDistanceChunks = previousRenderDistance;
        }
    }

    public boolean shouldDisableBlockBreakParticles() {
        return isEnabled() && disableBlockBreakParticles.getValue();
    }
}
