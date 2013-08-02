
package net.specialattack.modjam.item.crafting;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class RecipesLight implements IRecipe {

    public ItemStack result;
    public ItemStack[] ingredients;
    public int copyIndex;

    public RecipesLight(ItemStack result, int copyIndex, ItemStack[] args) {
        this.copyIndex = copyIndex;
        this.ingredients = args;
        this.result = result;
    }

    @Override
    public boolean matches(InventoryCrafting crafting, World world) {
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                boolean noMatch = true;
                if (this.checkMatch(crafting, i, j, true)) {
                    noMatch = false;
                }

                if (this.checkMatch(crafting, i, j, false)) {
                    noMatch = false;
                }

                if (noMatch) {
                    return false;
                }
            }
        }

        return true;
    }

    private boolean checkMatch(InventoryCrafting crafting, int x, int y, boolean mirror) {
        if (mirror) {
            x = 2 - x;
        }

        ItemStack ingredient = this.ingredients[x + y * 3];
        ItemStack item = crafting.getStackInSlot(x + y * 3);

        if (item != null && ingredient != null) {
            if (ingredient.itemID != item.itemID) {
                return false;
            }

            if (ingredient.getItemDamage() != 32767 && ingredient.getItemDamage() != item.getItemDamage()) {
                return false;
            }

            return true;
        }
        else if (item == null && ingredient == null) {
            return true;
        }

        return false;
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting crafting) {
        ItemStack result = this.result.copy();

        ItemStack parent = crafting.getStackInSlot(copyIndex);

        if (parent != null && parent.stackTagCompound != null) {
            result.stackTagCompound = (NBTTagCompound) parent.stackTagCompound.copy();
        }

        return result;
    }

    @Override
    public int getRecipeSize() {
        return this.ingredients.length;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return null;
    }

}
