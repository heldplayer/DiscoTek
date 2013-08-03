
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
    public int[] channels;

    private int ticksRemaining = 100;
    private boolean[] needsUpdate = new boolean[10];

    public int getColor() {
        return this.color;
    }

    public void setColor(int color) {
        this.color = color;
        this.needsUpdate[0] = true;
    }

    public boolean hasLens() {
        return this.hasLens;
    }

    public void setHasLens(boolean hasLens) {
        this.hasLens = hasLens;
        this.needsUpdate[1] = true;
    }

    public float getPitch(float partialTicks) {
        return this.pitch + (this.pitch - this.prevPitch) * partialTicks;
    }

    public void setPitch(float pitch) {
        this.prevPitch = this.pitch = pitch;
        this.needsUpdate[2] = true;
    }

    public float getYaw(float partialTicks) {
        return this.yaw + (this.yaw - this.prevYaw) * partialTicks;
    }

    public void setYaw(float yaw) {
        this.prevYaw = this.yaw = yaw;
        this.needsUpdate[3] = true;
    }

    public float getBrightness(float partialTicks) {
        return this.brightness + (this.brightness - this.prevBrightness) * partialTicks;
    }

    public void setBrightness(float brightness) {
        this.prevBrightness = this.brightness = brightness;
        this.needsUpdate[4] = true;
    }

    public float getFocus(float partialTicks) {
        return this.focus + (this.focus - this.prevFocus) * partialTicks;
    }

    public void setFocus(float focus) {
        this.prevFocus = this.focus = focus;
        this.needsUpdate[5] = true;
    }

    public float getValue(int index) {
        switch (index) {
        case 2:
            return this.brightness;
        case 3:
            return this.pitch;
        case 4:
            return this.yaw;
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
            this.brightness = value;
        break;
        case 3:
            this.pitch = value;
        break;
        case 4:
            this.yaw = value;
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

    public boolean setValue(int index, short value) {
        switch (index) {
        case 2:
            float prev = this.brightness;
            this.brightness = (float) value / 255.0F;
            return prev != this.brightness;
        case 3:
            prev = this.pitch;
            this.pitch = (float) value * 1.6F / 255.0F - 0.8F;
            return prev != this.pitch;
        case 4:
            prev = this.yaw;
            this.yaw = (float) value * 6.28318530718F / 255.0F; // 2 Pi Radians
            return prev != this.yaw;
        case 5:
            prev = this.focus;
            this.focus = (float) value * 20F / 255.0F;
            return prev != this.focus;
        case 6:
            prev = this.motionPitch;
            this.motionPitch = (float) value / 255.0F - 0.5F;
            return prev != this.motionPitch;
        case 7:
            prev = this.motionYaw;
            this.motionYaw = (float) value / 255.0F - 0.5F;
            return prev != this.motionYaw;
        case 8:
            prev = this.motionBrightness;
            this.motionBrightness = (float) value / 255.0F - 0.5F;
            return prev != this.motionBrightness;
        case 9:
            prev = this.motionFocus;
            this.motionFocus = (float) value / 255.0F - 0.5F;
            return prev != this.motionFocus;
        }

        return false;
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
        this.channels = compound.getIntArray("channels");
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
        compound.setIntArray("channels", this.channels);
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
        }
        else if (this.pitch < -0.8F) {
            this.prevPitch = this.pitch = -0.8F;
            this.motionPitch = 0.0F;
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

        if (!this.worldObj.isRemote) {
            int size = 0;
            switch (this.getBlockMetadata()) {
            case 0:
                size = 1;
            break;
            }
            if (this.channels == null || this.channels.length != size) {
                this.channels = new int[size];
            }

            int count = 0;
            for (int i = 0; i < this.needsUpdate.length; i++) {
                if (this.needsUpdate[i]) {
                    count++;
                }
            }
            if (count > 0) {
                int[] ints = new int[count];
                int j = 0;
                for (int i = 0; i < this.needsUpdate.length; i++) {
                    ints[j] = i;
                }
                this.sync(ints);
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
        for (int i = 0; this.channels != null && i < this.channels.length; i++) {
            if (this.setValue(i + 2, levels[this.channels[i]])) {
                this.sync(i + 2);
            }
        }
    }

}
