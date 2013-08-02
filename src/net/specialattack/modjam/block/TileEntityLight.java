
package net.specialattack.modjam.block;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;

public class TileEntityLight extends TileEntity {

    public int color = 0xFFFFFFFF;
    public float pitch = 0.0F;
    public float prevPitch = 0.0F;
    public float yaw = 0.0F;
    public float prevYaw = 0.0F;
    public ForgeDirection direction = ForgeDirection.UNKNOWN;

    private boolean debug = false;

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        this.color = compound.getInteger("color");
        this.pitch = compound.getFloat("pitch");
        this.yaw = compound.getFloat("yaw");
        this.direction = ForgeDirection.getOrientation(compound.getInteger("direction"));
    }

    @Override
    public void writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setInteger("color", this.color);
        compound.setFloat("pitch", this.pitch);
        compound.setInteger("direction", this.direction.ordinal());
    }

    public boolean hasGel() {
        return !(this.color == 0xFFFFFFFF);
    }

    @Override
    public void updateEntity() {
        this.prevPitch = this.pitch;
        this.prevYaw = this.yaw;

        this.yaw += 0.05F;

        if (this.debug) {
            this.pitch -= 0.1F;
        }
        else {
            this.pitch += 0.05F;
        }

        if (this.pitch > 0.6F) {
            this.debug = true;
        }
        else if (this.pitch < -0.6F) {
            this.debug = false;
        }
    }

}
