
package net.specialattack.forge.discotek.client.renderer.tileentity;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.specialattack.forge.core.client.RenderHelper;
import net.specialattack.forge.discotek.block.BlockLight;
import net.specialattack.forge.discotek.light.ILight;
import net.specialattack.forge.discotek.light.ILightRenderHandler;
import net.specialattack.forge.discotek.tileentity.TileEntityLight;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class TileEntityLightRenderer extends TileEntitySpecialRenderer {

    public static boolean renderLight = true;
    public static boolean lightOnly = false;

    public static int[] yawRotations = { 180, 0, 180, 180, 0, 0 };
    public static int[] pitchRotations = { 180, 0, 270, 90, 0, 0 };
    public static int[] rollRotations = { 0, 0, 0, 0, 90, 270 };

    @Override
    public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float partialTicks) {
        if (!(tile instanceof TileEntityLight)) {
            return;
        }
        if (!TileEntityLightRenderer.lightOnly) {
            Minecraft.getMinecraft().mcProfiler.startSection("discotek");
        }

        TileEntityLight tileLight = (TileEntityLight) tile;

        GL11.glPushMatrix();
        GL11.glTranslatef((float) x + 0.5F, (float) y + 0.5F, (float) z + 0.5F);

        Block block = tileLight.getBlockType();

        if (block != null && block instanceof BlockLight) {
            ILight light = ((BlockLight) block).getLight(tileLight.getBlockMetadata());
            ILightRenderHandler renderer = ((BlockLight) block).getLightRenderer(tileLight.getBlockMetadata());
            if (light != null && renderer != null) {
                Minecraft.getMinecraft().mcProfiler.startSection(light.getIdentifier());
                if (TileEntityLightRenderer.lightOnly) {
                    // Debug code, activate me to see render bounding boxes
                    boolean debug = false;
                    if (debug) {
                        //GL11.glDisable(GL11.GL_DEPTH_TEST);
                        int color = tileLight.getColor(partialTicks);
                        float red = ((color & 0xFF0000) >> 16) / 255.0F;
                        float green = ((color & 0xFF00) >> 8) / 255.0F;
                        float blue = (color & 0xFF) / 255.0F;
                        GL11.glColor4f(red, green, blue, 0.5F);
                        AxisAlignedBB aabb = renderer.getRenderingAABB(tileLight, partialTicks);
                        aabb.offset(-0.5D, -0.5D, -0.5D);
                        RenderHelper.drawBox(aabb);

                        double centerX = (aabb.minX + aabb.maxX) / 2.0D;
                        double centerY = (aabb.minY + aabb.maxY) / 2.0D;
                        double centerZ = (aabb.minZ + aabb.maxZ) / 2.0D;

                        GL11.glColor4f(red, green, blue, 1.0F);
                        RenderHelper.drawBox(AxisAlignedBB.getBoundingBox(centerX - 0.5D, centerY - 0.5D, centerZ - 0.5D, centerX + 0.5D, centerY + 0.5D, centerZ + 0.5D));

                        //GL11.glEnable(GL11.GL_DEPTH_TEST);
                    }

                    renderer.renderLight(tileLight, partialTicks);
                }
                else {
                    if (TileEntityLightRenderer.renderLight) {
                        GL11.glDisable(GL11.GL_ALPHA_TEST);
                        GL11.glEnable(GL11.GL_BLEND);
                        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                    }
                    renderer.renderSolid(tileLight, partialTicks, TileEntityLightRenderer.renderLight);
                    if (TileEntityLightRenderer.renderLight) {
                        GL11.glDisable(GL11.GL_BLEND);
                        GL11.glEnable(GL11.GL_ALPHA_TEST);
                    }
                }
                Minecraft.getMinecraft().mcProfiler.endSection();

                GL11.glPopMatrix();

                if (!TileEntityLightRenderer.lightOnly) {
                    Minecraft.getMinecraft().mcProfiler.endSection();
                }
                return;
            }
        }

        GL11.glPopMatrix();

        if (!TileEntityLightRenderer.lightOnly) {
            Minecraft.getMinecraft().mcProfiler.endSection();
        }
    }

}
