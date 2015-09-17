package net.specialattack.forge.discotek.client.render;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.IBlockAccess;
import net.specialattack.forge.discotek.block.BlockTruss;
import org.lwjgl.opengl.GL11;

public class BlockRendererTruss implements ISimpleBlockRenderingHandler {

    public final int renderId;

    public BlockRendererTruss(int renderId) {
        this.renderId = renderId;
    }

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer) {
        float off = 0.1875F;

        GL11.glPushMatrix();

        GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);

        renderer.overrideBlockTexture = block.getIcon(0, metadata);

        // Top-bottom connection
        block.setBlockBounds(0.0F, off, 0.0F, 0.1875F, 1.0F - off, 0.1875F);
        this.doRender(block, renderer);

        block.setBlockBounds(0.0F, off, 0.8125F, 0.1875F, 1.0F - off, 1.0F);
        this.doRender(block, renderer);

        block.setBlockBounds(0.8125F, off, 0.8125F, 1.0F, 1.0F - off, 1.0F);
        this.doRender(block, renderer);

        block.setBlockBounds(0.8125F, off, 0.0F, 1.0F, 1.0F - off, 0.1875F);
        this.doRender(block, renderer);

        // Bottom bars
        block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.1875F, 0.1875F);
        this.doRender(block, renderer);

        block.setBlockBounds(0.0F, 0.0F, 0.8125F, 1.0F, 0.1875F, 1.0F);
        this.doRender(block, renderer);

        block.setBlockBounds(0.0F, 0.0F, off, 0.1875F, 0.1875F, 1.0F - off);
        this.doRender(block, renderer);

        block.setBlockBounds(0.8125F, 0.0F, off, 1.0F, 0.1875F, 1.0F - off);
        this.doRender(block, renderer);

        // Top bars
        block.setBlockBounds(0.0F, 0.8125F, 0.0F, 1.0F, 1.0F, 0.1875F);
        this.doRender(block, renderer);

        block.setBlockBounds(0.0F, 0.8125F, 0.8125F, 1.0F, 1.0F, 1.0F);
        this.doRender(block, renderer);

        block.setBlockBounds(0.0F, 0.8125F, off, 0.1875F, 1.0F, 1.0F - off);
        this.doRender(block, renderer);

        block.setBlockBounds(0.8125F, 0.8125F, off, 1.0F, 1.0F, 1.0F - off);
        this.doRender(block, renderer);

        renderer.overrideBlockTexture = null;

        GL11.glPopMatrix();

        // Reset block
        block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
        // TODO: fix Z-fighting
        int meta = world.getBlockMetadata(x, y, z);
        boolean connectTop = this.canConnect(world, x, y + 1, z, meta, true);
        boolean connectBottom = this.canConnect(world, x, y - 1, z, meta, true);
        boolean connectNorth = this.canConnect(world, x, y, z - 1, meta, false);
        boolean connectSouth = this.canConnect(world, x, y, z + 1, meta, false);
        boolean connectWest = this.canConnect(world, x - 1, y, z, meta, false); // NOTE: According to IntelliJ, connectWest is always true AND false. Need testing on quantum computer.
        boolean connectEast = this.canConnect(world, x + 1, y, z, meta, false);

        if (((connectNorth || connectSouth) && (connectWest || connectEast)) || (!connectNorth && !connectSouth && !connectWest && !connectEast && !connectBottom && !connectTop)) {
            connectTop = true;
            connectBottom = true;
            connectNorth = true;
            connectSouth = true;
            connectWest = true;
            connectEast = true;
        }

        if (connectBottom || connectTop) {
            // Top-bottom connection
            float off = connectNorth || connectEast || connectSouth || connectWest ? 0.1875F : 0.0F;

            block.setBlockBounds(0.0F, off, 0.0F, 0.1875F, 1.0F - off, 0.1875F);
            this.doRender(block, x, y, z, renderer);

            block.setBlockBounds(0.0F, off, 0.8125F, 0.1875F, 1.0F - off, 1.0F);
            this.doRender(block, x, y, z, renderer);

            block.setBlockBounds(0.8125F, off, 0.8125F, 1.0F, 1.0F - off, 1.0F);
            this.doRender(block, x, y, z, renderer);

            block.setBlockBounds(0.8125F, off, 0.0F, 1.0F, 1.0F - off, 0.1875F);
            this.doRender(block, x, y, z, renderer);
        } else {
            float off = connectNorth || connectEast || connectSouth || connectWest ? 0.1875F : 0.0F;

            boolean flag = connectNorth && connectSouth && connectWest && connectEast;

            if ((!connectNorth && !connectWest) || flag) {
                block.setBlockBounds(0.0F, off, 0.0F, 0.1875F, 1.0F - off, 0.1875F);
                this.doRender(block, x, y, z, renderer);
            }
            if ((!connectSouth && !connectWest) || flag) {
                block.setBlockBounds(0.0F, off, 0.8125F, 0.1875F, 1.0F - off, 1.0F);
                this.doRender(block, x, y, z, renderer);
            }
            if ((!connectSouth && !connectEast) || flag) {
                block.setBlockBounds(0.8125F, off, 0.8125F, 1.0F, 1.0F - off, 1.0F);
                this.doRender(block, x, y, z, renderer);
            }
            if ((!connectNorth && !connectEast) || flag) {
                block.setBlockBounds(0.8125F, off, 0.0F, 1.0F, 1.0F - off, 0.1875F);
                this.doRender(block, x, y, z, renderer);
            }
        }

        if (connectWest || connectEast) {
            // Bottom bars
            block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.1875F, 0.1875F);
            this.doRender(block, x, y, z, renderer);

            block.setBlockBounds(0.0F, 0.0F, 0.8125F, 1.0F, 0.1875F, 1.0F);
            this.doRender(block, x, y, z, renderer);

            // Top bars
            block.setBlockBounds(0.0F, 0.8125F, 0.0F, 1.0F, 1.0F, 0.1875F);
            this.doRender(block, x, y, z, renderer);

            block.setBlockBounds(0.0F, 0.8125F, 0.8125F, 1.0F, 1.0F, 1.0F);
            this.doRender(block, x, y, z, renderer);
        } else {
            // Bottom bars
            if (connectTop && !connectBottom) {
                block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.1875F, 0.1875F);
                this.doRender(block, x, y, z, renderer);

                block.setBlockBounds(0.0F, 0.0F, 0.8125F, 1.0F, 0.1875F, 1.0F);
                this.doRender(block, x, y, z, renderer);
            } else if (connectNorth && !connectSouth) {
                block.setBlockBounds(0.0F, 0.0F, 0.8125F, 1.0F, 0.1875F, 1.0F);
                this.doRender(block, x, y, z, renderer);
            } else if (!connectNorth && connectSouth) {
                block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.1875F, 0.1875F);
                this.doRender(block, x, y, z, renderer);
            }

            // Top bars
            if (!connectTop && connectBottom) {
                block.setBlockBounds(0.0F, 0.8125F, 0.0F, 1.0F, 1.0F, 0.1875F);
                this.doRender(block, x, y, z, renderer);

                block.setBlockBounds(0.0F, 0.8125F, 0.8125F, 1.0F, 1.0F, 1.0F);
                this.doRender(block, x, y, z, renderer);
            } else if (connectNorth && !connectSouth) {
                block.setBlockBounds(0.0F, 0.8125F, 0.8125F, 1.0F, 1.0F, 1.0F);
                this.doRender(block, x, y, z, renderer);
            } else if (!connectNorth && connectSouth) {
                block.setBlockBounds(0.0F, 0.8125F, 0.0F, 1.0F, 1.0F, 0.1875F);
                this.doRender(block, x, y, z, renderer);
            }
        }

        if (connectNorth || connectSouth) {
            float off = connectWest || connectEast ? 0.1875F : 0.0F;
            // Bottom bars
            block.setBlockBounds(0.0F, 0.0F, off, 0.1875F, 0.1875F, 1.0F - off);
            this.doRender(block, x, y, z, renderer);

            block.setBlockBounds(0.8125F, 0.0F, off, 1.0F, 0.1875F, 1.0F - off);
            this.doRender(block, x, y, z, renderer);

            // Top bars
            block.setBlockBounds(0.0F, 0.8125F, off, 0.1875F, 1.0F, 1.0F - off);
            this.doRender(block, x, y, z, renderer);

            block.setBlockBounds(0.8125F, 0.8125F, off, 1.0F, 1.0F, 1.0F - off);
            this.doRender(block, x, y, z, renderer);
        } else {
            float off = connectWest || connectEast ? 0.1875F : 0.0F;

            // Bottom bars
            if (connectTop && !connectBottom) {
                block.setBlockBounds(0.0F, 0.0F, off, 0.1875F, 0.1875F, 1.0F - off);
                this.doRender(block, x, y, z, renderer);

                block.setBlockBounds(0.8125F, 0.0F, off, 1.0F, 0.1875F, 1.0F - off);
                this.doRender(block, x, y, z, renderer);
            } else if (connectWest && !connectEast) {
                block.setBlockBounds(0.8125F, 0.0F, off, 1.0F, 0.1875F, 1.0F - off);
                this.doRender(block, x, y, z, renderer);
            } else if (!connectWest && connectEast) {
                block.setBlockBounds(0.0F, 0.0F, off, 0.1875F, 0.1875F, 1.0F - off);
                this.doRender(block, x, y, z, renderer);
            }

            // Top bars
            if (!connectTop && connectBottom) {
                block.setBlockBounds(0.0F, 0.8125F, off, 0.1875F, 1.0F, 1.0F - off);
                this.doRender(block, x, y, z, renderer);

                block.setBlockBounds(0.8125F, 0.8125F, off, 1.0F, 1.0F, 1.0F - off);
                this.doRender(block, x, y, z, renderer);
            } else if (connectWest && !connectEast) {
                block.setBlockBounds(0.8125F, 0.8125F, off, 1.0F, 1.0F, 1.0F - off);
                this.doRender(block, x, y, z, renderer);
            } else if (!connectWest && connectEast) {
                block.setBlockBounds(0.0F, 0.8125F, off, 0.1875F, 1.0F, 1.0F - off);
                this.doRender(block, x, y, z, renderer);
            }
        }

        // Reset block
        block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);

        return true;
    }

    public boolean canConnect(IBlockAccess world, int x, int y, int z, int meta, boolean strict) {
        Block block = world.getBlock(x, y, z);

        if (block == null || block.isAir(world, x, y, z)) {
            return false;
        }

        if (block instanceof BlockTruss && world.getBlockMetadata(x, y, z) == meta) {
            return true;
        } else if (strict) {
            return false;
        }

        return !(!block.renderAsNormalBlock() || !block.isOpaqueCube());

    }

    private void doRender(Block block, int x, int y, int z, RenderBlocks renderer) {
        renderer.setRenderBoundsFromBlock(block);
        renderer.renderStandardBlockWithColorMultiplier(block, x, y, z, 1.0F, 1.0F, 1.0F);
    }

    @Override
    public boolean shouldRender3DInInventory(int modelId) {
        return true;
    }

    @Override
    public int getRenderId() {
        return this.renderId;
    }

    private void doRender(Block block, RenderBlocks renderer) {
        Tessellator tess = Tessellator.instance;

        renderer.setRenderBoundsFromBlock(block);
        tess.startDrawingQuads();
        tess.setNormal(0.0F, -1.0F, 0.0F);
        renderer.renderFaceYNeg(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 0, 0));
        tess.draw();
        tess.startDrawingQuads();
        tess.setNormal(0.0F, 1.0F, 0.0F);
        renderer.renderFaceYPos(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 1, 0));
        tess.draw();
        tess.startDrawingQuads();
        tess.setNormal(0.0F, 0.0F, -1.0F);
        renderer.renderFaceZNeg(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 2, 0));
        tess.draw();
        tess.startDrawingQuads();
        tess.setNormal(0.0F, 0.0F, 1.0F);
        renderer.renderFaceZPos(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 3, 0));
        tess.draw();
        tess.startDrawingQuads();
        tess.setNormal(-1.0F, 0.0F, 0.0F);
        renderer.renderFaceXNeg(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 4, 0));
        tess.draw();
        tess.startDrawingQuads();
        tess.setNormal(1.0F, 0.0F, 0.0F);
        renderer.renderFaceXPos(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 5, 0));
        tess.draw();
    }
}
