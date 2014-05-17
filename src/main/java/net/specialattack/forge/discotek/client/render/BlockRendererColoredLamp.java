
package net.specialattack.forge.discotek.client.render;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.specialattack.forge.core.client.RenderHelper;
import net.specialattack.forge.discotek.tileentity.TileEntityColoredLamp;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

public class BlockRendererColoredLamp implements ISimpleBlockRenderingHandler {

    public final int renderId;

    public BlockRendererColoredLamp(int renderId) {
        this.renderId = renderId;
    }

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer) {
        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);

        Tessellator tessellator = Tessellator.instance;

        tessellator.startDrawingQuads();

        // Inner
        renderer.setRenderBounds(0.005D, 0.005D, 0.005D, 0.995D, 0.995D, 0.995D);

        tessellator.setNormal(0.0F, -1.0F, 0.0F);
        renderer.renderFaceYNeg(block, 0, 0, 0, RenderHelper.getIconSafe(block.getIcon(0, 1), false));

        tessellator.setNormal(0.0F, 1.0F, 0.0F);
        renderer.renderFaceYPos(block, 0, 0, 0, RenderHelper.getIconSafe(block.getIcon(1, 1), false));

        tessellator.setNormal(0.0F, 0.0F, -1.0F);
        renderer.renderFaceZNeg(block, 0, 0, 0, RenderHelper.getIconSafe(block.getIcon(5, 1), false));

        tessellator.setNormal(0.0F, 0.0F, 1.0F);
        renderer.renderFaceZPos(block, 0, 0, 0, RenderHelper.getIconSafe(block.getIcon(4, 1), false));

        tessellator.setNormal(-1.0F, 0.0F, 0.0F);
        renderer.renderFaceXNeg(block, 0, 0, 0, RenderHelper.getIconSafe(block.getIcon(2, 1), false));

        tessellator.setNormal(1.0F, 0.0F, 0.0F);
        renderer.renderFaceXPos(block, 0, 0, 0, RenderHelper.getIconSafe(block.getIcon(3, 1), false));

        // Overlay
        renderer.setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);

        tessellator.setNormal(0.0F, -1.0F, 0.0F);
        renderer.renderFaceYNeg(block, 0, 0, 0, RenderHelper.getIconSafe(block.getIcon(0, 0), false));

        tessellator.setNormal(0.0F, 1.0F, 0.0F);
        renderer.renderFaceYPos(block, 0, 0, 0, RenderHelper.getIconSafe(block.getIcon(1, 0), false));

        tessellator.setNormal(0.0F, 0.0F, -1.0F);
        renderer.renderFaceZNeg(block, 0, 0, 0, RenderHelper.getIconSafe(block.getIcon(5, 0), false));

        tessellator.setNormal(0.0F, 0.0F, 1.0F);
        renderer.renderFaceZPos(block, 0, 0, 0, RenderHelper.getIconSafe(block.getIcon(4, 0), false));

        tessellator.setNormal(-1.0F, 0.0F, 0.0F);
        renderer.renderFaceXNeg(block, 0, 0, 0, RenderHelper.getIconSafe(block.getIcon(2, 0), false));

        tessellator.setNormal(1.0F, 0.0F, 0.0F);
        renderer.renderFaceXPos(block, 0, 0, 0, RenderHelper.getIconSafe(block.getIcon(3, 0), false));

        tessellator.draw();
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
        TileEntityColoredLamp tile = (TileEntityColoredLamp) world.getTileEntity(x, y, z);

        float red = 1.0F;
        float green = 1.0F;
        float blue = 1.0F;
        if (tile != null) {
            int color = tile.color.getValue();
            int iRed = (color >> 16) & 0xFF;
            int iGreen = (color >> 8) & 0xFF;
            int iBlue = color & 0xFF;
            red = (float) iRed / 255.0F;
            green = (float) iGreen / 255.0F;
            blue = (float) iBlue / 255.0F;
            if (!tile.lit.getValue()) {
                red *= 0.5F;
                green *= 0.5F;
                blue *= 0.5F;
            }
        }

        // Inner
        block.setBlockBounds(0.05F, 0.05F, 0.05F, 0.95F, 0.95F, 0.95F);
        boolean rendered = renderStandardBlockWithColorMultiplier(block, x, y, z, red, green, blue, renderer, RenderHelper.getIconSafe(block.getIcon(0, 1), false));

        // Outer
        block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        rendered |= renderStandardBlockWithColorMultiplier(block, x, y, z, 1.0F, 1.0F, 1.0F, renderer, RenderHelper.getIconSafe(block.getIcon(0, 0), false));

        return rendered;
    }

    @Override
    public boolean shouldRender3DInInventory(int modelId) {
        return true;
    }

    @Override
    public int getRenderId() {
        return this.renderId;
    }

    private static boolean renderStandardBlockWithColorMultiplier(Block block, int posX, int posY, int posZ, float red, float green, float blue, RenderBlocks renderer, IIcon icon) {
        renderer.enableAO = false;
        Tessellator tessellator = Tessellator.instance;
        boolean rendered = false;

        int brightness = block.getMixedBrightnessForBlock(renderer.blockAccess, posX, posY, posZ);

        if (renderer.renderAllFaces || block.shouldSideBeRendered(renderer.blockAccess, posX, posY - 1, posZ, 0)) {
            tessellator.setBrightness(renderer.renderMinY > 0.0D ? brightness : block.getMixedBrightnessForBlock(renderer.blockAccess, posX, posY - 1, posZ));
            tessellator.setColorOpaque_F(0.5F * red, 0.5F * green, 0.5F * blue);
            renderer.renderFaceYNeg(block, (double) posX, (double) posY, (double) posZ, icon);
            rendered = true;
        }

        if (renderer.renderAllFaces || block.shouldSideBeRendered(renderer.blockAccess, posX, posY + 1, posZ, 1)) {
            tessellator.setBrightness(renderer.renderMaxY < 1.0D ? brightness : block.getMixedBrightnessForBlock(renderer.blockAccess, posX, posY + 1, posZ));
            tessellator.setColorOpaque_F(red, green, blue);
            renderer.renderFaceYPos(block, (double) posX, (double) posY, (double) posZ, icon);
            rendered = true;
        }

        if (renderer.renderAllFaces || block.shouldSideBeRendered(renderer.blockAccess, posX, posY, posZ - 1, 2)) {
            tessellator.setBrightness(renderer.renderMinZ > 0.0D ? brightness : block.getMixedBrightnessForBlock(renderer.blockAccess, posX, posY, posZ - 1));
            tessellator.setColorOpaque_F(0.8F * red, 0.8F * green, 0.8F * blue);
            renderer.renderFaceZNeg(block, (double) posX, (double) posY, (double) posZ, icon);
            rendered = true;
        }

        if (renderer.renderAllFaces || block.shouldSideBeRendered(renderer.blockAccess, posX, posY, posZ + 1, 3)) {
            tessellator.setBrightness(renderer.renderMaxZ < 1.0D ? brightness : block.getMixedBrightnessForBlock(renderer.blockAccess, posX, posY, posZ + 1));
            tessellator.setColorOpaque_F(0.8F * red, 0.8F * green, 0.8F * blue);
            renderer.renderFaceZPos(block, (double) posX, (double) posY, (double) posZ, icon);
            rendered = true;
        }

        if (renderer.renderAllFaces || block.shouldSideBeRendered(renderer.blockAccess, posX - 1, posY, posZ, 4)) {
            tessellator.setBrightness(renderer.renderMinX > 0.0D ? brightness : block.getMixedBrightnessForBlock(renderer.blockAccess, posX - 1, posY, posZ));
            tessellator.setColorOpaque_F(0.6F * red, 0.6F * green, 0.6F * blue);
            renderer.renderFaceXNeg(block, (double) posX, (double) posY, (double) posZ, icon);
            rendered = true;
        }

        if (renderer.renderAllFaces || block.shouldSideBeRendered(renderer.blockAccess, posX + 1, posY, posZ, 5)) {
            tessellator.setBrightness(renderer.renderMaxX < 1.0D ? brightness : block.getMixedBrightnessForBlock(renderer.blockAccess, posX + 1, posY, posZ));
            tessellator.setColorOpaque_F(0.6F * red, 0.6F * green, 0.6F * blue);
            renderer.renderFaceXPos(block, (double) posX, (double) posY, (double) posZ, icon);
            rendered = true;
        }

        return rendered;
    }

}
