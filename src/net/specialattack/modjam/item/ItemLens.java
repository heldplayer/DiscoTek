
package net.specialattack.modjam.item;

import java.util.List;
import java.util.Random;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import net.specialattack.modjam.Objects;
import net.specialattack.modjam.tileentity.TileEntityLight;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemLens extends Item {

    @SideOnly(Side.CLIENT)
    public Icon overlay;

    public ItemLens(int itemId) {
        super(itemId);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getColorFromItemStack(ItemStack stack, int pass) {
        if (pass == 0) {
            NBTTagCompound compound = stack.stackTagCompound;
            if (compound != null) {
                if (compound.hasKey("color")) {
                    return compound.getInteger("color");
                }
            }
        }
        return 0xFFFFFF;
    }

    @Override
    public Icon getIcon(ItemStack stack, int pass) {
        if (pass == 1) {
            return this.overlay;
        }
        return super.getIcon(stack, pass);
    }

    @Override
    @SideOnly(Side.CLIENT)
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean extra) {
        super.addInformation(stack, player, list, extra);

        if (extra) {
            NBTTagCompound compound = stack.stackTagCompound;
            if (compound != null) {
                if (compound.hasKey("color")) {
                    String color = Integer.toHexString(compound.getInteger("color")).toUpperCase();
                    while (color.length() < 6) {
                        color = "0" + color;
                    }
                    list.add("Color: #" + color);
                }
            }
        }
    }

    @Override
    public boolean onItemUse(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        //Dont need that... Silly me :p
        TileEntity te = world.getBlockTileEntity(x, y, z);
        if (te != null && te instanceof TileEntityLight) {
            TileEntityLight light = (TileEntityLight) te;
            if (light.hasLens) {
                if (!world.isRemote) {
                    ItemStack is = new ItemStack(Objects.itemLens);
                    NBTTagCompound cpnd = new NBTTagCompound("tag");
                    cpnd.setInteger("color", light.color);
                    is.setTagCompound(cpnd);

                    Random rand = new Random();
                    EntityItem ent = player.entityDropItem(is, 1.0F);
                    ent.motionY += rand.nextFloat() * 0.05F;
                    ent.motionX += (rand.nextFloat() - rand.nextFloat()) * 0.1F;
                    ent.motionZ += (rand.nextFloat() - rand.nextFloat()) * 0.1F;
                    ent.delayBeforeCanPickup = 1;
                }
            }
            NBTTagCompound compound = itemStack.stackTagCompound;
            int color = 0xFFFFFF;
            if (compound.hasKey("color")) {
                color = compound.getInteger("color");
            }
            light.color = color;
            light.hasLens = true;

            if (!player.capabilities.isCreativeMode) {
                itemStack.stackSize--;
                return true;
            }
        }

        return false;
    }

    @Override
    public int getRenderPasses(int meta) {
        return 2;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister register) {
        super.registerIcons(register);
        this.overlay = register.registerIcon(this.field_111218_cA + "_overlay");
    }

}
