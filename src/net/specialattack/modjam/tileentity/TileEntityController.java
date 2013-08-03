
package net.specialattack.modjam.tileentity;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;

public class TileEntityController extends TileEntity {

    public List<ChunkCoordinates> lightsLinked = new ArrayList<ChunkCoordinates>();
    public short[] levels = new short[512];

    public TileEntityController() {
        for (int i = 0; i < this.levels.length; i++) {
            this.levels[i] = 0x0;
        }
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
