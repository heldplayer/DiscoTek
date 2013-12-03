
package net.specialattack.discotek.tileentity;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import me.heldplayer.util.HeldCore.sync.ISyncable;
import me.heldplayer.util.HeldCore.sync.ISyncableObjectOwner;
import me.heldplayer.util.HeldCore.sync.SBoolean;
import me.heldplayer.util.HeldCore.sync.SDouble;
import me.heldplayer.util.HeldCore.sync.SFloat;
import me.heldplayer.util.HeldCore.sync.SInteger;
import me.heldplayer.util.HeldCore.sync.packet.Packet4InitiateClientTracking;
import me.heldplayer.util.HeldCore.sync.packet.PacketHandler;
import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.packet.Packet;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.specialattack.discotek.block.BlockLight;
import net.specialattack.discotek.client.ClientProxy;
import net.specialattack.discotek.lights.Channels;
import net.specialattack.discotek.lights.ILight;

import com.google.common.io.ByteArrayDataInput;

public class TileEntityLight extends TileEntity implements ISyncableObjectOwner {

    private SBoolean hasLens;
    private SInteger direction;

    private SFloat pitch;
    private SFloat yaw;
    private SFloat brightness;
    private SFloat focus;
    private SInteger red;
    private SInteger green;
    private SInteger blue;
    private float prevPitch = 0.0F;
    private float prevYaw = 0.0F;
    private float prevBrightness = 1.0F;
    private float prevFocus = 1.0F;
    private int prevRed = 0xFF;
    private int prevGreen = 0xFF;
    private int prevBlue = 0xFF;

    private List<ISyncable> syncables;

    public ChannelLevel[] levels;

    public TileEntityLight() {
        this.hasLens = new SBoolean(this, true);
        this.pitch = new SFloat(this, 0.0F);
        this.yaw = new SFloat(this, 0.0F);
        this.brightness = new SFloat(this, 1.0F);
        this.focus = new SFloat(this, 1.0F);
        this.direction = new SInteger(this, 0);
        this.red = new SInteger(this, 0xFF);
        this.green = new SInteger(this, 0xFF);
        this.blue = new SInteger(this, 0xFF);
        this.syncables = Arrays.asList((ISyncable) this.hasLens, this.pitch, this.yaw, this.brightness, this.focus, this.direction, this.red, this.green, this.blue);
    }

    public TileEntityLight(Block blockType, int meta) {
        this();
        this.blockMetadata = meta;
        this.blockType = blockType;
        this.setupChannels();
    }

    private void setupChannels() {
        List<Channels> channels = this.getChannels();
        this.levels = new ChannelLevel[channels.size()];
        for (int i = 0; i < this.levels.length; i++) {
            Channels channel = channels.get(i);
            ISyncable syncable = null;
            switch (channel.id) {
            case 0:
            case 7:
                syncable = this.brightness;
            break;
            case 1:
                syncable = this.pitch;
            break;
            case 2:
                syncable = this.yaw;
            break;
            case 3:
                syncable = this.focus;
            break;
            case 4:
                syncable = this.red;
            break;
            case 5:
                syncable = this.green;
            break;
            case 6:
                syncable = this.blue;
            break;
            }
            this.levels[i] = new ChannelLevel(channel, syncable);
        }
    }

    public void setDirection(int side) {
        this.direction.setValue(side);
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
        return this.direction.getValue();
    }

    public int getColor(float partialTicks) {
        int red = (int) (this.prevRed + (this.red.getValue() - this.prevRed) * partialTicks);
        int green = (int) (this.prevGreen + (this.green.getValue() - this.prevGreen) * partialTicks);
        int blue = (int) (this.prevBlue + (this.blue.getValue() - this.prevBlue) * partialTicks);
        return red << 16 | green << 8 | blue;
    }

    public void setColor(int color) {
        int red = (color & 0xFF0000) >> 16;
        int green = (color & 0xFF00) >> 8;
        int blue = (color & 0xFF);
        this.prevRed = red;
        this.prevGreen = green;
        this.prevBlue = blue;
        this.red.setValue(red);
        this.green.setValue(green);
        this.blue.setValue(blue);
    }

    public boolean hasLens() {
        return this.hasLens.getValue();
    }

    public void setHasLens(boolean hasLens) {
        this.hasLens.setValue(hasLens);
    }

    public float getPitch(float partialTicks) {
        return this.prevPitch + (this.pitch.getValue() - this.prevPitch) * partialTicks;
    }

