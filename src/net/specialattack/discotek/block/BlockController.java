
package net.specialattack.discotek.block;

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
import net.specialattack.discotek.Assets;
import net.specialattack.discotek.PacketHandler;
import net.specialattack.discotek.client.ClientProxy;
import net.specialattack.discotek.tileentity.TileEntityController;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockController extends Block {

    // This is the rythem of the night
    // http://www.youtube.com/watch?v=BfSpU0vEh4M

    private Icon[] bottom;
    private Icon[] top;
    private Icon[] side;

    public BlockController(int blockId) {
        super(blockId, Material.iron);
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
                    ClientProxy.openControllerGui(meta, controller);
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
    public int damageDropped(int meta) {
        return meta;
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
        for (int i = 0; i < this.top.length; i++) {
            list.add(new ItemStack(itemId, 1, i));
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister register) {
        this.top = new Icon[3];
        this.bottom = new Icon[3];
        this.side = new Icon[3];
        for (int i = 0; i < this.top.length; i++) {
            this.top[i] = register.registerIcon(Assets.DOMAIN + "controller-top" + i);
            this.bottom[i] = register.registerIcon(Assets.DOMAIN + "controller-bottom" + i);
            this.side[i] = register.registerIcon(Assets.DOMAIN + "controller-side" + i);
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
