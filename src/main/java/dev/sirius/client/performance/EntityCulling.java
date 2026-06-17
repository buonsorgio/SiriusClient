package dev.sirius.client.performance;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EntityCulling {

    private static final Frustum frustum = new Frustum();
    private static float partialTicks = 0f;

    @SubscribeEvent
    public void onRenderTick(TickEvent.RenderTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            partialTicks = event.renderTickTime;
        }
    }

    public static boolean isEntityInFrustum(Entity entity) {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.getRenderViewEntity() == null) return true;

        Entity viewEntity = mc.getRenderViewEntity();
        double x = viewEntity.lastTickPosX + (viewEntity.posX - viewEntity.lastTickPosX) * partialTicks;
        double y = viewEntity.lastTickPosY + (viewEntity.posY - viewEntity.lastTickPosY) * partialTicks;
        double z = viewEntity.lastTickPosZ + (viewEntity.posZ - viewEntity.lastTickPosZ) * partialTicks;

        frustum.setPosition(x, y, z);

        AxisAlignedBB bb = entity.getEntityBoundingBox();
        if (bb == null) return true;

        return frustum.isBoundingBoxInFrustum(bb);
    }

    public static boolean shouldRenderEntity(Entity entity) {
        Minecraft mc = Minecraft.getMinecraft();

        // Always render the player
        if (entity == mc.thePlayer) return true;

        // Skip entities too far away
        double distSq = mc.thePlayer.getDistanceSqToEntity(entity);
        if (distSq > 4096) return false; // 64 blocks

        return isEntityInFrustum(entity);
    }
}