    public void setPitch(float pitch) {
        if (pitch > 0.8F) {
            this.prevPitch = 0.8F;
            this.pitch.setValue(0.8F);
        }
        else if (pitch < -0.8F) {
            this.prevPitch = -0.8F;
            this.pitch.setValue(-0.8F);
        }
        else {
            this.prevPitch = pitch;
            this.pitch.setValue(pitch);
        }
    }

    public float getYaw(float partialTicks) {
        return this.prevYaw + (this.yaw.getValue() - this.prevYaw) * partialTicks;
    }

    public void setYaw(float yaw) {
        this.prevYaw = yaw;
        this.yaw.setValue(yaw);
    }

    public float getBrightness(float partialTicks) {
        return this.prevBrightness + (this.brightness.getValue() - this.prevBrightness) * partialTicks;
    }

    public void setBrightness(float brightness) {
        if (brightness > 1.0F) {
            this.prevBrightness = 1.0F;
            this.brightness.setValue(1.0F);
        }
        else if (brightness < 0.0F) {
            this.prevBrightness = 0.0F;
            this.brightness.setValue(0.0F);
        }
        else {
            this.prevBrightness = brightness;
            this.brightness.setValue(brightness);
        }
    }

    public float getFocus(float partialTicks) {
        return this.prevFocus + (this.focus.getValue() - this.prevFocus) * partialTicks;
    }

    public void setFocus(float focus) {
        if (focus > 20.0F) {
            this.prevFocus = 20.0F;
            this.focus.setValue(20.0F);
        }
        else if (focus < 0.0F) {
            this.prevFocus = 0.0F;
            this.focus.setValue(0.0F);
        }
        else {
            this.prevFocus = focus;
            this.focus.setValue(focus);
        }
    }

    public void setLevel(int id, int value) {
        this.setLevel(Channels.getChannel(id), value);
    }

    public void setLevel(String identifier, int value) {
        this.setLevel(Channels.getChannel(identifier), value);
    }

    public void setLevel(Channels channel, int value) {
        if (channel == null) {
            return;
        }
        for (ChannelLevel level : this.levels) {
            if (level.channel == channel) {
                level.setValue(value);
            }
        }
    }

    public int getLevel(int id) {
        return this.getLevel(Channels.getChannel(id));
    }

    public int getLevel(String identifier) {
        return this.getLevel(Channels.getChannel(identifier));
    }

