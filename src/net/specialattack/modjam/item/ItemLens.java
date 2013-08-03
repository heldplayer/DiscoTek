
package net.specialattack.modjam.item;

import java.util.List;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Icon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemLens extends Item {

    @SideOnly(Side.CLIENT)
    public Icon overlay;

    public ItemLens(int itemId) {
        super(itemId);
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
    public Icon getIcon(ItemStack stack, int pass) {
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

        if (extra) {
            NBTTagCompound compound = stack.stackTagCompound;
            if (compound != null) {
                if (compound.hasKey("color")) {
                    list.add("Color: #" + Integer.toHexString(compound.getInteger("color")).toUpperCase());
                }
            }
        }
    }

    @Override
    public int getRenderPasses(int meta) {
        return 2;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister register) {
        super.registerIcons(register);
        this.overlay = register.registerIcon(this.field_111218_cA + "_overlay");
    }

}
