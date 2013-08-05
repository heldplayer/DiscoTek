
package net.specialattack.discotek.block;

import java.util.List;
import java.util.Random;

import net.minecraft.block.BlockRedstoneLight;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import net.specialattack.discotek.Assets;
import net.specialattack.discotek.Objects;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockColoredLamp extends BlockRedstoneLight {

    @SideOnly(Side.CLIENT)
    private Icon[] icons;

    private final boolean powered;

    public BlockColoredLamp(int blockId, boolean powered) {
        super(blockId, powered);
        this.powered = powered;
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
        this.icons = new Icon[16];

        for (int i = 0; i < this.icons.length; i++) {
            this.icons[i] = register.registerIcon(Assets.DOMAIN + "lamp-" + (this.powered ? "on" : "off") + i);
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

    @Override
    public void onBlockAdded(World world, int x, int y, int z) {
        if (!world.isRemote) {
            if (this.powered && !world.isBlockIndirectlyGettingPowered(x, y, z)) {
                world.scheduleBlockUpdate(x, y, z, this.blockID, 4);
            }
            else if (!this.powered && world.isBlockIndirectlyGettingPowered(x, y, z)) {
                world.setBlock(x, y, z, Objects.blockColoredLampOn.blockID, world.getBlockMetadata(x, y, z), 2);
            }
        }
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, int neighbor) {
        if (!world.isRemote) {
            if (this.powered && !world.isBlockIndirectlyGettingPowered(x, y, z)) {
                world.scheduleBlockUpdate(x, y, z, this.blockID, 4);
            }
            else if (!this.powered && world.isBlockIndirectlyGettingPowered(x, y, z)) {
                world.setBlock(x, y, z, Objects.blockColoredLampOn.blockID, world.getBlockMetadata(x, y, z), 2);
            }
        }
    }

    @Override
    public void updateTick(World world, int x, int y, int z, Random rand) {
        if (!world.isRemote && this.powered && !world.isBlockIndirectlyGettingPowered(x, y, z)) {
            world.setBlock(x, y, z, Objects.blockColoredLampOff.blockID, world.getBlockMetadata(x, y, z), 2);
        }
    }

    @Override
    public int idDropped(int meta, Random rand, int fortune) {
        return Objects.blockColoredLampOff.blockID;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int idPicked(World world, int x, int y, int z) {
        return Objects.blockColoredLampOff.blockID;
    }

}