    public int getLevel(Channels channel) {
        if (channel != null) {
            for (ChannelLevel level : this.levels) {
                if (level.channel == channel) {
                    return level.getValue();
                }
            }
        }
        return 0;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        this.red.setValue(compound.getInteger("red"));
        this.green.setValue(compound.getInteger("green"));
        this.blue.setValue(compound.getInteger("blue"));
        this.hasLens.setValue(compound.getBoolean("hasLens"));
        this.pitch.setValue(compound.getFloat("pitch"));
        this.prevPitch = this.pitch.getValue();
        this.yaw.setValue(compound.getFloat("yaw"));
        this.prevYaw = this.yaw.getValue();
        this.brightness.setValue(compound.getFloat("brightness"));
        this.prevBrightness = this.brightness.getValue();
        this.focus.setValue(compound.getFloat("focus"));
        this.prevFocus = this.focus.getValue();
        this.direction.setValue(compound.getInteger("direction"));

        NBTTagList levels = compound.getTagList("levels");
        for (int i = 0; i < levels.tagCount(); i++) {
            NBTTagCompound level = (NBTTagCompound) levels.tagAt(i);
            int id = level.getInteger("id");
            int value = level.getInteger("value");
            this.setLevel(id, value);
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setInteger("red", this.red.getValue());
        compound.setInteger("green", this.green.getValue());
        compound.setInteger("blue", this.blue.getValue());
        compound.setBoolean("hasLens", this.hasLens.getValue());
        compound.setFloat("pitch", this.pitch.getValue());
        compound.setFloat("yaw", this.yaw.getValue());
        compound.setFloat("brightness", this.brightness.getValue());
        compound.setFloat("focus", this.focus.getValue());
        compound.setInteger("direction", this.direction.getValue());

        NBTTagList levels = new NBTTagList("levels");
        for (int i = 0; i < this.levels.length; i++) {
            NBTTagCompound level = new NBTTagCompound();
            ChannelLevel channel = this.levels[i];
            level.setInteger("id", channel.channel.id);
            level.setInteger("value", channel.getValue());
        }
        compound.setTag("levels", levels);
    }

    @Override
    public Packet getDescriptionPacket() {
        return PacketHandler.instance.createPacket(new Packet4InitiateClientTracking(this));
    }

    @Override
    public void updateEntity() {
        this.prevPitch = this.pitch.getValue();
        this.prevYaw = this.yaw.getValue();
        this.prevBrightness = this.brightness.getValue();
        this.prevFocus = this.focus.getValue();
        this.prevRed = this.red.getValue();
        this.prevGreen = this.green.getValue();
        this.prevBlue = this.blue.getValue();

        if (this.pitch.getValue() > 0.8F) {
            this.prevPitch = 0.8F;
            this.pitch.setValue(0.8F);
        }
        else if (this.pitch.getValue() < -0.8F) {
            this.prevPitch = -0.8F;
            this.pitch.setValue(-0.8F);
        }

        if (this.brightness.getValue() > 1.0F) {
            this.prevBrightness = 1.0F;
            this.brightness.setValue(1.0F);
        }
        else if (this.brightness.getValue() < 0.0F) {
            this.prevBrightness = 0.0F;
            this.brightness.setValue(0.0F);
        }

        if (this.focus.getValue() > 20.0F) {
            this.prevFocus = 20.0F;
            this.focus.setValue(20.0F);
        }
        else if (this.focus.getValue() < 0.0F) {
            this.prevFocus = 0.0F;
            this.focus.setValue(0.0F);
        }

        if (!this.worldObj.isRemote) {
            if (this.levels == null) {
                this.setupChannels();
            }
        }
    }

    @Deprecated
    public void sendUniverseData(int[] levels) {
// @formatter:off
        /*
        for (int i = 0; this.channels != null && i < this.channels.length; i++) {
            if (this.channels[i] == 0) {
                continue;
            }
            if (levels[this.channels[i]] != this.cachedLevels[this.channels[i]]) {
                this.setValue(i + 2, levels[this.channels[i]]);
            }
        }
        if (this.getBlockMetadata() == 3) {
            this.worldObj.notifyBlocksOfNeighborChange(this.xCoord, this.yCoord, this.zCoord, ModDiscoTek.blockLightId.getValue());
        }
        System.arraycopy(levels, 0, this.cachedLevels, 0, levels.length);
        */
        // @formatter:on
    }

    public ILight getLight() {
        Block block = this.getBlockType();
        if (block != null && block instanceof BlockLight) {
            return ((BlockLight) block).getLight(this.getBlockMetadata());
        }
        return null;
    }

    public List<Channels> getChannels() {
        ILight light = this.getLight();
        return light == null ? null : light.getChannels();
    }

    // ISyncableObjectOwner

    @Override
    public boolean isNotValid() {
        return super.isInvalid();
    }

    @Override
    public List<ISyncable> getSyncables() {
        return this.syncables;
    }

    @Override
    public void readSetup(ByteArrayDataInput in) throws IOException {
        for (int i = 0; i < this.syncables.size(); i++) {
            ISyncable syncable = this.syncables.get(i);
            syncable.setId(in.readInt());
            syncable.read(in);
        }
    }

    @Override
    public void writeSetup(DataOutputStream out) throws IOException {
        for (int i = 0; i < this.syncables.size(); i++) {
            ISyncable syncable = this.syncables.get(i);
            out.writeInt(syncable.getId());
            syncable.write(out);
        }
    }

    @Override
    public String getIdentifier() {
        return "TileEntityLight_" + this.xCoord + ";" + this.yCoord + ";" + this.zCoord;
    }

    @Override
    public boolean isWorldBound() {
        return true;
    }

    @Override
    public World getWorld() {
        return this.getWorldObj();
    }

    @Override
    public int getPosX() {
        return this.xCoord;
    }

    @Override
    public int getPosY() {
        return this.yCoord;
    }

    @Override
    public int getPosZ() {
        return this.zCoord;
    }

    @Override
    public void onDataChanged(ISyncable syncable) {}

    public static class ChannelLevel {

        public Channels channel;
        public ISyncable syncable;
        private int value;

        public ChannelLevel(Channels channel, ISyncable syncable) {
            this.channel = channel;
            this.syncable = syncable;
        }

        public void setValue(int value) {
            this.value = value;
            if (this.syncable instanceof SFloat) {
                ((SFloat) this.syncable).setValue((float) value / 255.0F);
                return;
            }
            if (this.syncable instanceof SDouble) {
                ((SDouble) this.syncable).setValue((double) value / 255.0D);
                return;
            }
            this.syncable.setValue(value);
        }

        public int getValue() {
            return this.value;
        }

    }

}
