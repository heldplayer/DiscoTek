
package net.specialattack.modjam.client.render.tileentity;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.specialattack.modjam.Assets;
import net.specialattack.modjam.block.TileEntityLight;
import net.specialattack.modjam.client.model.ModelLightParCan;
import net.specialattack.modjam.client.model.ModelLightYoke;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class TileEntityLightRenderer extends TileEntitySpecialRenderer {

    private ModelLightParCan modelLightParCan = new ModelLightParCan();
    private ModelLightYoke modelLightYoke = new ModelLightYoke();

    @Override
    public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float partialTicks) {
        if (!(tile instanceof TileEntityLight)) {
            return;
        }

        TileEntityLight light = (TileEntityLight) tile;

        GL11.glPushMatrix();
        this.func_110628_a(Assets.LIGHT_YOKE_TEXTURE);
        GL11.glTranslatef((float) x, (float) y, (float) z);
        this.modelLightYoke.renderAll();
        this.modelLightParCan.renderAll();

        GL11.glPopMatrix();
    }

}
