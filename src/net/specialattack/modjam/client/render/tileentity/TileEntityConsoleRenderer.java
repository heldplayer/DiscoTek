
package net.specialattack.modjam.client.render.tileentity;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.specialattack.modjam.Assets;
import net.specialattack.modjam.client.model.ModelBasicConsole;
import net.specialattack.modjam.tileentity.TileEntityController;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class TileEntityConsoleRenderer extends TileEntitySpecialRenderer {

    private ModelBasicConsole modelConsoleBasic = new ModelBasicConsole();

    public static boolean disableLight = true;

    @Override
    public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float partialTicks) {
        if (!(tile instanceof TileEntityController)) {
            return;
        }

        TileEntityController controller = (TileEntityController) tile;

        GL11.glPushMatrix();
        GL11.glTranslatef((float) x + 0.5F, (float) y + 0.5F, (float) z + 0.5F);

        switch (controller.getBlockMetadata() & 0xFF) {
        case 0:
            this.render1(controller, x, y, z, partialTicks);
        break;
        case 1:
            this.render2(controller, x, y, z, partialTicks);
        break;
        case 2:
            this.render2(controller, x, y, z, partialTicks);
        break;
        }

        GL11.glPopMatrix();
    }

    public void render1(TileEntityController controller, double x, double y, double z, float partialTicks) {
        this.func_110628_a(Assets.BASIC_CONSOLE_TEXTURE);
        this.modelConsoleBasic.renderAll();
    }
    
    public void render2(TileEntityController light, double x, double y, double z, float partialTicks) {
        this.func_110628_a(Assets.LIGHT_YOKE_TEXTURE);

    }

}
