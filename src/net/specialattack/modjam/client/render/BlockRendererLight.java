
package net.specialattack.modjam.client.render;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.world.IBlockAccess;
import net.specialattack.modjam.block.TileEntityLight;
import net.specialattack.modjam.client.render.tileentity.TileEntityLightRenderer;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

public class BlockRendererLight implements ISimpleBlockRenderingHandler {

    public final int renderId;
    private final TileEntityLight renderTile;

    public BlockRendererLight(int renderId) {
        this.renderId = renderId;
        this.renderTile = new TileEntityLight();
    }

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer) {
        GL11.glPushMatrix();
        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
        TileEntityLightRenderer.disableLight = false;
        TileEntityRenderer.instance.renderTileEntityAt(this.renderTile, 0.0D, 0.0D, 0.0D, 0.0F);
        TileEntityLightRenderer.disableLight = true;
        GL11.glPopMatrix();
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
        return false;
    }

    @Override
    public boolean shouldRender3DInInventory() {
        return true;
    }

    @Override
    public int getRenderId() {
        return this.renderId;
    }

}
