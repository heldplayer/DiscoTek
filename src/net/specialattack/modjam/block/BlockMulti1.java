
package net.specialattack.modjam.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class BlockMulti1 extends Block {

    public BlockMulti1(int blockId) {
        super(blockId, Material.piston);
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

}
