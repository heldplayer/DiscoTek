
package net.specialattack.modjam.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockLight extends Block {

    public BlockLight(int blockId) {
        super(blockId, Material.piston);
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
        return -1;
    }

}
