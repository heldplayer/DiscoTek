
package net.specialattack.modjam.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockController extends Block {

    public BlockController(int blockId) {
        super(blockId, Material.piston);
    }

    @Override
    public TileEntity createTileEntity(World world, int metadata) {
        //return new TileEntityLight();
        return null;
    }

    @Override
    public boolean hasTileEntity(int metadata) {
        return false;
    }

}
