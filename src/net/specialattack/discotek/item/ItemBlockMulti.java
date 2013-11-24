
package net.specialattack.discotek.item;

import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockMulti extends ItemBlock {

    public ItemBlockMulti(int itemId) {
        super(itemId);
        this.setHasSubtypes(true);
    }

    @Override
    public String getUnlocalizedNameInefficiently(ItemStack stack) {
        return super.getUnlocalizedNameInefficiently(stack) + stack.getItemDamage();
    }

}
