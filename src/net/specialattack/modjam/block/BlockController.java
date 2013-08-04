
package net.specialattack.modjam.block;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.Packet;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import net.specialattack.modjam.PacketHandler;
import net.specialattack.modjam.client.gui.GuiBasicController;
import net.specialattack.modjam.client.gui.GuiController;
import net.specialattack.modjam.tileentity.TileEntityController;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockController extends Block {
    
    // This is the rythem of the night
    // http://www.youtube.com/watch?v=BfSpU0vEh4M

    private Icon[] bottom;
    private Icon[] top;
    private Icon[] side;

    public BlockController(int blockId) {
        super(blockId, Material.piston);
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float posX, float posY, float posZ) {
        TileEntity tile = world.getBlockTileEntity(x, y, z);

        if (tile != null && tile instanceof TileEntityController) {
            TileEntityController controller = (TileEntityController) tile;

            if (player.isSneaking()) {
                int meta = world.getBlockMetadata(x, y, z);
                if (meta == 1) {
                    controller.startStop();
                }
            }
            else {
                if (world.isRemote) {
                    int meta = world.getBlockMetadata(x, y, z);
                    if (meta == 0) {
                        FMLClientHandler.instance().displayGuiScreen(player, new GuiBasicController(controller));
                    }
                    else if (meta == 1) {
                        FMLClientHandler.instance().displayGuiScreen(player, new GuiController(controller));
                    }
                }
                else {
                    if (player instanceof EntityPlayerMP) {
                        Packet packet = PacketHandler.createPacket(5, tile);
                        if (packet != null) {
                            ((EntityPlayerMP) player).playerNetServerHandler.sendPacketToPlayer(packet);
                        }
                    }
                }
            }
        }

        return true;
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack stack) {
        super.onBlockPlacedBy(world, x, y, z, entity, stack);
        world.setBlockMetadataWithNotify(x, y, z, stack.getItemDamage(), 0);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(int itemId, CreativeTabs tab, List list) {
        list.add(new ItemStack(itemId, 1, 0));
        list.add(new ItemStack(itemId, 1, 1));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister register) {
        this.top = new Icon[2];
        this.bottom = new Icon[2];
        this.side = new Icon[2];
        for (int i = 0; i < this.top.length; i++) {
            this.top[i] = register.registerIcon("modjam:controller-top" + i);
            this.bottom[i] = register.registerIcon("modjam:controller-bottom" + i);
            this.side[i] = register.registerIcon("modjam:controller-side" + i);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Icon getIcon(int side, int meta) {
        Icon[] array = this.side;
        if (side == 0) {
            array = this.bottom;
        }
        else if (side == 1) {
            array = this.top;
        }
        return array[meta % array.length];
    }

    @Override
    public TileEntity createTileEntity(World world, int metadata) {
        return new TileEntityController();
    }

    @Override
    public boolean hasTileEntity(int metadata) {
        return true;
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

}
