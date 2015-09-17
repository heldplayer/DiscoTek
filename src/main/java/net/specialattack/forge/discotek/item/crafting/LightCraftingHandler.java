package net.specialattack.forge.discotek.item.crafting;

import java.util.List;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.specialattack.forge.core.crafting.ICraftingResultHandler;
import net.specialattack.forge.core.crafting.ISpACoreRecipe;
import net.specialattack.forge.discotek.Objects;

public class LightCraftingHandler implements ICraftingResultHandler {

    @Override
    public ItemStack getOutput(ISpACoreRecipe recipe, List<ItemStack> input) {
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
            result.stackTagCompound = new NBTTagCompound();
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

    @Override
    public boolean isValidRecipeInput(ItemStack input) {
        return true; // Ignore, because there is no special casing here
    }

    @Override
    public String getNEIOverlayText() {
        return null;
    }
}
