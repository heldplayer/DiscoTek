
package net.specialattack.discotek.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.specialattack.discotek.Config;
import net.specialattack.discotek.PacketHandler;
import net.specialattack.discotek.client.ClientProxy;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class TileEntityLight extends TileEntity {

    private int color = 0xFFFFFF;
    private int prevColor = 0xFFFFFF;
    private boolean hasLens = true; // Relax, don't do it
    private float pitch = 0.0F;
    private float prevPitch = 0.0F;
    private float yaw = 0.0F;
    private float prevYaw = 0.0F;
    private float brightness = 1.0F;
    private float prevBrightness = 1.0F;
    private float focus = 1.0F;
    private float prevFocus = 1.0F;
    private float motionYaw = 0.0f;
    private float motionPitch = 0.0f;

    //Channels 1 - 512 (0 - 511)
    public int[] channels;
    private int[] cachedLevels = new int[256];

    private int ticksRemaining = 100;
    private boolean[] needsUpdate = new boolean[9];

    private int direction = 0;

    public void setDirection(int side) {
        this.direction = side;
    }

    @Override
    public void validate() {
        super.validate();

        if (this.worldObj != null && this.worldObj.isRemote) {
            ClientProxy.addTile(this);
        }
    }

    @Override
    public void invalidate() {
        super.invalidate();

        if (this.worldObj != null && this.worldObj.isRemote) {
            ClientProxy.addTile(this);
        }
    }

    public int getDirection() {
        return this.direction;
    }

    public int getColor(float partialTicks) {
        int red = (int) (((this.prevColor >> 16) & 0xFF) + (((this.color >> 16) & 0xFF) - ((this.prevColor >> 16) & 0xFF)) * partialTicks);
        int green = (int) (((this.prevColor >> 8) & 0xFF) + (((this.color >> 8) & 0xFF) - ((this.prevColor >> 8) & 0xFF)) * partialTicks);
        int blue = (int) ((this.prevColor & 0xFF) + ((this.color & 0xFF) - (this.prevColor & 0xFF)) * partialTicks);
        return red << 16 | green << 8 | blue;
    }

    public void setColor(int color) {
        this.prevColor = this.color = color;
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
        return this.prevPitch + (this.pitch - this.prevPitch) * partialTicks;
    }

    public void setPitch(float pitch) {
        this.prevPitch = this.pitch = pitch;
        this.needsUpdate[3] = true;
    }

    public float getYaw(float partialTicks) {
        return this.prevYaw + (this.yaw - this.prevYaw) * partialTicks;
    }

    public void setYaw(float yaw) {
        this.prevYaw = this.yaw = yaw;
        this.needsUpdate[4] = true;
    }

    public float getBrightness(float partialTicks) {
        return this.prevBrightness + (this.brightness - this.prevBrightness) * partialTicks;
    }

    public void setBrightness(float brightness) {
        this.prevBrightness = this.brightness = brightness;
        this.needsUpdate[2] = true;
    }

    public float getFocus(float partialTicks) {
        return this.prevFocus + (this.focus - this.prevFocus) * partialTicks;
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
        case 6: // Red
            return (float) ((this.color & 0xFF0000) >> 16) / 255.0F;
        case 7: // Green
            return (float) ((this.color & 0x00FF00) >> 8) / 255.0F;
        case 8: // Blue
            return (float) (this.color & 0x0000FF) / 255.0F;
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
        case 6: // Red
            this.color = (this.color & 0x00FFFF) | (((int) (value * 255.0F) << 16) & 0xFF0000);
        break;
        case 7: // Green
            this.color = (this.color & 0xFF00FF) | (((int) (value * 255.0F) << 8) & 0x00FF00);
        break;
        case 8: // Blue
            this.color = (this.color & 0xFFFF00) | ((int) (value * 255.0F) & 0x0000FF);
        break;
        }
    }

    public void setValue(int index, int value) {
        switch (index) {
        case 2:
            this.brightness = (float) value / 255.0F;
        break;
        case 3:
            this.pitch = (float) value * 1.6F / 255.0F - 0.8F;
        break;
        case 4:
            this.yaw = (float) value * 6.28318530718F / 255.0F; // 2 Pi Radians
        break;
        case 5:
            this.focus = (float) value * 20F / 255.0F;
        break;
        case 6:
            this.color = (this.color & 0x00FFFF) | ((value << 16) & 0xFF0000);
        break;
        case 7:
            this.color = (this.color & 0xFF00FF) | ((value << 8) & 0x00FF00);
        break;
        case 8:
            this.color = (this.color & 0xFFFF00) | (value & 0x0000FF);
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
        this.channels = compound.getIntArray("channels");
        this.direction = compound.getInteger("direction");
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
        compound.setInteger("direction", this.direction);
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
        this.prevColor = this.color;

        
        if (this.pitch > 0.8F) {
            this.prevPitch = this.pitch = 0.8F;
        }
        else if (this.pitch < -0.8F) {
            this.prevPitch = this.pitch = -0.8F;
        }

        if (this.brightness > 1.0F) {
            this.brightness = 1.0F;
        }
        else if (this.brightness < 0.0F) {
            this.brightness = 0.0F;
        }

        if (this.focus > 20.0F) {
            this.focus = 20.0F;
        }
        else if (this.focus < 0.0F) {
            this.focus = 0.0F;
        }

        if (!this.worldObj.isRemote) {
            int size = 0;
            switch (this.getBlockMetadata() & 0xFF) {
            case 0:
                size = 1;
            break;
            case 1:
                size = 4;
            break;
            case 2:
                size = 7;
            break;
            case 3:
                size = 1;
            break;
            case 4:
                size = 7;
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
                    if (this.needsUpdate[i]) {
                        ints[j] = i;
                        j++;
                        this.needsUpdate[i] = false;
                    }
                }
                this.sync(ints);
            }

            this.ticksRemaining--;
            if (this.ticksRemaining <= 0) {
                this.ticksRemaining = 100;
                //this.sync(2, 3, 4, 5, 9, 10, 11, 12);
            }
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getRenderBoundingBox() {
        if (this.getBlockMetadata() == 3) {
            return super.getRenderBoundingBox();
        }
        return super.getRenderBoundingBox().expand(64.0D, 64.0D, 64.0D);
    }

    public void sendUniverseData(int[] levels) {
        for (int i = 0; this.channels != null && i < this.channels.length; i++) {
            if (this.channels[i] == 0) {
                continue;
            }
            if (levels[this.channels[i]] != this.cachedLevels[this.channels[i]]) {
                this.setValue(i + 2, levels[this.channels[i]]);
                this.sync(i + 2);
            }
        }
        if (this.getBlockMetadata() == 3) {
            this.worldObj.notifyBlocksOfNeighborChange(this.xCoord, this.yCoord, this.zCoord, Config.blockLightId);
        }
        System.arraycopy(levels, 0, this.cachedLevels, 0, levels.length);
    }

}