
package net.specialattack.forge.discotek.block;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRedstoneLight;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.specialattack.forge.discotek.Assets;
import net.specialattack.forge.discotek.Objects;
import net.specialattack.forge.discotek.client.render.BlockRendererColoredLamp;
import net.specialattack.forge.discotek.tileentity.TileEntityColoredLamp;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockColoredLamp extends BlockRedstoneLight {

    private final int renderId;
    @SideOnly(Side.CLIENT)
    private IIcon icon;
    @SideOnly(Side.CLIENT)
    private IIcon overlayIcon;

    public BlockColoredLamp() {
        super(false);
        this.renderId = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(this.renderId, new BlockRendererColoredLamp(this.renderId));
    }

    @Override
    public int damageDropped(int meta) {
        return meta;
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack stack) {
        super.onBlockPlacedBy(world, x, y, z, entity, stack);
        world.setBlockMetadataWithNotify(x, y, z, stack.getItemDamage(), 3);

        TileEntityColoredLamp tile = (TileEntityColoredLamp) world.getTileEntity(x, y, z);

        if (tile == null) {
            return;
        }

        if (stack.stackTagCompound != null) {
            if (stack.stackTagCompound.hasKey("color", 3)) {
                tile.color.setValue(stack.stackTagCompound.getInteger("color"));
            }
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
        if (meta == 0) {
            return this.overlayIcon;
        }
        return this.icon;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister register) {
        this.icon = register.registerIcon(Assets.DOMAIN + "lamp");
        this.overlayIcon = register.registerIcon(Assets.DOMAIN + "lamp-overlay");
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item item, CreativeTabs tab, List list) {
        for (int color : ItemDye.field_150922_c) {
            ItemStack stack = new ItemStack(item);
            stack.stackTagCompound = new NBTTagCompound();
            stack.stackTagCompound.setInteger("color", color);
            list.add(stack);
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
            TileEntityColoredLamp tile = (TileEntityColoredLamp) world.getTileEntity(x, y, z);
            if (tile != null) {
                boolean lit = tile.lit.getValue();
                boolean changed = false;
                if (lit && !world.isBlockIndirectlyGettingPowered(x, y, z)) {
                    tile.lit.setValue(false);
                    changed = true;
                }
                else if (!lit && world.isBlockIndirectlyGettingPowered(x, y, z)) {
                    tile.lit.setValue(true);
                    changed = true;
                }
                if (changed) {
                    world.updateLightByType(EnumSkyBlock.Block, x, y, z);
                }
            }
        }
    }

    @Override
    public int getLightValue(IBlockAccess world, int x, int y, int z) {
        TileEntityColoredLamp tile = (TileEntityColoredLamp) world.getTileEntity(x, y, z);
        if (tile != null) {
            return tile.lit.getValue() ? 15 : 0;
        }
        return 0;
    }

    @Override
    public Item getItemDropped(int meta, Random rand, int fortune) {
        return Item.getItemFromBlock(Objects.blockColoredLamp);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Item getItem(World world, int x, int y, int z) {
        return Item.getItemFromBlock(Objects.blockColoredLamp);
    }

    @Override
    protected ItemStack createStackedBlock(int meta) {
        return new ItemStack(Objects.blockColoredLamp, meta);
    }

    @Override
    public int getRenderType() {
        return this.renderId;
    }

    @Override
    public TileEntity createTileEntity(World world, int metadata) {
        return new TileEntityColoredLamp();
    }

    @Override
    public boolean hasTileEntity(int metadata) {
        return true;
    }

}
