
package net.specialattack.forge.discotek.item;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import net.specialattack.forge.discotek.tileentity.TileEntityLight;

public class ItemDebug extends Item {

    public ItemDebug() {
        super();
        this.setFull3D();
        this.setMaxStackSize(1);
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float posX, float posY, float posZ) {
        if (world.isRemote) {
            return true;
        }

        Block block = world.getBlock(x, y, z);
        ItemStack itemStack = block.getPickBlock(Minecraft.getMinecraft().objectMouseOver, world, x, y, z);

        player.addChatMessage(new ChatComponentText("Block clicked: ").appendSibling(itemStack.func_151000_E()));

        TileEntity tile = world.getTileEntity(x, y, z);
        if (tile != null) {
            player.addChatMessage(new ChatComponentText(tile.toString()));
            if (tile instanceof TileEntityLight) {
                player.addChatMessage(new ChatComponentText("Level: " + ((TileEntityLight) tile).getBrightness(1.0F)));
                String color = Integer.toHexString(((TileEntityLight) tile).getColor(1.0F)).toUpperCase();
                while (color.length() < 6) {
                    color = "0" + color;
                }
                player.addChatMessage(new ChatComponentText("Color: " + color));
            }
        }

        return true;
    }

}
