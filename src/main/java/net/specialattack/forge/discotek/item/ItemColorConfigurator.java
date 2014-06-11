
package net.specialattack.forge.discotek.item;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.specialattack.forge.discotek.item.crafting.RecipesColoredLamp;
import net.specialattack.forge.discotek.tileentity.TileEntityColoredLamp;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemColorConfigurator extends Item {

    @SideOnly(Side.CLIENT)
    public IIcon overlay;

    public ItemColorConfigurator() {
        super();
        this.setMaxStackSize(1);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getColorFromItemStack(ItemStack stack, int pass) {
        if (pass == 0) {
            NBTTagCompound compound = stack.stackTagCompound;
            if (compound != null) {
                if (compound.hasKey("color")) {
                    return compound.getInteger("color");
                }
            }
        }
        return 0xFFFFFF;
    }

    @Override
    public IIcon getIcon(ItemStack stack, int pass) {
        if (pass == 1) {
            return this.overlay;
        }
        return super.getIcon(stack, pass);
    }

    @Override
    @SideOnly(Side.CLIENT)
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean extra) {
        super.addInformation(stack, player, list, extra);

        NBTTagCompound compound = stack.stackTagCompound;
        if (compound != null) {
            if (compound.hasKey("color")) {
                String color = Integer.toHexString(compound.getInteger("color")).toUpperCase();
                while (color.length() < 6) {
                    color = "0" + color;
                }
                list.add(StatCollector.translateToLocalFormatted("gui.tooltip.colorConfigurator.color", "#" + color));
            }
            else {
                list.add(StatCollector.translateToLocal("gui.tooltip.colorConfigurator.nocolor"));
            }
        }
        else {
            list.add(StatCollector.translateToLocal("gui.tooltip.colorConfigurator.nocolor"));
        }
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        TileEntity tile = world.getTileEntity(x, y, z);
        if (tile != null && tile instanceof TileEntityColoredLamp) {
            TileEntityColoredLamp lamp = (TileEntityColoredLamp) tile;

            if (player.isSneaking()) {
                NBTTagCompound compound = stack.stackTagCompound;
                if (compound == null) {
                    compound = stack.stackTagCompound = new NBTTagCompound();
                }
                compound.setInteger("color", lamp.color.getValue());

                return true;
            }
            else {
                NBTTagCompound compound = stack.stackTagCompound;
                if (compound == null || !compound.hasKey("color")) {
                    return false;
                }
                int color = compound.getInteger("color");

                lamp.color.setValue(color);
                return true;
            }
        }

        return false;
    }

    @Override
    public int getRenderPasses(int meta) {
        return 2;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister register) {
        super.registerIcons(register);
        this.overlay = register.registerIcon(this.iconString + "-overlay");
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tab, List list) {
        list.add(new ItemStack(item, 1, 0));

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

}
