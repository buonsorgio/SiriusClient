package dev.sirius.client.cape;

import dev.sirius.client.SiriusClient;
import dev.sirius.client.module.modules.visual.CapeModule;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

public class LayerSiriusCape implements LayerRenderer<AbstractClientPlayer> {

    private final RenderPlayer playerRenderer;

    public LayerSiriusCape(RenderPlayer playerRenderer) {
        this.playerRenderer = playerRenderer;
    }

    @Override
    public void doRenderLayer(AbstractClientPlayer player, float limbSwing, float limbSwingAmount,
                              float partialTicks, float ageInTicks, float netHeadYaw,
                              float headPitch, float scale) {
        CapeModule capeModule = SiriusClient.instance.getModuleManager().getModule(CapeModule.class);
        if (capeModule == null || !capeModule.shouldShowCape()) return;

        if (player.isInvisible()) return;

        CapeManager capeManager = SiriusClient.instance.getCapeManager();
        ResourceLocation capeTexture = capeManager.getCapeTexture(player.getUniqueID());

        if (capeTexture == null) return;

        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        playerRenderer.bindTexture(capeTexture);
        GlStateManager.pushMatrix();

        GlStateManager.translate(0.0f, 0.0f, 0.125f);

        double capeX = player.prevChasingPosX + (player.chasingPosX - player.prevChasingPosX) * partialTicks
                - (player.prevPosX + (player.posX - player.prevPosX) * partialTicks);
        double capeY = player.prevChasingPosY + (player.chasingPosY - player.prevChasingPosY) * partialTicks
                - (player.prevPosY + (player.posY - player.prevPosY) * partialTicks);
        double capeZ = player.prevChasingPosZ + (player.chasingPosZ - player.prevChasingPosZ) * partialTicks
                - (player.prevPosZ + (player.posZ - player.prevPosZ) * partialTicks);

        float yaw = player.prevRenderYawOffset + (player.renderYawOffset - player.prevRenderYawOffset) * partialTicks;
        double sin = MathHelper.sin(yaw * (float) Math.PI / 180.0f);
        double cos = -MathHelper.cos(yaw * (float) Math.PI / 180.0f);

        float vertAngle = (float) capeY * 10.0f;
        vertAngle = MathHelper.clamp_float(vertAngle, -6.0f, 32.0f);

        float horizAngle = (float) (capeX * sin + capeZ * cos) * 100.0f;
        horizAngle = MathHelper.clamp_float(horizAngle, 0.0f, 150.0f);

        float sideAngle = (float) (capeX * cos - capeZ * sin) * 100.0f;
        sideAngle = MathHelper.clamp_float(sideAngle, -20.0f, 20.0f);

        if (horizAngle < 0.0f) horizAngle = 0.0f;

        float cameraYaw = player.prevCameraYaw + (player.cameraYaw - player.prevCameraYaw) * partialTicks;
        vertAngle += MathHelper.sin((player.prevDistanceWalkedModified + (player.distanceWalkedModified - player.prevDistanceWalkedModified) * partialTicks) * 6.0f) * 32.0f * cameraYaw;

        if (player.isSneaking()) {
            vertAngle += 25.0f;
        }

        GlStateManager.rotate(6.0f + horizAngle / 2.0f + vertAngle, 1.0f, 0.0f, 0.0f);
        GlStateManager.rotate(sideAngle / 2.0f, 0.0f, 0.0f, 1.0f);
        GlStateManager.rotate(-sideAngle / 2.0f, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(180.0f, 0.0f, 1.0f, 0.0f);

        playerRenderer.getMainModel().renderCape(0.0625f);
        GlStateManager.popMatrix();
    }

    @Override
    public boolean shouldCombineTextures() {
        return false;
    }
}
