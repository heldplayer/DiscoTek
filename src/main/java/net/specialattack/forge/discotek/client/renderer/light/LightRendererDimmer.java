
package net.specialattack.forge.discotek.client.renderer.light;

import net.minecraft.client.Minecraft;
import net.minecraft.util.AxisAlignedBB;
import net.specialattack.forge.core.client.RenderHelper;
import net.specialattack.forge.discotek.Assets;
import net.specialattack.forge.discotek.client.model.ModelDimmer;
import net.specialattack.forge.discotek.light.ILightRenderHandler;
import net.specialattack.forge.discotek.tileentity.TileEntityLight;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class LightRendererDimmer implements ILightRenderHandler {

    private ModelDimmer modelDimmer = new ModelDimmer();

    @Override
    public void renderSolid(TileEntityLight light, float partialTicks, boolean disableLightmap) {
        Minecraft.getMinecraft().mcProfiler.startSection("model");
        RenderHelper.bindTexture(Assets.DIMMER_TEXTURE);
        this.modelDimmer.renderAll();
        Minecraft.getMinecraft().mcProfiler.endSection();
    }

    @Override
    public void renderLight(TileEntityLight light, float partialTicks) {}

    @Override
    public boolean rendersLight() {
        return false;
    }

    @Override
    public boolean rendersFirst() {
        return false;
    }

    @Override
    public AxisAlignedBB getRenderingAABB(TileEntityLight light, float partialTicks) {
        return null;
    }

}
