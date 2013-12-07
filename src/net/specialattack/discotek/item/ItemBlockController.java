
package net.specialattack.discotek.item;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.specialattack.discotek.block.BlockController;
import net.specialattack.discotek.controllers.IController;

public class ItemBlockController extends ItemBlock {

    private BlockController block;

    public ItemBlockController(int itemId) {
        super(itemId);
        this.setHasSubtypes(true);
        this.block = (BlockController) Block.blocksList[itemId + 256];
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        IController controller = this.block.getController(stack.getItemDamage());
        if (controller == null) {
            return super.getUnlocalizedName(stack) + ".error";
        }

        return super.getUnlocalizedName(stack) + "." + controller.getIdentifier();
    }

    // Fix for silly ItemBlock
    @Override
    public int getMetadata(int damage) {
        return damage;
    }

}
