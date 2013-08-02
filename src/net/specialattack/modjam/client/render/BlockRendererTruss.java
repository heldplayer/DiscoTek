
package net.specialattack.modjam.client.render;

import java.lang.ProcessBuilder.Redirect;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.world.IBlockAccess;
import net.specialattack.modjam.block.TileEntityLight;
import net.specialattack.modjam.client.model.ModelLightParCan;
import net.specialattack.modjam.client.model.ModelTruss;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

public class BlockRendererTruss implements ISimpleBlockRenderingHandler {

    public final int renderId;
    private final ModelTruss truss = new ModelTruss();

    public BlockRendererTruss(int renderId) {
        this.renderId = renderId;
    }

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer) {
        GL11.glPushMatrix();
        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
        
        
        
        GL11.glPopMatrix();
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
        Tessellator.instance.setColorOpaque(255, 255, 0);
        renderBeam(x, y + 1, z, 0.5D);
        
        return true;
    }

    public void renderBeam(double x, double y, double z, double length){
        
        double size = 0.1D;
        
        Tessellator.instance.addVertex(x, y, z);
        Tessellator.instance.addVertex(x, y, z + length);
        Tessellator.instance.addVertex(x + size, y, z + length);
        Tessellator.instance.addVertex(x + size, y, z);
        
        Tessellator.instance.addVertex(x, y - size, z);
        Tessellator.instance.addVertex(x + size, y - size, z);
        Tessellator.instance.addVertex(x + size, y - size, z + length);
        Tessellator.instance.addVertex(x, y - size, z + length);
        
        Tessellator.instance.addVertex(x, y, z);
        Tessellator.instance.addVertex(x, y - size, z);
        Tessellator.instance.addVertex(x, y - size, z + length);
        Tessellator.instance.addVertex(x, y, z + length);
        
        Tessellator.instance.addVertex(x + size, y, z);
        Tessellator.instance.addVertex(x + size, y, z + length);
        Tessellator.instance.addVertex(x + size, y - size, z + length);
        Tessellator.instance.addVertex(x + size, y - size, z);
        
        Tessellator.instance.addVertex(x, y, z + length);
        Tessellator.instance.addVertex(x, y - size, z + length);
        Tessellator.instance.addVertex(x + size, y - size, z + length);
        Tessellator.instance.addVertex(x + size, y, z + length);
        
        Tessellator.instance.addVertex(x, y, z);
        Tessellator.instance.addVertex(x + size, y, z);
        Tessellator.instance.addVertex(x + size, y - size, z);
        Tessellator.instance.addVertex(x, y - size, z);

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
