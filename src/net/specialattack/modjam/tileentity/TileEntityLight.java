
package net.specialattack.modjam.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.specialattack.modjam.PacketHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class TileEntityLight extends TileEntity {

    private int color = 0xFFFFFF;
    private boolean hasLens = true; // Relax, don't do it
    private float pitch = 0.0F;
    private float prevPitch = 0.0F;
    private float yaw = 0.0F;
    private float prevYaw = 0.0F;
    private float brightness = 1.0F;
    private float prevBrightness = 1.0F;
    private float focus = 1.0F;
    private float prevFocus = 1.0F;

    private float motionPitch = 0.0F;
    private float motionYaw = 0.0F;
    private float motionBrightness = 0.0F;
    private float motionFocus = 0.0F;
    //Channels 1 - 512 (0 - 511)
    public int channel = 0;
    public static final int numChannels = 1;

    private int ticksRemaining = 100;

    private boolean debug = false;

    public int getColor() {
        return this.color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public boolean hasLens() {
        return this.hasLens;
    }

    public void setHasLens(boolean hasLens) {
        this.hasLens = hasLens;
    }

    public float getPitch(float partialTicks) {
        return this.pitch + (this.pitch - this.prevPitch) * partialTicks;
    }

    public void setPitch(float pitch) {
        this.prevPitch = this.pitch = pitch;
    }

    public float getYaw(float partialTicks) {
        return this.yaw + (this.yaw - this.prevYaw) * partialTicks;
    }

    public void setYaw(float yaw) {
        this.prevYaw = this.yaw = yaw;
    }

    public float getBrightness(float partialTicks) {
        return this.brightness + (this.brightness - this.prevBrightness) * partialTicks;
    }

    public void setBrightness(float brightness) {
        this.prevBrightness = this.brightness = brightness;
    }

    public float getFocus(float partialTicks) {
        return this.focus + (this.focus - this.prevFocus) * partialTicks;
    }

    public void setFocus(float focus) {
        this.prevFocus = this.focus = focus;
    }

    public float getValue(int index) {
        switch (index) {
        case 2:
            return this.pitch;
        case 3:
            return this.yaw;
        case 4:
            return this.brightness;
        case 5:
            return this.focus;
        case 6:
            return this.motionPitch;
        case 7:
            return this.motionYaw;
        case 8:
            return this.motionBrightness;
        case 9:
            return this.motionFocus;
        default:
            return 0.0F;
        }
    }

    public void setValue(int index, float value) {
        switch (index) {
        case 2:
            this.pitch = value;
        break;
        case 3:
            this.yaw = value;
        break;
        case 4:
            this.brightness = value;
        break;
        case 5:
            this.focus = value;
        break;
        case 6:
            this.motionPitch = value;
        break;
        case 7:
            this.motionYaw = value;
        break;
        case 8:
            this.motionBrightness = value;
        break;
        case 9:
            this.motionFocus = value;
        break;
        }
    }

    public void sync(int... values) {
        Packet250CustomPayload packet = PacketHandler.createPacket(2, this, values);
        PacketHandler.sendPacketToPlayersWatchingBlock(packet, this.worldObj, this.xCoord, this.zCoord);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        this.color = compound.getInteger("color");
        this.hasLens = compound.getBoolean("hasLens");
        this.prevPitch = this.pitch = compound.getFloat("pitch");
        this.prevYaw = this.yaw = compound.getFloat("yaw");
        this.prevBrightness = this.brightness = compound.getFloat("brightness");
        this.prevFocus = this.focus = compound.getFloat("focus");
        this.channel = compound.getInteger("channel");
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
        compound.setInteger("channel", this.channel);
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
            this.debug = false;
        }
        else if (this.pitch < -0.8F) {
            this.prevPitch = this.pitch = -0.8F;
            this.motionPitch = 0.0F;
            this.debug = true;
        }

        //        if (this.brightness > 1.0F) {
        //            this.brightness = 1.0F;
        //            this.motionBrightness = 0.0F;
        //        }
        //        else if (this.brightness < 0.0F) {
        //            this.brightness = 0.0F;
        //            this.motionBrightness = 0.0F;
        //        }
        if (this.focus > 20.0F) {
            this.focus = 20.0F;
            this.motionFocus = 0.0F;
        }
        else if (this.focus < 0.0F) {
            this.focus = 0.0F;
            this.motionFocus = 0.0F;
        }

        if (!this.worldObj.isRemote) {
            if (this.debug) {
                this.motionPitch = 0.01F;
            }
            else {
                this.motionPitch = -0.01F;
            }
            this.ticksRemaining--;
            if (this.ticksRemaining <= 0) {
                this.ticksRemaining = 100;
                this.sync(2, 3, 4, 5, 6, 7, 8, 9);
            }
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getRenderBoundingBox() {
        return super.getRenderBoundingBox().expand(10.0D, 10.0D, 10.0D);
    }

    public void sendUniverseData(short[] levels) {
        this.setValue(4, (float) (levels[channel] / 255.0f));
        this.sync(4);
    }

}
