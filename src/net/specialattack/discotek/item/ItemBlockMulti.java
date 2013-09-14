
package net.specialattack.discotek.item;

import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockMulti extends ItemBlock {

    public ItemBlockMulti(int itemId) {
        super(itemId);
        this.setHasSubtypes(true);
    }

    @Override
    public String getItemDisplayName(ItemStack stack) {
        return super.getItemDisplayName(stack) + stack.getItemDamage();
    }

}
