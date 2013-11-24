
package net.specialattack.discotek.item;

import java.util.List;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.specialattack.discotek.Assets;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemCrafting extends Item {

    @SideOnly(Side.CLIENT)
    public Icon[] icons;

    public ItemCrafting(int itemId) {
        super(itemId);
        this.setHasSubtypes(true);
    }

    @Override
    public String getUnlocalizedNameInefficiently(ItemStack stack) {
        return super.getUnlocalizedNameInefficiently(stack) + stack.getItemDamage();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister register) {
        this.icons = new Icon[4];
        for (int i = 0; i < this.icons.length; i++) {
            this.icons[i] = register.registerIcon(Assets.DOMAIN + "crafting" + i);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Icon getIconFromDamage(int meta) {
        return this.icons[meta % this.icons.length];
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(int itemId, CreativeTabs tab, List list) {
        for (int i = 0; i < 4; i++) {
            list.add(new ItemStack(itemId, 1, i));
        }
    }

}
