
package net.specialattack.modjam.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.packet.Packet;
import net.minecraft.tileentity.TileEntity;
import net.specialattack.modjam.PacketHandler;

public class TileEntityLight extends TileEntity {

    public int color = 0xFFFFFF;
    public float pitch = 0.0F;
    public float prevPitch = 0.0F;
    public float yaw = 0.0F;
    public float prevYaw = 0.0F;
    public float brightness = 0.0F;

    public float motionPitch = 0.0F;
    public float motionYaw = 0.0F;

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        this.color = compound.getInteger("color");
        this.pitch = compound.getFloat("pitch");
        this.yaw = compound.getFloat("yaw");
    }

    @Override
    public void writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setInteger("color", this.color);
        compound.setFloat("pitch", this.pitch);
        compound.setFloat("yaw", this.yaw);
    }

    @Override
    public Packet getDescriptionPacket() {
        return PacketHandler.createPacket(1, this);
    }

    @Override
    public void updateEntity() {
        this.prevPitch = this.pitch;
        this.prevYaw = this.yaw;

        this.pitch += this.motionPitch;
        this.yaw += this.motionYaw;

        if (this.pitch > 0.8F) {
            this.prevPitch = this.pitch = 0.8F;
        }
        else if (this.pitch < -0.8F) {
            this.prevPitch = this.pitch = -0.8F;
        }
        
//        brightness += 0.01f;
//        if (brightness > 1f){
//            brightness = 0f;
//        }
        brightness = 1.0f;
    }

    public boolean hasGel() {
        return !(color == 0xFFFFFF);
    }

    public void setBrightness(float percent) {
        this.brightness = percent;
    }

}
