
package net.specialattack.discotek.item.crafting;

import java.util.ArrayList;

import net.minecraft.block.BlockColored;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.specialattack.discotek.item.ItemLens;

public class RecipesLens implements IRecipe {

    public static float[][] dyeColors = new float[][] { { 1.0F, 1.0F, 1.0F }, { 1.0F, 0.5F, 0.0F }, { 0.7F, 0.3F, 0.7F }, { 0.5F, 0.5F, 1.0F }, { 1.0F, 1.0F, 0.0F }, { 0.0F, 1.0F, 0.0F }, { 1.0F, 0.5F, 0.5F }, { 0.3F, 0.3F, 0.3F }, { 0.6F, 0.6F, 0.6F }, { 0.0F, 0.7F, 0.7F }, { 0.7F, 0.0F, 0.7F }, { 0.0F, 0.0F, 1.0F }, { 0.4F, 0.3F, 0.2F }, { 0.0F, 0.5F, 0.0F }, { 1.0F, 0.0F, 0.0F }, { 0.1F, 0.1F, 0.1F } };

    @Override
    public boolean matches(InventoryCrafting crafting, World world) {
        ItemStack lens = null;
        ArrayList<ItemStack> dyes = new ArrayList<ItemStack>();

        for (int i = 0; i < crafting.getSizeInventory(); ++i) {
            ItemStack stack = crafting.getStackInSlot(i);

            if (stack != null && stack.getItem() != null) {
                if (stack.getItem() instanceof ItemLens) {
                    if (lens != null) {
                        return false;
                    }
                    lens = stack;
                }
                else if (stack.itemID == Item.dyePowder.itemID) {
                    dyes.add(stack);
                }
                else {
                    return false;
                }
            }
        }

        return lens != null && !dyes.isEmpty();
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting crafting) {
        ItemStack lens = null;
        int[] colors = new int[3];
        int totalHue = 0;
        int dyeCount = 0;

        for (int i = 0; i < crafting.getSizeInventory(); i++) {
            ItemStack stack = crafting.getStackInSlot(i);

            if (stack != null) {
                if (stack.getItem() instanceof ItemLens) {
                    if (lens != null) {
                        return null;
                    }
                    lens = stack.copy();
                    lens.stackSize = 1;

                    NBTTagCompound compound = lens.stackTagCompound;

                    if (compound != null && compound.hasKey("color")) {
                        int color = compound.getInteger("color");
                        float red = (float) (color >> 16 & 255) / 255.0F;
                        float green = (float) (color >> 8 & 255) / 255.0F;
                        float blue = (float) (color & 255) / 255.0F;
                        totalHue = (int) ((float) totalHue + Math.max(red, Math.max(green, blue)) * 255.0F);
                        colors[0] = (int) ((float) colors[0] + red * 255.0F);
                        colors[1] = (int) ((float) colors[1] + green * 255.0F);
                        colors[2] = (int) ((float) colors[2] + blue * 255.0F);
                        dyeCount++;
                    }
                }
                else if (stack.itemID == Item.dyePowder.itemID) {
                    float[] dyeColors = RecipesLens.dyeColors[BlockColored.getBlockFromDye(stack.getItemDamage())];
                    int red = (int) (dyeColors[0] * 255.0F);
                    int green = (int) (dyeColors[1] * 255.0F);
                    int blue = (int) (dyeColors[2] * 255.0F);
                    totalHue += Math.max(red, Math.max(green, blue));
                    colors[0] += red;
                    colors[1] += green;
                    colors[2] += blue;
                    dyeCount++;
                }
                else {
                    return null;
                }
            }
        }

        if (lens == null) {
            return null;
        }
        else {
            int red = colors[0] / dyeCount;
            int green = colors[1] / dyeCount;
            int blue = colors[2] / dyeCount;
            float hue = (float) totalHue / (float) dyeCount;
            float correctedHue = (float) Math.max(red, Math.max(green, blue));
            red = (int) ((float) red * hue / correctedHue);
            green = (int) ((float) green * hue / correctedHue);
            blue = (int) ((float) blue * hue / correctedHue);
            int color = (red << 16) | (green << 8) | blue;

            NBTTagCompound compound = lens.stackTagCompound;
            if (compound == null) {
                compound = new NBTTagCompound("tag");
                lens.stackTagCompound = compound;
            }
            compound.setInteger("color", color);

            return lens;
        }
    }

    @Override
    public int getRecipeSize() {
        return 10;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return null;
    }

}
