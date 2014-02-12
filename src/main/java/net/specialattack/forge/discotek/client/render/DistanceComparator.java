
package net.specialattack.forge.discotek.client.render;

import java.util.Comparator;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.util.AxisAlignedBB;
import net.specialattack.forge.discotek.lights.ILightRenderHandler;
import net.specialattack.forge.discotek.tileentity.TileEntityLight;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class DistanceComparator implements Comparator<TileEntityLight> {

    @Override
    public int compare(TileEntityLight arg1, TileEntityLight arg2) {
        EntityClientPlayerMP player = Minecraft.getMinecraft().thePlayer;
        if (player == null) {
            return 0;
        }

        ILightRenderHandler handler1 = arg1.getRenderHandler();
        ILightRenderHandler handler2 = arg2.getRenderHandler();

        if (handler1.rendersLight() && !handler2.rendersLight()) {
            return -1;
        }
        else if (!handler1.rendersLight() && handler2.rendersLight()) {
            return 1;
        }

        if (handler1.rendersFirst() && !handler2.rendersFirst()) {
            return -1;
        }
        else if (!handler1.rendersFirst() && handler2.rendersFirst()) {
            return 1;
        }

        AxisAlignedBB aabb1 = handler1.getRenderingAABB(arg1, 1.0F);
        AxisAlignedBB aabb2 = handler2.getRenderingAABB(arg2, 1.0F);

        double offsetX1 = (aabb1.maxX - aabb1.minX) / 2.0D;
        double offsetY1 = (aabb1.maxY - aabb1.minY) / 2.0D;
        double offsetZ1 = (aabb1.maxZ - aabb1.minZ) / 2.0D;
        double offsetX2 = (aabb2.maxX - aabb2.minX) / 2.0D;
        double offsetY2 = (aabb2.maxY - aabb2.minY) / 2.0D;
        double offsetZ2 = (aabb2.maxZ - aabb2.minZ) / 2.0D;

        double distX1 = arg1.xCoord - player.posX;
        double distY1 = arg1.yCoord - player.posY;
        double distZ1 = arg1.zCoord - player.posZ;
        double distX2 = arg2.xCoord - player.posX;
        double distY2 = arg2.yCoord - player.posY;
        double distZ2 = arg2.zCoord - player.posZ;

        double distOffX1 = distX1 + offsetX1;
        double distOffY1 = distY1 + offsetY1;
        double distOffZ1 = distZ1 + offsetZ1;
        double distOffX2 = distX2 + offsetX2;
        double distOffY2 = distY2 + offsetY2;
        double distOffZ2 = distZ2 + offsetZ2;

        //double dist1 = distX1 * distX1 + distY1 * distY1 + distZ1 * distZ1;
        //double dist2 = distX2 * distX2 + distY2 * distY2 + distZ2 * distZ2;

        double distOff1 = distOffX1 * distOffX1 + distOffY1 * distOffY1 + distOffZ1 * distOffZ1;
        double distOff2 = distOffX2 * distOffX2 + distOffY2 * distOffY2 + distOffZ2 * distOffZ2;

        if (distOff1 < distOff2) {
            return 1;
        }
        else if (distOff1 > distOff2) {
            return -1;
        }
        else {
            return 0;
        }
    }

}
