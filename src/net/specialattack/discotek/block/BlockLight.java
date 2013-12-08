
package net.specialattack.discotek.block;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import me.heldplayer.util.Table;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import net.specialattack.discotek.Objects;
import net.specialattack.discotek.client.render.BlockRendererLight;
import net.specialattack.discotek.item.ItemOrienter;
import net.specialattack.discotek.lights.Channels;
import net.specialattack.discotek.lights.ILight;
import net.specialattack.discotek.lights.ILightRenderHandler;
import net.specialattack.discotek.packet.Packet2LightGui;
import net.specialattack.discotek.packet.PacketHandler;
import net.specialattack.discotek.tileentity.TileEntityLight;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockLight extends Block {

    private final int renderId;
    private int temp = 0;
    private Table<Integer, ILight, ILightRenderHandler> lights;

    public BlockLight(int blockId) {
        super(blockId, Material.iron);
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

    public ILight getLight(int id) {
        return this.lights.getValue1(Integer.valueOf(id));
    }

    public ILightRenderHandler getLightRenderer(int id) {
        return this.lights.getValue2(Integer.valueOf(id));
    }

    @Override
    public int damageDropped(int meta) {
        return meta;
    }

    @Override
    public int onBlockPlaced(World world, int x, int y, int z, int side, float posX, float posY, float posZ, int meta) {
        this.temp = side;

        return super.onBlockPlaced(world, x, y, z, side, posX, posY, posZ, meta);
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack stack) {
        super.onBlockPlacedBy(world, x, y, z, entity, stack);

        TileEntityLight tile = (TileEntityLight) world.getBlockTileEntity(x, y, z);

        ILight light = this.getLight(world.getBlockMetadata(x, y, z));

        if (stack.stackTagCompound != null) {
            if (stack.stackTagCompound.hasKey("color")) {
                tile.setColor(stack.stackTagCompound.getInteger("color"));
            }
            if (light != null && light.supportsLens() && stack.stackTagCompound.hasKey("hasLens")) {
                tile.setHasLens(stack.stackTagCompound.getBoolean("hasLens"));
            }
        }

        float yaw = -entity.rotationYawHead;
        float pitch = entity.rotationPitch;
        if (pitch > 46.0F) {
            pitch = 46.0F;
        }
        if (pitch < -46.0F) {
            pitch = -46.0F;
        }

        if (this.temp == 0) {
            yaw = -yaw;
        }
        if (this.temp == 4 || this.temp == 5) {
            yaw = -yaw;
        }

        tile.setYaw((float) (yaw * Math.PI / 180.0D));
        tile.setPitch((float) (pitch * Math.PI / 180.0D));
        tile.setDirection(this.temp);
        world.setBlockMetadataWithNotify(x, y, z, stack.getItemDamage(), 0);
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float posX, float posY, float posZ) {
        ItemStack selected = player.getCurrentEquippedItem();

        if (selected != null && selected.getItem() != null && selected.getItem() instanceof ItemOrienter) {
            return false;
        }

        TileEntity tile = world.getBlockTileEntity(x, y, z);

        if (tile != null && tile instanceof TileEntityLight) {
            TileEntityLight light = (TileEntityLight) tile;

            if (player.isSneaking()) {
                if (light.hasLens() && light.getLight().supportsLens()) {
                    if (!world.isRemote) {
                        ItemStack is = new ItemStack(Objects.itemLens);
                        NBTTagCompound cpnd = new NBTTagCompound("tag");
                        cpnd.setInteger("color", light.getColor(1.0F));
                        is.setTagCompound(cpnd);

                        Random rand = new Random();
                        EntityItem ent = player.entityDropItem(is, 1.0F);
                        ent.motionY += rand.nextFloat() * 0.05F;
                        ent.motionX += (rand.nextFloat() - rand.nextFloat()) * 0.1F;
                        ent.motionZ += (rand.nextFloat() - rand.nextFloat()) * 0.1F;
                        ent.delayBeforeCanPickup = 1;

                        light.setColor(0xFFFFFF);
                        light.setHasLens(false);
                        light.onInventoryChanged();
                    }
                    return true;
                }
            }

            if (!world.isRemote) {
                if (player instanceof EntityPlayerMP) {
                    ((EntityPlayerMP) player).playerNetServerHandler.sendPacketToPlayer(PacketHandler.instance.createPacket(new Packet2LightGui(light)));
                }
            }

            return true;
        }

        return false;
    }

    private TileEntityLight light;

    @Override
    public void onBlockHarvested(World world, int x, int y, int z, int side, EntityPlayer player) {
        super.onBlockHarvested(world, x, y, z, side, player);

        TileEntity tile = world.getBlockTileEntity(x, y, z);

        if (tile != null && tile instanceof TileEntityLight) {
            this.light = (TileEntityLight) tile;
        }
    }

    @Override
    public ArrayList<ItemStack> getBlockDropped(World world, int x, int y, int z, int metadata, int fortune) {
        ArrayList<ItemStack> result = new ArrayList<ItemStack>();

        ItemStack stack = new ItemStack(this, 1, metadata);

        ILight light = this.getLight(metadata);

        NBTTagCompound compound = stack.stackTagCompound = new NBTTagCompound("tag");

        if (this.light != null) {
            compound.setInteger("color", this.light.getColor(1.0F));
            if (light.supportsLens()) {
                compound.setBoolean("hasLens", this.light.hasLens());
            }
            this.light = null;
        }
        else {
            compound.setInteger("color", 0xFFFFFF);
            if (light != null && light.supportsLens()) {
                compound.setBoolean("hasLens", light.supportsLens());
            }
        }

        result.add(stack);

        return result;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(int itemId, CreativeTabs tab, List list) {
        for (int i = 0; i < 16; i++) {
            if (this.lights.containsKey(Integer.valueOf(i))) {
                list.add(new ItemStack(itemId, 1, i));
            }
        }
    }

    @Override
    public TileEntity createTileEntity(World world, int metadata) {
        return new TileEntityLight(this, metadata, !world.isRemote);
    }

    @Override
    public boolean hasTileEntity(int metadata) {
        return true;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public int getRenderType() {
        return this.renderId;
    }

    @Override
    public int isProvidingWeakPower(IBlockAccess world, int x, int y, int z, int side) {
        ILight light = this.getLight(world.getBlockMetadata(x, y, z));
        TileEntity tile = world.getBlockTileEntity(x, y, z);

        if (light != null && tile != null && tile instanceof TileEntityLight) {
            return light.getRedstonePower(((TileEntityLight) tile).getLevel(Channels.STRENGTH));
        }

        return 0;
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z) {
        ILight light = this.getLight(world.getBlockMetadata(x, y, z));
        TileEntity tile = world.getBlockTileEntity(x, y, z);

        if (light != null && tile != null && tile instanceof TileEntityLight) {
            light.setBlockBounds(this, ((TileEntityLight) tile).getDirection());
        }
        else {
            this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        }
    }

    @Override
    public boolean isBlockSolidOnSide(World world, int x, int y, int z, ForgeDirection side) {
        return true;
    }

}
