
package net.specialattack.discotek.client.render;

import java.util.Comparator;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.specialattack.discotek.tileentity.TileEntityLight;
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

        if (arg1.getBlockMetadata() == 4 && arg2.getBlockMetadata() != 4) {
            return -1;
        }
        else if (arg1.getBlockMetadata() != 4 && arg2.getBlockMetadata() == 4) {
            return 1;
        }

        double distX1 = arg1.xCoord - player.posX;
        double distY1 = arg1.yCoord - player.posY;
        double distZ1 = arg1.zCoord - player.posZ;
        double distX2 = arg2.xCoord - player.posX;
        double distY2 = arg2.yCoord - player.posY;
        double distZ2 = arg2.zCoord - player.posZ;

        double dist1 = distX1 * distX1 + distY1 * distY1 + distZ1 * distZ1;
        double dist2 = distX2 * distX2 + distY2 * distY2 + distZ2 * distZ2;

        if (dist1 < dist2) {
            return 1;
        }
        else if (dist2 < dist1) {
            return -1;
        }
        else {
            return 0;
        }
    }

}
