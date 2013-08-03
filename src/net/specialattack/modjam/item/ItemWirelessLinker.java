
package net.specialattack.modjam.item;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;
import net.specialattack.modjam.tileentity.TileEntityController;
import net.specialattack.modjam.tileentity.TileEntityLight;

public class ItemWirelessLinker extends Item {

    public ItemWirelessLinker(int itemId) {
        super(itemId);
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
            }else {
                list.add("Light: None!");
            }
        }
    }
    
    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float posX, float posY, float posZ) {
        if (world.isRemote) {
            return true;
        }

        if (stack.stackTagCompound == null || !stack.stackTagCompound.hasKey("x") || !stack.stackTagCompound.hasKey("y") || !stack.stackTagCompound.hasKey("z")){
        
            TileEntity tile = world.getBlockTileEntity(x, y, z);
            if (tile != null && tile instanceof TileEntityLight) {
                player.addChatMessage("Created link to light (" + x + ", " + y + ", " + z + ")");
                NBTTagCompound compound = new NBTTagCompound();
                compound.setInteger("x", x);
                compound.setInteger("y", y);
                compound.setInteger("z", z);
                stack.stackTagCompound = compound;
            }
        }else{
            
            TileEntity tile = world.getBlockTileEntity(x, y, z);
            if (tile != null && tile instanceof TileEntityController) {
                NBTTagCompound compound = stack.stackTagCompound;
                int lx = compound.getInteger("x");
                        int ly = compound.getInteger("y");
                        int lz = compound.getInteger("z");
                player.addChatMessage("Added light @ (" + lx + ", " + ly + ", " + lz + ") to the controller");
                stack.stackTagCompound = null;
                
                TileEntityController controller = (TileEntityController)tile;
                controller.lightsLinked.add(new ChunkCoordinates(lx, ly, lz));
            }else{
                player.addChatMessage("Must select a controller to add this to");
            }
            
        }
        return true;
    }
}
