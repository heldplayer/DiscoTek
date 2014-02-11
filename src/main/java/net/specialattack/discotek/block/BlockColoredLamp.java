
package net.specialattack.discotek.block;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRedstoneLight;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.specialattack.discotek.Assets;
import net.specialattack.discotek.Objects;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockColoredLamp extends BlockRedstoneLight {

    @SideOnly(Side.CLIENT)
    private IIcon[] icons;

    private final boolean powered;

    public BlockColoredLamp(boolean powered) {
        super(powered);
        this.powered = powered;
    }

    @Override
    public int damageDropped(int meta) {
        return meta;
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack stack) {
        super.onBlockPlacedBy(world, x, y, z, entity, stack);
        world.setBlockMetadataWithNotify(x, y, z, stack.getItemDamage(), 3);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
        return this.icons[meta % this.icons.length];
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister register) {
        this.icons = new IIcon[16];

        for (int i = 0; i < this.icons.length; i++) {
            this.icons[i] = register.registerIcon(Assets.DOMAIN + "lamp-" + (this.powered ? "on" : "off") + i);
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

    @Override
    public void onBlockAdded(World world, int x, int y, int z) {
        if (!world.isRemote) {
            world.scheduleBlockUpdate(x, y, z, this, 1);
        }
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block neighbor) {
        if (!world.isRemote) {
            world.scheduleBlockUpdate(x, y, z, this, 1);
        }
    }

    @Override
    public void updateTick(World world, int x, int y, int z, Random rand) {
        if (!world.isRemote) {
            if (this.powered && !world.isBlockIndirectlyGettingPowered(x, y, z)) {
                world.setBlock(x, y, z, Objects.blockColoredLampOff, world.getBlockMetadata(x, y, z), 2);
            }
            else if (!this.powered && world.isBlockIndirectlyGettingPowered(x, y, z)) {
                world.setBlock(x, y, z, Objects.blockColoredLampOn, world.getBlockMetadata(x, y, z), 2);
            }
        }
    }

    //    @Override
    //    public int idDropped(int meta, Random rand, int fortune) {
    //        return Objects.blockColoredLampOff.blockID;
    //    }

    public Item getItemDropped(int meta, Random rand, int fortune) {
        return Item.getItemFromBlock(Objects.blockColoredLampOff);
    }

    //    @Override
    //    @SideOnly(Side.CLIENT)
    //    public int idPicked(World world, int x, int y, int z) {
    //        return Objects.blockColoredLampOff.blockID;
    //    }

    @SideOnly(Side.CLIENT)
    public Item getItem(World world, int x, int y, int z) {
        return Item.getItemFromBlock(Objects.blockColoredLampOff);
    }

    @Override
    protected ItemStack createStackedBlock(int meta) {
        return new ItemStack(Objects.blockColoredLampOff, meta);
    }

}
