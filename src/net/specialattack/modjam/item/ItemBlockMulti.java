
package net.specialattack.modjam.item;

import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockMulti extends ItemBlock {

    public ItemBlockMulti(int itemId) {
        super(itemId);
        this.setHasSubtypes(true);
    }

    @Override
    public String getLocalizedName(ItemStack stack) {
        return super.getLocalizedName(stack) + stack.getItemDamage();
    }

}
