
package net.specialattack.modjam.tileentity;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;

public class TileEntityController extends TileEntity {

    private List<ChunkCoordinates> lightsLinked = new ArrayList<ChunkCoordinates>();
    public short[] levels = new short[512];

    public TileEntityController() {
        for (int i = 0; i < this.levels.length; i++) {
            this.levels[i] = 0x0;
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        NBTTagList lightsLinked = compound.getTagList("Lights");
        for (int i = 0; i < lightsLinked.tagCount(); i++) {
            NBTTagCompound tag = (NBTTagCompound) lightsLinked.tagAt(i);
            ChunkCoordinates coord = new ChunkCoordinates();
            coord.posX = tag.getInteger("x");
            coord.posY = tag.getInteger("y");
            coord.posZ = tag.getInteger("z");
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        NBTTagList lightsLinked = new NBTTagList();
        for (ChunkCoordinates coord : this.lightsLinked) {
            NBTTagCompound tag = new NBTTagCompound();
            tag.setInteger("x", coord.posX);
            tag.setInteger("y", coord.posY);
            tag.setInteger("z", coord.posZ);
            lightsLinked.appendTag(tag);
        }
        compound.setTag("Lights", lightsLinked);
    }

    public boolean link(ChunkCoordinates coords) {
        if (this.lightsLinked.contains(coords)) {
            return false;
        }

        double reach = 32.0D * 32.0D;
        double distance = (coords.posX - this.xCoord) * (coords.posX - this.xCoord);
        distance += (coords.posY - this.yCoord) * (coords.posY - this.yCoord);
        distance += (coords.posZ - this.zCoord) * (coords.posZ - this.zCoord);

        if (distance < reach) {
            this.lightsLinked.add(coords);

            return true;
        }

        return false;
    }

    public void setChannelLevel(int channel, short percent) {
        if (channel >= 0 && channel < 256) {
            float oldPercent = this.levels[channel];
            if (oldPercent != percent) {
                this.levels[channel] = percent;
                for (ChunkCoordinates coord : this.lightsLinked) {
                    TileEntity te = this.worldObj.getBlockTileEntity(coord.posX, coord.posY, coord.posZ);
                    if (te != null && te instanceof TileEntityLight) {
                        ((TileEntityLight) te).sendUniverseData(this.levels);
                    }
                }
            }
        }
    }

    @Override
    public void updateEntity() {
        this.setChannelLevel(0, (short) 9);
        //        for (ChunkCoordinates coord : lightsLinked){
        //            TileEntity te = worldObj.getBlockTileEntity(coord.posX, coord.posY, coord.posZ);
        //            if (te != null && te instanceof TileEntityLight){
        //                worldObj.getPlayerEntityByName("mbl111").addChatMessage(te.toString());
        //            }
        //        }
    }

}
