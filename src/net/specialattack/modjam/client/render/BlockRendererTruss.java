
package net.specialattack.modjam.client.render;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.specialattack.modjam.client.model.ModelTruss;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

public class BlockRendererTruss implements ISimpleBlockRenderingHandler {

    public final int renderId;
    private final ModelTruss truss = new ModelTruss();
    private float minu;
    private float minv;
    private float maxu;
    private float maxv;

    public BlockRendererTruss(int renderId) {
        this.renderId = renderId;
    }

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer) {}

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
        int brightness = block.getMixedBrightnessForBlock(world, x, y, z);
        Tessellator.instance.setBrightness(brightness);
        Tessellator.instance.setColorOpaque(100, 100, 100);
        Icon icon = block.getIcon(0, 0);

        minu = icon.getMinU();
        minv = icon.getMinV();
        maxu = icon.getMaxU();
        maxv = icon.getMaxV();

        renderCenter(x, y, z);

        return true;
    }

    public void renderCenter(int x, int y, int z) {
        renderBeam(x, y + 1, z, 1D);
        renderBeam(x + 0.8D, y + 1, z, 1D);
        renderBeam(x, y + 0.2D, z, 1D);
        renderBeam(x + 0.8D, y + 0.2D, z, 1D);
    }

    public void renderBeam(double x, double y, double z, double length) {

        double size = 0.2D;

        Tessellator.instance.addVertexWithUV(x, y, z, minu, minv);
        Tessellator.instance.addVertexWithUV(x, y, z + length, maxu, minv);
        Tessellator.instance.addVertexWithUV(x + size, y, z + length, minu, maxv);
        Tessellator.instance.addVertexWithUV(x + size, y, z, maxu, maxv);

        Tessellator.instance.addVertexWithUV(x, y - size, z, minu, minv);
        Tessellator.instance.addVertexWithUV(x + size, y - size, z, maxu, minv);
        Tessellator.instance.addVertexWithUV(x + size, y - size, z + length, minu, maxv);
        Tessellator.instance.addVertexWithUV(x, y - size, z + length, maxu, maxv);

        Tessellator.instance.addVertexWithUV(x, y, z, minu, minv);
        Tessellator.instance.addVertexWithUV(x, y - size, z, maxu, minv);
        Tessellator.instance.addVertexWithUV(x, y - size, z + length, minu, maxv);
        Tessellator.instance.addVertexWithUV(x, y, z + length, maxu, maxv);

        Tessellator.instance.addVertexWithUV(x + size, y, z, minu, minv);
        Tessellator.instance.addVertexWithUV(x + size, y, z + length, maxu, minv);
        Tessellator.instance.addVertexWithUV(x + size, y - size, z + length, minu, maxv);
        Tessellator.instance.addVertexWithUV(x + size, y - size, z, maxu, maxv);

        Tessellator.instance.addVertexWithUV(x, y, z + length, minu, minv);
        Tessellator.instance.addVertexWithUV(x, y - size, z + length, maxu, minv);
        Tessellator.instance.addVertexWithUV(x + size, y - size, z + length, minu, maxv);
        Tessellator.instance.addVertexWithUV(x + size, y, z + length, maxu, maxv);

        Tessellator.instance.addVertexWithUV(x, y, z, minu, minv);
        Tessellator.instance.addVertexWithUV(x + size, y, z, maxu, minv);
        Tessellator.instance.addVertexWithUV(x + size, y - size, z, minu, maxv);
        Tessellator.instance.addVertexWithUV(x, y - size, z, maxu, maxv);

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
