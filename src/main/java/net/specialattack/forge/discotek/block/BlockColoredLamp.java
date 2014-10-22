package net.specialattack.forge.discotek.block;

import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRedstoneLight;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.specialattack.forge.discotek.Assets;
import net.specialattack.forge.discotek.Objects;
import net.specialattack.forge.discotek.client.render.BlockRendererColoredLamp;
import net.specialattack.forge.discotek.item.crafting.RecipesColoredLamp;
import net.specialattack.forge.discotek.tileentity.TileEntityColoredLamp;

public class BlockColoredLamp extends BlockRedstoneLight {

    private final int renderId;
    @SideOnly(Side.CLIENT)
    private IIcon icon;
    @SideOnly(Side.CLIENT)
    private IIcon overlayIcon;
    private TileEntityColoredLamp tile;

    public BlockColoredLamp() {
        super(false);
        this.renderId = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(this.renderId, new BlockRendererColoredLamp(this.renderId));
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
                } else if (!lit && world.isBlockIndirectlyGettingPowered(x, y, z)) {
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
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
        if (meta == 0) {
            return this.overlayIcon;
        }
        return this.icon;
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

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item item, CreativeTabs tab, List list) {
        for (float[] colorArray : RecipesColoredLamp.dyeColors) {
            int red = (int) (colorArray[0] * 255.0F);
            int green = (int) (colorArray[1] * 255.0F);
            int blue = (int) (colorArray[2] * 255.0F);
            int color = red << 16 | green << 8 | blue;

            ItemStack stack = new ItemStack(item, 1, 0);
            NBTTagCompound compound = stack.stackTagCompound = new NBTTagCompound();
            compound.setInteger("color", color);

            list.add(stack);
        }
    }

    @Override
    public void onBlockHarvested(World world, int x, int y, int z, int side, EntityPlayer player) {
        super.onBlockHarvested(world, x, y, z, side, player);

        TileEntity tile = world.getTileEntity(x, y, z);

        if (tile != null && tile instanceof TileEntityColoredLamp) {
            this.tile = (TileEntityColoredLamp) tile;
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister register) {
        this.icon = register.registerIcon(Assets.DOMAIN + "lamp");
        this.overlayIcon = register.registerIcon(Assets.DOMAIN + "lamp-overlay");
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
    public boolean hasTileEntity(int metadata) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, int metadata) {
        return new TileEntityColoredLamp();
    }

    @Override
    public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune) {
        ArrayList<ItemStack> result = new ArrayList<ItemStack>();

        ItemStack stack = new ItemStack(this, 1, metadata);

        NBTTagCompound compound = stack.stackTagCompound = new NBTTagCompound();
        compound.setInteger("color", 0xFFFFFF);

        if (this.tile != null) {
            compound.setInteger("color", this.tile.color.getValue());
        }

        result.add(stack);

        return result;
    }

    @Override
    public boolean canCreatureSpawn(EnumCreatureType type, IBlockAccess world, int x, int y, int z) {
        return false;
    }

    @Override
    public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z) {
        ItemStack stack = new ItemStack(this);
        NBTTagCompound compound = stack.stackTagCompound = new NBTTagCompound();
        compound.setInteger("color", 0xFFFFFF);

        TileEntity tile = world.getTileEntity(x, y, z);
        if (tile != null && tile instanceof TileEntityColoredLamp) {
            compound.setInteger("color", ((TileEntityColoredLamp) tile).color.getValue());
        }

        return stack;
    }

}
