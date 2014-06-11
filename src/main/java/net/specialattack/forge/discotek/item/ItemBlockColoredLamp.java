
package net.specialattack.forge.discotek.item;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemBlockColoredLamp extends ItemBlock {

    public ItemBlockColoredLamp(Block block) {
        super(block);
        this.setHasSubtypes(true);
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
                list.add(StatCollector.translateToLocalFormatted("gui.tooltip.coloredLamp.color", "#" + color));
            }
            else {
                list.add(StatCollector.translateToLocal("gui.tooltip.coloredLamp.nocolor"));
            }
        }
        else {
            list.add(StatCollector.translateToLocal("gui.tooltip.coloredLamp.nocolor"));
        }
    }

}
