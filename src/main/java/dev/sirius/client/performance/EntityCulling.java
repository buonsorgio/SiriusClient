package dev.sirius.client.performance;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;

public class EntityCulling {

    private static final Frustum frustum = new Frustum();

    public static boolean isEntityInFrustum(Entity entity) {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.getRenderViewEntity() == null) return true;

        Entity viewEntity = mc.getRenderViewEntity();
        double x = viewEntity.lastTickPosX + (viewEntity.posX - viewEntity.lastTickPosX) * getPartialTicks();
        double y = viewEntity.lastTickPosY + (viewEntity.posY - viewEntity.lastTickPosY) * getPartialTicks();
        double z = viewEntity.lastTickPosZ + (viewEntity.posZ - viewEntity.lastTickPosZ) * getPartialTicks();

        frustum.setPosition(x, y, z);

        AxisAlignedBB bb = entity.getEntityBoundingBox();
        if (bb == null) return true;

        return frustum.isBoundingBoxInFrustum(bb);
    }

    private static float getPartialTicks() {
        return Minecraft.getMinecraft().timer.renderPartialTicks;
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
