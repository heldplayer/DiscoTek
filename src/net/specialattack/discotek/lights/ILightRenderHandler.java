
package net.specialattack.discotek.lights;

import net.specialattack.discotek.tileentity.TileEntityLight;

public interface ILightRenderHandler {

    void renderSolid(TileEntityLight light, float partialTicks, boolean disableLightmap);

    void renderLight(TileEntityLight light, float partialTicks);

}
