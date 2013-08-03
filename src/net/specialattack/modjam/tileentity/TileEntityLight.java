
package net.specialattack.modjam.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.packet.Packet;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.specialattack.modjam.PacketHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class TileEntityLight extends TileEntity {

    public int color = 0xFFFFFF;
    public boolean hasLens = true; // Relax, don't do it
    public float pitch = 0.0F;
    public float prevPitch = 0.0F;
    public float yaw = 0.0F;
    public float prevYaw = 0.0F;
    public float brightness = 1.0F;
    public float prevBrightness = 1.0F;
    public float focus = 1.0F;
    public float prevFocus = 1.0F;

    public float motionPitch = 0.0F;
    public float motionYaw = 0.0F;
    public float motionBrightness = 0.0F;
    public float motionFocus = 0.0F;

    private boolean debug = false;

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        this.color = compound.getInteger("color");
        this.hasLens = compound.getBoolean("hasLens");
        this.prevPitch = this.pitch = compound.getFloat("pitch");
        this.prevYaw = this.yaw = compound.getFloat("yaw");
        this.prevBrightness = this.brightness = compound.getFloat("brightness");
        this.prevFocus = this.focus = compound.getFloat("focus");
    }

    @Override
    public void writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setInteger("color", this.color);
        compound.setBoolean("hasLens", this.hasLens);
        compound.setFloat("pitch", this.pitch);
        compound.setFloat("yaw", this.yaw);
        compound.setFloat("brightness", this.brightness);
        compound.setFloat("focus", this.focus);
    }

    @Override
    public Packet getDescriptionPacket() {
        return PacketHandler.createPacket(1, this);
    }

    @Override
    public void updateEntity() {
        this.prevPitch = this.pitch;
        this.prevYaw = this.yaw;
        this.prevBrightness = this.brightness;
        this.prevFocus = this.focus;

        if (debug) {
            this.pitch += 0.01F;
        }
        else {
            this.pitch -= 0.01F;
        }

        if (this.motionYaw > 1.0F) {
            this.motionYaw = 1.0F;
        }
        else if (this.motionYaw < -1.0F) {
            this.motionYaw = -1.0F;
        }

        this.pitch += this.motionPitch;
        this.yaw += this.motionYaw;
        this.brightness += this.motionBrightness;
        this.focus += this.motionFocus;

        if (this.pitch > 0.8F) {
            this.prevPitch = this.pitch = 0.8F;
            this.motionPitch = 0.0F;
            debug = false;
        }
        else if (this.pitch < -0.8F) {
            this.prevPitch = this.pitch = -0.8F;
            this.motionPitch = 0.0F;
            debug = true;
        }

        if (this.brightness > 1.0F) {
            this.brightness = 1.0F;
            this.motionBrightness = 0.0F;
        }
        else if (this.brightness < 0.0F) {
            this.brightness = 0.0F;
            this.motionBrightness = 0.0F;
        }

        if (this.focus > 20.0F) {
            this.focus = 20.0F;
            this.motionFocus = 0.0F;
        }
        else if (this.focus < 0.0F) {
            this.focus = 0.0F;
            this.motionFocus = 0.0F;
        }
    }

    public void setBrightness(float percent) {
        this.brightness = percent;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getRenderBoundingBox() {
        return super.getRenderBoundingBox().expand(10.0D, 10.0D, 10.0D);
    }

}
