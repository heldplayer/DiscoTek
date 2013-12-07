
package net.specialattack.discotek.item;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.specialattack.discotek.block.BlockLight;
import net.specialattack.discotek.lights.ILight;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemBlockLight extends ItemBlock {

    private BlockLight block;

    public ItemBlockLight(int itemId) {
        super(itemId);
        this.setHasSubtypes(true);
        this.block = (BlockLight) Block.blocksList[itemId + 256];
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        ILight light = this.block.getLight(stack.getItemDamage());
        if (light == null) {
            return super.getUnlocalizedName(stack) + ".error";
        }

        return super.getUnlocalizedName(stack) + "." + light.getIdentifier();
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
                ILight light = this.block.getLight(stack.getItemDamage());
                if (light != null && light.supportsLens()) {
                    list.add(StatCollector.translateToLocal("gui.tooltip.light.lens.true"));
                }
            }
        }
    }

    // Fix for silly ItemBlock
    @Override
    public int getMetadata(int damage) {
        return damage;
    }

}
