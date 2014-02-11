
package net.specialattack.discotek.item;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import net.specialattack.discotek.tileentity.TileEntityController;
import net.specialattack.discotek.tileentity.TileEntityLight;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemWirelessLinker extends Item {

    public ItemWirelessLinker() {
        super();
        this.setFull3D();
        this.setMaxStackSize(1);
    }

    @Override
    @SideOnly(Side.CLIENT)
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean extra) {
        super.addInformation(stack, player, list, extra);

        if (extra) {
            NBTTagCompound compound = stack.stackTagCompound;
            if (compound != null) {
                if (compound.hasKey("x") && compound.hasKey("y") && compound.hasKey("z")) {
                    list.add("Light: (" + compound.getInteger("x") + ", " + compound.getInteger("y") + ", " + compound.getInteger("z") + ")");
                }
            }
            else {
                list.add("Light: None!");
            }
        }
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float posX, float posY, float posZ) {
        TileEntity tile = world.getTileEntity(x, y, z);

        if (tile != null && tile instanceof TileEntityLight) {
            if (world.isRemote) {
                return true;
            }

            player.addChatMessage(new ChatComponentText("Created link to light (" + x + ", " + y + ", " + z + ")"));

            NBTTagCompound compound = new NBTTagCompound();
            compound.setInteger("x", x);
            compound.setInteger("y", y);
            compound.setInteger("z", z);

            stack.stackTagCompound = compound;

            return true;
        }
        else if (tile != null && tile instanceof TileEntityController) {
            if (world.isRemote) {
                return true;
            }

            if (stack.stackTagCompound == null || !stack.stackTagCompound.hasKey("x") || !stack.stackTagCompound.hasKey("y") || !stack.stackTagCompound.hasKey("z")) {
                player.addChatMessage(new ChatComponentText("No light has been selected yet"));
            }
            else {
                TileEntityController controller = (TileEntityController) tile;

                NBTTagCompound compound = stack.stackTagCompound;

                int lx = compound.getInteger("x");
                int ly = compound.getInteger("y");
                int lz = compound.getInteger("z");

                TileEntity lightTile = world.getTileEntity(lx, ly, lz);

                if (lightTile != null && lightTile instanceof TileEntityLight) {
                    TileEntityLight light = (TileEntityLight) lightTile;

                    if (controller.link(light)) {
                        player.addChatMessage(new ChatComponentText("Added light @ (" + lx + ", " + ly + ", " + lz + ") to the controller"));
                    }
                    else {
                        player.addChatMessage(new ChatComponentText("Link failed"));
                    }
                }
                else {
                    player.addChatMessage(new ChatComponentText("No light at linked location"));
                }

                stack.stackTagCompound = null;
            }

            return true;
        }

        return false;
    }
}
