
package net.specialattack.forge.discotek.light;

import net.minecraft.util.AxisAlignedBB;
import net.specialattack.forge.discotek.tileentity.TileEntityLight;

public interface ILightRenderHandler {

    void renderSolid(TileEntityLight light, float partialTicks, boolean disableLightmap);

    void renderLight(TileEntityLight light, float partialTicks);

    boolean rendersLight();

    boolean rendersFirst();

    AxisAlignedBB getRenderingAABB(TileEntityLight light, float partialTicks);

}
