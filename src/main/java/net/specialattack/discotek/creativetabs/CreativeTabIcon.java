
package net.specialattack.discotek.creativetabs;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class CreativeTabIcon extends CreativeTabs {

    private ItemStack iconItemStack;

    public CreativeTabIcon(String name) {
        super(name);
    }

    @Override
    public ItemStack getIconItemStack() {
        return this.iconItemStack;
    }

    public void setIconItemStack(ItemStack iconItemStack) {
        this.iconItemStack = iconItemStack;
    }

}
