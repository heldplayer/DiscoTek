package net.specialattack.forge.discotek.item;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockMulti extends ItemBlock {

    public ItemBlockMulti(Block block) {
        super(block);
        this.setHasSubtypes(true);
    }

    @Override
    public String getUnlocalizedNameInefficiently(ItemStack stack) {
        return super.getUnlocalizedNameInefficiently(stack) + stack.getItemDamage();
    }
}
