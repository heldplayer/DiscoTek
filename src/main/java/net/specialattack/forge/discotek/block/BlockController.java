package net.specialattack.forge.discotek.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.specialattack.forge.discotek.Assets;
import net.specialattack.forge.discotek.controller.IController;
import net.specialattack.forge.discotek.controller.instance.IControllerInstance;
import net.specialattack.forge.discotek.tileentity.TileEntityController;

import java.util.List;
import java.util.TreeMap;

public class BlockController extends Block {

    private TreeMap<Integer, IController> controllers;
    private IIcon missingno;

    public BlockController() {
        super(Material.iron);
        this.controllers = new TreeMap<Integer, IController>();
    }

    public void setController(int id, IController controller) {
        this.controllers.put(Integer.valueOf(id), controller);
    }

    public IController getController(int id) {
        return this.controllers.get(Integer.valueOf(id));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
        if (this.controllers.containsKey(Integer.valueOf(meta))) {
            return this.controllers.get(Integer.valueOf(meta)).getIcon(side);
        }

        return this.missingno;
    }

    @Override
    public int damageDropped(int meta) {
        return meta;
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float posX, float posY, float posZ) {
        TileEntity tile = world.getTileEntity(x, y, z);

        if (tile != null && tile instanceof TileEntityController) {
            TileEntityController controller = (TileEntityController) tile;

            if (!world.isRemote) {
                IControllerInstance instance = controller.getControllerInstance();
                if (instance != null) {
                    if (!instance.onRightClick(player, player.isSneaking())) {
                        instance.openGui(player, Side.SERVER);
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
    public void getSubBlocks(Item item, CreativeTabs tab, List list) {
        for (int i = 0; i < 16; i++) {
            if (this.controllers.containsKey(Integer.valueOf(i))) {
                list.add(new ItemStack(item, 1, i));
            }
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister register) {
        this.missingno = register.registerIcon(Assets.DOMAIN + "missingno");

        for (int i = 0; i < 16; i++) {
            if (this.controllers.containsKey(Integer.valueOf(i))) {
                this.controllers.get(Integer.valueOf(i)).registerIcons(register);
            }
        }
    }

    @Override
    public boolean hasTileEntity(int metadata) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, int metadata) {
        return new TileEntityController(this, metadata, !world.isRemote);
    }

}
