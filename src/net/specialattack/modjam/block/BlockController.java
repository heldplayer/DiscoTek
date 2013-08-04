
package net.specialattack.modjam.block;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.Packet;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.specialattack.modjam.PacketHandler;
import net.specialattack.modjam.client.gui.GuiBasicController;
import net.specialattack.modjam.client.gui.GuiController;
import net.specialattack.modjam.tileentity.TileEntityController;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockController extends Block {

    public BlockController(int blockId) {
        super(blockId, Material.piston);
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float posX, float posY, float posZ) {
        TileEntity tile = world.getBlockTileEntity(x, y, z);

        if (tile != null && tile instanceof TileEntityController) {
            TileEntityController controller = (TileEntityController) tile;

            if (world.isRemote) {
                FMLClientHandler.instance().displayGuiScreen(player, new GuiBasicController(controller));
            }
            else {
                if (player instanceof EntityPlayerMP) {
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

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(int itemId, CreativeTabs tab, List list) {
        list.add(new ItemStack(itemId, 1, 0));
    }

    @Override
    public TileEntity createTileEntity(World world, int metadata) {
        return new TileEntityController();
    }

    @Override
    public boolean hasTileEntity(int metadata) {
        return true;
    }

}
