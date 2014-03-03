
package net.specialattack.forge.discotek.item;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.specialattack.forge.discotek.tileentity.TileEntityController;
import net.specialattack.forge.discotek.tileentity.TileEntityLight;
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

        NBTTagCompound compound = stack.stackTagCompound;

        boolean written = false;

        if (compound != null) {
            if (compound.hasKey("lights", 9)) {
                NBTTagList lights = compound.getTagList("lights", 10);
                for (int i = 0; i < lights.tagCount(); i++) {
                    NBTTagCompound light = lights.getCompoundTagAt(i);

                    if (light.hasKey("x") && light.hasKey("y") && light.hasKey("z")) {
                        list.add(StatCollector.translateToLocalFormatted("item.wirelesslinker.linked", light.getInteger("x"), light.getInteger("y"), light.getInteger("z")));
                        written = true;
                    }
                }
            }
        }

        if (!written) {
            list.add(StatCollector.translateToLocal("item.wirelesslinker.unlinked"));
        }
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float posX, float posY, float posZ) {
        TileEntity tile = world.getTileEntity(x, y, z);

        if (tile != null && tile instanceof TileEntityLight) {
            if (world.isRemote) {
                return true;
            }

            NBTTagCompound compound = stack.stackTagCompound;
            if (compound == null) {
                compound = stack.stackTagCompound = new NBTTagCompound();
            }

            NBTTagList lights = null;
            if (compound.hasKey("lights", 9)) {
                lights = compound.getTagList("lights", 10);
            }
            else {
                lights = new NBTTagList();
                compound.setTag("lights", lights);
            }

            for (int i = 0; i < lights.tagCount(); i++) {
                NBTTagCompound light = lights.getCompoundTagAt(i);

                if (light.hasKey("x") && light.hasKey("y") && light.hasKey("z")) {
                    if (light.getInteger("x") == x && light.getInteger("y") == y && light.getInteger("z") == z) {
                        player.addChatMessage(new ChatComponentText("This light is already linked"));

                        return true;
                    }
                }
            }
            NBTTagCompound light = new NBTTagCompound();
            light.setInteger("x", x);
            light.setInteger("y", y);
            light.setInteger("z", z);
            lights.appendTag(light);

            player.addChatMessage(new ChatComponentText("Created link to light (" + x + ", " + y + ", " + z + ")"));

            return true;
        }
        else if (tile != null && tile instanceof TileEntityController) {
            if (world.isRemote) {
                return true;
            }

            if (stack.stackTagCompound == null || !stack.stackTagCompound.hasKey("lights", 9)) {
                player.addChatMessage(new ChatComponentText("No lights have been selected yet"));
            }
            else {
                TileEntityController controller = (TileEntityController) tile;

                NBTTagCompound compound = stack.stackTagCompound;

                if (compound != null) {
                    if (compound.hasKey("lights", 9)) {
                        NBTTagList lights = compound.getTagList("lights", 10);
                        for (int i = 0; i < lights.tagCount(); i++) {
                            NBTTagCompound light = lights.getCompoundTagAt(i);

                            if (light.hasKey("x") && light.hasKey("y") && light.hasKey("z")) {
                                int lx = light.getInteger("x");
                                int ly = light.getInteger("y");
                                int lz = light.getInteger("z");

                                TileEntity tileEntity = world.getTileEntity(lx, ly, lz);

                                if (tileEntity != null && tileEntity instanceof TileEntityLight) {
                                    TileEntityLight lightTile = (TileEntityLight) tileEntity;

                                    if (controller.link(lightTile)) {
                                        player.addChatMessage(new ChatComponentText("Added light @ (" + lx + ", " + ly + ", " + lz + ") to the controller"));
                                    }
                                    else {
                                        player.addChatMessage(new ChatComponentText("Link failed @ (" + lx + ", " + ly + ", " + lz + ")"));
                                    }
                                }
                                else {
                                    player.addChatMessage(new ChatComponentText("No light @ (" + lx + ", " + ly + ", " + lz + ")"));
                                }

                            }
                        }
                    }
                }

                stack.stackTagCompound = null;
            }

            return true;
        }

        return false;
    }
}
