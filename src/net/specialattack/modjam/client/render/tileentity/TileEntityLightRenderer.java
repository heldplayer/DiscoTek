
package net.specialattack.modjam.client.render.tileentity;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.specialattack.modjam.Assets;
import net.specialattack.modjam.client.model.ModelLight1;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class TileEntityLightRenderer extends TileEntitySpecialRenderer {

    private ModelLight1 modelLight1 = new ModelLight1();

    @Override
    public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float partialTicks) {
        GL11.glPushMatrix();
        this.func_110628_a(Assets.LIGHT1_TEXTURE);
        GL11.glTranslatef((float) x, (float) y, (float) z);
        modelLight1.renderAll();
        GL11.glPopMatrix();
    }

}
