package net.specialattack.forge.discotek.block;

import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.specialattack.forge.discotek.ModDiscoTek;
import net.specialattack.forge.discotek.Objects;
import net.specialattack.forge.discotek.client.render.BlockRendererLight;
import net.specialattack.forge.discotek.client.renderer.light.ILightRenderHandler;
import net.specialattack.forge.discotek.item.ItemOrienter;
import net.specialattack.forge.discotek.light.ILight;
import net.specialattack.forge.discotek.light.instance.ILightInstance;
import net.specialattack.forge.discotek.packet.Packet2LightGui;
import net.specialattack.forge.discotek.tileentity.TileEntityLight;
import net.specialattack.util.Table;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BlockLight extends Block {

    private final int renderId;
    private int temp = 0;
    private Table<Integer, ILight, ILightRenderHandler> lights;
    private TileEntityLight light;

    public BlockLight() {
        super(Material.iron);
        this.renderId = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(this.renderId, new BlockRendererLight(this.renderId));
        this.lights = new Table<Integer, ILight, ILightRenderHandler>();
    }

    public void setLight(int id, ILight light) {
        this.lights.insert(Integer.valueOf(id), light, null);
    }

    public void setLightRenderer(int id, ILightRenderHandler handler) {
        this.lights.getValue(Integer.valueOf(id)).setValue2(handler);
    }

    public ILightRenderHandler getLightRenderer(int id) {
        return this.lights.getValue2(Integer.valueOf(id));
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public int getRenderType() {
        return this.renderId;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void addCollisionBoxesToList(World world, int x, int y, int z, AxisAlignedBB aabb, List list, Entity entity) {
        this.setBlockBoundsBasedOnState(world, x, y, z);

        super.addCollisionBoxesToList(world, x, y, z, aabb, list, entity);
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public int damageDropped(int meta) {
        return meta;
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float posX, float posY, float posZ) {
        ItemStack selected = player.getCurrentEquippedItem();

        if (selected != null && selected.getItem() != null && selected.getItem() instanceof ItemOrienter) {
            return false;
        }

        TileEntity tile = world.getTileEntity(x, y, z);

        if (tile != null && tile instanceof TileEntityLight) {
            TileEntityLight light = (TileEntityLight) tile;

            if (player.isSneaking()) {
                if (light.getBoolean("hasLens", 1.0F) && light.getLight().supportsLens()) {
                    if (!world.isRemote) {
                        ItemStack is = new ItemStack(Objects.itemLens);
                        NBTTagCompound cpnd = new NBTTagCompound();
                        cpnd.setInteger("color", light.getInteger("color", 1.0F));
                        is.setTagCompound(cpnd);

                        Random rand = new Random();
                        EntityItem ent = player.entityDropItem(is, 1.0F);
                        ent.motionY += rand.nextFloat() * 0.05F;
                        ent.motionX += (rand.nextFloat() - rand.nextFloat()) * 0.1F;
                        ent.motionZ += (rand.nextFloat() - rand.nextFloat()) * 0.1F;
                        ent.delayBeforeCanPickup = 1;

                        light.setValue("color", 0xFFFFFF);
                        light.setValue("hasLens", false);
                        light.markDirty();
                    }
                    return true;
                }
            }

            if (!world.isRemote) {
                ModDiscoTek.packetHandler.sendPacketToPlayer(new Packet2LightGui(light), player);
            }

            return true;
        }

        return false;
    }

    @Override
    public int onBlockPlaced(World world, int x, int y, int z, int side, float posX, float posY, float posZ, int meta) {
        this.temp = side;

        return super.onBlockPlaced(world, x, y, z, side, posX, posY, posZ, meta);
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z) {
        ILight light = this.getLight(world.getBlockMetadata(x, y, z));
        TileEntity tile = world.getTileEntity(x, y, z);

        if (light != null && tile != null && tile instanceof TileEntityLight) {
            ILightInstance instance = ((TileEntityLight) tile).getLightInstance();
            if (instance != null) {
                instance.setBlockBounds(this);
            }
        } else {
            this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        }
    }

    @Override
    public int isProvidingWeakPower(IBlockAccess world, int x, int y, int z, int side) {
        ILight light = this.getLight(world.getBlockMetadata(x, y, z));
        TileEntity tile = world.getTileEntity(x, y, z);

        if (light != null && tile != null && tile instanceof TileEntityLight) {
            return ((TileEntityLight) tile).getInteger("redstone", 1.0F);
        }

        return 0;
    }

    public ILight getLight(int id) {
        return this.lights.getValue1(Integer.valueOf(id));
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack stack) {
        super.onBlockPlacedBy(world, x, y, z, entity, stack);

        TileEntityLight tile = (TileEntityLight) world.getTileEntity(x, y, z);

        ILightInstance instance = tile.getLightInstance();

        if (instance != null && stack.stackTagCompound != null) {
            instance.readLosely(stack.stackTagCompound);
        }

        float rotation = -entity.rotationYawHead;
        float pitch = entity.rotationPitch;
        if (pitch > 46.0F) {
            pitch = 46.0F;
        }
        if (pitch < -46.0F) {
            pitch = -46.0F;
        }

        if (this.temp == 0) {
            rotation = -rotation;
        }
        if (this.temp == 4 || this.temp == 5) {
            rotation = -rotation;
        }

        tile.setValue("rotation", (float) (rotation * Math.PI / 180.0D));
        tile.setValue("pitch", (float) (pitch * Math.PI / 180.0D));
        tile.setValue("direction", this.temp);
        world.setBlockMetadataWithNotify(x, y, z, stack.getItemDamage(), 0);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item item, CreativeTabs tab, List list) {
        for (int i = 0; i < 16; i++) {
            if (this.lights.containsKey(Integer.valueOf(i))) {
                list.add(new ItemStack(item, 1, i));
            }
        }
    }

    @Override
    public void onBlockHarvested(World world, int x, int y, int z, int side, EntityPlayer player) {
        super.onBlockHarvested(world, x, y, z, side, player);

        TileEntity tile = world.getTileEntity(x, y, z);

        if (tile != null && tile instanceof TileEntityLight) {
            this.light = (TileEntityLight) tile;
        }
    }

    @Override
    public boolean isSideSolid(IBlockAccess world, int x, int y, int z, ForgeDirection side) {
        return true;
    }

    @Override
    public boolean hasTileEntity(int metadata) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, int metadata) {
        return new TileEntityLight(this, metadata, !world.isRemote);
    }

    @Override
    public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune) {
        ArrayList<ItemStack> result = new ArrayList<ItemStack>();

        ItemStack stack = new ItemStack(this, 1, metadata);

        NBTTagCompound compound = stack.stackTagCompound = new NBTTagCompound();

        if (this.light != null) {
            ILightInstance instance = this.light.getLightInstance();
            if (instance != null) {
                instance.writeLosely(compound);
            }
            this.light = null;
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
        int meta = world.getBlockMetadata(x, y, z);
        ItemStack stack = new ItemStack(this, 1, meta);
        NBTTagCompound compound = stack.stackTagCompound = new NBTTagCompound();

        ILight lightType = this.getLight(meta);

        TileEntity tile = world.getTileEntity(x, y, z);
        if (tile == null || !(tile instanceof TileEntityLight)) {
            compound.setInteger("color", 0xFFFFFF);
            if (lightType != null && lightType.supportsLens()) {
                compound.setBoolean("hasLens", lightType.supportsLens());
            }
            return stack;
        }

        TileEntityLight light = (TileEntityLight) tile;
        ILightInstance instance = light.getLightInstance();

        if (instance != null) {
            instance.writeLosely(compound);
        }

        return stack;
    }

}
