
package net.specialattack.modjam.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.specialattack.modjam.client.render.BlockRendererLight;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class BlockLight extends Block {

    private final int renderId;

    public BlockLight(int blockId) {
        super(blockId, Material.piston);
        this.renderId = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(this.renderId, new BlockRendererLight(this.renderId));
        this.setLightOpacity(100);
        this.setLightValue(0.2f);
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean hasTileEntity(int metadata) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, int metadata) {
        return new TileEntityLight();
    }

    @Override
    public int getRenderType() {
        return this.renderId;
    }

}
