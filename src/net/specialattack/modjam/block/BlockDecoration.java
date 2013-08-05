
package net.specialattack.modjam.block;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockDecoration extends Block {

    @SideOnly(Side.CLIENT)
    private Icon[] icons;

    public BlockDecoration(int blockId) {
        super(blockId, Material.iron);
    }

    @Override
    public int damageDropped(int meta) {
        return meta;
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack stack) {
        super.onBlockPlacedBy(world, x, y, z, entity, stack);
        world.setBlockMetadataWithNotify(x, y, z, stack.getItemDamage(), 0);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Icon getIcon(int side, int meta) {
        return this.icons[meta % this.icons.length];
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister register) {
        this.icons = new Icon[5];

        for (int i = 0; i < this.icons.length; i++) {
            this.icons[i] = register.registerIcon("modjam:decoration" + i);
        }
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(int itemId, CreativeTabs tab, List list) {
        for (int i = 0; i < this.icons.length; i++) {
            list.add(new ItemStack(itemId, 1, i));
        }
    }

}
