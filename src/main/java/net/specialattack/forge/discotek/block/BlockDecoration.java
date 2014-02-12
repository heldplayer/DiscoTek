
package net.specialattack.forge.discotek.block;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.specialattack.forge.discotek.Assets;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockDecoration extends Block {

    @SideOnly(Side.CLIENT)
    private IIcon[] icons;

    public BlockDecoration() {
        super(Material.iron);
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
    public IIcon getIcon(int side, int meta) {
        return this.icons[meta % this.icons.length];
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister register) {
        this.icons = new IIcon[5];

        for (int i = 0; i < this.icons.length; i++) {
            this.icons[i] = register.registerIcon(Assets.DOMAIN + "decoration" + i);
        }
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item item, CreativeTabs tab, List list) {
        for (int i = 0; i < this.icons.length; i++) {
            list.add(new ItemStack(item, 1, i));
        }
    }

}
