package net.specialattack.forge.discotek.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.specialattack.forge.discotek.block.BlockLight;
import net.specialattack.forge.discotek.light.ILight;

import java.util.List;

public class ItemBlockLight extends ItemBlock {

    private BlockLight block;

    public ItemBlockLight(Block block) {
        super(block);
        this.setHasSubtypes(true);
        this.block = (BlockLight) block;
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        ILight light = this.block.getLight(stack.getItemDamage());
        if (light == null) {
            return super.getUnlocalizedName(stack) + ".error";
        }

        return super.getUnlocalizedName(stack) + "." + light.getIdentifier();
    }

    // Fix for silly ItemBlock
    @Override
    public int getMetadata(int damage) {
        return damage;
    }

    @Override
    @SideOnly(Side.CLIENT)
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean extra) {
        super.addInformation(stack, player, list, extra);

        ILight light = this.block.getLight(stack.getItemDamage());
        if (light != null) {
            light.addInformation(stack, player, list, extra);
        }
    }

}
