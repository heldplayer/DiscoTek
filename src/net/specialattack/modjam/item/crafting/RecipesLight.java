
package net.specialattack.modjam.item.crafting;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;

public class RecipesLight implements IRecipe {

    public ItemStack[] ingredients;
    public int copyIndex;

    public RecipesLight(int copyIndex, ItemStack... args) {
        this.copyIndex = copyIndex;
        this.ingredients = args;
    }

    @Override
    public boolean matches(InventoryCrafting crafting, World world) {
        for (int i = 0; i < 4; ++i) {
            for (int j = 0; j < 4; ++j) {
                if (this.checkMatch(crafting, i, j, true)) {
                    return true;
                }

                if (this.checkMatch(crafting, i, j, false)) {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean checkMatch(InventoryCrafting crafting, int x, int y, boolean mirror) {

        if (itemstack1 != null || itemstack != null) {
            if (itemstack1 == null && itemstack != null || itemstack1 != null && itemstack == null) {
                return false;
            }

            if (itemstack.itemID != itemstack1.itemID) {
                return false;
            }

            if (itemstack.getItemDamage() != 32767 && itemstack.getItemDamage() != itemstack1.getItemDamage()) {
                return false;
            }
        }

        return true;
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting crafting) {
        return null;
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
