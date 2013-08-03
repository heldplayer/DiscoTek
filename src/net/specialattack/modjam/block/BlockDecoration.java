
package net.specialattack.modjam.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.util.Icon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockDecoration extends Block {

    @SideOnly(Side.CLIENT)
    private Icon[] icons;

    public BlockDecoration(int blockId) {
        super(blockId, Material.iron);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Icon getIcon(int side, int meta) {
        return this.icons[meta];
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister register) {
        this.icons = new Icon[16];

        for (int i = 0; i < this.icons.length; i++) {
            this.icons[i] = register.registerIcon("modjam:decoration" + i);
        }
    }

}
