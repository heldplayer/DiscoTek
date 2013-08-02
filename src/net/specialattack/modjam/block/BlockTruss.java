
package net.specialattack.modjam.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.specialattack.modjam.client.render.BlockRendererTruss;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class BlockTruss extends Block {

    private final int renderId;

    public BlockTruss(int blockId) {
        super(blockId, Material.iron);
        this.renderId = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(this.renderId, new BlockRendererTruss(this.renderId));
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public int getRenderType() {
        return this.renderId;
    }

}
