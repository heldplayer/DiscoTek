
package net.specialattack.discotek.item.crafting;

import java.util.List;

import me.heldplayer.util.HeldCore.crafting.ICraftingResultHandler;
import me.heldplayer.util.HeldCore.crafting.IHeldCoreRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.specialattack.discotek.Objects;

public class LightCraftingHandler implements ICraftingResultHandler {

    @Override
    public ItemStack getOutput(IHeldCoreRecipe recipe, List<ItemStack> input) {
        ItemStack result = recipe.getOutput();

        for (ItemStack stack : input) {
            if (stack != null && stack.getItem() == Objects.itemLens) {
                if (stack.stackTagCompound != null) {
                    result.stackTagCompound = stack.stackTagCompound;
                    break;
                }
            }
        }

        if (result.stackTagCompound == null) {
            result.stackTagCompound = new NBTTagCompound("tag");
            result.stackTagCompound.setInteger("color", 0xFFFFFF);
        }

        return result;
    }

    @Override
    public String getOwningModName() {
        return Objects.MOD_NAME;
    }

    @Override
    public String getOwningModId() {
        return Objects.MOD_ID;
    }

}
