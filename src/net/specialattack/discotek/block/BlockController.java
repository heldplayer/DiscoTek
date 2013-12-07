
package net.specialattack.discotek.block;

import java.util.List;
import java.util.TreeMap;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import net.specialattack.discotek.controllers.IController;
import net.specialattack.discotek.controllers.IControllerInstance;
import net.specialattack.discotek.tileentity.TileEntityController;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockController extends Block {

    private TreeMap<Integer, IController> controllers;
    private Icon missingno;

    public BlockController(int blockId) {
        super(blockId, Material.iron);
        this.controllers = new TreeMap<Integer, IController>();
    }

    public void setController(int id, IController controller) {
        this.controllers.put(Integer.valueOf(id), controller);
    }

    public IController getController(int id) {
        return this.controllers.get(Integer.valueOf(id));
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float posX, float posY, float posZ) {
        TileEntity tile = world.getBlockTileEntity(x, y, z);

        if (tile != null && tile instanceof TileEntityController) {
            TileEntityController controller = (TileEntityController) tile;

            if (!world.isRemote) {
                IControllerInstance instance = controller.getControllerInstance();
                if (instance != null) {
                    instance.openGui(player, Side.SERVER);
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
        for (int i = 0; i < 16; i++) {
            if (this.controllers.containsKey(Integer.valueOf(i))) {
                list.add(new ItemStack(itemId, 1, i));
            }
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister register) {
        this.missingno = register.registerIcon("missingno");

        for (int i = 0; i < 16; i++) {
            if (this.controllers.containsKey(Integer.valueOf(i))) {
                this.controllers.get(Integer.valueOf(i)).registerIcons(register);
            }
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Icon getIcon(int side, int meta) {
        if (this.controllers.containsKey(Integer.valueOf(meta))) {
            return this.controllers.get(Integer.valueOf(meta)).getIcon(side);
        }

        return missingno;
    }

    @Override
    public TileEntity createTileEntity(World world, int metadata) {
        return new TileEntityController(this, metadata, !world.isRemote);
    }

    @Override
    public boolean hasTileEntity(int metadata) {
        return true;
    }

}
