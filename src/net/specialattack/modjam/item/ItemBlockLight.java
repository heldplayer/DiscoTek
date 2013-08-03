
package net.specialattack.modjam.item;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemBlockLight extends ItemBlock {

    public ItemBlockLight(int itemId) {
        super(itemId);
        this.setHasSubtypes(true);
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
                    String color = Integer.toHexString(compound.getInteger("color")).toUpperCase();
                    while (color.length() < 6) {
                        color = "0" + color;
                    }
                    list.add("Color: #" + color);
                }
            }
        }
    }

}
