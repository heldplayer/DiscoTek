package net.specialattack.forge.discotek.item.crafting;

import java.util.ArrayList;
import net.minecraft.block.BlockColored;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.specialattack.forge.discotek.Objects;

public class RecipesColoredLamp implements IRecipe {

    public static float[][] dyeColors = new float[][] { { 1.0F, 1.0F, 1.0F }, { 1.0F, 0.5F, 0.0F }, { 0.7F, 0.3F, 0.7F }, { 0.5F, 0.5F, 1.0F }, { 1.0F, 1.0F, 0.0F }, { 0.0F, 1.0F, 0.0F }, { 1.0F, 0.5F, 0.5F }, { 0.3F, 0.3F, 0.3F }, { 0.6F, 0.6F, 0.6F }, { 0.0F, 0.7F, 0.7F }, { 0.7F, 0.0F, 0.7F }, { 0.0F, 0.0F, 1.0F }, { 0.4F, 0.3F, 0.2F }, { 0.0F, 0.5F, 0.0F }, { 1.0F, 0.0F, 0.0F }, { 0.1F, 0.1F, 0.1F } };

    @Override
    public boolean matches(InventoryCrafting crafting, World world) {
        ItemStack lights = null;
        ArrayList<ItemStack> dyes = new ArrayList<ItemStack>();

        for (int i = 0; i < crafting.getSizeInventory(); ++i) {
            ItemStack stack = crafting.getStackInSlot(i);

            if (stack != null && stack.getItem() != null) {
                if (stack.getItem() == Item.getItemFromBlock(Blocks.redstone_lamp) || stack.getItem() == Item.getItemFromBlock(Objects.blockColoredLamp)) {
                    if (lights == null) {
                        lights = stack.copy();
                    } else {
                        if (lights.getItem() != stack.getItem() || !ItemStack.areItemStackTagsEqual(lights, stack)) {
                            return false;
                        }
                    }
                } else if (stack.getItem() == Items.dye) {
                    dyes.add(stack);
                } else {
                    return false;
                }
            }
        }

        return lights != null && !dyes.isEmpty();
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting crafting) {
        ItemStack lights = null;
        int[] colors = new int[3];
        int totalHue = 0;
        int dyeCount = 0;

        for (int i = 0; i < crafting.getSizeInventory(); i++) {
            ItemStack stack = crafting.getStackInSlot(i);

            if (stack != null) {
                if (stack.getItem() == Item.getItemFromBlock(Blocks.redstone_lamp) || stack.getItem() == Item.getItemFromBlock(Objects.blockColoredLamp)) {
                    if (lights == null) {
                        lights = stack.copy();
                        lights.stackSize = 1;
                    } else {
                        if (lights.getItem() != stack.getItem() || !ItemStack.areItemStackTagsEqual(lights, stack)) {
                            return null;
                        }
                        lights.stackSize++;
                    }

                    NBTTagCompound compound = lights.stackTagCompound;

                    if (compound != null && compound.hasKey("color")) {
                        int color = compound.getInteger("color");
                        float red = (color >> 16 & 255) / 255.0F;
                        float green = (color >> 8 & 255) / 255.0F;
                        float blue = (color & 255) / 255.0F;
                        totalHue = (int) (totalHue + Math.max(red, Math.max(green, blue)) * 255.0F);
                        colors[0] = (int) (colors[0] + red * 255.0F);
                        colors[1] = (int) (colors[1] + green * 255.0F);
                        colors[2] = (int) (colors[2] + blue * 255.0F);
                        dyeCount++;
                    }
                } else if (stack.getItem() == Items.dye) {
                    float[] dyeColors = RecipesColoredLamp.dyeColors[BlockColored.func_150032_b(stack.getItemDamage())];
                    int red = (int) (dyeColors[0] * 255.0F);
                    int green = (int) (dyeColors[1] * 255.0F);
                    int blue = (int) (dyeColors[2] * 255.0F);
                    totalHue += Math.max(red, Math.max(green, blue));
                    colors[0] += red;
                    colors[1] += green;
                    colors[2] += blue;
                    dyeCount++;
                } else {
                    return null;
                }
            }
        }

        if (lights == null) {
            return null;
        } else {
            int red = colors[0] / dyeCount;
            int green = colors[1] / dyeCount;
            int blue = colors[2] / dyeCount;
            float hue = (float) totalHue / (float) dyeCount;
            float correctedHue = Math.max(red, Math.max(green, blue));
            red = (int) (red * hue / correctedHue);
            green = (int) (green * hue / correctedHue);
            blue = (int) (blue * hue / correctedHue);
            int color = (red << 16) | (green << 8) | blue;

            ItemStack result = new ItemStack(Objects.blockColoredLamp);
            result.stackSize = lights.stackSize;
            result.stackTagCompound = lights.stackTagCompound;

            NBTTagCompound compound = result.stackTagCompound;
            if (compound == null) {
                compound = new NBTTagCompound();
                result.stackTagCompound = compound;
            }
            compound.setInteger("color", color);

            return result;
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
