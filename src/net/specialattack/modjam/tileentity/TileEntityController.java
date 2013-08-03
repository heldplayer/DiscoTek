
package net.specialattack.modjam.tileentity;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.tileentity.TileEntity;

public class TileEntityController extends TileEntity {

    public List<LightRecord>[] lightsOnChannel = new List[512];
    public float[] brightnessPercent = new float[512];

    public TileEntityController() {
        for (int i = 0; i < this.lightsOnChannel.length; i++) {
            this.lightsOnChannel[i] = new ArrayList<LightRecord>();
        }

        for (int i = 0; i < this.brightnessPercent.length; i++) {
            this.brightnessPercent[i] = 0.0f;
        }
    }

    public void setChannelLevel(int channel, float percent) {
        if (channel >= 0 || channel < 256) {
            float oldPercent = this.brightnessPercent[channel];
            if (oldPercent != percent) {
                this.brightnessPercent[channel] = percent;
                for (LightRecord record : this.lightsOnChannel[channel]) {
                    record.dim(percent);
                }
            }
        }
    }

    @Override
    public void updateEntity() {
        this.setChannelLevel(0, 0.5f);
    }

    public class LightRecord {

        public int x, y, z;

        public LightRecord(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public boolean dim(float percent) {
            TileEntity te = TileEntityController.this.worldObj.getBlockTileEntity(this.x, this.y, this.z);
            if (te instanceof TileEntityLight) {
                ((TileEntityLight) te).setBrightness(percent);
            }
            return false;
        }
    }

}
