
package net.specialattack.discotek.lights;

import net.minecraft.util.AxisAlignedBB;
import net.specialattack.discotek.tileentity.TileEntityLight;

public interface ILightRenderHandler {

    void renderSolid(TileEntityLight light, float partialTicks, boolean disableLightmap);

    void renderLight(TileEntityLight light, float partialTicks);

    boolean rendersLight();

    boolean rendersFirst();

    AxisAlignedBB getRenderingAABB(TileEntityLight light, float partialTicks);

}
