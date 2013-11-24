
package net.specialattack.discotek.item;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemBlockLight extends ItemBlock {

    public ItemBlockLight(int itemId) {
        super(itemId);
        this.setHasSubtypes(true);
    }

    @Override
    public String getUnlocalizedNameInefficiently(ItemStack stack) {
        return super.getUnlocalizedNameInefficiently(stack) + stack.getItemDamage();
    }

    @Override
    @SideOnly(Side.CLIENT)
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean extra) {
        super.addInformation(stack, player, list, extra);

        // if (extra) {
        NBTTagCompound compound = stack.stackTagCompound;
        if (compound != null) {
            if (compound.hasKey("color")) {
                String color = Integer.toHexString(compound.getInteger("color")).toUpperCase();
                while (color.length() < 6) {
                    color = "0" + color;
                }
                list.add(StatCollector.translateToLocalFormatted("gui.tooltip.light.color", "#" + color));
            }
            if (compound.hasKey("hasLens")) {
                if (compound.getBoolean("hasLens")) {
                    list.add(StatCollector.translateToLocal("gui.tooltip.light.lens.true"));
                }
                else {
                    list.add(StatCollector.translateToLocal("gui.tooltip.light.lens.false"));
                }
            }
            else {
                int meta = stack.getItemDamage();
                if (meta < 2) {
                    list.add(StatCollector.translateToLocal("gui.tooltip.light.lens.true"));
                }
                else {
                    list.add(StatCollector.translateToLocal("gui.tooltip.light.lens.false"));
                }
            }
        }
        // }
    }

}
