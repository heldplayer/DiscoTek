package net.specialattack.forge.discotek.item;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.specialattack.forge.discotek.block.BlockController;
import net.specialattack.forge.discotek.controller.IController;

public class ItemBlockController extends ItemBlock {

    private BlockController block;

    public ItemBlockController(Block block) {
        super(block);
        this.setHasSubtypes(true);
        this.block = (BlockController) block;
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
