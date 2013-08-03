
package net.specialattack.modjam.block;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.packet.Packet;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.specialattack.modjam.Objects;
import net.specialattack.modjam.PacketHandler;
import net.specialattack.modjam.client.gui.GuiConfigureSmall;
import net.specialattack.modjam.client.render.BlockRendererLight;
import net.specialattack.modjam.tileentity.TileEntityLight;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockLight extends Block {

    private final int renderId;

    public BlockLight(int blockId) {
        super(blockId, Material.piston);
        this.renderId = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(this.renderId, new BlockRendererLight(this.renderId));
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack stack) {
        super.onBlockPlacedBy(world, x, y, z, entity, stack);

        TileEntityLight tile = (TileEntityLight) world.getBlockTileEntity(x, y, z);

        if (stack.stackTagCompound != null) {
            if (stack.stackTagCompound.hasKey("color")) {
                tile.setColor(stack.stackTagCompound.getInteger("color"));
            }
            if (stack.stackTagCompound.hasKey("hasLens")) {
                tile.setHasLens(stack.stackTagCompound.getBoolean("hasLens"));
            }
        }

        float yaw = (float) (-entity.rotationYawHead * Math.PI / 180.0D);
        float pitch = (float) (entity.rotationPitch * Math.PI / 180.0D);
        if (pitch > 0.8F) {
            pitch = 0.8F;
        }
        if (pitch < -0.8F) {
            pitch = -0.8F;
        }

        tile.setYaw(yaw);
        tile.setPitch(pitch);
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float posX, float posY, float posZ) {
        TileEntity tile = world.getBlockTileEntity(x, y, z);

        if (tile != null && tile instanceof TileEntityLight) {
            TileEntityLight light = (TileEntityLight) tile;

            if (player.isSneaking()) {
                if (light.hasLens()) {
                    if (!world.isRemote) {
                        ItemStack is = new ItemStack(Objects.itemLens);
                        NBTTagCompound cpnd = new NBTTagCompound("tag");
                        cpnd.setInteger("color", light.getColor());
                        is.setTagCompound(cpnd);

                        Random rand = new Random();
                        EntityItem ent = player.entityDropItem(is, 1.0F);
                        ent.motionY += rand.nextFloat() * 0.05F;
                        ent.motionX += (rand.nextFloat() - rand.nextFloat()) * 0.1F;
                        ent.motionZ += (rand.nextFloat() - rand.nextFloat()) * 0.1F;
                        ent.delayBeforeCanPickup = 1;

                        light.setColor(0xFFFFFF);
                        light.setHasLens(false);
                    }
                    return true;
                }
            }

            if (world.isRemote) {
                FMLClientHandler.instance().displayGuiScreen(player, new GuiConfigureSmall(light));
            }
            else {
                if (player instanceof EntityPlayerMP) {
                    if (player instanceof EntityPlayerMP) {
                        Packet packet = PacketHandler.createPacket(3, tile);
                        if (packet != null) {
                            ((EntityPlayerMP) player).playerNetServerHandler.sendPacketToPlayer(packet);
                        }
                    }
                }
            }
        }

        return true;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(int itemId, CreativeTabs tab, List list) {
        list.add(new ItemStack(itemId, 1, 0));
    }

    @Override
    public TileEntity createTileEntity(World world, int metadata) {
        return new TileEntityLight();
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

}
